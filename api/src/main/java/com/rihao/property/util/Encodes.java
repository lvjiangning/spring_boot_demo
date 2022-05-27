/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.rihao.property.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * 封装各种格式的编码解码工具类.
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自制的base62 编码
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 * @author calvin
 * @version 2013-01-15
 */
public class Encodes {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final Logger logger = LoggerFactory.getLogger(Encodes.class);

	/**
	 * Hex编码.
	 */
	public static String encodeHex(byte[] input) {
		return new String(Hex.encodeHex(input));
	}

	/**
	 * Hex解码.
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			logger.error(e.getMessage(),e);
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}
	
	/**
	 * Base64编码.
	 */
	public static String encodeBase64(String input) {
		try {
			return new String(Base64.encodeBase64(input.getBytes(DEFAULT_URL_ENCODING)));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
			return "";
		}
	}

//	/**
//	 * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
//	 */
//	public static String encodeUrlSafeBase64(byte[] input) {
//		return Base64.encodeBase64URLSafe(input);
//	}

	/**
	 * Base64解码.
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input.getBytes());
	}
	
	/**
	 * Base64解码.
	 */
	public static String decodeBase64String(String input) {
		try {
			return new String(Base64.decodeBase64(input.getBytes()), DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
			return "";
		}
	}

	/**
	 * Base62编码。
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}

	/**
	 * Html 转码.
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码.
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml10(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * URL 编码, Encode默认为UTF-8. 
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8. 
	 */
	public static String urlDecode(String part) {

		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(),e);
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 人社局特供加密代码
	 */

	/**
	 * 加密算法
	 */
	public static String encrypt(String txt, String key) {
		String encrypt_key = "0f9cfb7a9acced8a4167ea8006ccd098";
		int ctr = 0;
		String tmp = "";
		int i;
		for (i = 0; i < txt.length(); i++) {
			ctr = (ctr == encrypt_key.length()) ? 0 : ctr;
			tmp = tmp + encrypt_key.charAt(ctr)
					+ (char) (txt.charAt(i) ^ encrypt_key.charAt(ctr));
			ctr++;
		}

		return encodeBase64(key(tmp, key));
	}


	/**
	 * 解密算法
	 */
	public static String decrypt(String cipherText, String key) {
		// base64解码
		try{
			cipherText = decodeBase64String(cipherText);
			cipherText = key(cipherText, key);
			String tmp = "";
			for (int i = 0; i < cipherText.length(); i++) {
				int c = cipherText.charAt(i) ^ cipherText.charAt(i + 1);
				String x = "" + (char) c;

				tmp += x;
				i++;
			}
			return tmp;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		return null;
	}


	public static String key(String txt, String encrypt_key) {
		encrypt_key = strMD5(encrypt_key);
		int ctr = 0;
		String tmp = "";
		for (int i = 0; i < txt.length(); i++) {
			ctr = (ctr == encrypt_key.length()) ? 0 : ctr;
			int c = txt.charAt(i) ^ encrypt_key.charAt(ctr);
			String x = "" + (char) c;
			tmp = tmp + x;
			ctr++;
		}
		return tmp;
	}

	public static final String strMD5(String s) {
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		return null;
	}

	//人社局代码结束
}
