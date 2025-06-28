package com.ruijie.cloud.dc.core.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author Hui
 *
 *	接口单实体返回类
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ObjectResult<T> extends BaseResult {
	private T data;
}
