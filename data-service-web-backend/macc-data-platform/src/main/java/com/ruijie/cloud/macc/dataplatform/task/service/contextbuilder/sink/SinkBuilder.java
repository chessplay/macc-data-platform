package com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.sink;

import com.ruijie.cloud.macc.dataplatform.task.domain.vo.ParameterDescriptor;
import com.ruijie.cloud.macc.dataplatform.task.entity.SinkTemplate;
import org.apache.velocity.VelocityContext;

;import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/04/08 14:45
 **/
public interface SinkBuilder {
	void buildContext(VelocityContext context, SinkTemplate sinkTemplate);
	/**
	 * [新增方法]
	 * 获取该Sink类型专属的启动参数描述符列表。
	 * 默认返回一个空列表，只有需要特殊参数的Sink才需要重写此方法。
	 * @return 参数描述符列表
	 */
	default List<ParameterDescriptor> getSpecificParameterDescriptors() {
		return Collections.emptyList();
	}
	/**
	 * [新增方法]
	 * 校验该Sink类型专属的运行时参数。
	 * @param params 用户传入的全部运行时参数
	 */
	default void validateSpecificParameters(Map<String, Object> params,SinkTemplate sinkTemplate) {
		// 默认不做任何操作
	}

}
