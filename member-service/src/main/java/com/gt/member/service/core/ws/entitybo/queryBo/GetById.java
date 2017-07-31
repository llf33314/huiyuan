package com.gt.member.service.core.ws.entitybo.queryBo;

import java.io.Serializable;

/**
 * 根据id查询数据统一bean
 * 调用接口有:
 * 1：根据卡包查询卡券信息:2：查询粉丝信息  3：判断是否是会员  4:判断用户的卡类型
 * 1：findCardByReceiveId  2：findMemberById  3：isMemberById  4:isCardType
 *
 */
public class GetById implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
