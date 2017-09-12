package com.gt.member.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 二维码下载
 *
 * @author 李逢喜
 * @version 创建时间：2015年9月7日 下午7:14:20
 */
public class URLConnectionDownloader {

    @Autowired
    private PropertiesUtil propertiesUtil;

    private static Image srcImage = null;
    private static File  destFile = null;

    private static int imageWidth  = 0;
    private static int imageHeight = 0;

    /**
     * 下载文件到本地
     *
     * @param urlString 被下载的文件地址
     * @param filename  本地文件名
     *
     * @throws Exception 各种异常
     */
    public static void download( String urlString, String filename ) throws Exception {
	// 构造URL
	URL url = new URL( urlString );
	// 打开连接
	HttpURLConnection con = (HttpURLConnection) url.openConnection();
	// 输入流
	InputStream is = con.getInputStream();
	// 1K的数据缓冲
	byte[] bs = new byte[1024];
	// 读取到的数据长度
	int len;
	// 输出的文件流
	OutputStream os = new FileOutputStream( filename );
	// 开始读取
	while ( ( len = is.read( bs ) ) != -1 ) {
	    os.write( bs, 0, len );
	}
	// 完毕，关闭所有链接
	os.close();
	is.close();
    }

    /**
     * 检测当前URL是否可连接或是否有效
     *
     * @param url 指定URL网络地址
     *
     * @return 如果链接有效则返回地址，无效则返回Null
     * @throws IOException
     */
    public static String isConnect( String url ) {
	String succ = null;
	try {
	    if ( url == null || url.length() <= 0 ) {
		return succ;
	    }
	    URL urlStr = new URL( url );
	    HttpURLConnection connection = (HttpURLConnection) urlStr.openConnection();
	    int state = connection.getResponseCode();
	    if ( state == 200 ) {
		succ = connection.getURL().toString();
	    }
	} catch ( Exception e ) {
	    return null;
	}
	return succ;
    }

    /**
     * 临时下载图片
     *
     * @param urlString 图片访问路径
     *
     * @return
     * @throws Exception
     */
    public static String download( String urlString ) throws Exception {
	String format = urlString.substring( urlString.lastIndexOf( "." ) + 1, urlString.length() );
	System.out.println( "format:" + format );
	String path = PropertiesUtil.getRes_image_path() + "/temp";
	String resultPath = path + "/" + System.currentTimeMillis() + "." + format;
	File file = new File( path );
	if ( !file.exists() ) {
	    file.mkdirs();
	}
	download( urlString, resultPath );
	return resultPath;
    }

    /**
     * 临时下载图片
     *
     * @param urlString 图片访问路径
     * @param serverurl 服务器存放路径
     * @param w         宽
     * @param h         高
     *
     * @return
     * @throws Exception
     */
    public static String downloadRqcode( String urlString, String serverurl, int w, int h ) throws Exception {
	String format = urlString.substring( urlString.lastIndexOf( "." ) + 1, urlString.length() );
	System.out.println( "format:" + format );
	String resultPath = serverurl + "/" + System.currentTimeMillis() + ".jpg";
	System.out.println( resultPath );
	File file = new File( serverurl );
	if ( !file.exists() ) {
	    file.mkdirs();
	}
	download( urlString, resultPath );
	String url = serverurl + "/" + System.currentTimeMillis() + ".jpg";
	init( new File( resultPath ), url );
	resize( w, h );
	UploadManager.delFile( resultPath );
	System.out.println( "url:" + url );
	return url;
    }

    public static void init( File fileName, String path ) throws IOException {
	File _file = fileName;
	_file.setReadOnly();
	File srcFile = _file;
	String fileSuffix = _file.getName().substring( ( _file.getName().indexOf( "." ) + 1 ), ( _file.getName().length() ) );
	destFile = new File( path );
	srcImage = javax.imageio.ImageIO.read( _file );
	//得到图片的原始大小， 以便按比例压缩。
	imageWidth = srcImage.getWidth( null );
	imageHeight = srcImage.getHeight( null );
	System.out.println( "width: " + imageWidth );
	System.out.println( "height: " + imageHeight );
    }

    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param w int 新宽度
     * @param h int 新高度
     *
     * @throws IOException
     */
    public static void resize( int w, int h ) throws IOException {
	//得到合适的压缩大小，按比例。
	if ( imageWidth >= imageHeight ) {
	    w = w;
	    h = (int) Math.round( ( imageHeight * w * 1.0 / imageWidth ) );
	} else {
	    h = h;
	    w = (int) Math.round( ( imageWidth * h * 1.0 / imageHeight ) );
	}

	//构建图片对象
	BufferedImage _image = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
	//绘制缩小后的图
	_image.getGraphics().drawImage( srcImage, 0, 0, w, h, null );
	//输出到文件流
	FileOutputStream out = new FileOutputStream( destFile );
	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( out );
	encoder.encode( _image );
	out.flush();
	out.close();

    }

    public static void main( String[] args ) {
	String url = "http://wx.qlogo.cn/mmopen/ajNVdqHZLLCCf4dqcCCe7dIRIOpVZvgQ5NZySGgPfsibxNiaQhunnusJMGfT7V5OZW0hvWCd6BdsE2ubLHibgVcOg/0";
	try {
	    downloadRqcode( url, "D:/tomcat/webapps/upload/temp", 40, 40 );
	    //			init(new File("D:/tomcat/webapps/upload/temp/1482749124521.jpg"),"D:/tomcat/webapps/upload/temp");
	    //			resize(40,40);
	} catch ( Exception e ) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
