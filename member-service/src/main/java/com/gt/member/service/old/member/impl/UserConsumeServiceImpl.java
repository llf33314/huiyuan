///**
// * P 2016年3月30日
// */
//package com.gt.member.service.old.member.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.gt.dao.member.UserConsumeMapper;
//import com.gt.service.member.UserConsumeService;
//import com.gt.util.CommonUtil;
//import com.gt.util.Page;
//
///**
// * @author pengjiangli
// * @version
// * 创建时间:2016年3月30日
// *
// */
//@Service
//public class UserConsumeServiceImpl implements UserConsumeService{
//
//	@Autowired
//	private UserConsumeMapper userConsumeMapper;
//
//	@Override
//	public Page findUserConume(Integer busId,Map<String, Object> params) {
//		params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
//				: CommonUtil.toInteger(params.get("curPage")));
//		int pageSize = 10;
//
//		Object ctIdObj = params.get("ctId");
//		Integer ctId = 0;
//		if (CommonUtil.isNotEmpty(ctIdObj)) {
//			ctId = Integer.parseInt(ctIdObj.toString());
//		}else{
//			ctId=1;
//		}
//		Object recordTypeObj = params.get("recordType");
//		Integer recordType = 0;
//		if (CommonUtil.isNotEmpty(recordTypeObj)) {
//			recordType = Integer.parseInt(recordTypeObj.toString());
//		}
//		String startDate=null;
//		if(CommonUtil.isNotEmpty(params.get("startDate"))){
//			startDate=params.get("startDate").toString();
//		}
//		String endDate=null;
//		if(CommonUtil.isNotEmpty(params.get("endDate"))){
//			endDate=params.get("endDate").toString();
//		}
//		String search=null;
//        if(CommonUtil.isNotEmpty(params.get("search"))){
//            search=params.get("search").toString();
//        }
//
//		int rowCount= userConsumeMapper.countUserConume(busId, ctId, recordType,startDate,endDate,search);
//
//
//		Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
//				pageSize, rowCount, "member/findUserConsume.do");
//		params.put("firstResult", pageSize
//				* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
//		params.put("maxResult", pageSize);
//
//		List<Map<String, Object>> list = userConsumeMapper.findUserConume(
//				busId,Integer.parseInt(params.get("firstResult").toString()),
//				pageSize, ctId, recordType,startDate,endDate,search);
//		List<Map<String, Object>> memberList=new ArrayList<Map<String,Object>>();
//		for (Map<String,Object> map : list) {
//			if(map.containsKey("nickname")){
//				try {
//					byte[] bytes=(byte[])map.get("nickname");
//					map.put("nickname", new String(bytes, "UTF-8"));
//				} catch (Exception e) {
//					map.put("nickname",null);
//				}
//				memberList.add(map);
//			}else{
//				memberList.add(map);
//			}
//
//		}
//		page.setSubList(memberList);
//
//		return page;
//	}
//
//
//	@Override
//	public Page findUserConume1(Integer busId,Map<String, Object> params) {
//		try {
//			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
//					: CommonUtil.toInteger(params.get("curPage")));
//			int pageSize = 10;
//			String startDate=null;
//			if(CommonUtil.isNotEmpty(params.get("startDate"))){
//				startDate=params.get("startDate").toString()+" 00:00:00";
//			}
//			String endDate=null;
//			if(CommonUtil.isNotEmpty(params.get("endDate"))){
//				endDate=params.get("endDate").toString()+" 23:59:59";
//			}
//			Integer payStatus=-1;
//			if(CommonUtil.isNotEmpty(params.get("payStatus"))){
//				payStatus=CommonUtil.toInteger(params.get("payStatus"));
//			}
//
//			int rowCount= userConsumeMapper.countUserConume1(busId, 2,startDate,endDate,payStatus);
//			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
//					pageSize, rowCount, "offlinepay/transactionlog.do");
//			params.put("firstResult", pageSize
//					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
//			params.put("maxResult", pageSize);
//			List<Map<String, Object>> list = userConsumeMapper.findUserConume1(
//					busId,Integer.parseInt(params.get("firstResult").toString()),
//					pageSize,  2,startDate,endDate,payStatus);
//			List<Map<String, Object>> memberList=new ArrayList<Map<String,Object>>();
//			for (Map<String,Object> map : list) {
//				if(map.containsKey("nickname")){
//					try {
//						byte[] bytes=(byte[])map.get("nickname");
//						map.put("nickname", new String(bytes, "UTF-8"));
//					} catch (Exception e) {
//						map.put("nickname",null);
//					}
//					memberList.add(map);
//				}else{
//					memberList.add(map);
//				}
//
//			}
//			page.setSubList(memberList);
//			return page;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//}
