package com.ruijie.cloud.macc.dataplatform.task.ds.project;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectUpdateParam {

  private Long projectCode;

  private String projectName;

  private String description;

  private String userName;
}
