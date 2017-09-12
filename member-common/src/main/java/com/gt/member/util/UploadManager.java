package com.gt.member.util;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.gt.api.util.ftp.ContinueFTP;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * 上传管理类
 * 
 * @author 伟杰
 *
 */
public class UploadManager {
	public static final String ALLOW_IMAGE_TYPE_PNG = "png"; // 允许的图片类
	public static final String ALLOW_IMAGE_TYPE_JPG = "jpg"; // 允许的图片累心
	public static final String ALLOW_IMAGE_TYPE_JPEG = "jpeg"; // 允许的图片累心
	public static final String ALLOW_IMAGE_TYPE_GIF = "gif"; // 允许的图片累心
	private static UploadManager manager = null;

	private UploadManager() {
		super();
	}

	public static UploadManager getInstall() {
		if (manager == null) {
			manager = new UploadManager();
		}
		return manager;
	}

	Logger logger = Logger.getLogger(UploadManager.class);
	
	/**
	 * 剪切并且保存图片
	 * @param in		图片流	
	 * @param path		保存图片路径
	 * @param imageType 图片类型
	 * @param width		剪切宽度
	 * @param height	剪切高度
	 * @return
	 */
	private List<Integer> cut(InputStream in, String path, String imageType, int width, int height){
		 try {
			BufferedImage bufImg = ImageIO.read(in);
			int oWidth 		= bufImg.getWidth(); 		// 原图宽
			int oHeight 	= bufImg.getHeight(); 		// 原图高
			int cWidth 		= width;					// 剪切图片宽度				
			int cHeight 	= height;					// 剪切后图片高度
			int x = 0;
			int y = 0;
			double oR = new Double(oWidth)/ new Double(oHeight); // 原图宽高比
			double r = new Double(width) / new Double(height);	 // 剪切宽高比
			
			// 当宽高都是 大于剪切宽高时, 进行压缩
			if(oWidth > width && oHeight > height){
				if(oR > r){	// 宽度偏长		
					cWidth =  new Double(new Double(height) / new Double(oHeight) * oWidth).intValue() ;
				}else if(oR < r){ // 高度偏长
					cHeight = new Double(new Double(width) / new Double(oWidth) * oHeight).intValue();
				}
				// 压缩图片
				BufferedImage image = new BufferedImage(cWidth, cHeight,BufferedImage.TYPE_INT_RGB );   
		        image.getGraphics().drawImage(bufImg, 0, 0, cWidth, cHeight, null); // 绘制缩小后的图 
		        oWidth = cWidth;
		        oHeight = cHeight;
		        bufImg = image ;
		        image = null;
			}else if(oHeight > height && oWidth <= width){ 	// 高偏长. 宽相等或者偏小
				width = oWidth;
			}else if(oWidth > width && oHeight <= height){	// 宽偏长. 高相等或者偏小
				height = oHeight;
			}else{ 											// 宽度长度都偏小
				width = oWidth;
				height = oHeight;
			}
			
			// 剪切标准判断
			if (width < oWidth) { 	    	// 宽度超出范围
				x = (oWidth - width) / 2;
				y = 0;
			} else if (height < oHeight) { 	// 高度超出范围
				y = (oHeight - height) / 2;
				x = 0;
			} else{ 											// 正常
				x = 0;
				y = 0;
			}
			
			File file =  new File(path);
			// 图片剪切
			BufferedImage subImg = bufImg.getSubimage(x, y, width, height) ;
			ImageIO.write(subImg, imageType, file);
			ContinueFTP myFtp = new ContinueFTP();
			try {
			    myFtp.upload( file.getPath(),PropertiesUtil.getStatic_source_ftp_ip(),PropertiesUtil.getStatic_source_ftp_port(),PropertiesUtil.getStatic_source_ftp_user(),PropertiesUtil.getStatic_source_ftp_password());
			} catch (Exception e) {
				e.printStackTrace();
			}
			bufImg = null;
			subImg = null;
			int size = new Long(file.length() / 1024).intValue();
			// 封装剪切后的信息
			List<Integer> list = new ArrayList<>();
			list.add(width);
			list.add(height);
			list.add(size);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 剪切并且保存图片
	 * @param mpf  		spring对象
	 * @param rootDir	图片根路径
	 * @param id		用户ID
	 * @param width		宽度
	 * @param height	高度
	 * @return
	 */
	public Image paserImage(MultipartFile mpf, String rootDir, int id, int width, int height){
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paserImage called");
			}
			// 参数验证
			if (mpf.isEmpty() || StringUtils.isEmpty(rootDir)) {
				throw new Exception("paserImage invaild params");
			}
			// file
			String originalFileExtension = mpf.getOriginalFilename()
					.substring(mpf.getOriginalFilename().lastIndexOf(".") + 1);
			String name = mpf.getName();
			
			// extension
			if (!(ALLOW_IMAGE_TYPE_JPG.equalsIgnoreCase(originalFileExtension) || ALLOW_IMAGE_TYPE_PNG.equalsIgnoreCase(originalFileExtension)
					|| ALLOW_IMAGE_TYPE_JPEG.equalsIgnoreCase(originalFileExtension) || ALLOW_IMAGE_TYPE_GIF.equalsIgnoreCase(originalFileExtension))) {
				throw new Exception("paserImage invaild image type");
			}
			// create new file by date an user id
			String secondDir = id + "//"+ DateTimeKit.getDateTime(new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD);
			File dir = new File(rootDir + "//"+ secondDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String newFileName = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase() + "." + originalFileExtension;
			
			List<Integer> info = null;
			// cut image
			if((info = cut(mpf.getInputStream(), rootDir + "//"+ secondDir + "//"+ newFileName, originalFileExtension, width, height)) == null){
				throw new Exception("cut image fail");
			}

			// init image
			Image image = new Image();
			image.setWidth(info.get(0));
			image.setHeight(info.get(1));
			image.setSize(info.get(2));
			image.setName(name);
			image.setPath(secondDir + "//"+ newFileName);

			return image;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *            spring文件分装类
	 * @param rootDir
	 *            图片根目录
	 * @return
	 */
	public Image paserImage(MultipartFile mpf, String rootDir) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("paserImage called");
			}
			// 参数验证
			if (mpf.isEmpty() || StringUtils.isEmpty(rootDir)) {
				throw new Exception("paserImage invaild params");
			}
			// file
			String originalFileExtension = mpf.getOriginalFilename()
					.substring(mpf.getOriginalFilename().lastIndexOf(".") + 1);
			String name = mpf.getName();
			long size = mpf.getSize() / 1024;

			// extension
			if ( !(ALLOW_IMAGE_TYPE_JPG.equalsIgnoreCase(originalFileExtension)
					|| ALLOW_IMAGE_TYPE_PNG.equalsIgnoreCase(originalFileExtension)
					  || ALLOW_IMAGE_TYPE_JPEG.equalsIgnoreCase(originalFileExtension) 
					    || ALLOW_IMAGE_TYPE_GIF.equalsIgnoreCase(originalFileExtension))) {
				throw new Exception("paserImage invaild image type");
			}

			// create new file by date
			File dir = new File(rootDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String newFileName = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase() + "." + originalFileExtension.toLowerCase();
			File imageFile = new File(dir, newFileName);
			// move image to new Image File
			mpf.transferTo(imageFile);
			ContinueFTP myFtp = new ContinueFTP();
		   	 myFtp.upload( imageFile.getPath(),PropertiesUtil.getStatic_source_ftp_ip(),PropertiesUtil.getStatic_source_ftp_port(),PropertiesUtil.getStatic_source_ftp_user(),PropertiesUtil.getStatic_source_ftp_password());
			// get image info
			BufferedImage imageBuff = ImageIO.read(imageFile);
			int width = imageBuff.getWidth();
			int height = imageBuff.getHeight();

			// init image
			Image image = new Image();
			image.setHeight(height);
			image.setWidth(width);
			image.setSize(size);
			image.setName(name);
			image.setPath(rootDir + "/" + newFileName);

			return image;
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}

	}

	/**
	 * 图片实体类
	 * 
	 * @author 伟杰
	 *
	 */
	public class Image {
		/**
		 * 图片路径
		 */
		private String path;
		/**
		 * 图片名字
		 */
		private String name;
		/**
		 * 图片类型
		 */
		private String type;
		/**
		 * 宽度
		 */
		private int width;
		/**
		 * 高度
		 */
		private int height;
		/**
		 * 大小 /kb
		 */
		private long size;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}
	};

