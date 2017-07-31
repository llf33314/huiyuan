///**
// * P 2016年3月8日
// */
//package com.gt.member.service.old.member.impl;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import net.sf.json.JSONObject;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.gt.dao.member.CardMapper;
//import com.gt.dao.member.GiveRuleGoodsTypeMapper;
//import com.gt.dao.member.GiveRuleMapper;
//import com.gt.dao.member.GradeTypeMapper;
//import com.gt.dao.member.MemberFindMapper;
//import com.gt.dao.member.PublicParameterSetMapper;
//import com.gt.dao.util.DaoUtil;
//import com.gt.entity.common.BusUser;
//import com.gt.entity.member.GradeType;
//import com.gt.entity.member.MemberFind;
//import com.gt.entity.member.PublicParameterSet;
//import com.gt.service.common.dict.DictService;
//import com.gt.service.member.GradeTypeService;
//import com.gt.util.CommonUtil;
//import com.gt.util.JsonUtil;
//
///**
// * @author pengjiangli
// * @version 创建时间:2016年3月8日
// *
// */
//@Service
//public class GradeTypeServiceImpl implements GradeTypeService {
//
//	@Autowired
//	private GradeTypeMapper gradeTypeMapper;
//
//	@Autowired
//	private GiveRuleMapper giveRuleMapper;
//
//	@Autowired
//	private GiveRuleGoodsTypeMapper giveRuleGoodsTypeMapper;
//
//	@Autowired
//	private CardMapper cardMapper;
//
//	@Autowired
//	private MemberFindMapper memberFindMapper;
//
//
//	@Autowired
//	private PublicParameterSetMapper publicParameterSetMapper;
//
//	@Autowired
//	private DictService dictService;
//
//	/**
//	 * 查询商户所拥有的会员卡类型
//	 */
//	@Override
//	public List<Map<String, Object>> findByBusId(Integer busId) {
//		return gradeTypeMapper.findBybusId(busId);
//	}
//
//	/**
//	 * 保存或修改卡片
//	 */
//	public Integer saveOrUpdate(String jsonArray, Integer busId)
//			throws Exception {
//		try {
//			List<GradeType> gradeType = JsonUtil.asList(jsonArray,
//					GradeType.class);
//			Integer qiandao=0;
//			for (GradeType gt : gradeType) {
//				gt.setBusid(busId);
//				String gtLoginUrl = gt.getGtLoginUrl();
//
//				if ("/images/add_Image.png".equals(gtLoginUrl)) {
//					gt.setGtLoginUrl(null);
//				}
//
//				if(!"/images/add_Image.png".equals(gtLoginUrl) && CommonUtil.isNotEmpty(gtLoginUrl)){
//					gt.setGtLoginUrl(gtLoginUrl.split("/upload")[1]);
//				}
//
//				if (CommonUtil.isEmpty(gt.getGtId())) {
//					gradeTypeMapper.insertSelective(gt);
//				} else {
//					gradeTypeMapper.updateByPrimaryKeySelective(gt);
//				}
//				if(gt.getIseasy()==1){
//					qiandao=gt.getQiandao();
//				}
//
//			}
//			if(qiandao>0){
//				MemberFind memberFind=memberFindMapper.findByQianDao(busId);
//				if(CommonUtil.isEmpty(memberFind)){
//					MemberFind mf=new MemberFind();
//					mf.setIntegral(qiandao);
//					mf.setModel(1);
//					mf.setBusid(busId);
//					mf.setSoure((byte)0);
//					mf.setTitle("签到");
//					mf.setType((byte)2);
//					memberFindMapper.insertSelective(mf);
//				}else{
//					memberFind.setIntegral(qiandao);
//					memberFind.setModel(1);
//					memberFind.setBusid(busId);
//					memberFind.setSoure((byte)0);
//					memberFind.setTitle("签到");
//					memberFind.setType((byte)2);
//					memberFindMapper.updateByPrimaryKeySelective(memberFind);
//				}
//			}
//
//			return gradeType.get(0).getCtId();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception();
//		}
//	}
//
//	/**
//	 * 根据商户id和卡片类型查询
//	 */
//	@Override
//	public List<Map<String, Object>> findBybusIdAndCtId(Integer busId,
//			Integer ctId) {
//		return gradeTypeMapper.findBybusIdAndCtId(busId, ctId);
//	}
//
//	/**
//	 * 根据商户id和模板类型查询
//	 */
//	@Override
//	public List<Map<String, Object>> findByBusIdAndCmType(Integer busId,
//			Integer cmType) {
//		return gradeTypeMapper.findBybusIdAndCmType(busId, cmType);
//	}
//
//	/**
//	 * 根据公众号id和卡片类型删除
//	 *
//	 * @throws Exception
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public boolean deleteGradeType(int busId, Integer ctId)
//			throws Exception {
//		try {
////			Integer count = cardMapper.countCard1(busId, ctId);
////			if (count > 0) {
////				return false;
////			}
////			// 删除卡片操作
////			gradeTypeMapper.deleteBybusIdAndCtId(busId, ctId);
////
////			List<Map<String, Object>> giveRules = giveRuleMapper
////					.findByBusIdAndCtId(busId, ctId);
////			if (giveRules.size() != 0) {
////				List<Integer> list = new ArrayList<Integer>();
////				for (Map<String, Object> map : giveRules) {
////					if (CommonUtil.isNotEmpty(map.get("gr_id"))) {
////						list.add(Integer.valueOf(map.get("gr_id").toString()));
////
////					}
////				}
////				if (list.size() > 0) {
////					// 删除赠送规则
////					giveRuleMapper.deleteBygrIds(list);
////					// 删除赠送物品
////					giveRuleGoodsTypeMapper.deleteByGrIds(list);
////				}
////
////
////				Integer recFreezeType=0;
////				Integer flowType=0;
////				switch (ctId) {
////				case 1:
////					recFreezeType=20;
////					flowType=25;
////					break;
////				case 2:
////					recFreezeType=21;
////					flowType=26;
////					break;
////				case 3:
////					recFreezeType=22;
////					flowType=27;
////					break;
////				case 4:
////					recFreezeType=23;
////					flowType=28;
////					break;
////				case 5:
////					recFreezeType=24;
////					flowType=29;
////					break;
////
////				default:
////					break;
////				}
////				//数据回滚
////				//粉币
////				List<FenbiFlowRecord> fenbiRecords=	fenbiFlowRecordMapper.findByUserIdAndFreezeTypeAndPkid(busId,recFreezeType, ctId);
////				boolean fenbiBool=false;
////				String flowStr="";
////				for (FenbiFlowRecord fenbiFlowRecord : fenbiRecords) {
////					if(fenbiFlowRecord.getRecType()==1 && fenbiFlowRecord.getRecCount()>0){
////						//粉币归还账户
////						Double shengyu=fenbiFlowRecord.getRecCount()-fenbiFlowRecord.getRecUseCount();
////						String sb="update bus_user set  fans_currency=fans_currency+"+shengyu +" where id="+busId;
////						daoUtil.execute(sb);
////					}
////				}
////
////				//流量
////				List<FenbiFlowRecord> flowRecords=	fenbiFlowRecordMapper.findByUserIdAndFreezeTypeAndPkid(busId,flowType, ctId);
////				for (FenbiFlowRecord fenbiFlowRecord : flowRecords) {
////					if(fenbiFlowRecord.getRecType()==2 && fenbiFlowRecord.getRecCount()>0){
////						Double shengyu=fenbiFlowRecord.getRecCount()-fenbiFlowRecord.getRecUseCount();
////						int flow=shengyu.intValue();
////						if(flow>0){
////							String sb="update t_wx_bus_flow set count=count+"+flow+" where bus_id="+busId+" and type="+fenbiFlowRecord.getFlowType();
////							daoUtil.execute(sb);
////						}
////					}
////				}
////
////				String sb="	DELETE FROM t_wx_fenbi_flow_record WHERE bus_user_id ="+busId+" AND rec_fk_id = "+ctId;
////				daoUtil.execute(sb);
////			}
//
//			return true;
//		} catch (Exception e) {
//			throw new Exception();
//		}
//	}
//
//	@Transactional(rollbackFor=Exception.class)
//	@Override
//	public Map<String, Object> saveOrUpdataRadio(BusUser busUser,String json,Integer qiandao) throws Exception {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			int busId=busUser.getId();
//			if (busUser.getPid() != 0) {
//				busId=dictService.pidUserId(busUser.getPid());
//			}
//			PublicParameterSet ps =(PublicParameterSet) JSONObject.toBean(JSONObject.fromObject(json),PublicParameterSet.class);
//			ps.setBusid(busId);
//			if (CommonUtil.isNotEmpty(ps.getId())) {
//				publicParameterSetMapper.updateByPrimaryKeySelective(ps);
//			}
//			else {
//				ps.setBusid(busId);
//				publicParameterSetMapper.insertSelective(ps);
//			}
//
//			//签到
//			MemberFind mf=memberFindMapper.findByQianDao(busId);
//			if(CommonUtil.isNotEmpty(mf)){
//				mf.setIntegral(qiandao);
//				memberFindMapper.updateByPrimaryKeySelective(mf);
//			}else{
//				mf=new MemberFind();
//				mf.setBusid(busId);
//				mf.setModel(1);
//				mf.setSoure((byte)0);
//				mf.setIntegral(qiandao);
//				memberFindMapper.insertSelective(mf);
//			}
//			map.put("result", true);
//			map.put("message", "操作成功");
//		}
//		catch (Exception e) {
//			throw new Exception();
//		}
//		return map;
//	}
//
//}
