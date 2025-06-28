package com.ruijie.cloud.dc.core.response;

import com.ruijie.cloud.dc.core.response.ResultCode;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/04/07 14:54
 **/
public enum BaseResultCode implements ResultCode {
    OK(0, "操作成功"),
    METHOD_NOT_SUPPORTED(405, "请求方式不支持"),
    DUPLICATE_KEY(409, "数据库中已存在该记录"),
    MYBATIS_SYSTEM_ERROR(500, "Mybatis系统异常"),
    MISSING_PATH_VARIABLE(400, "请求路径中缺少必需的路径变量"),
    ARGUMENT_TYPE_MISMATCH(400, "请求参数类型不匹配"),
    RUNTIME_EXCEPTION(500, "发生异常,{}"),
    SYSTEM_ERROR(500, "发生系统异常"),
    VALIDATION_ERROR(400, "数据验证失败"),
    MESSAGE_NOT_READABLE(400, "请求格式错误或不可读");

    private final int code;
    private final String msg;

    BaseResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String msg() {
        return this.msg;
    }
}
