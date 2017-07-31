/**
 * P 2016年5月27日
 */
package com.gt.member.service.old.member.color;

/**
 * @author pengjiangli  
 * @version 
 * 创建时间:2016年5月27日
 * 
 */
public enum CardStatus {
	
	//NORMAL          正常  0
	//	CONSUMED     已核销  4
	//	EXPIRE              已过期 2
	//	GIFTING            转赠中3
	//	GIFT_TIMEOUT  转赠超时 
	//	DELETE              已删除  1
	//	UNAVAILABLE   已失效 
	NORMAL("NORMAL",0),DELETE("DELETE",1),EXPIRE("EXPIRE",2),GIFTING("GIFTING",3),CONSUMED("CONSUMED",4),UNAVAILABLE("UNAVAILABLE",5),GIFT_TIMEOUT("GIFT_TIMEOUT",6);
	public static Integer getCode(String status){
		for(CardStatus c: CardStatus.values()){
			if(status.equals(c.getStatus())){
				return c.getCode();
			}
		}
		return -1;
	}
	
	private String status;
	private Integer code;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	private CardStatus(String status, Integer code) {
		this.status = status;
		this.code = code;
	}
}
