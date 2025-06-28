package com.ruijie.cloud.macc.dataplatform.metadata.utils;

import java.util.UUID;

public class PasswordUtils {
	public static String encryptPassword(String password, String salt) {
		return AESCrypt.encrypt(password, salt);
	}

	public static String descryptPassword(String passwordEncrypt, String salt) {
		return AESCrypt.decrypt(passwordEncrypt, salt);
	}

	public static String generatePasswordSalt() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

	public static void main(String[] args) throws Exception {
		String aaaa = "fd12@!@!";
		String salt = generatePasswordSalt();

		System.out.println("salt = " + salt);
		String eny = encryptPassword(aaaa, salt);
		String des = descryptPassword(eny, salt);

		System.out.println("eny=" + eny);
		System.out.println("des=" + des);
	}
}
