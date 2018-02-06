package com.gt.member.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.Member;
import com.gt.api.util.ftp.ContinueFTP;
import com.gt.member.entity.MemberEntity;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import org.springframework.web.multipart.MultipartFile;

/**
 * 获取登录用户信息
 * 
 * @author Administrator
 * 
 */
public class CommonUtil {
    private static final Logger log = Logger.getLogger( CommonUtil.class );

    public static String webRealPath;
    public final static String[] AGENT = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser" };

    @SuppressWarnings( "unused" )
    private static List< Map< String,Object > > dic = new ArrayList< Map< String,Object > >();

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
	return applicationContext;
    }

    public static void setApplicationContext( ApplicationContext applicationContext ) {
	CommonUtil.applicationContext = applicationContext;
    }

    /**
     * 获取图片存放域名
     *
     * @param request
     */
    public static void getImageHomePath( HttpServletRequest request ) {
	request.setAttribute( "resourceUrl", request.getServletContext().getAttribute( "resourceUrl" ) );
    }

    /**
     * 判断对象是否为空
     *
     * @param obj
     *
     * @return
     */
    public static boolean isEmpty( Object obj ) {
	boolean b = false;
	try {
	    if ( obj == null || "".equals( obj ) ) {
		b = true;
	    } else {
		b = false;
	    }
	} catch ( Exception e ) {
	    b = false;
	    e.printStackTrace();
	}
	return b;
    }

    /**
     * 判断对象是否不为空
     *
     * @param obj
     *
     * @return
     */
    public static boolean isNotEmpty( Object obj ) {
	boolean b = false;
	try {
	    if ( obj == null || "".equals( obj ) ) {
		b = false;
	    } else {
		b = true;
	    }
	} catch ( Exception e ) {
	    b = false;
	    e.printStackTrace();
	}
	return b;
    }

    /**
     * 转Integer
     *
     * @param obj
     */
    public static Integer toInteger( Object obj ) {
	try {
	    if ( !isEmpty( obj ) ) {
		return Integer.parseInt( obj.toString() );
	    } else {
		throw new Exception( "对象为空，转换失败！" );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 转String
     *
     * @param obj
     */
    public static String toString( Object obj ) {
	try {
	    if ( !isEmpty( obj ) ) {
		return obj.toString();
	    } else {
		throw new Exception( "对象为空，转换失败！" );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 转String
     *
     * @param obj
     */
    public static boolean toBoolean( Object obj ) {
	try {
	    if ( !isEmpty( obj ) ) {
		return (Boolean) obj;
	    } else {
		throw new Exception( "对象为空，转换失败！" );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return false;
    }

    /**
     * 转Double
     *
     * @param obj
     */
    public static Double toDouble( Object obj ) {
	try {
	    if ( !isEmpty( obj ) ) {
		return Double.parseDouble( obj.toString() );
	    } else {
		throw new Exception( "对象为空，转换失败！" );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 校验是否是double数据
     */
    public static boolean isDouble( Object obj ) {
	try {
	    Double.parseDouble( obj.toString() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * 根据网络图片URL返回信息
     *
     * @param imgurl
     *
     * @return
     */
    public static BufferedImage getBufferedImage( String imgurl ) {
	URL url = null;
	InputStream is = null;
	BufferedImage img = null;
	try {
	    url = new URL( imgurl );
	    is = url.openStream();
	    img = ImageIO.read( is );
	} catch ( MalformedURLException e ) {
	    e.printStackTrace();
	} catch ( IOException e ) {
	    e.printStackTrace();
	} finally {

	    try {
		is.close();
	    } catch ( IOException e ) {
		e.printStackTrace();
	    }
	}
	return img;
    }

    /**
     * 是否为手机号码
     *
     * @param mobiles
     *
     * @return
     */
    public static boolean isMobileNO( String mobiles ) {
	Pattern p = Pattern.compile( "^((14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$" );
	Matcher m = p.matcher( mobiles );
	return m.matches();
    }

    /**
     * 获取用户选择字段信息
     *
     * @param request
     *
     * @return d
     */
    @SuppressWarnings( "unchecked" )
    public static Map< String,Map< String,Object > > getBusDict( HttpServletRequest request ) {
	return (Map< String,Map< String,Object > >) request.getSession().getAttribute( "busDict" );
    }

    private final static double PI = 3.14159265358979323; // 圆周率
    private final static double R  = 6371229; // 地球的半径

    /**
     * 获取两点间的距离
     *
     * @param longt1 经度1
     * @param lat1   纬度1
     * @param longt2 经度2
     * @param lat2   纬度2
     *
     * @return
     */
    public static double getDistance( double longt1, double lat1, double longt2, double lat2 ) {
	double x, y, distance;
	x = ( longt2 - longt1 ) * PI * R * Math.cos( ( ( lat1 + lat2 ) / 2 ) * PI / 180 ) / 180;
	y = ( lat2 - lat1 ) * PI * R / 180;
	distance = Math.hypot( x, y );
	if ( distance > 0 ) {
	    return distance;
	} else {
	    return 0.0;
	}
    }

    /**
     * 移除map中为空的项
     *
     * @param map
     *
     * @return
     */
    public static Map< String,Object > removeEmptyKey( Map< String,Object > map ) {
	List< String > keyLs = new ArrayList<>();
	for ( String key : map.keySet() ) {
	    if ( isEmpty( map.get( key ) ) ) {
		keyLs.add( key );
	    }
	}
	for ( String string : keyLs ) {
	    map.remove( string );
	}
	return map;
    }

    /**
     * 是否为正整数
     *
     * @param str
     *
     * @return
     */
    public static boolean isInteger( String str ) {
	Pattern pattern = Pattern.compile( "^[-\\+]?[\\d]*$" );
	return pattern.matcher( str ).matches();
    }

    /**
     * 判断手机营业商
     *
     * @param phone_number
     *
     * @return flag 运营商 3：电信 1 移动 2 联通 0 未知号码
     */
    public static int matchesPhoneNumber( String phone_number ) {
	String cm = "^((13[4-9])|(147)|(15[0-2,7-9])|(18[2-3,7-8]))\\d{8}$";
	String cu = "^((13[0-2])|(145)|(15[5-6])|(186))\\d{8}$";
	String ct = "^((133)|(153)|(18[0,1,9]))\\d{8}$";

	int flag = 0;
	if ( phone_number.matches( cm ) ) {
	    flag = 1;
	} else if ( phone_number.matches( cu ) ) {
	    flag = 2;
	} else if ( phone_number.matches( ct ) ) {
	    flag = 3;
	} else {
	    flag = 4;
	}
	return flag;
    }

    /**
     * 获取IP
     *
     * @param request
     *
     * @return
     */
    public static String getIpAddr( HttpServletRequest request ) {
	String ip = request.getHeader( "x-forwarded-for" );
	if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip ) ) {
	    ip = request.getHeader( "Proxy-Client-IP" );
	}
	if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip ) ) {
	    ip = request.getHeader( "WL-Proxy-Client-IP" );
	}
	if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase( ip ) ) {
	    ip = request.getRemoteAddr();
	}
	if ( "0:0:0:0:0:0:0:1".equals( ip ) ) {
	    ip = "127.0.0.1";
	}
	return ip;
    }

    /**
     * Object转成String
     *
     * @param obj
     *
     * @return
     */
    public static String getStr( Object obj ) {
	if ( obj == null ) {
	    return "";
	}
	return obj.toString();
    }

    /**
     * 判断是否是日期格式
     *
     * @param str
     *
     * @return
     */
    public static boolean isValidDate( String str ) {
	boolean convertSuccess = true;
	// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
	SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
	try {
	    format.setLenient( false );
	    format.parse( str );
	} catch ( Exception e ) {
	    convertSuccess = false;
	}
	return convertSuccess;
    }

    /**
     * 格式化字符串
     *
     * @param format
     * @param args
     *
     * @return
     */
    public static String format( String format, Object... args ) {
	String str = null;
	str = String.format( format, args );
	return str;
    }

    /**
     * 两数相加
     *
     * @return
     */
    public static Double add( Double number1, Object number2 ) {
	Double number = toDouble( number2 );
	if ( isNotEmpty( number2 ) ) {
	    return number1 + number;
	} else {
	    return number1;
	}
    }

    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param xml xml格式的字符串
     *
     * @return 成功返回json 格式的字符串;失败反回null
     */
    @SuppressWarnings( "unchecked" )
    public static String xml2JSON( String xml ) {
	JSONObject obj = new JSONObject();
	try {
	    InputStream is = new ByteArrayInputStream( xml.getBytes( "utf-8" ) );
	    SAXBuilder sb = new SAXBuilder();
	    Document doc = sb.build( is );
	    Element root = doc.getRootElement();
	    obj.put( root.getName(), iterateElement( root ) );
	    return obj.toString();
	} catch ( Exception e ) {
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * 一个迭代方法
     *
     * @param element : org.jdom.Element
     *
     * @return java.util.Map 实例
     */
    @SuppressWarnings( "unchecked" )
    private static Map iterateElement( Element element ) {
	List jiedian = element.getChildren();
	Element et = null;
	Map obj = new HashMap();
	List list = null;
	for ( int i = 0; i < jiedian.size(); i++ ) {
	    list = new LinkedList();
	    et = (Element) jiedian.get( i );
	    if ( et.getTextTrim().equals( "" ) ) {
		if ( et.getChildren().size() == 0 ) continue;
		if ( obj.containsKey( et.getName() ) ) {
		    list = (List) obj.get( et.getName() );
		}
		list.add( iterateElement( et ) );
		obj.put( et.getName(), list );
	    } else {
		if ( obj.containsKey( et.getName() ) ) {
		    list = (List) obj.get( et.getName() );
		}
		list.add( et.getTextTrim() );
		obj.put( et.getName(), list );
	    }
	}
	return obj;
    }

    /**
     * 卡号加密
     *
     * @param key
     * @param cardNo
     *
     * @return
     */
    public static String encryption( String key, String cardNo ) {
	Charset charset = Charset.forName( "UTF-8" );
	byte[] b = cardNo.getBytes( charset );
	for ( int i = 0, size = b.length; i < size; i++ ) {
	    for ( byte keyBytes0 : key.getBytes( charset ) ) {
		b[i] = (byte) ( b[i] ^ keyBytes0 );
	    }
	}
	return new String( b );
    }

    /**
     * 解密
     *
     * @param key
     * @param cardNo
     *
     * @return
     */
    public static String decrypt( String key, String cardNo ) {
	Charset charset = Charset.forName( "UTF-8" );
	byte[] e = cardNo.getBytes( charset );
	byte[] dee = e;
	for ( int i = 0, size = e.length; i < size; i++ ) {
	    for ( byte keyBytes0 : key.getBytes( charset ) ) {
		e[i] = (byte) ( dee[i] ^ keyBytes0 );
	    }
	}
	return new String( e );
    }

    public static String getpath( HttpServletRequest request ) {
	String url = "http://" + request.getServerName() // 服务器地址
			+ request.getContextPath() // 项目名称
			+ request.getServletPath() // 请求页面或其他地址
			+ ( CommonUtil.isEmpty( request.getQueryString() ) ? "" : "?" + request.getQueryString() ); // 参数
	return url;
    }

    /**
     * 获取卡号 截取一位 是生成条形码17位
     *
     * @return
     */
    public static String getCode() {
	StringBuffer buf = new StringBuffer( "1,2,3,4,5,6,7,8,9,0" );
	String[] arr = buf.toString().split( "," );
	StringBuffer sb = new StringBuffer();
	Random random = new Random();
	for ( int i = 0; i < 4; i++ ) {
	    Integer count = arr.length;
	    int a = random.nextInt( count );
	    sb.append( arr[a] );
	}
	Long date = new Date().getTime();
	String cardNo = date.toString().substring( 2 ) + sb.toString();
	return cardNo;
    }


    public static String getDuofenCardCode(Integer busId){
	StringBuffer buf = new StringBuffer( "1,2,3,4,5,6,7,8,9,0" );
	String[] arr = buf.toString().split( "," );
	StringBuffer sb = new StringBuffer();
	Random random = new Random();
	for ( int i = 0; i < 4; i++ ) {
	    Integer count = arr.length;
	    int a = random.nextInt( count );
	    sb.append( arr[a] );
	}
	Long date = new Date().getTime();
	String duofenCardCode = busId+date.toString() + sb.toString();
	return duofenCardCode;
    }


    /**
     * 获取推荐码 6位
     *
     * @return
     */
    public static String getNominateCode() {
	StringBuffer buf = new StringBuffer( "a,b,c,d,e,f,g,h,i,g,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z" );
	buf.append( ",1,2,3,4,5,6,7,8,9,0" );
	String[] arr = buf.toString().split( "," );
	StringBuffer sb = new StringBuffer();
	Random random = new Random();
	for ( int i = 0; i < 8; i++ ) {
	    Integer count = arr.length;
	    int a = random.nextInt( count );
	    sb.append( arr[a] );
	}
	return sb.toString();
    }

    /**
     * 获取推荐码 6位
     *
     * @return
     */
    public static String getPhoneCode() {
	StringBuffer buf = new StringBuffer( "1,2,3,4,5,6,7,8,9,0" );
	String[] arr = buf.toString().split( "," );
	StringBuffer sb = new StringBuffer();
	Random random = new Random();
	for ( int i = 0; i < 6; i++ ) {
	    Integer count = arr.length;
	    int a = random.nextInt( count );
	    sb.append( arr[a] );
	}
	return sb.toString();
    }

    /**
     * 获取4位随机码
     *
     * @return
     */
    public static String get4RandomCode() {
	StringBuffer buf = new StringBuffer( "1,2,3,4,5,6,7,8,9,0" );
	String[] arr = buf.toString().split( "," );
	StringBuffer sb = new StringBuffer();
	Random random = new Random();
	for ( int i = 0; i < 4; i++ ) {
	    Integer count = arr.length;
	    int a = random.nextInt( count );
	    sb.append( arr[a] );
	}
	return sb.toString();
    }

    public static String Blob2String( Object obj ) {
	String string = null;
	try {
	    if ( obj == null || obj.equals( "" ) ) {
		return "";
	    }
	    byte[] bytes = (byte[]) obj;
	    string = new String( bytes, "UTF-8" );
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	}
	return string;
    }

    public static String filterEmoji( String source ) {
	if ( source != null ) {
	    Pattern emoji = Pattern.compile( "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE );
	    Matcher emojiMatcher = emoji.matcher( source );
	    if ( emojiMatcher.find() ) {
		return "未知用户";
	    }
	    return source;
	}
	return source;
    }

    /**
     * 自定义长度校验码
     *
     * @return
     */
    public static String getNominateCode( int length ) {
	StringBuffer buf = new StringBuffer( "a,b,c,d,e,f,g,h,i,g,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z" );
	buf.append( ",1,2,3,4,5,6,7,8,9,0" );
	String[] arr = buf.toString().split( "," );
	StringBuffer sb = new StringBuffer();
	Random random = new Random();
	for ( int i = 0; i < length; i++ ) {
	    Integer count = arr.length;
	    int a = random.nextInt( count );
	    sb.append( arr[a] );
	}
	return sb.toString();
    }

    // 将字符串转换成二进制字符串，以空格相隔
    public static String StrToBinstr( String str ) {
	char[] strChar = str.toCharArray();
	String result = "";
	for ( int i = 0; i < strChar.length; i++ ) {
	    result += Integer.toBinaryString( strChar[i] ) + " ";
	}
	return result;
    }

    // 将初始二进制字符串转换成字符串数组，以空格相隔
    public static String[] StrToStrArray( String str ) {
	return str.split( " " );
    }

    // 将二进制字符串转换成Unicode字符串
    public static String BinstrToStr( String binStr ) {
	String[] tempStr = StrToStrArray( binStr );
	char[] tempChar = new char[tempStr.length];
	for ( int i = 0; i < tempStr.length; i++ ) {
	    tempChar[i] = BinstrToChar( tempStr[i] );
	}
	return String.valueOf( tempChar );
    }

    // 将二进制字符串转换为char
    public static char BinstrToChar( String binStr ) {
	int[] temp = BinstrToIntArray( binStr );
	int sum = 0;
	for ( int i = 0; i < temp.length; i++ ) {
	    sum += temp[temp.length - 1 - i] << i;
	}
	return (char) sum;
    }

    // 将二进制字符串转换成int数组
    public static int[] BinstrToIntArray( String binStr ) {
	char[] temp = binStr.toCharArray();
	int[] result = new int[temp.length];
	for ( int i = 0; i < temp.length; i++ ) {
	    result[i] = temp[i] - 48;
	}
	return result;
    }

    /**
     * 保留2位小数（四舍五入）
     *
     * @param d
     *
     * @return
     */
    public static Double getDecimal_2( Double d ) {
	BigDecimal bg = new BigDecimal( d );
	double f1 = bg.setScale( 2, BigDecimal.ROUND_HALF_UP ).doubleValue();
	return f1;
    }

    /**
     * 转Integer
     *
     * @param obj
     */
    public static Integer parseInt( Object obj ) throws Exception {
	if ( !isEmpty( obj ) ) {
	    return Integer.parseInt( obj.toString() );
	} else {
	    throw new Exception( "对象为空，转换失败！" );
	}
    }

    /**
     * 转Double
     *
     * @param obj
     *
     * @return
     * @throws Exception
     */
    public static Double parseDouble( Object obj ) throws Exception {
	if ( !isEmpty( obj ) ) {
	    return Double.parseDouble( obj.toString() );
	} else {
	    throw new Exception( "对象为空，转换失败！" );
	}
    }

    /**
     * 验证是否来自移动端，是移动端，返回true
     *
     * @param ua
     *
     * @return
     */
    public static boolean checkAgentIsMobile( String ua ) {
	String[] agent = CommonUtil.AGENT;
	boolean flag = false;
	if ( !ua.contains( "Windows NT" ) || ( ua.contains( "Windows NT" ) && ua.contains( "compatible; MSIE 9.0;" ) ) ) {
	    // 排除 苹果桌面系统
	    if ( !ua.contains( "Windows NT" ) && !ua.contains( "Macintosh" ) ) {
		for ( String item : agent ) {
		    if ( ua.contains( item ) ) {
			flag = true;
			break;
		    }
		}
	    }
	}
	return flag;
    }

    /**
     * 判断浏览器
     *
     * @return 1:微信浏览器,99:其他浏览器
     */
    public static Integer judgeBrowser( HttpServletRequest request ) {
	Integer result = null;
	String ua = request.getHeader( "user-agent" ).toLowerCase();
	if ( ua.indexOf( "micromessenger" ) > 0 ) {// 微信-1
	    result = 1;
	} else {//其他 -99
	    result = 99;
	}
	return result;
    }

    /**
     * 设置商家会员信息  未设置过期
     *
     * @param request
     *
     * @return
     */
    public static void setLoginMember( HttpServletRequest request, MemberEntity memberEntity ) {
	try {
	    if ( CommonUtil.isEmpty( memberEntity ) || CommonUtil.isEmpty( memberEntity.getId() ) ) {// 充值
		request.getSession().setAttribute( "member", memberEntity );
	    } else {
		Integer busId = memberEntity.getBusId();
	    }
	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	}
	request.getSession().setAttribute( "member", memberEntity );
    }

    ;

    /**
     * 获取商家的会员，不是该商家的会员返回null
     *
     * @param request
     *
     * @return
     */
    public static MemberEntity getMemberByBusId( HttpServletRequest request, Integer busId ) {
	try {
	    MemberEntity memberEntity = (MemberEntity) request.getSession().getAttribute( "memberEntity" );
	    if ( isNotEmpty( memberEntity ) && memberEntity.getBusId().equals( busId ) ) {
		return memberEntity;
	    } else {
		return null;
	    }
	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * 获取会员订单号
     *
     * @return
     */
    public static String getMEOrderCode() {
	StringBuffer buf = new StringBuffer(
			"1,2,3,4,5,6,7,8,9,0");
	String[] arr = buf.toString().split(",");
	StringBuffer sb = new StringBuffer();
	Random random = new Random();
	for (int i = 0; i < 4; i++) {
	    Integer count = arr.length;
	    int a = random.nextInt(count);
	    sb.append(arr[a]);
	}
	return "ME" + new Date().getTime()+sb.toString();
    }

    /**
     * 返回json数据到客户端
     *
     * @param response
     * @param obj
     *
     * @throws IOException
     */
    public static void write( HttpServletResponse response, Object obj ) throws IOException {
	response.setContentType( "application/json;charset=UTF-8" );//防止数据传递乱码
	if ( obj instanceof List || obj instanceof Object[] ) {
	    response.getWriter().print( JSONArray.toJSON( obj ) );
	} else if ( obj instanceof Boolean || obj instanceof Integer || obj instanceof String || obj instanceof Double ) {
	    Map< String,Object > result = new HashMap< String,Object >();
	    result.put( "status", obj );
	    response.getWriter().print( JSONObject.fromObject( result ) );
	} else {
	    response.getWriter().print( JSONObject.fromObject( obj ) );
	}
	response.getWriter().flush();
	response.getWriter().close();
    }

    /**
     * 返回json数据到客户端
     *
     * @param response
     * @param obj
     *
     * @throws IOException
     */
    public static void writeToAilibaba( HttpServletResponse response, Object obj ) throws IOException {
	response.setContentType( "application/json;charset=UTF-8" );//防止数据传递乱码
	response.getWriter().print( JSON.parseObject( JSON.toJSONString( obj ) ) );
	response.getWriter().flush();
	response.getWriter().close();
    }

    /**
     * 浮点数比较
     *
     * @param a
     * @param b
     *
     * @return
     */
    public static boolean compareTo( BigDecimal a, Integer b ) {
	BigDecimal c = new BigDecimal( b );
	if ( a.compareTo( c ) == 0 || a.compareTo( c ) == 1 ) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * 图文保存，返回信息
     *
     * @param multipartFile
     *
     * @return
     */
    @SuppressWarnings( "rawtypes" )
    public static Map fileUploadByBusUser( MultipartFile multipartFile, Integer busId ) {
	String userName = busId.toString();
	String originalFilename = multipartFile.getOriginalFilename();
	// 后缀
	String suffix = originalFilename.substring( originalFilename.lastIndexOf( "." ) );
	String phonejsp = originalFilename.substring( originalFilename.lastIndexOf( "." ) + 1 );
	// 文件大小
	Integer Size = Integer.parseInt( String.valueOf( multipartFile.getSize() ) );
	// 判断上传图片是否是支持的格式

	String path = PropertiesUtil.getRes_image_path() + "/2/" + userName + "/" + PropertiesUtil.IMAGE_FOLDER_TYPE_14 + "/" + DateTimeKit
			.getDateTime( new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD ) + "/jietu/";

	File file = new File( path );
	if ( !file.exists() && !file.isDirectory() ) {
	    file.mkdirs();
	}
	Long time = System.currentTimeMillis();
	path += MD5Util.getMD5( time + originalFilename.substring( 0, originalFilename.lastIndexOf( "." ) ) ) + suffix;
	byte[] bytes;
	Map< String,Object > map = null;
	try {
	    bytes = multipartFile.getBytes();
	    InputStream is = new ByteArrayInputStream( bytes );
	    BufferedImage bufimg = ImageIO.read( is );
	    ImageIO.write( bufimg, phonejsp, new File( path ) );
	    ContinueFTP myFtp = new ContinueFTP();
	    try {
		map = myFtp.upload( path, PropertiesUtil.getStatic_source_ftp_ip(), PropertiesUtil.getStatic_source_ftp_port(), PropertiesUtil.getStatic_source_ftp_user(),
				PropertiesUtil.getStatic_source_ftp_password() );
	    } catch ( Exception e ) {
		e.printStackTrace();
	    }
	    is.close();

	    if ( CommonUtil.isNotEmpty( map ) && "0".equals( CommonUtil.toString( map.get( "code" ) ) ) ) {
		String url = "/image/2/" + userName + "/" + PropertiesUtil.IMAGE_FOLDER_TYPE_14 + "/" + DateTimeKit
				.getDateTime( new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD ) + "/jietu/" + MD5Util
				.getMD5( time + originalFilename.substring( 0, originalFilename.lastIndexOf( "." ) ) ) + suffix;
		map.put( "url", url );// 保存路径
	    }
	} catch ( IOException e ) {
	    map = new HashMap<>();
	    e.printStackTrace();
	    map.put( "code", "1" );
	    map.put( "message", "抛出异常" );
	}
	return map;

    }


    public static Member memberEntityToMember(MemberEntity m){
	Member member=new Member();
	member.setId( m.getId() );
	member.setNickname( m.getNickname() );
	member.setBusid( m.getBusId() );
	member.setPhone( m.getPhone() );
	member.setHeadimgurl( m.getHeadimgurl() );
	return member;
    }


    public static Object get( String key, Map< String,Object > requestBody ) {
	if ( requestBody.containsKey( key ) ) return requestBody.get( key );

	return null;
    }

    public static String byteToUTF8str( String str ) {
	try {
	    if (str == null) {
		throw new Exception( "对象为空，转换失败！" );
	    }
	    byte[] byteArray = str.getBytes();
	    System.out.println( new String( byteArray, "UTF-8" ) );


	    return new String( byteArray, "UTF-8" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }
}
