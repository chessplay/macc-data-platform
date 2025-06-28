package com.ruijie.cloud.macc.dataplatform.task.ds.core;

import com.ruijie.cloud.macc.dataplatform.task.ds.remote.DolphinsRestTemplate;
import com.ruijie.cloud.macc.dataplatform.task.ds.remote.Header;

public abstract class AbstractOperator {

  protected final String dolphinAddress;

  private final String token;

  protected final DolphinsRestTemplate dolphinsRestTemplate;

  public AbstractOperator(
      String dolphinAddress, String token, DolphinsRestTemplate dolphinsRestTemplate) {
    this.dolphinAddress = dolphinAddress;
    this.token = token;
    this.dolphinsRestTemplate = dolphinsRestTemplate;
  }

  /**
   * get header for dolphin scheduler
   *
   * @return
   */
  protected Header getHeader() {
    Header header = Header.newInstance();
    header.addParam("token", this.token);
    return header;
  }
}