	/**
	 * 视频类
	 * 
	 * @author 伟杰
	 *
	 */
	public class Video {
		/**
		 * 存放路径
		 */
		private String path;
		/**
		 * 视频名称
		 */
		private String name;
		/**
		 * 时长
		 */
		private long duration;
		/**
		 * 大小 /kb
		 */
		private long size;
		/**
		 * 数据类型
		 */
		private String type;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getDuration() {
			return duration;
		}

		public void setDuration(long duration) {
			this.duration = duration;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	} ;
	
	/**
	 * 音频类
	 * 
	 * @author 伟杰
	 *
	 */
	public class Voice {
		/**
		 * 存放路径
		 */
		private String path;
		/**
		 * 音频名称
		 */
		private String name;
		/**
		 * 时长
		 */
		private long duration;
		/**
		 * 大小 /kb
		 */
		private long size;
		/**
		 * 数据类型
		 */
		private String type;
		
		public String getPath() {
			return path;
		}
		
		public void setPath(String path) {
			this.path = path;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public long getDuration() {
			return duration;
		}
		
		public void setDuration(long duration) {
			this.duration = duration;
		}
		
		public long getSize() {
			return size;
		}
		
		public void setSize(long size) {
			this.size = size;
		}
		
		public String getType() {
			return type;
		}
		
		public void setType(String type) {
			this.type = type;
		}
		
	}
	
	
	/**
	 * 删除文件
	 * @param path 路径(绝对路径)
	 * @return 返回 -1: 删除失败出现异常 0:该路径是管理后台的资源，不能删除，可继续下一步操作 1:删除文件(夹)成功 ,2:目录不存在
	 */
	public static int  delFile(String path){
		int imgIndex=(path==null||path.isEmpty())?-2:path.indexOf("upload/1");
		int result=-1;
		if(imgIndex>0){//管理后台的图片，不能删除
			result=0;
		}else if(imgIndex==-2){//path为空
			result=1;
		}else{
		// String tempPth=getPath(path);
		 File file=new File(path);
		 if(file.exists()&&file.isDirectory()){//当path是一个目录时,先通过递归的方式删除目录下的所有文件
				result=deleteDir(file)?1:-1;
		  }else if(file.exists()&&file.isDirectory()==false) {//当好path是一个文件时，直接删除文件
				result=file.delete()?1:-1;
		  }else{
			  result=2;
		  }
		}
		return result;
	}
	
