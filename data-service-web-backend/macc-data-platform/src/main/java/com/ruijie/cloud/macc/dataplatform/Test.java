package com.ruijie.cloud.macc.dataplatform;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

public class Test {
	public static void main(String[] args) throws IORuntimeException, FileNotFoundException {
		byte[] bytes = IoUtil.readBytes(new FileInputStream("C:\\Users\\wuchaohui\\Desktop\\aaa.bin"));
		System.out.println(new String(bytes));
	}
}
