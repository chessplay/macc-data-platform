package com.ruijie.cloud.dc.core.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BooleanResult extends BaseResult {
	private boolean result;
}
