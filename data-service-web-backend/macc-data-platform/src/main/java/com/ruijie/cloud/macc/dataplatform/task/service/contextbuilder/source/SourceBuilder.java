package com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.source;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilterTemplate;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.common.TemplateVariableExpression;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import com.ruijie.cloud.macc.dataplatform.metadata.entity.ColumnInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilter;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TableInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataPropertyType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataTypeFilterConstraint;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.OperatorType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType;
import com.ruijie.cloud.macc.dataplatform.task.entity.SourceTemplate;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskErrorCode;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskException;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/04/08 14:45
 **/
public interface SourceBuilder {
	void buildContext(VelocityContext context, SourceTemplate sourceTemplate);

	default String buildSelectQuery(String schemaName, String tableName, List<ColumnInfo> columnInfos,
			List<QueryFilterTemplate> queryFilterList) {
		// 构建 SELECT 语句
		Select select = new Select();
		PlainSelect plainSelect = new PlainSelect();
		select.setSelectBody(plainSelect);

		// 设置表名
		Table table = new Table();
		if (StringUtils.isNotEmpty(schemaName)) {
			table.setSchemaName(schemaName);
		}
		table.setName(tableName);
		plainSelect.setFromItem(table);

		// 设置列名
		List<SelectItem> selectItems = new ArrayList<>();
		for (ColumnInfo columnInfo : columnInfos) {
			SelectExpressionItem selectExpressionItem = buildSelectExpressionItem(columnInfo);
			selectItems.add(selectExpressionItem);
		}
		plainSelect.setSelectItems(selectItems);

		// 添加查询条件
		if (CollectionUtils.isNotEmpty(queryFilterList)) {
			Expression whereExpression = null;
			for (QueryFilterTemplate filter : queryFilterList) {
				String columnType = getColumnType(filter.getField(), columnInfos);
				Expression parsedCondition = buildExpressionFromFilterTemplate(filter);
				if (whereExpression == null) {
					whereExpression = parsedCondition;
				} else {
					whereExpression = new AndExpression(whereExpression, parsedCondition);
				}
			}
			plainSelect.setWhere(whereExpression);
		}

		// 返回构建的 SQL 语句
		return select.toString();
	}

	default Expression buildExpressionFromFilterTemplate(QueryFilterTemplate filter) {
		Column column = new Column(filter.getField());
		Expression expression = null;

		switch (filter.getType()) {
			case EQ:
				expression = new EqualsTo();
				((EqualsTo) expression).setLeftExpression(column);
				((EqualsTo) expression)
						.setRightExpression(new TemplateVariableExpression(filter.getValue()));
				break;
			case NOT_EQ:
				expression = new NotEqualsTo();
				((NotEqualsTo) expression).setLeftExpression(column);
				((NotEqualsTo) expression)
						.setRightExpression(new TemplateVariableExpression(filter.getValue()));
				break;
			case IN:

				InExpression inExpression = new InExpression();
				inExpression.setLeftExpression(column);

				inExpression.setRightExpression(new TemplateVariableExpression(filter.getValue()));
				expression = inExpression;
				break;
			case NOT_IN:
				InExpression notInExpression = new InExpression();
				notInExpression.setLeftExpression(column);
				notInExpression.setRightExpression(new TemplateVariableExpression(filter.getValue()));
				notInExpression.setNot(true);
				expression = notInExpression;
				break;
			case GTE:
				expression = new GreaterThanEquals();
				((GreaterThanEquals) expression).setLeftExpression(column);
				((GreaterThanEquals) expression)
						.setRightExpression(new TemplateVariableExpression(filter.getValue()));
				break;
			case LTE:
				expression = new MinorThanEquals();
				((MinorThanEquals) expression).setLeftExpression(column);
				((MinorThanEquals) expression)
						.setRightExpression(new TemplateVariableExpression(filter.getValue()));
				break;
			case GT:
				expression = new GreaterThan();
				((GreaterThan) expression).setLeftExpression(column);
				((GreaterThan) expression)
						.setRightExpression(new TemplateVariableExpression(filter.getValue()));
				break;
			case LT:
				expression = new MinorThan();
				((MinorThan) expression).setLeftExpression(column);
				((MinorThan) expression)
						.setRightExpression(new TemplateVariableExpression(filter.getValue()));
				break;

			case IS_NULL:
				IsNullExpression isNullExpression = new IsNullExpression();
				isNullExpression.setLeftExpression(column);
				isNullExpression.setNot(false); // 表示 IS NULL
				expression = isNullExpression;
				break;

			case IS_NOT_NULL:
				IsNullExpression isNotNullExpression = new IsNullExpression();
				isNotNullExpression.setLeftExpression(column);
				isNotNullExpression.setNot(true);
				expression = isNotNullExpression;
				break;
			default:
				break;
		}
		return expression;
	}



