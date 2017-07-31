//package com.gt.member.service.old.member.export;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//import java.util.Enumeration;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;
//import java.util.zip.ZipOutputStream;
//
//import org.springframework.stereotype.Component;
///**
// * 文件压缩 解压util
// *
// */
//@Component
//public class ZipUtil {
//	public boolean zip(String zipFilePath, String[] fromZipFileArray) throws Exception {
//		File toZipFile = new File(zipFilePath);
//		if (!toZipFile.exists()) {
//			toZipFile.createNewFile();
//		}
//		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(toZipFile));
//		for (String str : fromZipFileArray) {
//			this.writeToZip(out, new File(str), "");
//		}
//		out.close();
//		return true;
//	}
//
//	private void writeToZip(ZipOutputStream out, File fromZipFile, String base) throws Exception {
//		if (fromZipFile.isDirectory()) {
//			for (File file : fromZipFile.listFiles()) {
//				this.writeToZip(out, file, base + fromZipFile.getName() + File.separator);
//			}
//		} else {
//			// add this file
//			this.addFileToZip(out, fromZipFile, base + fromZipFile.getName());
//		}
//	}
//
//	private void addFileToZip(ZipOutputStream out, File file, String base) throws Exception {
//		byte[] buff = new byte[1024];
//		int bytesRead = -1;
//		ZipEntry entry = new ZipEntry(base);
//		out.putNextEntry(entry);
//		InputStream in = new BufferedInputStream(new FileInputStream(file));
//		while (-1 != (bytesRead = in.read(buff, 0, buff.length))) {
//			out.write(buff, 0, bytesRead);
//		}
//		in.close();
//		out.flush();
//	}
//
//	public void unzip(String zipFilePath, String unzipFilePath) throws Exception {
//		ZipFile zipfile = new ZipFile(zipFilePath,Charset.defaultCharset());
//		Enumeration enu = zipfile.entries();
//		while (enu.hasMoreElements()) {
//			ZipEntry entry = (ZipEntry) enu.nextElement();
//			this.writeToDir(zipfile, entry,
//					new File(unzipFilePath + File.separator + entry.getName()));
//
//		}
//	}
//
//	private void writeToDir(ZipFile zip, ZipEntry entry, File toFile) throws Exception {
//		if (!entry.isDirectory()) {
//			File file = toFile.getParentFile();
//			if (!file.exists()) {
//				file.mkdirs();
//			}
//			FileOutputStream fos = new FileOutputStream(toFile);
//			byte[] buffer = new byte[1024];
//			InputStream is = zip.getInputStream(entry);
//			int len;
//			while ((len = is.read(buffer, 0, buffer.length)) != -1) {
//				fos.write(buffer, 0, len);
//			}
//			fos.close();
//		}
//	}
//}
