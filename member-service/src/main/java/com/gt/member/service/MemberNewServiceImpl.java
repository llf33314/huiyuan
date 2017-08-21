package com.gt.member.service;

import com.gt.member.dao.MemberCardrecordDAO;
import com.gt.member.dao.MemberDAO;
import com.gt.member.entity.Member;
import com.gt.member.entity.MemberCardrecord;
import com.gt.member.service.member.SystemMsgService;
import com.gt.member.util.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 会员公用数据存储、业务操作接口
 * Created by Administrator on 2017/7/28 0028.
 */
@Service
public class MemberNewServiceImpl implements  MemberNewService {

    private static final Logger LOG=Logger.getLogger( MemberNewServiceImpl.class );

    @Autowired
    private MemberCardrecordDAO memberCardrecordDAO;

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private SystemMsgService systemMsgService;

    @Override
    public void saveCardRecordNew(Integer cardId, Integer recordType,
		    String number, String itemName, Integer busId, String balance,
		    Integer ctId, double amount) {
	if ( CommonUtil.isEmpty(busId)) {
	    return;
	}

	MemberCardrecord cr = new MemberCardrecord();
	cr.setCardId(cardId);
	cr.setRecordType(recordType);
	cr.setNumber(number);
	cr.setCreateDate(new Date());
	cr.setItemName(itemName);
	cr.setBusId(busId);
	cr.setBalance(balance);
	cr.setCtId(ctId);
	cr.setAmount(amount);
	try {
	    memberCardrecordDAO.insert(cr);
	    if (recordType == 2) {
		Member member = memberDAO.findByMcId1(cardId);
		// 积分变动通知
		systemMsgService.jifenMsg(cr, member);

	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    LOG.error("保存手机端记录异常", e);
	}
    }

}
