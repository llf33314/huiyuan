//package com.gt.member.service.old.memberpay.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import net.sf.json.JSONObject;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.gt.service.member.MemberService;
//import com.gt.service.memberpay.MemberCommonService;
//import com.gt.service.memberpay.MemberPayService;
//import com.gt.util.CommonUtil;
//
//@Service
//public class MemberCommonServiceImpl implements MemberCommonService{
//	private static final Logger LOG = Logger
//			.getLogger(MemberCommonServiceImpl.class);
//
//	@Autowired
//	private MemberService memberService;
//
//	@Autowired
//	private MemberPayService memberPayService;
//
//	@Override
//	public void clearJifen(String busIds) {
//		try {
//			List<Integer> list=new ArrayList<Integer>();
//			String[] str=busIds.split(",");
//			for (String s : str) {
//				if(CommonUtil.isNotEmpty(s)){
//					list.add(CommonUtil.toInteger(s));
//				}
//			}
//			memberService.clearJifen(list);
//		} catch (Exception e) {
//			LOG.error("httpclien调用积分清0异常", e);
//		}
//	}
//
//
//
//	@Override
//	public void jifenLog(String str) {
//		JSONObject obj=JSONObject.fromObject(str);
//		memberPayService.saveCardRecordNew(obj.getInt("mcId"), (byte)2, obj.getString("number"), obj.getString("itemName"), obj.getInt("busId"), null, null, obj.getDouble("amount"));
//
//	}
//}
