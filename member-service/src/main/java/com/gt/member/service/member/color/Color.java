/**
 * P 2016年5月18日
 */
package com.gt.member.service.member.color;

/**
 * @author pengjiangli  
 * @version 
 * 创建时间:2016年5月18日
 * 
 */
public enum Color {
	Color010("#63b359","Color010"),Color020("#2c9f67","Color020"),Color030("#509fc9","Color030"),
	Color040("#5885cf","Color040"),Color050("#8f62bf","Color050"),Color060("#d09a45","Color060"),
	Color070("#e4b138","Color070"),Color080("#ee903c","Color080"),Color090("#de654a","Color090"),
	Color100("#cc463d","Color100");
	
	private String code;
	private String color;
	public static String getColor(String code){
		for (Color c : Color.values()) {
			if(c.getCode().equals(code)){
				return c.getColor();
			}
		}
		return null;
	}
	
	public static String getCode(String color){
		for (Color c : Color.values()) {
			if(c.getColor().equals(color)){
				return c.getCode();
			}
		}
		return null;
	}
	
	Color(String code,String color){
		this.code=code;
		this.color=color;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
}
