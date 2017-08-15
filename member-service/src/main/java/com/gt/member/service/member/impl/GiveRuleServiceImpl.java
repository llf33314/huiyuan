///**
// * P 2016年3月11日
// */
//package com.gt.member.service.old.member.impl;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.SortedMap;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.gt.dao.member.CardMapper;
//import com.gt.dao.member.GiveRuleGoodsTypeMapper;
//import com.gt.dao.member.GiveRuleMapper;
//import com.gt.dao.member.GoodsTypeMapper;
//import com.gt.dao.member.GradeTypeMapper;
//import com.gt.dao.member.MemberMapper;
//import com.gt.dao.member.RechargeGiveMapper;
//import com.gt.dao.util.DaoUtil;
//import com.gt.entity.member.Card;
//import com.gt.entity.member.GiveRule;
//import com.gt.entity.member.GiveRuleGoodsType;
//import com.gt.entity.member.GiveRuleGoodsTypeKey;
//import com.gt.entity.member.GoodsType;
//import com.gt.entity.member.GradeType;
//import com.gt.entity.member.Member;
//import com.gt.entity.member.RechargeGive;
//import com.gt.controller.common.dict.DictService;
//import com.gt.controller.member.GiveRuleService;
//import com.gt.controller.member.SystemMsgService;
//import com.gt.util.CommonUtil;
//import com.gt.util.DateTimeKit;
//import com.gt.util.JsonUtil;
//
///**
// * @author pengjiangli
// * @version 创建时间:2016年3月11日
// *
// */
//@Service
//public class GiveRuleServiceImpl implements GiveRuleService {
//
//	private static final Logger LOG = Logger
//			.getLogger(GiveRuleServiceImpl.class);
//	@Autowired
//	private GiveRuleMapper giveRuleMapper;
//
//	@Autowired
//	private GiveRuleGoodsTypeMapper giveRuleGoodsTypeMapper;
//
//	@Autowired
//	private GoodsTypeMapper goodsTypeMapper;
//
//	@Autowired
//	private RechargeGiveMapper rechargeGiveMapper;
//
//
//	@Autowired
//	private DictService dictService;
//
//	@Autowired
//	private CardMapper cardMapper;
//
//	@Autowired
//	private GradeTypeMapper gradeTypeMapper;
//
//	@Autowired
//	private SystemMsgService systemMsgService;
//
//	@Autowired
//	private MemberMapper memberMapper;
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public void saveOrUpdate(String giveRule,
//			Integer busUserId) throws Exception {
//		try {
//			boolean bool = false; // 用来标识是否新增
//			Integer ctId=0;
//			List<Map> giveRuleBeans = JsonUtil.asList(giveRule, Map.class);
//			if (giveRuleBeans != null && giveRuleBeans.size() != 0) {
//				for (Map map : giveRuleBeans) {
//					GiveRule gr = new GiveRule();
//					gr.setCreateUserid(busUserId);
//					gr.setCtId(Integer.parseInt(map.get("ctId").toString()));
//					ctId=gr.getCtId();
//					if (CommonUtil.isNotEmpty(map.get("grDiscount"))) {
//						Double discount = Double.valueOf(map.get("grDiscount")
//								.toString()) * 10;
//						Integer discount1 = (new Double(discount)).intValue();
//						gr.setGrDiscount(discount1);
//					}
//					if (CommonUtil.isNotEmpty(map.get("grEquities"))) {
//						gr.setGrEquities(map.get("grEquities").toString());
//					}
//					if (CommonUtil.isNotEmpty(map.get("grGivecount"))) {
//						gr.setGrGivecount(Integer.parseInt(map.get(
//								"grGivecount").toString()));
//					}
//					if (CommonUtil.isNotEmpty(map.get("grGivetype"))) {
//						gr.setGrGivetype(Byte.valueOf(map.get("grGivetype")
//								.toString()));
//					}
//
//					if (CommonUtil.isNotEmpty(map.get("grId"))) {
//						gr.setGrId(Integer.parseInt(map.get("grId").toString()));
//					}
//
//					if (CommonUtil.isNotEmpty(map.get("grNumber"))) {
//						gr.setGrNumber(Integer.parseInt(map.get("grNumber")
//								.toString()));
//					}
//
//					if (CommonUtil.isNotEmpty(map.get("grRechargemoney"))) {
//						gr.setGrRechargemoney(Double.parseDouble(map.get(
//								"grRechargemoney").toString()));
//					}
//
//					if (CommonUtil.isNotEmpty(map.get("grStartDate"))) {
//						gr.setGrStartdate(DateTimeKit
//								.parse(map.get("grStartDate").toString(),
//										"yyyy-MM-dd"));
//					}
//
//					if (CommonUtil.isNotEmpty(map.get("grUpgradeCount"))) {
//						gr.setGrUpgradecount(Integer.parseInt(map.get(
//								"grUpgradeCount").toString()));
//					}
//
//					if (CommonUtil.isNotEmpty(map.get("grUpgradeType"))) {
//						gr.setGrUpgradetype(Byte.parseByte(map.get(
//								"grUpgradeType").toString()));
//					}
//					if (CommonUtil.isNotEmpty(map.get("grValidDate"))
//							&& !"0".equals(map.get("grValidDate"))) {
//						Integer grValiddate = Integer.parseInt(map.get(
//								"grValidDate").toString());
//						gr.setGrValiddate(grValiddate);
//					}
//
//					if (CommonUtil.isNotEmpty(map.get("gtId"))) {
//						gr.setGtId(Integer.parseInt(map.get("gtId").toString()));
//					}
//
//					gr.setBusid(busUserId);
//
//					GradeType g=new GradeType();
//					g.setGtId(gr.getGtId());
//					int buyModel=CommonUtil.toInteger(map.get("buyModel"));
//					g.setBuymodel((byte)buyModel);
//					if(buyModel==1){
//						g.setApplytype((byte)3);
//					}
//
//					if(CommonUtil.isNotEmpty(map.get("buyMoney"))){
//						Double buyMoney=CommonUtil.toDouble(map.get("buyMoney"));
//						g.setBuymoney(buyMoney);
//					}
//					if(CommonUtil.isNotEmpty(map.get("costMoney"))){
//						Double costMoney=CommonUtil.toDouble(map.get("costMoney"));
//						g.setCostmoney(costMoney);
//					}
//					if(CommonUtil.isNotEmpty(map.get("balance"))){
//						String balance=CommonUtil.toString(map.get("balance"));
//						g.setBalance(balance);
//					}
//
//
//
//					int ismemberDate=CommonUtil.toInteger(map.get("ismemberDate"));
//					g.setIsmemberdate((byte)ismemberDate);
//					int view=CommonUtil.toInteger(map.get("view"));
//					g.setIsview((byte)view);
//
//					gradeTypeMapper.updateByPrimaryKeySelective(g);
//
//					if(CommonUtil.isNotEmpty(map.get("delayDay"))){
//						gr.setDelayday(CommonUtil.toInteger(map.get("delayDay")));
//					}
//					if(CommonUtil.isNotEmpty(map.get("upgradeType"))){
//						int upgradeType=CommonUtil.toInteger(map.get("upgradeType"));
//						gr.setGrUpgradetype((byte)upgradeType);
//					}
//
//
//					if (CommonUtil.isNotEmpty(gr.getGrId())
//							&& gr.getGrId() != 0) {
//						// 保存卡片规则总表
//						giveRuleMapper.updateByPrimaryKeySelective(gr);
//					} else {
//						giveRuleMapper.insertSelective(gr);
//						bool = true;
//					}
//
//					if (CommonUtil.isNotEmpty(map.get("goodsTypes"))) {
//						JSONArray json = JSONArray.fromObject(map
//								.get("goodsTypes"));
//						for (int i = 0; i < json.size(); i++) {
//							JSONObject obj = json.getJSONObject(i);
//							GiveRuleGoodsType grgt = new GiveRuleGoodsType();
//							GiveRuleGoodsTypeKey gk = new GiveRuleGoodsTypeKey();
//							gk.setGrId(gr.getGrId());
//							// -1表示新增赠送物品类型
//							if ("-1".equals(obj.get("gtId").toString())) {
//								GoodsType goodsType = new GoodsType();
//								if (CommonUtil.isNotEmpty(obj.get("gtUnit"))) {
//									goodsType.setGtUnit(obj.get("gtUnit")
//											.toString() + obj.get("gtName"));
//								}
//								if (CommonUtil.isNotEmpty(obj.get("gtName"))) {
//									goodsType.setGtName(obj.get("gtName")
//											.toString());
//								}
//								goodsType.setGtFlow((byte) 1);
//								goodsType.setGtypeId(gr.getGtId());
//								goodsType.setBusid(busUserId);
//								goodsTypeMapper.insertSelective(goodsType);
//								gk.setGtId(goodsType.getGtId());
//								grgt.setGtId(goodsType.getGtId());
//							} else {
//								gk.setGtId(obj.getInt("gtId"));
//								grgt.setGtId(obj.getInt("gtId"));
//							}
//							grgt.setGiveType(Byte.valueOf(obj
//									.getString("giveType")));
//							grgt.setGrId(gr.getGrId());
//							grgt.setMoney(obj.getDouble("money"));
//							grgt.setNumber(obj.getInt("number"));
//							if (CommonUtil.isNotEmpty(obj.get("upperLmit"))) {
//								grgt.setUpperlmit(obj.getInt("upperLmit"));
//							}
//
//							// 旧物品删除
//							if (CommonUtil.isNotEmpty(obj.get("delete"))) {
//								if ("delete".equals(obj.getString("delete"))) {
//									// 非平台物品可以删除
//									GoodsType gt = goodsTypeMapper
//											.selectByPrimaryKey(obj
//													.getInt("gtId"));
//									if (gt.getBusid() != 0) {
//										// 删除赠送规则
//										GiveRuleGoodsTypeKey grgtk = new GiveRuleGoodsTypeKey();
//										grgtk.setGrId(gr.getGrId());
//										grgtk.setGtId(obj.getInt("gtId"));
//										giveRuleGoodsTypeMapper
//												.deleteByPrimaryKey(grgtk);
//
//										// 删除物品类型表
//										goodsTypeMapper.deleteByPrimaryKey(obj
//												.getInt("gtId"));
//									}
//								}
//							}
//
//							// 卡片赠送规则操作
//							GiveRuleGoodsType giveRuleGoodsType = giveRuleGoodsTypeMapper
//									.selectByPrimaryKey(gk);
//
//							if (giveRuleGoodsType == null) {
//								grgt.setGrId(gr.getGrId());
//								giveRuleGoodsTypeMapper.insertSelective(grgt);
//							} else {
//								giveRuleGoodsTypeMapper
//										.updateByPrimaryKeySelective(grgt);
//							}
//						}
//					}
//
//					// 针对储值卡和次卡
//					if (CommonUtil.isNotEmpty(map.get("rechargeGive"))) {
//						JSONArray json = JSONArray.fromObject(map
//								.get("rechargeGive"));
//						// 先删除再添加
//						rechargeGiveMapper.deleteBybusIdAndGrid(busUserId,
//								gr.getGrId());
//						for (int i = 0; i < json.size(); i++) {
//							JSONObject obj = json.getJSONObject(i);
//							RechargeGive rg = new RechargeGive();
//							rg.setCtid(gr.getCtId());
//							if (CommonUtil.isNotEmpty(obj.get("id"))) {
//								rg.setId(obj.getInt("id"));
//							}
//							if (CommonUtil.isNotEmpty(obj.get("giveCount"))) {
//								rg.setGivecount(Integer.parseInt(obj.get(
//										"giveCount").toString()));
//							}
//							if (CommonUtil.isNotEmpty(obj.get("money"))) {
//								rg.setMoney(Double.parseDouble(obj.get("money")
//										.toString()));
//							}
//							if (CommonUtil.isNotEmpty(obj.get("number"))) {
//								rg.setNumber(Integer.parseInt(obj.get("number")
//										.toString()));
//							}
//							if(CommonUtil.isNotEmpty(obj.get("isDate"))){
//								int isDate=CommonUtil.toInteger(obj.get("isDate"));
//								rg.setIsdate((byte)isDate);
//							}
//							rg.setBusid(busUserId);
//							rg.setGrId(gr.getGrId());
//
//							rechargeGiveMapper.insertSelective(rg);
//						}
//					}
//
//				}
//
//
////				if (bool) {
////					Integer recFreezeType=0;
////					Integer flowType=0;
////					switch (ctId) {
////					case 1:
////						recFreezeType=20;
////						flowType=25;
////						break;
////					case 2:
////						recFreezeType=21;
////						flowType=26;
////						break;
////					case 3:
////						recFreezeType=22;
////						flowType=27;
////						break;
////					case 4:
////						recFreezeType=23;
////						flowType=28;
////						break;
////					case 5:
////						recFreezeType=24;
////						flowType=29;
////						break;
////
////					default:
////						break;
////					}
////
////					// 新增粉笔和流量分配表
////					FenbiFlowRecord fenbi = new FenbiFlowRecord();
////					fenbi.setBusUserId(busUserId);
////					fenbi.setRecType(1);
////					fenbi.setRecCount(0.0);
////					fenbi.setRecUseCount(0.0);
////					fenbi.setRecDesc("会员赠送粉币");
////					fenbi.setRecFreezeType(recFreezeType);
////					fenbi.setRecFkId(Integer.parseInt(giveRuleBeans.get(0)
////							.get("ctId").toString()));
////					fenbi.setRollStatus(1);
////					fenbi.setFlowType(0);
////					fenbi.setFlowId(0);
////					fenbiFlowRecordMapper.insertSelective(fenbi);
////					SortedMap<String, Object> map = dictService.getDict("1054");
////					for (String str : map.keySet()) {
////						fenbi = new FenbiFlowRecord();
////						fenbi.setBusUserId(busUserId);
////						fenbi.setRecType(2);
////						fenbi.setRecDesc("会员赠送流量");
////						fenbi.setRecCount(0.0);
////						fenbi.setRecUseCount(0.0);
////						fenbi.setRecFreezeType(flowType);
////						fenbi.setRecFkId(Integer.parseInt(giveRuleBeans.get(0)
////								.get("ctId").toString()));
////						fenbi.setRollStatus(1);
////						switch (str) {
////						case "10":
////							fenbi.setFlowType(10);
////							fenbi.setFlowId(1);
////							break;
////						case "30":
////							fenbi.setFlowType(30);
////							fenbi.setFlowId(2);
////							break;
////						case "100":
////							fenbi.setFlowType(100);
////							fenbi.setFlowId(3);
////							break;
////						case "200":
////							fenbi.setFlowType(200);
////							fenbi.setFlowId(4);
////							break;
////						case "500":
////							fenbi.setFlowType(500);
////							fenbi.setFlowId(5);
////							break;
////						}
////						fenbiFlowRecordMapper.insertSelective(fenbi);
////					}
////				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error("方法saveOrUpdate：保存和修改赠送规则异常");
//			throw new Exception();
//		}
//	}
//
//	/**
//	 * 手动升级
//	 */
//	@Override
//	public Map<String, Object> upCard(Integer mcId,Integer busId, Integer gtId) {
//		Map<String, Object> map=new HashMap<String, Object>();
//		try {
//			Integer grId=giveRuleMapper.findBybusIdAndGtId(busId, gtId);
//			if(CommonUtil.isEmpty(grId)){
//				map.put("result", false);
//				map.put("message", "升级失败,该等级未设置赠送规则");
//				return map;
//			}
//			Card card=new Card();
//			card.setMcId(mcId);
//			card.setGrId(grId);
//			card.setGtId(gtId);
//			cardMapper.updateByPrimaryKeySelective(card);
//			map.put("result", true);
//			map.put("message", "升级成功");
//			card=cardMapper.selectByPrimaryKey(mcId);
//			Member member=memberMapper.findByMcIdAndbusId(busId, mcId);
//			//升级通知
//			systemMsgService.upgradeMemberMsg(member, card.getCardno(), CommonUtil.isEmpty(card.getExpiredate())?"长期有效":DateTimeKit.format(card.getExpiredate()));
//
//		} catch (Exception e) {
//			LOG.error("手动升级失败",e);
//			map.put("result", false);
//			map.put("message", "升级失败!");
//		}
//		return map;
//	}
//
//}
