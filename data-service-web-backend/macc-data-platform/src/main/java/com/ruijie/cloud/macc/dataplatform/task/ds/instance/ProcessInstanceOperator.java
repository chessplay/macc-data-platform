package com.ruijie.cloud.macc.dataplatform.task.ds.instance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.ruijie.cloud.macc.dataplatform.task.ds.common.PageInfo;
import com.ruijie.cloud.macc.dataplatform.task.ds.core.AbstractOperator;
import com.ruijie.cloud.macc.dataplatform.task.ds.core.DolphinClientConstant;
import com.ruijie.cloud.macc.dataplatform.task.ds.core.DolphinException;
import com.ruijie.cloud.macc.dataplatform.task.ds.remote.DolphinsRestTemplate;
import com.ruijie.cloud.macc.dataplatform.task.ds.remote.HttpRestResult;
import com.ruijie.cloud.macc.dataplatform.task.ds.remote.Query;
import com.ruijie.cloud.macc.dataplatform.task.ds.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ProcessInstanceOperator extends AbstractOperator {

  public ProcessInstanceOperator(
      String dolphinAddress, String token, DolphinsRestTemplate dolphinsRestTemplate) {
    super(dolphinAddress, token, dolphinsRestTemplate);
  }

  /**
   * start/run process instance
   *
   * <p>api: /dolphinscheduler/projects/{projectCode}/executors/start-process-instance
   *
   * @param processInstanceCreateParam process instance create param
   * @return true for success,otherwise false
   */
  public Boolean start(Long projectCode, ProcessInstanceCreateParam processInstanceCreateParam) {
    String url = dolphinAddress + "/projects/" + projectCode + "/executors/start-process-instance";
    log.info("start process instance ,url:{}", url);
    try {
      HttpRestResult<JsonNode> restResult =
          dolphinsRestTemplate.postForm(
              url, getHeader(), processInstanceCreateParam, JsonNode.class);
      log.info("start process response:{}", restResult);
      return restResult.getSuccess();
    } catch (Exception e) {
      throw new DolphinException("start dolphin scheduler process instance fail", e);
    }
  }

  /**
   * page query process's instance list
   *
   * @param page page,default 1 while is null
   * @param size size,default 10 while is null
   * @param projectCode project code
   * @param workflowCode workflow id
   * @return
   */
  public PageInfo<ProcessInstanceQueryResp> page(
      Integer page, Integer size, Long projectCode, Long workflowCode) {
    page = Optional.ofNullable(page).orElse(DolphinClientConstant.Page.DEFAULT_PAGE);
    size = Optional.ofNullable(size).orElse(DolphinClientConstant.Page.DEFAULT_SIZE);

    String url = dolphinAddress + "/projects/" + projectCode + "/process-instances";

    Query query = new Query();
    query
        .addParam("pageNo", String.valueOf(page))
        .addParam("pageSize", String.valueOf(size));
    if(workflowCode!=null){
      query.addParam("processDefineCode",String.valueOf(workflowCode));
    }

    try {
      HttpRestResult<JsonNode> restResult =
          dolphinsRestTemplate.get(url, getHeader(), query, JsonNode.class);
      PageInfo<ProcessInstanceQueryResp> pageInfo= JacksonUtils.parseObject(
          restResult.getData().toString(),
          new TypeReference<PageInfo<ProcessInstanceQueryResp>>() {});
      for (ProcessInstanceQueryResp instance : pageInfo.getTotalList()) {
        ProcessInstanceTasksQueryResp tasks = fetchTasksForProcessInstance(projectCode, instance.getId());
        instance.setProcessInstanceTasksQueryResp(tasks); // Assuming ProcessInstanceQueryResp has a setTaskList method
      }
      return pageInfo;
    } catch (Exception e) {
      throw new DolphinException("page dolphin scheduler process instance list fail", e);
    }
  }
  public ProcessInstanceTasksQueryResp fetchTasksForProcessInstance(Long projectCode, int processInstanceId) {
    String url = dolphinAddress + "/projects/" + projectCode + "/process-instances/" + processInstanceId + "/tasks";

    try {
      HttpRestResult<JsonNode> restResult =
              dolphinsRestTemplate.get(url, getHeader(), null, JsonNode.class);
      ProcessInstanceTasksQueryResp tasksResponse = JacksonUtils.parseObject(
              restResult.getData().toString(),
              ProcessInstanceTasksQueryResp.class);
      return tasksResponse;
    } catch (Exception e) {
      throw new DolphinException("fetch dolphin scheduler process instance tasks fail", e);
    }
  }

  /**
   * repeat run dolphin scheduler workflow instance
   *
   * @param projectCode project code
   * @param processInstanceId process instance id
   * @return true for success,otherwise false
   */
  public Boolean reRun(Long projectCode, Long processInstanceId) {
    log.info("repeat run workflow instance,id:{}", processInstanceId);
    return execute(projectCode, processInstanceId, DolphinClientConstant.ExecuteType.RE_RUN);
  }

  /**
   * stop dolphin scheduler workflow instance
   *
   * @param projectCode project code
   * @param processInstanceId process instance id
   * @return true for success,otherwise false
   */
  public Boolean stop(Long projectCode, Long processInstanceId) {
    log.info("stop workflow instance,id:{}", processInstanceId);
    return execute(projectCode, processInstanceId, DolphinClientConstant.ExecuteType.STOP);
  }

  /**
   * pause dolphin scheduler workflow instance
   *
   * @param projectCode project code
   * @param processInstanceId process instance id
   * @return true for success,otherwise false
   */
  public Boolean pause(Long projectCode, Long processInstanceId) {
    log.info("stop workflow instance,id:{}", processInstanceId);
    return execute(projectCode, processInstanceId, DolphinClientConstant.ExecuteType.PAUSE);
  }

  /**
   * execute dolphin scheduler workflow instance with custom execute type
   *
   * @param projectCode project code
   * @param processInstanceId process instance id
   * @param executeType {@link com.ruijie.cloud.macc.dataplatform.task.ds.core.DolphinClientConstant.ExecuteType}
   * @return true for success,otherwise false
   */
  public Boolean execute(Long projectCode, Long processInstanceId, String executeType) {
    String url = dolphinAddress + "/projects/" + projectCode + "/executors/execute";
    ProcessInstanceRunParam reProcessInstanceRunParam =
        new ProcessInstanceRunParam()
            .setProcessInstanceId(processInstanceId)
            .setExecuteType(executeType);
    try {
      HttpRestResult<String> restResult =
          dolphinsRestTemplate.postForm(url, getHeader(), reProcessInstanceRunParam, String.class);
      return restResult.getSuccess();
    } catch (Exception e) {
      throw new DolphinException(executeType + " dolphin scheduler process instance fail", e);
    }
  }

  public Boolean delete(Long projectCode, Long processInstanceId) {
    String url =
        dolphinAddress + "/projects/" + projectCode + "/process-instances/" + processInstanceId;
    try {
      HttpRestResult<String> restResult =
          dolphinsRestTemplate.delete(url, getHeader(), null, String.class);
      return restResult.getSuccess();
    } catch (Exception e) {
      throw new DolphinException("delete dolphin scheduler process instance fail", e);
    }
  }

  public ProcessInstanceLogResp log(Long processInstanceId, Long limit, Long skipLineNum) {
    limit = Optional.ofNullable(limit).orElse(DolphinClientConstant.Log.LIMIT);
    skipLineNum = Optional.ofNullable(skipLineNum).orElse(DolphinClientConstant.Log.SKIP_LINE_NUM);

    String url = dolphinAddress + "/log/detail";

    Query query = new Query();
    query
        .addParam("taskInstanceId", String.valueOf(processInstanceId))
        .addParam("limit", String.valueOf(limit))
        .addParam("skipLineNum", String.valueOf(skipLineNum));

    try {
      HttpRestResult<JsonNode> restResult =
          dolphinsRestTemplate.get(url, getHeader(), query, JsonNode.class);
      return JacksonUtils.parseObject(
          restResult.getData().toString(), new TypeReference<ProcessInstanceLogResp>() {});
    } catch (Exception e) {
      throw new DolphinException("page dolphin scheduler process instance list fail", e);
    }
  }
  /**
   * 根据ID获取单个流程实例的详细信息
   * 对应的API: GET /dolphinscheduler/projects/{projectCode}/process-instances/{id}
   *
   * @param projectCode 项目代码
   * @param processInstanceId 流程实例ID
   * @return 流程实例的详细信息，如果未找到则返回 null
   */
  public ProcessInstanceQueryResp getById(Long projectCode, Long processInstanceId) {
    // 1. 构造请求 URL
    String url = dolphinAddress + "/projects/" + projectCode + "/process-instances/" + processInstanceId;
    log.info("开始根据ID获取DS实例详情, URL: {}", url);

    try {
      // 2. 发起 GET 请求，期望返回 ProcessInstanceQueryResp 类型的对象
      HttpRestResult<ProcessInstanceQueryResp> restResult =
              dolphinsRestTemplate.get(url, getHeader(), null, ProcessInstanceQueryResp.class);

      // 3. 处理返回结果
      if (restResult.getSuccess()) {
        return restResult.getData();
      } else {
        // 如果DS API返回失败（例如404 Not Found），则记录日志并返回null
        log.error("根据ID ({}) 获取DS实例详情失败, DS API返回: {}", processInstanceId, restResult);
        return null;
      }
    } catch (Exception e) {
      // 捕获请求过程中的异常
      log.error("根据ID ({}) 获取DS实例详情时发生异常", processInstanceId, e);
      throw new DolphinException("根据ID获取DS流程实例失败", e);
    }
  }
}
