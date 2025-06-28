package com.ruijie.cloud.macc.dataplatform.metadata.utils;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

public class AESCrypt {
	private static final String ALGORITHM = "AES";
	private static final int KEY_SIZE = 128;

	/**
	 * 转换密钥
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key toKey(byte[] key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
		return secretKey;
	}

	/**
	 * 生成随机密钥
	 *
	 * @return
	 * @throws Exception
	 */
	private static String getSecretKey() throws Exception {
		return getSecretKey(null);
	}

	/**
	 * 生成密钥
	 *
	 * @param seed 密钥种子
	 * @return
	 * @throws Exception
	 */
	private static String getSecretKey(String seed) throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		if (seed != null && !"".equals(seed)) {
			secureRandom.setSeed(seed.getBytes());
		}

		keyGenerator.init(KEY_SIZE, secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		return Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}

	/**
	 * 加密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptInternal(byte[] data, String key) throws Exception {
		Key k = toKey(Base64.getDecoder().decode(key));
		byte[] raw = k.getEncoded();
		SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		return cipher.doFinal(data);
	}

	public static String encrypt(String encryptData, String key) {
		String result = null;

		if (StringUtils.isEmpty(encryptData)) {
			return result;
		}

		try {
			String aesKey;

			if (StringUtils.isEmpty(key)) {
				aesKey = getSecretKey();
			} else {
				aesKey = getSecretKey(key);
			}

			byte[] inputData = encryptData.getBytes();
			byte[] outputData = encryptInternal(inputData, aesKey);
			result = Base64.getEncoder().encodeToString(outputData);
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	/**
	 * 解密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] decryptInternal(byte[] data, String key) throws Exception {
		Key k = toKey(Base64.getDecoder().decode(key));
		byte[] raw = k.getEncoded();
		SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		return cipher.doFinal(data);
	}

	public static String decrypt(String decryptData, String key) {
		String result = null;

		if (decryptData == null) {
			return result;
		}

		try {
			String aesKey;

			if (StringUtils.isEmpty(key)) {
				aesKey = getSecretKey();
			} else {
				aesKey = getSecretKey(key);
			}

			byte[] inputData = Base64.getDecoder().decode(decryptData);
			byte[] outputData = decryptInternal(inputData, aesKey);
			result = new String(outputData);
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}
}
