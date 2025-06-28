package com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.source;

import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.BIGINT;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.BOOLEAN;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.CHAR;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.DATE;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.DECIMAL;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.DOUBLE;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.FLOAT;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.INT;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.INTEGER;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.NUMERIC;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.SMALLINT;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.TIMESTAMP;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.TINYINT;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType.VARCHAR;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType.EQ;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType.GT;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType.GTE;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType.IN;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType.IS_NOT_NULL;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType.IS_NULL;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType.LT;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType.LTE;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType.NOT_EQ;
import static com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType.NOT_IN;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.ruijie.cloud.macc.dataplatform.metadata.datasource.DataSourceModelParser;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilterTemplate;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import org.apache.velocity.VelocityContext;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.ColumnInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.JdbcDataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilter;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataPropertyType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataTypeFilterConstraint;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.OperatorType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.PhoenixPropertyType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType;
import com.ruijie.cloud.macc.dataplatform.task.entity.SourceTemplate;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskErrorCode;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskException;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/07/29 13:52
 **/
@SourceBuilderType(DataSourceType.PHOENIX)
public class PhoenixSourceBuilder implements SourceBuilder {



	private static final Map<DataPropertyType, List<QueryFilterType>> CONSTRAINT_MAP;
	private static final Map<DataPropertyType, List<QueryFilterType>> REQUIRED_COLUMN_CONSTRAINT_MAP;
	private static final Map<QueryFilterType, OperatorType> FILTEROPERATOR_MAP;


