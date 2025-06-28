package com.ruijie.cloud.macc.dataplatform.metadata.entity.constant;


import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/04/09 14:56
 **/
@Data
public class DataTypeFilterConstraint {
	private Map<DataPropertyType, List<QueryFilterType>> constraintMap;
	private Map<QueryFilterType, OperatorType> filterOperatorMap;
	private Map<DataPropertyType, List<QueryFilterType>> requiredColumnConstraintMap;
}
