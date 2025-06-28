package com.ruijie.cloud.macc.dataplatform.task.ds.remote;

import com.google.common.base.Strings;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

public enum BaseHttpMethod {
  GET(HttpMethod.GET) {
    @Override
    protected HttpRequestBase createRequest(String url) {
      return new HttpGet(url);
    }
  },

  POST(HttpMethod.POST) {
    @Override
    protected HttpRequestBase createRequest(String url) {
      return new HttpPost(url);
    }
  },

  PUT(HttpMethod.PUT) {
    @Override
    protected HttpRequestBase createRequest(String url) {
      return new HttpPut(url);
    }
  },

  PATCH(HttpMethod.PATCH) {
    @Override
    protected HttpRequestBase createRequest(String url) {
      return new HttpPatch(url);
    }
  },

  DELETE(HttpMethod.DELETE) {
    @Override
    protected HttpRequestBase createRequest(String url) {
      return new HttpDelete(url);
    }
  };

  private String name;

  BaseHttpMethod(String name) {
    this.name = name;
  }

  public HttpRequestBase init(String url) {
    return createRequest(url);
  }

  protected HttpRequestBase createRequest(String url) {
    throw new UnsupportedOperationException();
  }

  /**
   * get base http method by name
   *
   * @param name
   * @return
   */
  public static BaseHttpMethod of(String name) {
    if (!Strings.isNullOrEmpty(name)) {
      for (BaseHttpMethod method : BaseHttpMethod.values()) {
        if (name.toLowerCase().equals(method.name.toLowerCase())) {
          return method;
        }
      }
    }
    throw new IllegalArgumentException("Unsupported http method : " + name);
  }
}
