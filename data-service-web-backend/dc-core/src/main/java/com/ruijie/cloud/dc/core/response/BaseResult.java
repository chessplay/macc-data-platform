package com.ruijie.cloud.dc.core.response;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * @author Hui 接口基础返回类
 */
@Data
public class BaseResult {
	private int code = 0;
	private String msg = "ok.";

	public BaseResult buildResult(ResultCode resultCode, Object... params) {
		code = resultCode.code();
		int start = 0;
		StringBuilder str = new StringBuilder();
		if (params != null && params.length > 0) {
			for (Object obj : params) {
				int q = StringUtils.indexOf(resultCode.msg(), "{}", start);

				if (q >= 0) {
					str.append(resultCode.msg().substring(start, q));
					str.append(String.valueOf(obj));
					start = q + 2;
				} else {
					break;
				}
			}
		}

		str.append(resultCode.msg().substring(start));
		this.msg = str.toString();
		return this;
	}

	public static BaseResult build(ResultCode resultCode, Object... params) {
		return new BaseResult().buildResult(resultCode, params);
	}
	public static BaseResult build(int c,String msg, Object... params){
		ResultCode dynamicError = new ResultCode() {
			// 动态参数
			private final int code = c;
			private final String message = msg;

			@Override
			public int code() {
				return code;
			}

			@Override
			public String msg() {
				return message;
			}
		};
		return new BaseResult().buildResult(dynamicError, params);
	}


}
