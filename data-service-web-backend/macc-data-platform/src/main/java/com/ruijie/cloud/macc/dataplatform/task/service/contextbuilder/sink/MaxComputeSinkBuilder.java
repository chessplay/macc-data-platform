package com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.sink;

import java.util.*;
import java.util.regex.Pattern;

import com.aliyun.odps.Odps;
import com.aliyun.odps.OdpsException;
import com.aliyun.odps.PartitionSpec;
import com.aliyun.odps.Table;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelParser;
import com.ruijie.cloud.macc.dataplatform.metadata.datasource.aliyun.AliyunOdpsDataSourceModel;
import com.ruijie.cloud.macc.dataplatform.task.constant.TaskParamsConstant;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.ParameterDescriptor;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskErrorCode;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskException;
import org.apache.velocity.VelocityContext;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceProperty;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.task.entity.SinkTemplate;
import org.springframework.util.StringUtils;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/07/29 13:52
 **/
@SinkBuilderType(DataSourceType.MAX_COMPUTE)
public class MaxComputeSinkBuilder implements SinkBuilder {


	@Override
	public void buildContext(VelocityContext context, SinkTemplate sinkTemplate) {

		AliyunOdpsDataSourceModel dataSourceModel = DataSourceModelParser.parseToAliyunOdpsDataSource(sinkTemplate.getDataSourceModel());
		context.put("accessKey", dataSourceModel.getAccessKey());
		context.put("accessId", dataSourceModel.getAccessId());
		context.put("endpoint",dataSourceModel.getEndPoint());
		context.put("project",dataSourceModel.getProjectId());


		context.put("tableName", sinkTemplate.getTableName());
	}
	/**
	 * [实现新增的方法]
	 * 提供MaxCompute作为Sink时所特有的启动参数。
	 */
	@Override
	public List<ParameterDescriptor> getSpecificParameterDescriptors() {
		List<ParameterDescriptor> descriptors = new ArrayList<>();

		descriptors.add(new ParameterDescriptor()
				.setName(TaskParamsConstant.MaxCompute.PARTITION_SPEC.toLowerCase())
				.setLabel("分区参数(MaxCompute)")
				.setComponent("input")
				.setRequired(false)
				.setDefaultValue("")
				.setProps(Collections.singletonMap("placeholder", "例如: pt=1,ds=20250101"))
		);

		descriptors.add(new ParameterDescriptor()
				.setName(TaskParamsConstant.MaxCompute.OVERWRITE.toLowerCase())
				.setLabel("是否覆盖(MaxCompute)")
				.setComponent("select")
				.setRequired(false)
				.setDefaultValue(null)
				.setProps(new HashMap<String, Object>() {{
					put("placeholder", "请选择（默认为否）");
					put("options", Arrays.asList(
							new HashMap<String, Object>() {{ put("label", "是 (覆盖)"); put("value", true); }},
							new HashMap<String, Object>() {{ put("label", "否 (追加)"); put("value", false); }}
					));
				}})
		);

		return descriptors;
	}
	/**
	 * [实现新增的校验方法]
	 * 校验 MaxCompute 的 partition_spec 参数格式是否正确。
	 */
	/**
	 * [重写方法]
	 * 校验 MaxCompute 的 partition_spec 参数格式，并检查分区是否存在。
	 * @param params 用户传入的全部运行时参数
	 * @param sinkTemplate 包含了数据源和表信息的Sink模板
	 */
	/**
	 * [重构后版本]
	 * 校验 MaxCompute 的 partition_spec 参数，并实现精细化的异常分类。
	 */
	@Override
	public void validateSpecificParameters(Map<String, Object> params, SinkTemplate sinkTemplate) {
		String partitionSpecKey = TaskParamsConstant.MaxCompute.PARTITION_SPEC.toLowerCase();

		if (!params.containsKey(partitionSpecKey) || !StringUtils.hasText((String) params.get(partitionSpecKey))) {
			return;
		}

		String partitionSpecString = (String) params.get(partitionSpecKey);

		// 1. 先进行格式校验 (快速失败)
		String regex = "^(\\w+=\\S+)(,\\s*\\w+=\\S+)*$";
		if (!Pattern.matches(regex, partitionSpecString)) {
			// 格式完全不匹配，直接抛出格式错误
			throw new TaskException(TaskErrorCode.INVALID_PARTITION_SPEC_FORMAT, "格式错误，应为 'key=value,key=value,...'");
		}

		// 2. 进行存在性及合法性校验 (会产生网络API调用)
		try {
			AliyunOdpsDataSourceModel odpsModel = DataSourceModelParser.parseToAliyunOdpsDataSource(sinkTemplate.getDataSourceModel());
			String tableName = sinkTemplate.getTableName();

			Account account = new AliyunAccount(odpsModel.getAccessId(), odpsModel.getAccessKey());
			Odps odps = new Odps(account);
			odps.setEndpoint(odpsModel.getEndPoint());
			odps.setDefaultProject(odpsModel.getProjectId());

			Table table = odps.tables().get(tableName);
			PartitionSpec spec = new PartitionSpec(partitionSpecString);
			boolean partitionExists = table.hasPartition(spec);

			if (!partitionExists) {
				// 这是明确的分区不存在（值不存在）
				throw new TaskException(TaskErrorCode.MAXCOMPUTE_PARTITION_NOT_EXIST, partitionSpecString, tableName);
			}
		} catch (OdpsException e) {
			// 捕获所有ODPS相关的API异常
			String odpsErrorMsg = e.getMessage();
			// 判断是否是关于分区定义不匹配的特定错误
			if (odpsErrorMsg != null && (odpsErrorMsg.contains("partition key") || odpsErrorMsg.contains("partition spec"))) {
				// 如果是，我们将其归类为“分区参数不合法”，并附上ODPS的具体原因
				throw new TaskException(TaskErrorCode.INVALID_PARTITION_SPEC_FORMAT, odpsErrorMsg);
			} else {
				// 其他ODPS异常（如权限、网络、表不存在等），归为“未知错误”
				throw new TaskException(TaskErrorCode.UNKNOWN_ERROR, "校验MaxCompute分区时出错(ODPS): " + odpsErrorMsg);
			}
		} catch (IllegalArgumentException e) {
			// 这个异常专门捕获 new PartitionSpec() 时的格式错误
			throw new TaskException(TaskErrorCode.INVALID_PARTITION_SPEC_FORMAT, e.getMessage());
		} catch (Exception e) {
			// 最后的保障，捕获所有其他未预料到的异常
			throw new TaskException(TaskErrorCode.UNKNOWN_ERROR, "校验MaxCompute分区时发生未知内部错误: " + e.getMessage());
		}
	}
}
