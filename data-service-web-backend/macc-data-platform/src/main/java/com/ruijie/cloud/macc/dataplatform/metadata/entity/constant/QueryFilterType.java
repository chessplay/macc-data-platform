package com.ruijie.cloud.macc.dataplatform.metadata.entity.constant;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/03/26 11:26
 **/
public enum QueryFilterType {
	EQ(OperatorType.UNARY),
	NOT_EQ(OperatorType.UNARY),
	IN(OperatorType.MULTI),
	NOT_IN(OperatorType.MULTI),
	GTE(OperatorType.UNARY),
	LTE(OperatorType.UNARY),
	GT(OperatorType.UNARY),
	LT(OperatorType.UNARY),
	IS_NULL(OperatorType.NONE),
	IS_NOT_NULL(OperatorType.NONE);

	private OperatorType operatorType;

	private QueryFilterType(OperatorType operatorType) {
		this.operatorType = operatorType;
	}

	public OperatorType getOperatorType() {
		return operatorType;
	}
}
