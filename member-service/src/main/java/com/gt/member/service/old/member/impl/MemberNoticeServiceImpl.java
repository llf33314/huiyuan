///**
// * P 2016年3月24日
// */
//package com.gt.member.service.old.member.impl;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.gt.dao.common.BusUserMapper;
//import com.gt.dao.common.WxPublicUsersMapper;
//import com.gt.dao.member.CardMapper;
//import com.gt.dao.member.MemberMapper;
//import com.gt.dao.member.MemberNoticeMapper;
//import com.gt.dao.member.NoticeUserMapper;
//import com.gt.dao.member.SystemNoticeCallMapper;
//import com.gt.dao.member.SystemNoticeMapper;
//import com.gt.dao.util.DaoUtil;
//import com.gt.entity.common.BusUser;
//import com.gt.entity.common.WxPublicUsers;
//import com.gt.entity.member.MemberNotice;
//import com.gt.entity.member.NoticeUser;
//import com.gt.entity.member.NoticeUserKey;
//import com.gt.controller.member.MemberNoticeService;
//import com.gt.util.CommonUtil;
//import com.gt.util.DateTimeKit;
//import com.gt.util.JsonUtil;
//import com.gt.util.Page;
//
///**
// * @author pengjiangli
// * @version 创建时间:2016年3月24日
// *
// */
//@Service
//public class MemberNoticeServiceImpl implements MemberNoticeService {
//
//	private static final Logger LOG = Logger
//			.getLogger(MemberNoticeServiceImpl.class);
//
//	@Autowired
//	private SystemNoticeMapper systemNoticeMapper;
//
//	@Autowired
//	private SystemNoticeCallMapper systemNoticeCallMapper;
//
//	@Autowired
//	private MemberMapper memberMapper;
//
//	@Autowired
//	private WxPublicUsersMapper wxPublicUsersMapper;
//
//
//	@Autowired
//	private MemberNoticeMapper memberNoticeMapper;
//
//	@Autowired
//	private NoticeUserMapper noticeUserMapper;
//
//	@Autowired
//	private BusUserMapper busUserMapper;
//
//	@Autowired
//	private CardMapper cardMapper;
//
//
//	@Override
//	public Page findMemberNotice(Integer busId, Map<String, Object> params) {
//		params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
//				: CommonUtil.toInteger(params.get("curPage")));
//		int pageSize = 10;
//
//		int rowCount = memberNoticeMapper.countMemberNotice(busId);
//
//		Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
//				pageSize, rowCount, "member/findMemberNoticeList.do");
//		params.put("firstResult", pageSize
//				* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
//		params.put("maxResult", pageSize);
//
//		List<Map<String, Object>> list = memberNoticeMapper.findMemberNotice(
//				busId, Integer.parseInt(params.get("firstResult").toString()),
//				pageSize);
//		page.setSubList(list);
//		return page;
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public void sendNoticeToUser(Integer id, String memberIds) throws Exception {
//		try {
//			MemberNotice memberNotice = memberNoticeMapper
//					.selectByPrimaryKey(id);
//			memberNotice.setSendtype((byte) 0);
//			memberNotice.setNoticemember((byte) 1);
//			/* memberNotice.setSendstuts((byte)1); */
//			memberNoticeMapper.updateByPrimaryKeySelective(memberNotice);
//			String[] strs = memberIds.split(",");
//			NoticeUser noticeUser = null;
//			NoticeUserKey nuk = null;
//			NoticeUser nUser = null;
//			for (int i = 0; i < strs.length; i++) {
//				if (CommonUtil.isNotEmpty(strs[i])) {
//					nuk = new NoticeUserKey();
//					nuk.setBusid(CommonUtil.toInteger(strs[i]));
//					nuk.setNoticeid(id);
//					nUser = noticeUserMapper.selectByPrimaryKey(nuk);
//					if (CommonUtil.isNotEmpty(nUser)) {
//						nUser.setSenddate(memberNotice.getSenddate());
//						nUser.setStatus((byte) 0);
//						noticeUserMapper.updateByPrimaryKeySelective(nUser);
//						continue;
//					}
//					;
//
//					noticeUser = new NoticeUser();
//					noticeUser.setNoticeid(id);
//					noticeUser.setBusid(Integer.parseInt(strs[i]));
//					noticeUser.setSenddate(memberNotice.getSenddate());
//					if (memberNotice.getSendmsg() == 1) {
//						noticeUser.setMsgtype((byte) 1);
//					}
//					noticeUserMapper.insertSelective(noticeUser);
//				}
//			}
//			if (memberNotice.getSendsms() == 1) {
//				sendNoticebyMemberIds(memberNotice,memberIds);
//			}
//		} catch (Exception e) {
//			LOG.error("方法sendNoticeToUser:发送信息给用户异常");
//			throw new Exception();
//		}
//	}
//
//	/**
//	 * 会员管理选择会员发送短信
//	 */
//	public void sendNoticebyMemberIds(MemberNotice memberNotice,
//			String memberIds) {
//		try {
//			if (CommonUtil.isEmpty(memberNotice)) {
//				return;
//			}
//			StringBuffer sb = null;
//			Map<String, Object> params = new HashMap<String, Object>();
//			List<Integer> ctId = null;
//			sb = new StringBuffer();
//			// 修改方式状态
//			BusUser busUser = busUserMapper.selectByPrimaryKey(memberNotice
//					.getBusid());
//			if (CommonUtil.isEmpty(busUser)) {
//				return;
//			}
//
//			String[] ids = memberIds.split(",");
//
//			List<Integer> memberList = new ArrayList<>();
//			for (int i = 0; i < ids.length; i++) {
//				if (CommonUtil.isNotEmpty(ids[i])) {
//					memberList.add(CommonUtil.toInteger(ids[i]));
//				}
//			}
//
//			// 通知某个人群
//			List<Map<String, Object>> members = memberMapper
//					.findByMemberIds(memberList);
//
//			if (CommonUtil.isEmpty(members) || members.size() <= 0){
//				return;
//			}
//
//			for (Map<String, Object> member : members) {
//				if (CommonUtil.isNotEmpty(member.get("phone"))) {
//					sb.append(member.get("phone") + ",");
//				}
//			}
//
//			try {
////				if (CommonUtil.isNotEmpty(sb.toString())) {
////					String phone = sb.substring(0, sb.lastIndexOf(","));
////					params.put("mobiles", phone);
////					params.put("content", memberNotice.getContent());
////					params.put("busId", busUser.getId());
////					params.put("model", 3);
////					WxPublicUsers wxPublicUsers = wxPublicUsersMapper
////							.selectByUserId(busUser.getId());
////					if (CommonUtil.isEmpty(wxPublicUsers)) {
////						params.put("company", busUser.getMerchant_name());
////					} else {
////						params.put("company", wxPublicUsers.getAuthorizerInfo());
////					}
////					smsSpendingService.sendSms(params);
////				}
//
//				// 修改方式状态
//			} catch (Exception e) {
//				LOG.error("循环发送信息异常", e);
//			}
//		} catch (Exception e) {
//			LOG.error("发送消息异常", e);
//		}
//	}
//
//	/**
//	 * 消息立即发送
//	 */
//	public void sendNotice() {
//		try {
//			List<Map<String, Object>> memberNotices = memberNoticeMapper
//					.findAllNotSend(new Date());
//			if (memberNotices == null || memberNotices.size() == 0) {
//				return;
//			}
//			List<Integer> memberIds = null;
//			StringBuffer sb = null;
//			Map<String, Object> params = new HashMap<String, Object>();
//			List<Integer> ctId = null;
//			List<Map<String, Object>> members = null;
//			for (Map<String, Object> map : memberNotices) {
//				sb = new StringBuffer();
//				// 修改方式状态
//				Byte noticeMember = Byte.parseByte(map.get("noticeMember")
//						.toString());
//				Integer noticeId = CommonUtil.toInteger(map.get("id"));
//				Integer busId = CommonUtil.toInteger(map.get("busId"));
//				BusUser busUser = busUserMapper.selectByPrimaryKey(busId);
//				if (CommonUtil.isEmpty(busUser)) {
//					continue;
//				}
//				// 通知某个人群
//				if (noticeMember == 1) {
//					List<Map<String, Object>> noticeUsers = noticeUserMapper
//							.findByNoticeId(noticeId);
//					if (CommonUtil.isEmpty(noticeUsers)
//							|| noticeUsers.size() == 0)
//						continue;
//					memberIds = new ArrayList<Integer>();
//					for (Map<String, Object> noticeUser : noticeUsers) {
//						Integer memberId = CommonUtil.toInteger(noticeUser
//								.get("busId"));
//						memberIds.add(memberId);
//					}
//					members = memberMapper.findByMemberIds(memberIds);
//					if (CommonUtil.isEmpty(members) || members.size() <= 0)
//						continue;
//
//					for (Map<String, Object> member : members) {
//						if (CommonUtil.isNotEmpty(member.get("phone"))) {
//							sb.append(member.get("phone") + ",");
//						}
//					}
//				}
//
//				Byte sendType = Byte.parseByte(map.get("sendType").toString());
//				if (sendType != 2) {
//					if (sendType == 1) {
//						if (CommonUtil.isNotEmpty(map.get("sendDate"))) {
//							Date sendDate = DateTimeKit.parseDate(
//									map.get("sendDate").toString(),
//									"yyyy/MM/dd HH:mm:ss");
//							// 未到发送时间
//							if (DateTimeKit.laterThanNow(sendDate)) {
//								continue;
//							}
//						}
//					}
//					String notice_user = map.get("notice_user").toString();
//					// 0表示所有会员
//					if ("0".equals(notice_user)) {
//						members = memberMapper.findBybusIdAndNotMcId(busId);
//						if (CommonUtil.isEmpty(members))
//							continue;
//
//						for (Map<String, Object> member : members) {
//							if (CommonUtil.isNotEmpty(member.get("phone"))) {
//								sb.append(member.get("phone") + ",");
//							}
//						}
//					} else if (CommonUtil.isNotEmpty(notice_user)) {
//						String[] str = notice_user.split(",");
//						ctId = new ArrayList<Integer>();
//						for (int i = 0; i < str.length; i++) {
//							if (CommonUtil.isNotEmpty(str[i])) {
//								ctId.add(CommonUtil.toInteger(str[i]));
//							}
//						}
//						if (CommonUtil.isEmpty(ctId))
//							continue;
//						members = memberMapper.findBybusIdAndctId(busId, ctId);
//						if (CommonUtil.isEmpty(members))
//							continue;
//
//						for (Map<String, Object> member : members) {
//							if (CommonUtil.isNotEmpty(member.get("phone"))) {
//								sb.append(member.get("phone") + ",");
//							}
//						}
//					}
//				}
//				try {
////					if (CommonUtil.isNotEmpty(sb.toString())) {
////						String phone = sb.substring(0, sb.lastIndexOf(","));
////						params.put("mobiles", phone);
////						params.put("content", map.get("content"));
////						params.put("busId", busUser.getId());
////						params.put("model", 3);
////						WxPublicUsers wxPublicUsers = wxPublicUsersMapper
////								.selectByUserId(busUser.getId());
////						if (CommonUtil.isEmpty(wxPublicUsers)) {
////							params.put("company", busUser.getMerchant_name());
////						} else {
////							params.put("company",
////									wxPublicUsers.getAuthorizerInfo());
////						}
////						smsSpendingService.sendSms(params);
////					}
//
//					// 修改方式状态
//					updateMemberNotice(noticeId, (byte) 1);
//				} catch (Exception e) {
//					LOG.error("循环发送信息异常", e);
//				}
//			}
//		} catch (Exception e) {
//			LOG.error("发送消息异常", e);
//		}
//	}
//
//	/**
//	 * 修改消息状态
//	 *
//	 * @param noticeId
//	 * @param sendstuts
//	 */
//	public void updateMemberNotice(Integer noticeId, Byte sendstuts) {
//		// 修改方式状态
//		MemberNotice memberNotice = new MemberNotice();
//		memberNotice.setId(noticeId);
//		memberNotice.setSendstuts(sendstuts);
//		memberNotice.setSenddate(new Date());
//		memberNoticeMapper.updateByPrimaryKeySelective(memberNotice);
//	}
//
//	@Transactional(rollbackFor = Exception.class)
//	@Override
//	public Map<String, Object> saveMemberNotice(HttpServletRequest request,
//			String json, String sendDate) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		BusUser busUser = CommonUtil.getLoginUser(request);
//		int busId = busUser.getId();
//		if (busUser.getPid() != 0) {
//			busId = busUser.getPid();
//		}
//		try {
//
//			MemberNotice memberNotice = JsonUtil.asObject(json,
//					MemberNotice.class);
//
//			if (CommonUtil.isNotEmpty(sendDate)) {
//				Date date = DateTimeKit.parse(sendDate, "yyyy-MM-dd HH:mm:ss");
//				memberNotice.setSenddate(date);
//			}
//
//			/*
//			 * if(memberNotice.getSendtype()==0){
//			 * memberNotice.setSendstuts((byte)1);
//			 * if(CommonUtil.isEmpty(memberNotice.getSenddate())){
//			 * memberNotice.setSenddate(new Date()); } }
//			 */
//
//			memberNotice.setBusid(busId);
//			memberNotice.setCreatedate(new Date());
//			memberNotice.setCreateuser(busUser.getId());
//			if (memberNotice.getSendtype() == 0) {
//				memberNotice.setSenddate(new Date());
//			}
//
//			if (CommonUtil.isNotEmpty(memberNotice.getId())) {
//				memberNoticeMapper.updateByPrimaryKeySelective(memberNotice);
//			} else {
//				memberNoticeMapper.insertSelective(memberNotice);
//			}
//
//			if (memberNotice.getSendtype() == 0) {
//				if (memberNotice.getSendsms() == 1) {
//					// 如果立即发送
//					sendNotice();
//				}
//			}
//
//			if (memberNotice.getSendmsg() == 1) {
//				// 消息 添加到用户信息表中
//				String noticeUser = memberNotice.getNoticeUser();
//				if (CommonUtil.isNotEmpty(noticeUser)) {
//					List<Map<String, Object>> cards = null;
//					if ("0".equals(noticeUser)) {
//						cards = cardMapper.findCardAll(busId);
//					} else {
//						String[] str = noticeUser.split(",");
//						List<Integer> ctIds = new ArrayList<Integer>();
//						for (int i = 0; i < str.length; i++) {
//							if (CommonUtil.isNotEmpty(str[i])) {
//								ctIds.add(Integer.parseInt(str[i]));
//							}
//						}
//						cards = cardMapper.findCardByCtIds(busId, ctIds);
//					}
//
//					List<Integer> mcIds = new ArrayList<Integer>();
//					for (Map<String, Object> card : cards) {
//						if (CommonUtil.isNotEmpty(card.get("mc_id"))) {
//							mcIds.add(CommonUtil.toInteger(card.get("mc_id")));
//						}
//					}
//					if (mcIds.size() > 0) {
//						List<Map<String, Object>> members = memberMapper
//								.findBymcIds(busId, mcIds);
//						NoticeUser nu = null;
//
//						noticeUserMapper.deleteByNoticeId(memberNotice.getId());
//
//						List<NoticeUser> list = new ArrayList<NoticeUser>();
//						for (Map<String, Object> member : members) {
//							nu = new NoticeUser();
//							nu.setBusid(CommonUtil.toInteger(member.get("id")));
//							nu.setMsgtype((byte) 1);
//							nu.setNoticeid(memberNotice.getId());
//							nu.setStatus((byte) 0);
//							nu.setSenddate(memberNotice.getSenddate());
//							list.add(nu);
//
//						}
//						if (list.size() > 0) {
//							noticeUserMapper.saveList(list);
//						}
//					}
//				}
//			}
//			map.put("result", true);
//			map.put("message", "操作成功!");
//		} catch (Exception e) {
//			LOG.error("保存消息模板异常", e);
//			throw new Exception();
//		}
//		return map;
//	}
//
//}
