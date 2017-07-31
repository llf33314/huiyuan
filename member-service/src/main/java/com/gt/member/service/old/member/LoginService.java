//package com.gt.member.service.old.member;
//
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 用户手机端登录 注册 service层
// * @author Administrator
// *
// */
//public interface LoginService {
//
//	/**
//	 * 用户登录
//	 * @param busId
//	 * @param phone
//	 * @param pwd
//	 * @return
//	 */
//	public Map<String, Object> findMemberByPhone(HttpServletRequest request, Integer busId, String phone, String pwd);
//
//	/**
//	 * 用户快捷登录
//	 * @param request
//	 * @param busId
//	 * @param phone
//	 * @param vcode
//	 * @return
//	 */
//	public Map<String, Object> quickLogin(HttpServletRequest request, Integer busId, String phone, String vcode);
//	/**
//	 * 用户注册
//	 * @param map
//	 * @return
//	 */
//	public Map<String, Object> register(HttpServletRequest request, Map<String, Object> map);
//
//
//	/**
//	 * 用户重置密码
//	 * @param map
//	 * @return
//	 */
//	public Map<String, Object> resetPwd(Map<String, Object> map);
//}
