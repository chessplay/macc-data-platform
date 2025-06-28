package com.ruijie.cloud.dc.core.response;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author Hui
 *	
 * 接口返回列表类的通用类
 *
 * @param <T>
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ListResult<T> extends BaseResult {
	private List<T> list;
}
