package com.ruijie.cloud.macc.dataplatform.task.ds.remote.request;

import com.ruijie.cloud.macc.dataplatform.task.ds.remote.RequestHttpEntity;
import com.ruijie.cloud.macc.dataplatform.task.ds.remote.response.HttpClientResponse;

import java.io.Closeable;
import java.net.URI;

public interface HttpClientRequest extends Closeable {

  HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity)
      throws Exception;
}
