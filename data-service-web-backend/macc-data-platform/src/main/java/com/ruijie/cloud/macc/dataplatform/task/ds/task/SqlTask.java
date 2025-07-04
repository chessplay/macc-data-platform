package com.ruijie.cloud.macc.dataplatform.task.ds.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/** copied from org.apache.dolphinscheduler.plugin.task.api.parameters.SqlParameters */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SqlTask extends AbstractTask {

  /** data source type，eg MYSQL, POSTGRES, HIVE ... */
  private String type;

  /** datasource id */
  private Integer datasource;

  /** sql */
  private String sql;

  /** sql type 0 query 1 NON_QUERY */
  private String sqlType;

  /** send email */
  private Boolean sendEmail;

  /** display rows */
  private Integer displayRows;

  /** udf list */
  private String udfs;

  /** SQL connection parameters */
  private String connParams;

  /** Pre Statements */
  private List<String> preStatements;

  /** Post Statements */
  private List<String> postStatements;

  /** groupId */
  private Integer groupId;

  /** title */
  private String title;

  private Integer limit;

  @Override
  public String getTaskType() {
    return "SQL";
  }
}
