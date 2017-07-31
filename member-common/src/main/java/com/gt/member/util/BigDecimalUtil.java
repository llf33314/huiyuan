package com.gt.member.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigDecimalUtil {

	
	private static final int DEF_DIV_SCALE=10;  
    
    private BigDecimalUtil(){}  
    
    
    /**
     * 两个数字相加
     * @param d1
     * @param d2
     * @return
     */
    public static Integer addInteger(Integer d1,Integer d2){  
        BigDecimal b1=new BigDecimal(Integer.toString(d1));  
        BigDecimal b2=new BigDecimal(Integer.toString(d2));  
        return b1.add(b2).intValue();  
          
    }  
      
    /**
     * 两个数字相加
     * @param d1
     * @param d2
     * @return
     */
    public static double add(double d1,double d2){  
        BigDecimal b1=new BigDecimal(Double.toString(d1));  
        BigDecimal b2=new BigDecimal(Double.toString(d2));  
        return b1.add(b2).doubleValue();  
          
    }  
      
    /**
     * 两个数字相减
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1,double d2){  
        BigDecimal b1=new BigDecimal(Double.toString(d1));  
        BigDecimal b2=new BigDecimal(Double.toString(d2));  
        return b1.subtract(b2).doubleValue();  
          
    }  
      
    /**
     * 两个数字相乘
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1,double d2){  
        BigDecimal b1=new BigDecimal(Double.toString(d1));  
        BigDecimal b2=new BigDecimal(Double.toString(d2));  
        return b1.multiply(b2).doubleValue();  
          
    } 
    
    public static Integer divInteger(double d1,double d2){  
        return (int)div(d1,d2,DEF_DIV_SCALE);  
          
    }
      
    public static double div(double d1,double d2){  
  
        return div(d1,d2,DEF_DIV_SCALE);  
          
    }  
     
    /**
     * 两个数字相除
     * @param d1
     * @param d2
     * @return
     */
    public static double div(double d1,double d2,int scale){  
        if(scale<0){  
            throw new IllegalArgumentException("The scale must be a positive integer or zero");  
        }  
        BigDecimal b1=new BigDecimal(Double.toString(d1));  
        BigDecimal b2=new BigDecimal(Double.toString(d2));  
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();  
          
    }  
    
    
    /**
	 * 处理emoji表情字符串
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {  
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find()) 
            {
                source = emojiMatcher.replaceAll("*");
                return source ; 
            }
        return source;
       }
       return source;  
    }
	
	/**
	 * 保留任意位小数（四舍五入）
	 * @param d 数字
	 * @param l 保留几位
	 * @return
	 */
	public static Double HALF_UP(Double d,int l){
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(l, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
}