	default Expression buildExpressionFromFilter(String columnType, QueryFilter filter) {
		Column column = new Column(filter.getField());
		Expression expression = null;

		switch (filter.getType()) {
			case EQ:
				expression = new EqualsTo();
				((EqualsTo) expression).setLeftExpression(column);
				((EqualsTo) expression)
						.setRightExpression(convertObjectToExpression(columnType, filter.getValue().get(0)));
				break;
			case NOT_EQ:
				expression = new NotEqualsTo();
				((NotEqualsTo) expression).setLeftExpression(column);
				((NotEqualsTo) expression)
						.setRightExpression(convertObjectToExpression(columnType, filter.getValue().get(0)));
				break;
			case IN:
				ExpressionList expressionList = new ExpressionList();
				expressionList.setExpressions(
						filter.getValue().stream().map(value -> convertObjectToExpression(columnType, value)) // 使用
								// lambda
								// 表达式传递额外的
								// columnType
								.collect(Collectors.toList()));
				InExpression inExpression = new InExpression();
				inExpression.setLeftExpression(column);
				inExpression.setRightItemsList(expressionList);
				expression = inExpression;
				break;

			case NOT_IN:

				ExpressionList notInList = new ExpressionList();
				notInList.setExpressions(
						filter.getValue().stream().map(value -> convertObjectToExpression(columnType, value)) // 使用
								// lambda
								// 表达式传递额外的
								// columnType
								.collect(Collectors.toList()));
				InExpression notInExpression = new InExpression();
				notInExpression.setLeftExpression(column);
				notInExpression.setRightItemsList(notInList);
				notInExpression.setNot(true);
				expression = notInExpression;
				break;
			case GTE:
				expression = new GreaterThanEquals();
				((GreaterThanEquals) expression).setLeftExpression(column);
				((GreaterThanEquals) expression)
						.setRightExpression(convertObjectToExpression(columnType, filter.getValue().get(0)));
				break;
			case LTE:
				expression = new MinorThanEquals();
				((MinorThanEquals) expression).setLeftExpression(column);
				((MinorThanEquals) expression)
						.setRightExpression(convertObjectToExpression(columnType, filter.getValue().get(0)));
				break;
			case GT:
				expression = new GreaterThan();
				((GreaterThan) expression).setLeftExpression(column);
				((GreaterThan) expression)
						.setRightExpression(convertObjectToExpression(columnType, filter.getValue().get(0)));
				break;
			case LT:
				expression = new MinorThan();
				((MinorThan) expression).setLeftExpression(column);
				((MinorThan) expression)
						.setRightExpression(convertObjectToExpression(columnType, filter.getValue().get(0)));
				break;
			case IS_NULL:
				IsNullExpression isNullExpression = new IsNullExpression();
				isNullExpression.setLeftExpression(column);
				isNullExpression.setNot(false); // 表示 IS NULL
				expression = isNullExpression;
				break;

			case IS_NOT_NULL:
				IsNullExpression isNotNullExpression = new IsNullExpression();
				isNotNullExpression.setLeftExpression(column);
				isNotNullExpression.setNot(true);
				expression = isNotNullExpression;
				break;
			default:
				break;
		}
		return expression;
	}

	Expression convertObjectToExpression(String columnType, Object value);

	SelectExpressionItem buildSelectExpressionItem(ColumnInfo columnInfo);

	default String getColumnType(String fieldName, List<ColumnInfo> columnInfos) {
		for (ColumnInfo columnInfo : columnInfos) {
			if (columnInfo.getColumnName().equalsIgnoreCase(fieldName)) {
				return columnInfo.getColumnType();
			}
		}
		return null;
	}
	default void checkAndParseTemplateParams(TableInfo tableInfo, List<QueryFilterTemplate> queryFilterList) {
		if (CollectionUtils.isEmpty(queryFilterList)) {
			throw new TaskException(TaskErrorCode.QUERY_FILTERED_IS_EMPTY);
		}
		List<String> requiredColumns;
		if(CollectionUtils.isEmpty(tableInfo.getRequiredColumns())){
			requiredColumns=tableInfo.getPrimaryKeys();
		}else{
			requiredColumns=tableInfo.getRequiredColumns();
		}

		// ======================= 修改开始 =======================

		// 检查：如果必传字段列表不为空，则必须包含最左侧的那个字段
		if (CollectionUtils.isNotEmpty(requiredColumns)) {
			String leftmostKey = requiredColumns.get(0); // 获取最左侧的键
			boolean hasLeftmostKey = queryFilterList.stream()
					.anyMatch(filter -> leftmostKey.equals(filter.getField()));

			if (!hasLeftmostKey) {
				// 如果没有包含最左侧的键，则抛出异常，并明确告知需要哪个键
				throw new TaskException(TaskErrorCode.INDEX_OR_PRIMARY_KEY_MISSING, leftmostKey);
			}
		}

		// ======================= 修改结束 =======================
	}

