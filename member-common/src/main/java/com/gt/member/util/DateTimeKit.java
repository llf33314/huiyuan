package com.gt.member.util;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 日期操作工具类
 *
 */
public class DateTimeKit {
    public static Date date = null;

    public static DateFormat dateFormat = null;

    public static Calendar calendar = null;

    /**
     * 缺省的日期显示格式： yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEFAULT_DATE_FORMAT_YYYYMMDD="yyyyMMdd";


    public static final String DEFAULT_DATE_FORMAT_YYYYMM="yyyyMM";

    /**
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_FORMAT_YYYYMMDD_MMSS = "yyyy-MM-dd HH:mm";

    /**
     * 时间显示格式：yyyyMMddHHmmssms yyyyMMddHHmmss
     */
    public static final String yyyyMMddHHmmssms = "yyyyMMddHHmmssms";

    /**
     * 时间显示格式：yyyyMMddHHmmss
     */
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    public static Map<Integer, String> weekMap ;
    static{
	weekMap= new HashMap<Integer, String>();
	weekMap.put(2, "周一");
	weekMap.put(3, "周二");
	weekMap.put(4, "周三");
	weekMap.put(5, "周四");
	weekMap.put(6, "周五");
	weekMap.put(7, "周六");
	weekMap.put(1, "周日");
    }

    public static final String MMDDHHMMSS = "MMddHHmmss";

    public java.sql.Timestamp getTimestamp() {
	return new java.sql.Timestamp(new Date().getTime());
    }

    /**
     * 时间显示格式：yyyyMMddHHmmssms
     *
     * @return 当前日期
     */
    public static String getDateIsLink() {
	return getDateTime(yyyyMMddHHmmssms);
    }

