/**
 * P 2016年6月12日
 */
package com.gt.member.service.old.member.color;

/**
 * 
 * 卡券状态
 * 
 * @author pengjiangli  
 * @version 
 * 创建时间:2016年6月12日
 * 
 */
public enum CardState {

  //状态 0未送审 1:审核中 2通过 3不通过
	/**
	 * “CARD_STATUS_NOT_VERIFY”,待审核 1；
“CARD_STATUS_VERIFY_FAIL”,审核失败 3；
“CARD_STATUS_VERIFY_OK”，通过审核；
“CARD_STATUS_USER_DELETE”，卡券被商户删除；
“CARD_STATUS_DISPATCH”，在公众平台投放过的卡券；
	 */
	CARD_STATUS_NOT_VERIFY("CARD_STATUS_NOT_VERIFY",1),CARD_STATUS_VERIFY_OK("CARD_STATUS_VERIFY_OK",2),CARD_STATUS_VERIFY_FAIL("CARD_STATUS_VERIFY_FAIL",3),
	CARD_STATUS_USER_DELETE("CARD_STATUS_USER_DELETE",4),CARD_STATUS_DISPATCH("CARD_STATUS_DISPATCH",5);
	
	public static Integer getCode(String status){
		for(CardState c: CardState.values()){
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
	private CardState(String status, Integer code) {
		this.status = status;
		this.code = code;
	}
}
