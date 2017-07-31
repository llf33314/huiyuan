package com.gt.member.service.old.member.impl; /**
 * P 2016年3月25日
 *//*
package com.gt.service.member.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gt.dao.member.MemberMapper;
import com.gt.dao.member.SystemNoticeCallMapper;
import com.gt.dao.member.SystemNoticeMapper;
import com.gt.dao.user.BusUserMapper;
import com.gt.dao.user.WxPublicUsersMapper;
import com.gt.entity.member.Member;
import com.gt.entity.member.SystemNotice;
import com.gt.entity.member.SystemNoticeCall;
import com.gt.entity.user.BusUser;
import com.gt.entity.user.WxPublicUsers;
import com.gt.service.common.sms.SmsSpendingService;
import com.gt.service.member.NoticeService;
import com.gt.util.CommonUtil;
import com.gt.util.JsonUtil;

*//**
 * @author pengjiangli
 * @version 创建时间:2016年3月25日
 * 
 *//*
@Service
public class NoticeServiceImpl implements NoticeService {

	private static final Logger LOG = Logger.getLogger(NoticeServiceImpl.class);

	@Autowired
	private SystemNoticeCallMapper systemNoticeCallMapper;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private WxPublicUsersMapper wxPublicUsersMapper;

	@Autowired
	private SmsSpendingService smsSpendingService;

	@Autowired
	private SystemNoticeMapper systemNoticeMapper;

	@Autowired
	private BusUserMapper busUserMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveSystemNotice(Integer busId, String json) throws Exception {
		try {
			List<SystemNotice> systemNotices = JsonUtil.asList(json,
					SystemNotice.class);
			if (systemNotices.size() != 0) {
				for (SystemNotice systemNotice : systemNotices) {
					systemNotice.setBusid(busId);
					if (CommonUtil.isNotEmpty(systemNotice.getId())) {
						systemNoticeMapper
								.updateByPrimaryKeySelective(systemNotice);
					} else {
						systemNoticeMapper.insertSelective(systemNotice);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("方法saveSystemNotice：保存消息异常");
			throw new Exception();
		}
	}

	*//**
	 * 保存系统消息
	 *//*
	@Override
	public void saveSystemNotice(Byte callType, String describe,
			Integer memberId, Integer publicId, Integer bususerId) {
		SystemNotice systemNotice = systemNoticeMapper.findBybusIdAndCallType(
				bususerId, callType);
		if (CommonUtil.isEmpty(systemNotice)) {
			return;
		}
		// 消息提醒
		if (1 == systemNotice.getMsgstatus()) {
			SystemNoticeCall snc = new SystemNoticeCall();
			snc.setCreatedate(new Date());
			snc.setDescribes(describe);
			snc.setMemberid(memberId);
			snc.setStatus((byte) 0);
			snc.setPublicid(publicId);
			systemNoticeCallMapper.insertSelective(snc);
		}

		// 短信提醒
		if (1 == systemNotice.getSmsstatus()) {
			Member member = memberMapper.selectByPrimaryKey(memberId);
			WxPublicUsers wxPublicUsers = wxPublicUsersMapper
					.selectByUserId(bususerId);
			Map<String, Object> params = new HashMap<String, Object>();
			if (CommonUtil.isNotEmpty(member.getPhone())) {
				params.put("mobiles", member.getPhone());
				params.put("content", describe);
				params.put("busId", bususerId);
				params.put("model", 3);
				try {
					if (CommonUtil.isNotEmpty(wxPublicUsers)) {
						params.put("company", wxPublicUsers.getAuthorizerInfo());
					} else {
						BusUser user = busUserMapper
								.selectByPrimaryKey(bususerId);
						params.put("company", user.getMerchant_name());
					}
					smsSpendingService.sendSms(params);
				} catch (Exception e) {
					LOG.error("方法saveSystemNotice：发送短信失败");
				}
			}
		}
	}

}
*/