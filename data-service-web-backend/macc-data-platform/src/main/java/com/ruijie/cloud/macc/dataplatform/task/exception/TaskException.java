package com.ruijie.cloud.macc.dataplatform.task.exception;


import com.ruijie.cloud.dc.core.exception.base.BaseException;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2023/10/10 15:39
 **/
public class TaskException extends BaseException {
    private static final long serialVersionUID = 1L;

    public TaskException(Integer code, String msg,Object[] args)
    {
        super("Task", code, args, msg);
    }
    public TaskException(Integer code, String msg)
    {
        super("Task", code, null, msg);
    }

    public TaskException(TaskErrorCode iErrorCode,Object... args)
    {

        super("Task", iErrorCode.code(), args, iErrorCode.msg());

    }
}