	default void checkAndParseParams(TableInfo tableInfo, List<QueryFilter> queryFilterList) {

		if (CollectionUtils.isEmpty(queryFilterList)) {
			throw new TaskException(TaskErrorCode.QUERY_FILTERED_IS_EMPTY);
		}

		// 1. [关键修改] 将“必传列”严格定义为“主键的最左列”
		List<String> primaryKeys = tableInfo.getPrimaryKeys();
		List<String> requiredColumns; // 这个列表现在只包含一个元素或为空

		if (CollectionUtils.isNotEmpty(primaryKeys)) {
			// 只取主键的第一个元素作为必传列
			requiredColumns = Collections.singletonList(primaryKeys.get(0));
		} else {
			requiredColumns = Collections.emptyList();
		}

		// 2. 入口校验：确保最左列存在
		if (CollectionUtils.isNotEmpty(requiredColumns)) {
			String leftmostKey = requiredColumns.get(0);
			boolean hasLeftmostKey = queryFilterList.stream()
					.anyMatch(filter -> leftmostKey.equals(filter.getField()));

			if (!hasLeftmostKey) {
				throw new TaskException(TaskErrorCode.INDEX_OR_PRIMARY_KEY_MISSING, leftmostKey);
			}
		}

		// 3. 循环逐项审查（此部分逻辑不变，但其行为因第1步的修改而改变）
		List<ColumnInfo> columnInfos = tableInfo.getColumns();
		Map<String, ColumnInfo> columnInfoMap =
				columnInfos.stream().collect(Collectors.toMap(ColumnInfo::getColumnName, Function.identity()));

		DataTypeFilterConstraint constraint = dataTypefilterConstraint();
		Map<DataPropertyType, List<QueryFilterType>> constraintMap = constraint.getConstraintMap();
		Map<QueryFilterType, OperatorType> filterOperatorMap = constraint.getFilterOperatorMap();
		Map<DataPropertyType, List<QueryFilterType>> requiredColumnConstraintMap =
				constraint.getRequiredColumnConstraintMap();

		for (QueryFilter filter : queryFilterList) {
			ColumnInfo columnInfo = columnInfoMap.get(filter.getField());
			if (columnInfo == null) {
				throw new TaskException(TaskErrorCode.COLUMN_NOT_EXIST, filter.getField());
			}
			DataPropertyType columnType = getColumnType(columnInfo.getColumnType());
			List<QueryFilterType> supportedFilterTypes = constraintMap.get(columnType);
			if (supportedFilterTypes == null || !supportedFilterTypes.contains(filter.getType())) {
				throw new TaskException(TaskErrorCode.UNSUPPORTED_FILTER_TYPE);
			}

			// [行为改变] 此处的 if 条件，现在只会对 "最左列" 生效。
			// 其他主键列（如 updateTime）因为不在 requiredColumns 列表中，将跳过此项严格检查。
			if (requiredColumns.contains(filter.getField())) {
				List<QueryFilterType> requiredSupportedFilterTypes = requiredColumnConstraintMap.get(columnType);
				if (requiredSupportedFilterTypes == null || !requiredSupportedFilterTypes.contains(filter.getType())) {
					throw new TaskException(TaskErrorCode.UNSUPPORTED_FILTER_TYPE_FOR_REQUIRED_COLUMN);
				}
			}
			OperatorType operatorType = filterOperatorMap.get(filter.getType());
			List<Object> originalValues = filter.getValue();
			if (!isValuesLengthValid(operatorType, originalValues)) {
				throw new TaskException(TaskErrorCode.VALUE_LENGTH_IS_NOT_VALID);
			}

			List<Object> convertedValues =
					originalValues.stream().map(value -> checkAndParseValueType(value, columnInfo.getColumnType()))
							.collect(Collectors.toList());

			filter.setValue(convertedValues);
		}
	}

	Object checkAndParseValueType(Object value, String type);

	DataTypeFilterConstraint dataTypefilterConstraint();

	DataPropertyType getColumnType(String type);

	default boolean isValuesLengthValid(OperatorType operatorType, List<Object> values) {
		switch (operatorType) {
			case NONE:
				return values.isEmpty(); // NONE类型不应该有任何值
			case UNARY:
				return values.size() == 1; // UNARY类型应该只有一个值
			case BINARY:
				return values.size() == 2; // BINARY类型应该有两个值
			case TERNARY:
				return values.size() == 3; // TERNARY类型应该有三个值
			case MULTI:
				return values.size() >= 1 && values.size() <= 1000; // MULTI类型至少有一个值,但不允许超过1000个
			default:

				return false;
		}
	}
}
