package com.ruijie.cloud.macc.dataplatform.task.ds.core;

import com.ruijie.cloud.macc.dataplatform.task.ds.datasource.DataSourceOperator;
import com.ruijie.cloud.macc.dataplatform.task.ds.instance.ProcessInstanceOperator;
import com.ruijie.cloud.macc.dataplatform.task.ds.process.ProcessOperator;
import com.ruijie.cloud.macc.dataplatform.task.ds.project.ProjectOperator;
import com.ruijie.cloud.macc.dataplatform.task.ds.remote.DolphinsRestTemplate;
import com.ruijie.cloud.macc.dataplatform.task.ds.resource.ResourceOperator;
import com.ruijie.cloud.macc.dataplatform.task.ds.schedule.ScheduleOperator;
import com.ruijie.cloud.macc.dataplatform.task.ds.tenant.TenantOperator;
import lombok.extern.slf4j.Slf4j;

/** dolphin scheduler client to operate dolphin scheduler */
@Slf4j
public class DolphinClient {

  private final DolphinsRestTemplate dolphinsRestTemplate;
  private final String dolphinAddress;
  private final String token;

  private DataSourceOperator dataSourceOperator;
  private ResourceOperator resourceOperator;
  private ProcessOperator processOperator;
  private ProcessInstanceOperator processInstanceOperator;
  private ScheduleOperator scheduleOperator;
  private ProjectOperator projectOperator;
  private TenantOperator tenantOperator;

  public DolphinClient(
      String token, String dolphinAddress, DolphinsRestTemplate dolphinsRestTemplate) {
    this.token = token;
    this.dolphinAddress = dolphinAddress;
    this.dolphinsRestTemplate = dolphinsRestTemplate;
    this.initOperators();
  }

  public void initOperators() {
    this.dataSourceOperator =
        new DataSourceOperator(this.dolphinAddress, this.token, this.dolphinsRestTemplate);
    this.resourceOperator =
        new ResourceOperator(this.dolphinAddress, this.token, this.dolphinsRestTemplate);
    this.processOperator =
        new ProcessOperator(this.dolphinAddress, this.token, this.dolphinsRestTemplate);
    this.processInstanceOperator =
        new ProcessInstanceOperator(this.dolphinAddress, this.token, this.dolphinsRestTemplate);
    this.scheduleOperator =
        new ScheduleOperator(this.dolphinAddress, this.token, this.dolphinsRestTemplate);
    this.projectOperator =
        new ProjectOperator(this.dolphinAddress, this.token, this.dolphinsRestTemplate);
    this.tenantOperator =
        new TenantOperator(this.dolphinAddress, this.token, this.dolphinsRestTemplate);
  }

  public DataSourceOperator opsForDataSource() {
    return this.dataSourceOperator;
  }

  public ResourceOperator opsForResource() {
    return this.resourceOperator;
  }

  public ProcessOperator opsForProcess() {
    return this.processOperator;
  }

  public ProcessInstanceOperator opsForProcessInst() {
    return this.processInstanceOperator;
  }

  public ScheduleOperator opsForSchedule() {
    return this.scheduleOperator;
  }

  public ProjectOperator opsForProject() {
    return this.projectOperator;
  }

  public TenantOperator opsForTenant() {
    return this.tenantOperator;
  }
}
