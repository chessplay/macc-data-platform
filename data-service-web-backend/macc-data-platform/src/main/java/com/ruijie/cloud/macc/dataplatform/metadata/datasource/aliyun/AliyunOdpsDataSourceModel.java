package com.ruijie.cloud.macc.dataplatform.metadata.datasource.aliyun;

import lombok.Data;

@Data
public class AliyunOdpsDataSourceModel {
	private String accessId;
	private String accessKey;
	private String endPoint;
	private String projectId;
}