    /**
     * 得到系统当前日期时间
     *
     * @return 当前日期时间
     */
    public static Date getNow() {
	return Calendar.getInstance().getTime();
    }


    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
	Date currentTime = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String dateString = formatter.format(currentTime);
	ParsePosition pos = new ParsePosition(8);
	Date currentTime_2 = formatter.parse(dateString, pos);
	return currentTime_2;
    }
    /**
     * 获取现在时间
     *
     * @return返回短时间格式 yyyy-MM-dd
     */
    public static Date getNowDateShort() {
	Date currentTime = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String dateString = formatter.format(currentTime);
	ParsePosition pos = new ParsePosition(8);
	Date currentTime_2 = formatter.parse(dateString, pos);
	return currentTime_2;
    }

    /**
     * 获取现在时间
     * @throws ParseException
     *
     * @return返回短时间格式 HH:mm:ss
     */
    public static Date getNowDateSmallShort() {
	Date currentTime = new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	String dateString = formatter.format(currentTime);
	Date currentTime_2 = null;
	try {
	    currentTime_2 = formatter.parse(dateString);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return currentTime_2;
    }


    /**
     * 得到用缺省方式格式化的当前日期
     *
     * @return 当前日期
     */
    public static String getDate() {
	return getDateTime(DEFAULT_DATE_FORMAT);
    }

    /**
     * 得到用缺省方式格式化的当前日期及时间
     *
     * @return 当前日期及时间
     */
    public static String getDateTime() {
	return getDateTime(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 得到系统当前日期及时间，并用指定的方式格式化
     *
     * @param pattern
     *            显示格式
     * @return 当前日期及时间
     */
    public static String getDateTime(String pattern) {
	Date datetime = Calendar.getInstance().getTime();
	return getDateTime(datetime, pattern);
    }
    /**
     * 得到系统当前日期及时间，并用指定的方式格式化
     *
     * @param pattern
     *            显示格式
     * @return 当前日期及时间
     */
    public static Date getNowDate(String pattern) {
	Date datetime = Calendar.getInstance().getTime();
	return parseDate(getDateTime(datetime, pattern),pattern);
    }

    /**
     * 得到用指定方式格式化的日期
     *
     * @param date
     *            需要进行格式化的日期
     * @param pattern
     *            显示格式
     * @return 日期时间字符串
     */
    public static String getDateTime(Date date, String pattern) {
	if (null == pattern || "".equals(pattern)) {
	    pattern = DEFAULT_DATETIME_FORMAT;
	}
	SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	return dateFormat.format(date);
    }

    /**
     * 得到当前年份
     *
     * @return 当前年份
     */
    public static int getCurrentYear() {
	return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 得到当前月份
     *
     * @return 当前月份
     */
    public static int getCurrentMonth() {
	// 用get得到的月份数比实际的小1，需要加上
	return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 得到当前日
     *
     * @return 当前日
     */
    public static int getCurrentDay() {
	return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
     *
     * @param days
     *            增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(int days) {
	return add(getNow(), days, Calendar.DATE);
    }

    /**
     * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
     *
     * @param date
     *            基准日期
     * @param days
     *            增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(Date date, int days) {
	return add(date, days, Calendar.DATE);
    }

    /**
     * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
     *
     * @param months
     *            增加的月份数
     * @return 增加以后的日期
     */
    public static Date addMonths(int months) {
	return add(getNow(), months, Calendar.MONTH);
    }


    /**
     * 当前时间增加秒数
     *
     * @return 增加以后的日期
     */
    public static Date addSenconds(int seconds) {
	return add(getNow(), seconds, Calendar.SECOND);
    }

    /**
     * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28
     *
     * @param date
     *            基准日期
     * @param months
     *            增加的月份数
     * @return 增加以后的日期
     */
    public static Date addMonths(Date date, int months) {
	return add(date, months, Calendar.MONTH);
    }

    /**
     * 内部方法。为指定日期增加相应的天数或月数
     *
     * @param date
     *            基准日期
     * @param amount
     *            增加的数量
     * @param field
     *            增加的单位，年，月或者日
     * @return 增加以后的日期
     */
    private static Date add(Date date, int amount, int field) {
	Calendar calendar = Calendar.getInstance();

	calendar.setTime(date);
	calendar.add(field, amount);

	return calendar.getTime();
    }

    /**
     * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     *
     * @param one
     *            第一个日期数，作为基准
     * @param two
     *            第二个日期数，作为比较
     * @return 两个日期相差天数
     */
    public static long diffDays(Date one, Date two) {
	return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
     *
     * @param one
     *            第一个日期数，作为基准
     * @param two
     *            第二个日期数，作为比较
     * @return 两个日期相差月份数
     */
    public static int diffMonths(Date one, Date two) {

	Calendar calendar = Calendar.getInstance();

	// 得到第一个日期的年分和月份数
	calendar.setTime(one);
	int yearOne = calendar.get(Calendar.YEAR);
	int monthOne = calendar.get(Calendar.MONDAY);

	// 得到第二个日期的年份和月份
	calendar.setTime(two);
	int yearTwo = calendar.get(Calendar.YEAR);
	int monthTwo = calendar.get(Calendar.MONDAY);

	return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
    }

    /**
     * 将一个字符串用给定的格式转换为日期类型。<br>
     * 注意：如果返回null，则表示解析失败
     *
     * @param datestr
     *            需要解析的日期字符串
     * @param pattern
     *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
     * @return 解析后的日期
     */
    public static Date parse(String datestr, String pattern) {
	Date date = null;
	if (null == pattern || "".equals(pattern)) {
	    pattern = DEFAULT_DATE_FORMAT;
	}

	try {
	    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	    date = dateFormat.parse(datestr);
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	return date;
    }

    /**
     * 返回本月的最后一天
     *
     * @return 本月最后一天的日期
     */
    public static Date getMonthLastDay() {
	return getMonthLastDay(getNow());
    }

    /**
     * 返回给定日期中的月份中的最后一天
     *
     * @param date
     *            基准日期
     * @return 该月最后一天的日期
     */
    public static Date getMonthLastDay(Date date) {

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);

	// 将日期设置为下一月第一天
	calendar.set(calendar.get(Calendar.YEAR),
			calendar.get(Calendar.MONTH) + 1, 1);

	// 减去1天，得到的即本月的最后一天
	calendar.add(Calendar.DATE, -1);

	return calendar.getTime();
    }

    public static String getDay() {
	String day = "";
	int d = DateTimeKit.getNow().getDay();
	if (d == 0) {
	    day = "星期日";
	} else if (d == 1) {
	    day = "星期一";
	} else if (d == 2) {
	    day = "星期二";
	} else if (d == 3) {
	    day = "星期三";
	} else if (d == 4) {
	    day = "星期四";
	} else if (d == 5) {
	    day = "星期五";
	} else if (d == 6) {
	    day = "星期六";
	}
	return day;
    }



    public static String getDayToEnglish() {
	String day = "";
	int d = DateTimeKit.getNow().getDay();
	if (d == 0) {
	    day = "SUNDAY";
	} else if (d == 1) {
	    day = "MONDAY";
	} else if (d == 2) {
	    day = "TUESDAY";
	} else if (d == 3) {
	    day = "WEDNESDAY";
	} else if (d == 4) {
	    day = "THURSDAY";
	} else if (d == 5) {
	    day = "FRIDAY";
	} else if (d == 6) {
	    day = "SUNDAY";
	}
	return day;
    }

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr
     *            String 字符型日期
     * @param format  yyyy/MM/dd HH:mm:ss
     *            String 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
	try {
	    dateFormat = new SimpleDateFormat(format);
			/*String dt = dateStr.replaceAll("-", "/");
			if ((!dt.equals("")) && (dt.length() < format.length())) {
				dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]",
						"0");
			}*/
	    date = dateFormat.parse(dateStr);
	} catch (Exception e) {
	}
	return date;
    }


    /**
     * 功能描述：格式化日期
     *
     * @param dateStr
     *            String 字符型日期：YYYY-MM-DD 格式
     * @return Date
     */
    public static Date parseDate(String dateStr) {
	return parseDate(dateStr, "yyyy/MM/dd");
    }

    /**
     * 功能描述：格式化输出日期
     *
     * @param date
     *            Date 日期
     * @param format
     *            String 格式
     * @return 返回字符型日期
     */
    public static String format(Date date, String format) {
	String result = "";
	try {
	    if (date != null) {
		dateFormat = new SimpleDateFormat(format);
		result = dateFormat.format(date);
	    }
	} catch (Exception e) {
	}
	return result;
    }
    /**
     * 功能描述：格式化输出日期
     *
     * @param date
     *            Date 日期
     * @param format
     *            String 格式
     * @return 返回字符型日期
     */
    public static Date format2(Date date, String format) {
	Date dateResult = null;
	try {
	    if (date != null) {
		dateFormat = new SimpleDateFormat(format);
		String result = dateFormat.format(date);
		dateResult=parseDate(result,format);
	    }
	} catch (Exception e) {
	}
	return dateResult;
    }

    /**
     * 功能描述：将带有时间的日期格式化为只有年月日
     *
     * @param date
     *            Date 日期
     * @return
     */
    public static String format(Date date) {
	return format(date, "yyyy-MM-dd");
    }

    /**
     * 功能描述：返回年份
     *
     * @param date
     *            Date 日期
     * @return 返回年份
     */
    public static int getYear(Date date) {
	calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(Calendar.YEAR);
    }

    /**
     * 功能描述：返回月份
     *
     * @param date
     *            Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
	calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 功能描述：返回日份
     *
     * @param date
     *            Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
	calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 功能描述：返回小时
     *
     * @param date
     *            日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
	calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 功能描述：返回分钟
     *
     * @param date
     *            日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
	calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(Calendar.MINUTE);
    }

    /**
     * 返回秒钟
     *
     * @param date
     *            Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
	calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.get(Calendar.SECOND);
    }

    /**
     * 功能描述：返回毫秒
     *
     * @param date
     *            日期
     * @return 返回毫秒
     */
    public static long getMillis(Date date) {
	calendar = Calendar.getInstance();
	calendar.setTime(date);
	return calendar.getTimeInMillis();
    }

    /**
     * 功能描述：返回字符型日期
     *
     * @param date
     *            日期
     * @return 返回字符型日期 yyyy/MM/dd 格式
     */
    public static String getDate(Date date) {
	return format(date, "yyyy/MM/dd");
    }

    /**
     * 功能描述：返回字符型时间
     *
     * @param date
     *            Date 日期
     * @return 返回字符型时间 HH:mm:ss 格式
     */
    public static String getTime(Date date) {
	return format(date, "HH:mm:ss");
    }

    /**
     * 功能描述：返回字符型日期时间
     *
     * @param date
     *            Date 日期
     * @return 返回字符型日期时间 yyyy/MM/dd HH:mm:ss 格式
     */
    public static String getDateTime(Date date) {
	return format(date, "yyyy/MM/dd HH:mm:ss");
    }

    /**
     * 功能描述：日期相加
     *
     * @param date
     *            Date 日期
     * @param day
     *            int 天数
     * @return 返回相加后的日期
     */
    public static Date addDate(Date date, int day) {
	calendar = Calendar.getInstance();
	long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
	calendar.setTimeInMillis(millis);
	return calendar.getTime();
    }

    /**
     * 功能描述：日期相加
     *
     * @param dateS
     *            dateS 日期
     * @param day
     *            int 天数
     * @return 返回相加后的日期
     */
    public static Date addDate(String dateS, int day,String format) {
	Date date = parseDate(dateS,format);
	calendar = Calendar.getInstance();
	long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
	calendar.setTimeInMillis(millis);
	return calendar.getTime();
    }

    /**
     * 功能描述：日期相减
     *
     * @param date
     *            Date 日期
     * @param date1
     *            Date 日期
     * @return 返回相减后的日期
     */
    public static int diffDate(Date date, Date date1) {
	return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }

    /**
     * 功能描述：取得指定月份的第一天
     *
     * @param strdate
     *            String 字符型日期
     * @return String yyyy-MM-dd 格式
     */
    public static String getMonthBegin(String strdate) {
	date = parseDate(strdate);
	return format(date, "yyyy-MM") + "-01";
    }

    /**
     * 功能描述：取得指定月份的最后一天
     *
     * @param strdate
     *            String 字符型日期
     * @return String 日期字符串 yyyy-MM-dd格式
     */
    public static String getMonthEnd(String strdate) {
	date = parseDate(getMonthBegin(strdate));
	calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.add(Calendar.MONTH, 1);
	calendar.add(Calendar.DAY_OF_YEAR, -1);
	return formatDate(calendar.getTime());
    }

    /**
     * 功能描述：常用的格式化日期
     *
     * @param date
     *            Date 日期
     * @return String 日期字符串 yyyy-MM-dd格式
     */
    public static String formatDate(Date date) {
	return formatDateByFormat(date, "yyyy-MM-dd");
    }

    /**
     * 功能描述：以指定的格式来格式化日期
     *
     * @param date
     *            Date 日期
     * @param format
     *            String 格式
     * @return String 日期字符串
     */
    public static String formatDateByFormat(Date date, String format) {
	String result = "";
	if (date != null) {
	    try {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		result = sdf.format(date);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return result;
    }

    public int getDaysBetween(Calendar d1, Calendar d2) {
	if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
	    Calendar swap = d1;
	    d1 = d2;
	    d2 = swap;
	}
	int days = d2.get(Calendar.DAY_OF_YEAR)
			- d1.get(Calendar.DAY_OF_YEAR);
	int y2 = d2.get(Calendar.YEAR);
	if (d1.get(Calendar.YEAR) != y2) {
	    d1 = (Calendar) d1.clone();
	    do {
		days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
		d1.add(Calendar.YEAR, 1);
	    } while (d1.get(Calendar.YEAR) != y2);
	}
	return days;
    }

    /**
     * 计算2个日期之间的相隔天数
     *
     * @param d1
     * @param d2
     * @return
     */
    public int getWorkingDay(Calendar d1, Calendar d2) {
	int result = -1;
	if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
	    Calendar swap = d1;
	    d1 = d2;
	    d2 = swap;
	}

	int betweendays = getDaysBetween(d1, d2);

	@SuppressWarnings("unused")
	int charge_date = 0;
	int charge_start_date = 0;// 开始日期的日期偏移量
	int charge_end_date = 0;// 结束日期的日期偏移量
	// 日期不在同一个日期内
	int stmp;
	int etmp;
	stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
	etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
	if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
	    charge_start_date = stmp - 1;
	}
	if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
	    charge_end_date = etmp - 1;
	}
	// }
	result = (getDaysBetween(this.getNextMonday(d1), this.getNextMonday(d2)) / 7)
			* 5 + charge_start_date - charge_end_date;
	// System.out.println("charge_start_date>" + charge_start_date);
	// System.out.println("charge_end_date>" + charge_end_date);
	// System.out.println("between day is-->" + betweendays);
	return result;
    }

    public String getChineseWeek(Calendar date) {
	final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
			"星期六" };

	int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);

	// System.out.println(dayNames[dayOfWeek - 1]);
	return dayNames[dayOfWeek - 1];

    }

    /**
     * 获得日期的下一个星期一的日期
     *
     * @param date
     * @return
     */
    public Calendar getNextMonday(Calendar date) {
	Calendar result = null;
	result = date;
	do {
	    result = (Calendar) result.clone();
	    result.add(Calendar.DATE, 1);
	} while (result.get(Calendar.DAY_OF_WEEK) != 2);
	return result;
    }

    /**
     *
     * @param d1
     * @param d2
     * @return
     */
    public int getHolidays(Calendar d1, Calendar d2) {
	return this.getDaysBetween(d1, d2) - this.getWorkingDay(d1, d2);

    }

    /**
     * 获取当前日期并转化成Oracle的日期格式
     *
     * @return
     */
    public static java.sql.Timestamp getDateDb() {
	return new java.sql.Timestamp(new Date().getTime());
    }

    /**
     * 将参数日期转化成Oracle的日期格式
     *
     * @param dateStr
     * @return
     */
    public static java.sql.Timestamp parseDateDb(String dateStr) {
	return parseDateDb(dateStr, DEFAULT_DATE_FORMAT);
    }

    /**
     * 将参数日期转化成Oracle的日期格式
     *
     * @param date
     * @return
     */
    public static java.sql.Timestamp parseDateDb(Date date) {
	return new java.sql.Timestamp(date.getTime());
    }

    /**
     * 将参数日期转化成Oracle的日期格式
     *
     * @param
     * @return
     */
    public static java.sql.Timestamp parseDateDb(String dateStr, String format) {
	return new java.sql.Timestamp(parse(dateStr, format).getTime());
    }

    /**
     * 判断两日期是否在同一天
     *
     * @param day1
     *            日期1
     * @param day2
     *            日期2
     * @return 在同一天返回true，不在同一天返回false
     */
    public static boolean isSameDay(Date day1, Date day2) {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	return df.format( day1 ).equals( df.format( day2 ) );
    }


    /**
     * 判断是否是昨天
     * @param oldTime
     * @return
     * @throws ParseException
     */
    public static int isYeaterday(Date oldTime) throws ParseException{
	Date newTime= newTime=new Date();
	//将下面的 理解成  yyyy-MM-dd 00：00：00 更好理解点
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String todayStr = format.format(newTime);
	Date today = format.parse(todayStr);
	//昨天 86400000=24*60*60*1000 一天
	if((today.getTime()-oldTime.getTime())>0 && (today.getTime()-oldTime.getTime())<=86400000) {
	    return 0;
	}
	else if((today.getTime()-oldTime.getTime())<=0){ //至少是今天
	    return -1;
	}
	else{ //至少是前天
	    return 1;
	}

    }



    /**
     * 判断所给时间是否大过当前时间
     *
     * @param day
     *            所给时间
     * @return 大于当前时间返回true，否则为false
     * @throws Exception
     */
    public static boolean isAfterToday(String day) throws Exception {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Date date = df.parse(day);
	return date.after( df.parse( getDate() ) );
    }


    /**
     * 将dateStr中缺省的秒添上
     *
     * @param dateStr
     * @return
     */
    public static String dateTimeAddSecond(String dateStr, String format) {
	String secondStr = "";
	if (DEFAULT_DATETIME_FORMAT.equals(format)) {
	    secondStr = ":00";
	}
	if (yyyyMMddHHmmssms.equals(format)) {
	    secondStr = "00";
	}
	return dateStr + secondStr;
    }
    /**
     * 给定时间与当前时间比较
     * yyyyMMddHHmmss
     * @param sDate
     * @return 大于或等于当前时间返回true其他返回false
     */
    public static boolean laterThanNow(String sDate) {
	if (StringUtils.isEmpty(sDate) || sDate.length() != 14) return false;
	Calendar calendar0 = Calendar.getInstance(Locale.CHINESE);
	calendar0.set(Integer.parseInt(sDate.substring(0, 4)), Integer.parseInt(sDate.substring(4, 6)) - 1, Integer.parseInt(sDate.substring(6, 8)), Integer.parseInt(sDate.substring(8, 10)), Integer.parseInt(sDate.substring(10, 12)), Integer.parseInt(sDate.substring(10, 12)));
	Calendar calendar = Calendar.getInstance(Locale.CHINESE);
	return calendar0.after(calendar);
    }


    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws Exception
     * @throws ParseException
     */
    public static int daysBetween(Date smdate,Date bdate) throws Exception
    {
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	try {
	    smdate=sdf.parse(sdf.format(smdate));
	    bdate=sdf.parse(sdf.format(bdate));
	} catch (ParseException e) {

	}
	Calendar cal = Calendar.getInstance();
	cal.setTime(smdate);
	long time1 = cal.getTimeInMillis();
	cal.setTime(bdate);
	long time2 = cal.getTimeInMillis();
	long between_days=(time2-time1)/(1000*3600*24);

	return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的天数
     * @param sDate
     * @param eDate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String sDate,String eDate) throws ParseException{
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	Calendar cal = Calendar.getInstance();
	cal.setTime(sdf.parse(sDate));
	long time1 = cal.getTimeInMillis();
	cal.setTime(sdf.parse(eDate));
	long time2 = cal.getTimeInMillis();
	long between_days=(time2-time1)/(1000*3600*24);

	return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 计算两个时间之间相差的多少时间
     * @param sDate
     * @param eDate
     * @param time 1000得到秒 60000得到分 3600000 得到小时
     * @return
     */
    public static long minsBetween(String sDate,String eDate,long time){
	SimpleDateFormat d = new SimpleDateFormat();// 格式化时间
	long result=0 ;
	try {
	    result = (d.parse(eDate).getTime() - d.parse(sDate)
			    .getTime()) / time;// 当前时间减去测试时间
	    // 这个的除以1000得到秒，相应的60000得到分，3600000得到小时
	    System.out.println("当前时间减去测试时间=" + result + "分钟");
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return result;
    }



    /**
     * 计算两个时间之间相差的多少时间
     * @param sDate
     * @param eDate
     * @param  sDate 60000得到分 3600000 得到小时
     * @return
     */
    public static long secondBetween(Date sDate,Date eDate){
	long result=0 ;
	try {
	    result = (eDate.getTime() - sDate.getTime())/1000;
	    //	    		   System.out.println("两个时间相差"+result+"秒");//会打印出相差3秒
	    // 这个的除以1000得到秒，相应的60000得到分，3600000得到小时
	    //	    		System.out.println("当前时间减去测试时间=" + result + "分钟");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }





    /**
     * 获取本月第一天
     * @param
     * @return
     */
    public static String getMonFirstDay(){
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	Date theDate = calendar.getTime();

	GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
	gcLast.setTime(theDate);
	gcLast.set(Calendar.DAY_OF_MONTH, 1);
	String day_first = df.format(gcLast.getTime());
	return day_first+" 00:00:00";
    }

    /**
     * 获取当前时间到晚上24点的时间差(毫秒数)
     * @return
     */
    public static Integer getCurrAndEnd(){
	Date curr = new Date();
	Long end =  null;
	try {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 24);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    end = sdf.parse(sdf.format(cal.getTime())).getTime();
	    Long number = end - curr.getTime();
	    return (int) (number/1000);
	} catch (Exception e) {
	    e.printStackTrace();
	    // TODO: handle exception
	}
	return 0;
    }

    /**
     * 于当前时间比较，大于当前时间返回true，否则false
     * @param date 用于比较的时间
     * @return
     */
    public static boolean laterThanNow(Date date){
	return date.getTime()>new Date().getTime();
    }

    /**
     * 获取N分钟之前或之后的时间
     * @param num 整数
     * @return
     */
    public static Date getFewMinutesLater(int num){
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.MINUTE, num);
	return cal.getTime();
    }



    /**
     * 获取指定日期的明天
     * @return
     */
    public static String getNextDay(String date){
	try {
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(df.parse(date));
	    calendar.add(Calendar.DAY_OF_MONTH, 1);
	    return df.format(calendar.getTime());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }


    /**
     * 比较当前时间是否在两者之间
     * @param date1 开始时间
     * @param date2 结束时间
     * @return
     */
    public static boolean isBetween(Date date1,Date date2){
	Date date = new Date();
	return date.getTime() > date1.getTime() && date.getTime() < date2.getTime();
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfWeek(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setFirstDayOfWeek(Calendar.SUNDAY);
	calendar.setTime(date);
	calendar.set(Calendar.DAY_OF_WEEK,
			calendar.getFirstDayOfWeek()); // Sunday
	return calendar.getTime().toString();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfWeek(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setFirstDayOfWeek(Calendar.SUNDAY);
	calendar.setTime(date);
	calendar.set(Calendar.DAY_OF_WEEK,
			calendar.getFirstDayOfWeek() + 6); // Saturday
	return calendar.getTime().toString();
    }

    /**
     * 取得当前日期所在周的前一周第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfLastWeek(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return getFirstDayOfWeek(calendar.get(Calendar.YEAR),
			calendar.get(Calendar.WEEK_OF_YEAR) - 1);
    }

    /**
     * 取得当前日期所在周的前一周最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfLastWeek(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	return getLastDayOfWeek(calendar.get(Calendar.YEAR),
			calendar.get(Calendar.WEEK_OF_YEAR) - 1);
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static String getFirstDayOfWeek(int year, int week) {
	week = week - 1;
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR, year);
	calendar.set(Calendar.MONTH, Calendar.JANUARY);
	calendar.set(Calendar.DATE, 1);
	Calendar cal = (Calendar) calendar.clone();
	cal.add(Calendar.DATE, week * 7);

	return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static String getLastDayOfWeek(int year, int week) {
	week = week - 1;
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.YEAR, year);
	calendar.set(Calendar.MONTH, Calendar.JANUARY);
	calendar.set(Calendar.DATE, 1);
	Calendar cal = (Calendar) calendar.clone();
	cal.add(Calendar.DATE, week * 7);

	return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 返回指定日期的上个月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year,int month) {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.YEAR,year); //设置年份
	cal.set(Calendar.MONTH, month-1); //设置月份
	cal.set(Calendar.DAY_OF_MONTH, 1); //设置日历中月份的第1天
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化日期
	String firstDayOfMonth = sdf.format(cal.getTime());
	return firstDayOfMonth ;
    }

    /**
     * 返回指定日期的上个月的第一天
     *
     * @param date
     * @param
     * @return
     */
    public static String getFirstDayOfLastMonth(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(calendar.get(Calendar.YEAR),
			calendar.get(Calendar.MONTH) - 1, 1);
	calendar.roll(Calendar.DAY_OF_MONTH, 0);

	calendar.add(Calendar.DATE, -1);
	return calendar.getTime().toString();
    }

    /**
     * 返回指定日期的上个月的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfLastMonth(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天
	calendar.add(Calendar.DATE, -1);
	return calendar.getTime().toString();
    }

    /**
     * 判断给定日期是否在当前时间和若干天以后这段区域内
     * @param date 给定的时间
     * @param few 若干天
     * @return
     */
    public static boolean isNextFewDays(Date date,int few){
	Date now = new Date();
	//判断是否满足大于当前时间
	if(now.getTime() > date.getTime()){
	    return false;
	}
	//获取若干天以后时间
	Date date_few = addDays(now, few);
	return date.getTime() <= date_few.getTime();
    }

    /**
     * 加/减若干小时
     * @param date 时间
     * @param hour 若干
     * @return
     */
    public static Date addHours(Date date,int hour){
	Calendar ca=Calendar.getInstance();
	ca.setTime(date);
	ca.add(Calendar.HOUR_OF_DAY, hour);
	return ca.getTime();
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：2016-01-01 12:00:00
     * @param str2 时间参数 2 格式：2016-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(String str1, String str2) {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date one;
	Date two;
	long day = 0;
	long hour = 0;
	long min = 0;
	long sec = 0;
	try {
	    if (CommonUtil.isEmpty(str1) || CommonUtil.isEmpty(str2)) {
		return null;
	    }
	    one = df.parse(str1);
	    two = df.parse(str2);
	    long time1 = one.getTime();
	    long time2 = two.getTime();
	    long diff;
	    if (time1 < time2) {
		diff = time2 - time1;
	    } else {
		diff = time1 - time2;
	    }
	    day = diff / (24 * 60 * 60 * 1000);
	    hour = (diff / (60 * 60 * 1000) - day * 24);
	    min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
	    sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
	} catch (ParseException e) {
	    e.printStackTrace();
	    return null;
	}
	long[] times = { day, hour, min, sec };
	return times;
    }


    /**
     * 昨天开始时间戳
     * @return
     */
    public static Long getStartTime(){
	Date currentTime = new Date();
	currentTime.setHours(0);
	currentTime.setMinutes(0);
	currentTime.setSeconds(0);
	Date backupTime=DateUtils.addDays(currentTime, -1);
	System.out.println(DateTimeKit.format(currentTime, yyyyMMddHHmmssms));
	return currentTime.getTime()/1000;
    }


    /**
     * 某一天开始时间戳
     * @return
     */
    public static Long getStartTime(Date date){
	date.setHours(0);
	date.setMinutes(0);
	date.setSeconds(0);
	System.out.println(DateTimeKit.format(date, yyyyMMddHHmmssms));
	return date.getTime()/1000;
    }


    /**
     * 某一天结束时间戳
     * @return
     */
    public static Long getEndTime(Date date){
	date.setHours(23);
	date.setMinutes(59);
	date.setSeconds(59);
	System.out.println(DateTimeKit.format(date, yyyyMMddHHmmssms));
	return date.getTime()/1000;
    }


    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
	Date date222 = parseDate("2017-02-28 17:00:00", "yyyy-MM-dd HH:mm:ss");
	System.out.println(date222);
    }



    /**
     * 当天开始时间戳
     * @return
     */
    public static Long getEndTime(){
	//	        Calendar todayEnd = Calendar.getInstance();
	//	        todayEnd.set(Calendar.DATE, Calendar.DATE-1);
	//	        todayEnd.set(Calendar.HOUR, 14);
	//	        todayEnd.set(Calendar.MINUTE, 59);
	//	        todayEnd.set(Calendar.SECOND, 59);
	Date currentTime = new Date();
	currentTime.setHours(0);
	currentTime.setMinutes(0);
	currentTime.setSeconds(0);
	Date backupTime=currentTime;//DateUtils.addDays(currentTime,+1);
	System.out.println(DateTimeKit.format(backupTime, yyyyMMddHHmmssms));
	return backupTime.getTime()/1000;


    }

    /**
     * 当天开始时间戳
     * @return
     */
    public static Long getToDayTimeStamp(){
	Date currentTime = new Date();
	currentTime.setHours(0);
	currentTime.setMinutes(0);
	currentTime.setSeconds(0);
	Date backupTime=currentTime;//DateUtils.addDays(currentTime,+1);
	return backupTime.getTime();
    }

    /** 计算两个时间之间相差的多少时间
     * @param sDate
     * @param eDate
     * @param time 1000得到秒 60000得到分 3600000 得到小时
     * @return
     */
    public static long minsBetween(String sDate,String eDate,long time,String format){
	SimpleDateFormat d = new SimpleDateFormat(format);// 格式化时间
	long result=0 ;
	try {
	    result = (d.parse(eDate).getTime() - d.parse(sDate)
			    .getTime()) / time;// 当前时间减去测试时间
	    // 这个的除以1000得到秒，相应的60000得到分，3600000得到小时
             /*System.out.println("当前时间减去测试时间=" + result + "分钟");  */
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return result;
    }


    /**
     * 获取两个日期之间的星期数
     * 1星期日、2星期一、3星期二、4星期三、5星期四、6星期五、7星期六
     * @param date1
     * @param date2
     * @return
     */
    public static Map<Integer, String> getWeekForDate2Date(String date1,String date2){
	Map<Integer, String> map = new HashMap<>();
	try {

	    Calendar cal = Calendar.getInstance();
	    Date startTime = parseDate(date1);
	    Date closeTime = parseDate(date2);
	    if(startTime.getTime() >= closeTime.getTime()){
		throw new Exception("时间参数错误！");
	    }
	    //获取两个日期之间相差的天数
	    Long days = diffDays(closeTime,startTime);
	    for (int i = 0; i < days.intValue(); i++) {
		if(i > 6){
		    break;
		}
		Date date = addDays(startTime, i);
		cal.setTime(date);
		int week = cal.get(Calendar.DAY_OF_WEEK);
		map.put(week, weekMap.get(week));
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return map;
    }

    /**
     * 获取当前时间是周几
     * @return
     */
    public static int getWeekToday(){
	Calendar cal = Calendar.getInstance();
	int week = cal.get(Calendar.DAY_OF_WEEK);
	return week;
    }

    /**
     * 日期转化时间轴
     * @param date_str 日期
     * @param format
     * @return
     */
    public static long date2TimeStamp(String date_str,String format){
	try {
	    SimpleDateFormat sdf = new SimpleDateFormat(format);
	    return sdf.parse(date_str).getTime();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return 0;
    }
    /**
     * 两个时间相差的时间，
     * @param date_str
     * @param format
     * @param date_str1
     * @param format1
     * @param mill 1000是秒数，60000是分钟，360000是小时，360000*24是天数
     * @return
     */
    public static long timeBetween(String date_str,String format,String date_str1,String format1,long mill){
	long result=0 ;
	try {
	    long l =  DateTimeKit.date2TimeStamp(date_str,format);
	    long l1 =  DateTimeKit.date2TimeStamp(date_str1,format1);
	    if(l>l1){
		result =  (l-l1)/mill;
	    }else{
		result =  (l1-l)/mill;
	    }
	    // 这个的除以1000得到秒，相应的60000得到分，3600000得到小时
              /*System.out.println("当前时间减去测试时间=" + result + "分钟");  */
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return result;
    }

    /**
     * 分钟转换成具体的时间
     * @return
     */
    public static int[] minuteForTimes(int diff){
	int day = diff / (24 * 60);
	int hour = (diff / (60) - day * 24);
	int min = (diff - day * 24 * 60 - hour * 60);
	int sec = (diff - day * 24 * 60 - hour * 60 - min);
	return new int[]{day,hour,min,sec};
    }

    /**
     * 返回指定日期的的上个月的第一天
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfsMonth(int year,int month) {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.YEAR,year); //设置年份
	cal.set(Calendar.MONTH, month-2); //设置月份
	cal.set(Calendar.DAY_OF_MONTH, 1); //设置日历中月份的第1天
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化日期
	String firstDayOfMonth = sdf.format(cal.getTime());
	return firstDayOfMonth ;
    }

    /**
     * 获取明天的开始时间点
     * @return
     */
    public static String getTomorrowBegin(){
	Calendar cal = Calendar.getInstance();
	cal.setTime(new Date());
	cal.add(Calendar.DAY_OF_MONTH, 1);
	Date date = cal.getTime();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	return sdf.format(date)+" 00:00:00";
    }

    /**
     * 判断时间是否在直接
     * HH:mm:ss 格式时间
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isBetweenByHHmmss(Date date1, Date date2) {
	Date date = getNowDateSmallShort();
	return date.getTime() > date1.getTime() && date.getTime() < date2.getTime();
    }

    /**
     * 判断所给时间是否小于当前时间
     *
     * @param day
     *            所给时间
     * @return 小于当前时间返回true，否则为false
     * @throws Exception
     */
    public static boolean isBeforeToday(String day) throws Exception {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Date date = df.parse(day);
	return date.before( df.parse( getDate() ) );
    }

    /**
     * 判断所给时间是否小于当前时间
     *
     * @param day    所给时间
     * @param format 日期格式
     *
     * @return 小于当前时间返回true，否则为false
     * @throws Exception
     */
    public static boolean isBeforeToday( String day, String format ) throws Exception {
	SimpleDateFormat df = new SimpleDateFormat( format );
	Date date = df.parse( day );
	return date.before( df.parse( getDate() ) );
    }

    /**
     * 判断所给时间是否小于当前时间
     *
     * @param date    所给时间
     * @param format 日期格式
     *
     * @return 小于当前时间返回true，否则为false
     * @throws Exception
     */
    public static boolean isBeforeToday( Date date, String format ) throws Exception {
	SimpleDateFormat df = new SimpleDateFormat( format );
	return date.before( df.parse( getDate() ) );
    }

    /**
     * 判断所给时间是否大过当前时间
     *
     * @param day    所给时间
     * @param format 日期格式
     *
     * @return 大于当前时间返回true，否则为false
     * @throws Exception
     */
    public static boolean isAfterToday( String day, String format ) throws Exception {
	SimpleDateFormat df = new SimpleDateFormat( format );
	Date date = df.parse( day );
	return date.after( df.parse( getDate() ) );
    }
    /**
     * 判断所给时间是否大过当前时间
     *
     * @param date    所给时间
     * @param format 日期格式
     *
     * @return 大于当前时间返回true，否则为false
     * @throws Exception
     */
    public static boolean isAfterToday( Date date, String format ) throws Exception {
	SimpleDateFormat df = new SimpleDateFormat( format );
	return date.after( df.parse( getDate() ) );
    }

    /**
     * 判断时间是否已过期
     *
     * @param startDateS
     * @param endDateS
     *
     * @return true 未过期   false 过期
     */
    public static boolean isValidDate( String startDateS, String endDateS, String format ) throws Exception {
	Date startDate =parseDate( startDateS, format );
	Date endDate =parseDate( endDateS, format );
	Date now = getNowDate( format );
	if ( isAfterToday( startDate, format ) ) {
	    return true;
	}
	if ( isAfterToday( endDate, format ) && isBeforeToday( startDate, format ) ) {
	    return true;
	}
	return isSameDay( startDate, now ) || isSameDay( endDate, now );
    }
    /**
     * 判断时间是否已过期
     *
     * @param startDate
     * @param endDate
     *
     * @return true 有效   false 无效
     */
    public static boolean isValidDate( Date startDate, Date endDate, String format ) throws Exception {
        startDate=format2( startDate,format );
	endDate=format2( endDate,format );
	Date now = getNowDate( format );
	if ( isAfterToday( startDate, format ) ) {
	    return true;
	}
	if ( isAfterToday( endDate, format ) && isBeforeToday( startDate, format ) ) {
	    return true;
	}
	return isSameDay( startDate, now ) || isSameDay( endDate, now );
    }

    /**
     * 判断今天是否在时间范围内  包括最后一天
     *
     * @param startDate
     * @param endDate
     *
     * @return true 是   false 否
     */
    public static boolean isBetweenDate( String startDate, String endDate, String format ) throws Exception {
	Date now = getNowDate( format );

	Date parseStartDate = parseDate( startDate, format );
	Date parseEndDate = parseDate( endDate, format );
	if ( isSameDay( parseStartDate, now ) || isSameDay( parseEndDate, now ) ) {
	    return true;
	}
	return isBeforeToday( startDate, format ) && isAfterToday( endDate, format );

    }
    /**
     * 判断今天是否在时间范围内  包括最后一天
     *
     * @param startDate
     * @param endDate
     *
     * @return true 是   false 否
     */
    public static boolean isBetweenDate( Date startDate, Date endDate, String format ) throws Exception {
	Date now = getNowDate( format );

	if ( isSameDay( startDate, now ) || isSameDay( endDate, now ) ) {
	    return true;
	}
	return isBeforeToday( startDate, format ) && isAfterToday( endDate, format );

    }

    /**
     * 判断今天星期几是否在可用范围
     *
     * @param useWeeks [0,1,2,3,4,5,6] 分别表示 周日 周一 周二....
     *
     * @return
     * @throws Exception
     */
    public static boolean isAvailableDate( String useWeeks ) throws Exception {
	int day = getWeekToday() - 1;
	return useWeeks.contains( "" + day );

    }
}
