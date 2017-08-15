///**
// * P 2016年3月24日
// */
//package com.gt.member.service.old.member;
//
//import com.gt.member.util.Page;
//
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//
///**
// * @author pengjiangli
// * @version
// * 创建时间:2016年3月24日
// *
// */
//public interface MemberNoticeService {
//
//	/**
//	 * 分页查询
//	 * @return
//	 */
//	public Page findMemberNotice(Integer busId, Map<String, Object> params);
//
//	/**
//	 * 给会员发送通知
//	 * @param id
//	 * @param memberIds
//	 */
//	public void sendNoticeToUser(Integer id, String memberIds) throws Exception ;
//
//
//	/**
//	 * 保存消息发送
//	 * @param request
//	 * @param json
//	 * @param sendDate
//	 * @return
//	 */
//	public Map<String, Object> saveMemberNotice(HttpServletRequest request, String json,
//                                                String sendDate) throws Exception;
//
//	}
