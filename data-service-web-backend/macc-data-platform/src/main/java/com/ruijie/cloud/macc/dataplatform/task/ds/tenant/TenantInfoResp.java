package com.ruijie.cloud.macc.dataplatform.task.ds.tenant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/** tenant info resp, copied from org.apache.dolphinscheduler.dao.entity.Tenant */
@Data
public class TenantInfoResp {

  /** id */
  private Integer id;

  /** tenant code */
  private String tenantCode;

  /** description */
  private String description;

  /** queue id */
  private int queueId;

  /** queue name */
  private String queueName;

  /** queue */
  private String queue;

  /** create time */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createTime;

  /** update time */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updateTime;
}