	/**
	 * 获取路径
	 * @param path
	 * @return
	 */
//	private static   String getPath(String path){
//		StringBuffer tempPath=new StringBuffer("");
//		if(path.indexOf("image/2")>=0){
//			tempPath.append(PropertiesUtil.getResImagePath()+path.split("image")[1]);
//		}else if(path.indexOf("voice/2")>=0){
//			tempPath.append(PropertiesUtil.getResVoicePath()+path.split("voice")[1]);
//		}else if(path.indexOf("video/2")>=0){
//			tempPath.append(PropertiesUtil.getResVideoPath()+path.split("video")[1]);
//		}else if(path.indexOf("editor/2")>=0){
//			tempPath.append(PropertiesUtil.getResEditorPath()+path.split("editor")[1]);
//		}
//		return tempPath.toString();
//	}
	
	/**
    * 递归删除目录下的所有文件及子目录下所有文件
    * @param dir 将要删除的文件目录
    * @return boolean Returns "true" if all deletions were successful.
    *                 If a deletion fails, the method stops attempting to
    *                 delete and returns "false".
    */
   private static boolean deleteDir(File dir) {
       if (dir.isDirectory()) {
           String[] children = dir.list();
           	//递归删除目录中的子目录下
           for (int i=0; i<children.length; i++) {
               boolean success = deleteDir(new File(dir, children[i]));
               if (!success) {
                   return false;
               }
           }
       }
       // 目录此时为空，可以删除
       return dir.delete();
   }
   
   /**
    * 判断图片类型和大小
    * @param file
    * @return
    */
   public static String checkFile(MultipartFile file) {
	   String result = "";
	   String contentType = file.getContentType();
	   String cType[] = contentType.split("/");
	   if(file.getSize() > 0){
			if(!(cType[1].equals(ALLOW_IMAGE_TYPE_PNG) || cType[1].equals(ALLOW_IMAGE_TYPE_JPG) 
					|| cType[1].equals(ALLOW_IMAGE_TYPE_GIF) || cType[1].equals(ALLOW_IMAGE_TYPE_JPEG))){
				result = "1";	//图片类型只限于png、jpg、gif、jpeg。
			}else{
				long size = (file.getSize())/1024;
				if(Math.round(size) > 1024){
					result = "2";	//图片不能大于1M。
				}
			}
	   }
	   return result;
   }
}
