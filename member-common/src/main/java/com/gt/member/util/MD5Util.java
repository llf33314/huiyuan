package com.gt.member.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MD5Util {

	private static char hexDigits[]    = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	private static char hexDigitsSM[]    = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	 
	  // 获取32位MD5
	  public final static String getMD5(String str) {
	    String md5Hex  = null;
	    try {
	      byte[] md5Digits = getMD5Digits(str);
	      md5Hex = getHex(md5Digits);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return md5Hex;
	  }
	  
	  // 获取32位MD5 小写
	  public final static String getMD5SM(String str) {
		  String md5Hex  = null;
		  try {
			  byte[] md5Digits = getMD5Digits(str);
			  md5Hex = getHexSM(md5Digits);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		  return md5Hex;
	  }
	 
	   
	   
	  // 获取8位MD5
	  public final static String getSmallMD5(String str) {
	    String md5Hex  = null;
	    try {
	      byte[] md5Digits = getMD5Digits(str);
	      md5Hex = getHex(md5Digits, 4);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return md5Hex;
	  }
	 
	  // 获取文件32位MD5
	  public final static String getFileMD5(String filename) {
	    String md5Hex  = null;
	    try {
	      byte[] md5Digits = getMD5Digits(new File(filename));
	      md5Hex = getHex(md5Digits);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return md5Hex;
	  }
	  
	  private static byte[] getMD5Digits(String str) throws NoSuchAlgorithmException {
	    byte[] btInput = str.getBytes();
	    MessageDigest mdInst = MessageDigest.getInstance("MD5");
	    mdInst.update(btInput);
	    return mdInst.digest();
	  }
	 
	  private static byte[] getMD5Digits(File file) throws IOException, NoSuchAlgorithmException {  
	    FileInputStream inputStream = new FileInputStream(file);  
	    FileChannel fileChannel = inputStream.getChannel();  
	    MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
	    MessageDigest mdInst = MessageDigest.getInstance("MD5");
	    mdInst.update(byteBuffer);
	    return mdInst.digest(); 
	  }
	 
	  private static String getHex(byte[] buffer) {
	    return getHex(buffer, buffer.length);
	  }
	  private static String getHex(byte[] buffer, int len) {
	    char str[] = new char[len * 2];
	    int k = 0;
	    for (int i = 0; i < len; i++) {
	      byte byte0 = buffer[i];
	      str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	      str[k++] = hexDigits[byte0 & 0xf];
	    }
	    return new String(str);
	  }
	  
	  private static String getHexSM(byte[] buffer) {
	    return getHexSM(buffer, buffer.length);
	  }
	  
	  private static String getHexSM(byte[] buffer, int len) {
	    char str[] = new char[len * 2];
	    int k = 0;
	    for (int i = 0; i < len; i++) {
	      byte byte0 = buffer[i];
	      str[k++] = hexDigitsSM[byte0 >>> 4 & 0xf];
	      str[k++] = hexDigitsSM[byte0 & 0xf];
	    }
	    return new String(str);
	  }
	 
	  public static void main(String[] args) {
	    System.out.println(MD5Util.getMD5("Duofen12580Df"));
	    System.out.println(MD5Util.getSmallMD5("pay"));
	    String str="A7D0A25023A47AF65F86769602D4dfpt";
	    System.out.println(str.length());		
	  }
}
