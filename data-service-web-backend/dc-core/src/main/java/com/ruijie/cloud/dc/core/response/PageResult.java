package com.ruijie.cloud.dc.core.response;

import java.util.List;

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
public class PageResult<T> extends BaseResult {
	private List<T> dataList;
	private int totalCount;
}
