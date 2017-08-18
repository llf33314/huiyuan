package com.gt.member.service.memberpay;



/**
 * 定时任务 访问 接口
 * @author Administrator
 *
 */
public interface MemberCommonService {
	 /**
	  * 积分清0
	  * @param busIds
	  */
	 void clearJifen(String busIds);
	 
	 /**
	  * 添加积分记录
	  * @param str
	  */
	 void jifenLog(String str);
}
