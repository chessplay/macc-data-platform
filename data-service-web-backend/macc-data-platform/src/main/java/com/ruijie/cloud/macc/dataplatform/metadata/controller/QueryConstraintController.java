package com.ruijie.cloud.macc.dataplatform.metadata.controller;

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruijie.cloud.dc.core.response.ObjectResult;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataPropertyType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType;

@RestController
@RequestMapping("/query/constraint")
public class QueryConstraintController {

	private static final Map<DataPropertyType, List<QueryFilterType>> CONSTRAINT_MAP;

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
	}

	@GetMapping("/types")
	public ObjectResult<Map> dataTypefilterConstraint() {
		ObjectResult<Map> rsp = new ObjectResult<>();
		rsp.setData(CONSTRAINT_MAP);
		return rsp;
	}
}
