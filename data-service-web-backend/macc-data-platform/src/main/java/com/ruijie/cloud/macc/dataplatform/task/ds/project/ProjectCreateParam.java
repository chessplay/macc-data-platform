package com.ruijie.cloud.macc.dataplatform.task.ds.project;

import lombok.Data;
import lombok.experimental.Accessors;

/** create project param */
@Data
@Accessors(chain = true)
public class ProjectCreateParam {

  /** project name */
  private String projectName;

  /** project description */
  private String description;
}