	static {
		CONSTRAINT_MAP = new HashMap<>();
		CONSTRAINT_MAP.put(BOOLEAN, Arrays.asList(EQ, NOT_EQ, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(FLOAT, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(BIGINT, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(DOUBLE, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(INTEGER, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(INT, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(SMALLINT, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(TINYINT, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(NUMERIC, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(DECIMAL, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(TIMESTAMP, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(DATE, Arrays.asList(EQ, NOT_EQ, GT, LT, GTE, LTE, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(VARCHAR, Arrays.asList(EQ, NOT_EQ, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		CONSTRAINT_MAP.put(CHAR, Arrays.asList(EQ, NOT_EQ, IS_NULL, IS_NOT_NULL, IN, NOT_IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP = new HashMap<>();
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(BOOLEAN, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(FLOAT, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(BIGINT, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(DOUBLE, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(INTEGER, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(INT, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(SMALLINT, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(TINYINT, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(NUMERIC, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(DECIMAL, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(TIMESTAMP, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(DATE, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(VARCHAR, Arrays.asList(EQ, IN));
		REQUIRED_COLUMN_CONSTRAINT_MAP.put(CHAR, Arrays.asList(EQ, IN));
		FILTEROPERATOR_MAP = Arrays.asList(QueryFilterType.values()).stream()
				.collect(Collectors.toMap(op -> op, op -> op.getOperatorType()));
	}

	@Override
	public void buildContext(VelocityContext context, SourceTemplate sourceTemplate) {
		JdbcDataSourceModel dataSourceModel = DataSourceModelParser.parseToJdbcDataSource(sourceTemplate.getDataSourceModel());
		checkAndParseTemplateParams(sourceTemplate.getTableInfo(), sourceTemplate.getSourceQueryFilter());
		context.put("phoenixUrl", dataSourceModel.getUrl());
		context.put("partition_num",sourceTemplate.getPartitionNum());
		context.put("partition_column", sourceTemplate.getTableInfo().getPrimaryKeys().get(0)+"_TMP");


		List<QueryFilterTemplate> filters = sourceTemplate.getSourceQueryFilter();



		String query = buildSelectQuery(dataSourceModel.getSchema(), sourceTemplate.getTableInfo().getTableName(),
				sourceTemplate.getTableInfo().getColumns(), filters);
		context.put("query", query);


	}

	@Override
	public Expression convertObjectToExpression(String columnType, Object value) {
		if (value instanceof String) {
			String stringValue = (String) value;
			return new StringValue(stringValue);

		} else if (value instanceof Number) {
			Number numberValue = (Number) value;
			if (numberValue instanceof Float || numberValue instanceof Double
					|| numberValue instanceof BigDecimal) {
				return new DoubleValue(value.toString());
			} else {
				return new LongValue(value.toString());
			}
		} else if (value instanceof Boolean) {
			return new LongValue(String.valueOf(value));
		}

		throw new TaskException(TaskErrorCode.UNSUPPORTED_VALUE_TYPE);

	}

	@Override
	public SelectExpressionItem buildSelectExpressionItem(ColumnInfo columnInfo) {
		SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
		Column column = new Column(columnInfo.getColumnName());
		String aliasName = columnInfo.getColumnName() + "_TMP";  // 例如：id → id_tmp

		if (columnInfo.getColumnType().toLowerCase().contains("array")) {
			// 处理 ARRAY 类型的列
			Function arrayToStringFunction = new Function();
			arrayToStringFunction.setName("ARRAY_TO_STRING");
			ExpressionList parameters = new ExpressionList();
			parameters.addExpressions(column, new StringValue(","));
			arrayToStringFunction.setParameters(parameters);
			selectExpressionItem.setExpression(arrayToStringFunction);
			// 设置别名并添加双引号（第二个参数 true 表示用双引号包裹）
			selectExpressionItem.setAlias(new Alias(aliasName, false));
		} else {
			selectExpressionItem.setExpression(column);
			// 设置别名并添加双引号
			selectExpressionItem.setAlias(new Alias(aliasName, false));
		}
		return selectExpressionItem;
	}


	@Override
	public Object checkAndParseValueType(Object value, String type) {
		try {
			switch (type.toUpperCase()) {
				case "VARCHAR":
				case "CHAR":
				case "TEXT":
					return value.toString();
				case "INTEGER":
				case "INT":
				case "BIGINT":
				case "SMALLINT":
				case "TINYINT":
					return value instanceof Number ? ((Number) value).longValue() : Long.parseLong(value.toString());
				case "DOUBLE":
				case "FLOAT":
				case "NUMERIC":
				case "DECIMAL":
					return value instanceof Number ? ((Number) value).doubleValue()
							: Double.parseDouble(value.toString());
				case "BOOLEAN":
					if (value instanceof Boolean) {
						return value;
					} else if (value instanceof String) {
						return Boolean.parseBoolean((String) value);
					}
					throw new TaskException(TaskErrorCode.VALUE_TYPE_MISMATCH, value, type);

					// [关键修改] 新增对字符串日期和时间戳的处理
				case "TIMESTAMP":
				case "DATE":
					if (value instanceof Long) {
						return value;
					}
					if (value instanceof Integer) {
						return ((Integer) value).longValue();
					}
					if (value instanceof String) {
						try {
							// 尝试按 "yyyy-MM-dd HH:mm:ss" 格式解析
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							// 注意：如果您的 Phoenix 集群时区不是UTC，可能需要调整这里的时区
							// sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
							return sdf.parse((String) value).getTime();
						} catch (java.text.ParseException e) {
							// 如果解析失败，再尝试将其作为纯数字的毫秒时间戳字符串
							try {
								return Long.parseLong((String) value);
							} catch (NumberFormatException nfe) {
								throw new TaskException(TaskErrorCode.VALUE_TYPE_MISMATCH, value, "无效的日期格式或时间戳");
							}
						}
					}
					throw new TaskException(TaskErrorCode.VALUE_TYPE_MISMATCH, value, type);

				default:
					return value;
			}
		} catch (NumberFormatException e) {
			throw new TaskException(TaskErrorCode.VALUE_TYPE_MISMATCH, value, type);
		}
	}

	public DataTypeFilterConstraint dataTypefilterConstraint() {
		DataTypeFilterConstraint dataTypeFilterConstraint = new DataTypeFilterConstraint();
		dataTypeFilterConstraint.setConstraintMap(CONSTRAINT_MAP);
		dataTypeFilterConstraint.setFilterOperatorMap(FILTEROPERATOR_MAP);
		dataTypeFilterConstraint.setRequiredColumnConstraintMap(REQUIRED_COLUMN_CONSTRAINT_MAP);
		return dataTypeFilterConstraint;
	}

	@Override
	public DataPropertyType getColumnType(String type) {
		return PhoenixPropertyType.fromString(type);
	}
}
