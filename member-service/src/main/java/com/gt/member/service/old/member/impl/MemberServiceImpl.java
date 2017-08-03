package com.gt.member.service.old.member.impl;/*package com.gt.controller.member.impl;

import io.socket.client.socke;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gt.dao.common.BusUserMapper;
import com.gt.dao.common.WxPublicUsersMapper;
import com.gt.dao.member.CardBuyMapper;
import com.gt.dao.member.CardLentMapper;
import com.gt.dao.member.CardMapper;
import com.gt.dao.member.CardOldMapper;
import com.gt.dao.member.CardRecordMapper;
import com.gt.dao.member.GiveRuleMapper;
import com.gt.dao.member.GradeTypeMapper;
import com.gt.dao.member.MemberAppletOpenidMapper;
import com.gt.dao.member.MemberDateMapper;
import com.gt.dao.member.MemberGiftMapper;
import com.gt.dao.member.MemberMapper;
import com.gt.dao.member.MemberOldMapper;
import com.gt.dao.member.MemberOptionMapper;
import com.gt.dao.member.MemberParameterMapper;
import com.gt.dao.member.MemberPickLogMapper;
import com.gt.dao.member.NoticeUserMapper;
import com.gt.dao.member.RechargeGiveMapper;
import com.gt.dao.member.RecommendMapper;
import com.gt.dao.member.UserConsumeMapper;
import com.gt.dao.util.DaoUtil;
import com.gt.entity.common.BusUser;
import com.gt.entity.common.WxPublicUsers;
import com.gt.entity.member.Card;
import com.gt.entity.member.CardBuy;
import com.gt.entity.member.CardLent;
import com.gt.entity.member.CardOld;
import com.gt.entity.member.ErrorWorkbook;
import com.gt.entity.member.GiveRule;
import com.gt.entity.member.GradeType;
import com.gt.entity.member.Member;
import com.gt.entity.member.MemberDate;
import com.gt.entity.member.MemberGift;
import com.gt.entity.member.MemberOld;
import com.gt.entity.member.MemberOption;
import com.gt.entity.member.MemberParameter;
import com.gt.entity.member.MemberPickLog;
import com.gt.entity.member.Recommend;
import com.gt.entity.member.UserConsume;
import com.gt.controller.common.dict.DictService;
import com.gt.controller.member.MemberService;
import com.gt.controller.member.SystemMsgService;
import com.gt.controller.member.export.ExcelStyle;
import com.gt.controller.memberpay.MemberPayService;
import com.gt.controller.memberpay.MemberTongjiService;
import com.gt.util.CommonUtil;
import com.gt.util.DateTimeKit;
import com.gt.util.EncryptUtil;
import com.gt.util.JedisUtil;
import com.gt.util.JsonUtil;
import com.gt.util.Page;
import com.gt.util.PropertiesUtil;
import com.gt.util.SHA1;

*//**
 * @author 李逢喜
 * @version 创建时间：2015年9月7日 下午7:14:20
 *//*
@Service
public class MemberServiceImpl implements MemberService {

	private static final Logger LOG = Logger.getLogger(MemberServiceImpl.class);
	

	@Autowired
	private MemberMapper memberMapper;


	@Autowired
	private GradeTypeMapper gradeTypeMapper;

	@Autowired
	private CardMapper cardMapper;

	@Autowired
	private GiveRuleMapper giveRuleMapper;

	@Autowired
	private DaoUtil daoUtil;

	@Autowired
	private BusUserMapper busUserMapper;

//	@Autowired
//	private StoreMapper storeMapper;

	@Autowired
	private RechargeGiveMapper rechargeGiveMapper;

	@Autowired
	private NoticeUserMapper noticeUserMapper;

//	@Autowired
//	private WxShopMapper wxShopMapper;

	@Autowired
	private WxCardReceiveMapper wxCardReceiveMapper;
//
//	@Autowired
//	private BusUserBranchRelationMapper busUserBranchRelationMapper;
//
//	@Autowired
//	private IPageService pageService;

	@Autowired
	private MemberDateMapper memberDateMapper;

	@Autowired
	private MemberPayService memberPayService;

//	@Autowired
//	private FenbiFlowRecordMapper fenbiFlowRecordMapper;

	@Autowired
	private CardRecordMapper cardRecordMapper;

	@Autowired
	private CardLentMapper cardLentMapper;

	@Autowired
	private WxPublicUsersMapper wxPublicUsersMapper;

	@Autowired
	private DictService dictService;

	@Autowired
	private RecommendMapper recommendMapper;

	@Autowired
	private MemberParameterMapper memberParameterMapper;

	@Autowired
	private MemberOptionMapper memberOptionMapper;

	@Autowired
	private MemberGiftMapper memberGiftMapper;

	@Autowired
	private SystemMsgService systemMsgService;

//	@Autowired
//	private UnionMobileService unionMobileService;

	@Autowired
	private UserConsumeMapper userConsumeMapper;

	@Autowired
	private CardBuyMapper cardBuyMapper;

//	@Autowired
//	private SmsSpendingService smsSpendingService;
//
//	@Autowired
//	private WxPayService wxPayService;

	@Autowired
	private MemberPickLogMapper memberPickLogMapper;

	@Autowired
	private MemberOldMapper memberOldMapper;

	@Autowired
	private CardOldMapper cardOldMapper;

	@Autowired
	private MemberTongjiService memberTongjiService;
	
	@Autowired
	private MemberAppletOpenidMapper memberAppletOpenidMapper;

	*//**
	 * 分配简易会员卡
	 * 
	 * @param member
	 *//*
	public Member applyCard(Member member) {
		GradeType gradeType = gradeTypeMapper.findByIsesasy(member.getBusid());
		if (CommonUtil.isEmpty(gradeType) || gradeType.getEasyapply() == 0) {
			return member;
		}
		// 判断会员领卡数量
		int count = cardMapper.countCardisBinding(member.getBusid());

		BusUser busUser = busUserMapper.selectByPrimaryKey(member.getBusid());

		int level = busUser.getLevel();

		String dictNum = dictService.dictBusUserNum(busUser.getId(), level, 4,
				"1093"); // 多粉 翼粉

		if (CommonUtil.toInteger(dictNum) < count) {
			return member;
		}

		try {
			if (CommonUtil.isEmpty(member.getMcId())) {
				Card card = new Card();
				card.setIschecked((byte) 1);
				card.setCardno(CommonUtil.getCode());
				card.setCtId(gradeType.getCtId());

				if (card.getCtId() == 4) {
					card.setExpiredate(new Date());
				}
				card.setSystemcode(CommonUtil.getNominateCode());
				card.setApplytype((byte) 4);
				card.setMemberid(member.getId());

				card.setGtId(gradeType.getGtId());

				GiveRule giveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId(
						member.getBusid(), gradeType.getGtId(),
						gradeType.getCtId());
				card.setGrId(giveRule.getGrId());

				card.setCardno(CommonUtil.getCode());
				card.setBusid(member.getBusid());
				card.setReceivedate(new Date());
				card.setIsbinding((byte) 1);
				cardMapper.insertSelective(card);

				Member member1 = new Member();
				member1.setMcId(card.getMcId());
				member1.setId(member.getId());
				memberMapper.updateByPrimaryKeySelective(member1);

				member.setMcId(card.getMcId());
			}
		} catch (Exception e) {
			LOG.error("分配简易会员卡异常", e);
		}
		return member;
	}

	@Override
	public Map<String, Object> upMemberPhone(Member member) {
		Member is_exist = memberMapper.selectByPublicIdAndPhone(
				member.getPublicId(), member.getPhone());
		Map<String, Object> result = new HashMap<String, Object>();
		if (CommonUtil.isEmpty(is_exist)) {
			member.setOperateType(CommonUtil.matchesPhoneNumber(member
					.getPhone()));
			memberMapper.updateByPrimaryKeySelective(member);
			result.put("code", "1");
			result.put("msg", "操作成功");
		} else {
			result.put("code", "-1");
			result.put("msg", "绑定手机失败，手机号码已存在");
		}
		return result;
	}

	@Override
	public Member findById(Integer id) {
		return memberMapper.selectByPrimaryKey(id);
	}

	*//** 分页查询会员 *//*
	@Override
	public Page findMemberByPublicId(Integer busId, Map<String, Object> params) {
		try {
			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
					: CommonUtil.toInteger(params.get("curPage")));
			int pageSize = 10;
			Object search1 = params.get("search");
			String search = null;
			if (CommonUtil.isNotEmpty(search1)) {
				search = search1.toString();
			}
			Object ctIdObj = params.get("ctId");
			Integer ctId = 0;
			Byte source = null;
			if (CommonUtil.isNotEmpty(ctIdObj)) {
				ctId = Integer.parseInt(ctIdObj.toString());
				if (ctId == -1) {
					ctId = ctId == -1 ? 0 : ctId;
					source = 1;
				}
			}
			Byte changeCardType = null;
			if (CommonUtil.isNotEmpty(params.get("changeCardType"))) {
				changeCardType = 1;
			}
			Object gtIdObj = params.get("gtId");
			Integer gtId = 0;
			if (CommonUtil.isNotEmpty(gtIdObj)) {
				gtId = Integer.parseInt(gtIdObj.toString());
			}
			String startDate = null;
			if (CommonUtil.isNotEmpty(params.get("startDate"))) {
				startDate = CommonUtil.toString(params.get("startDate"))
						+ " 00:00:00";
			}
			String endDate = null;
			if (CommonUtil.isNotEmpty(params.get("endDate"))) {
				endDate = CommonUtil.toString(params.get("endDate"))
						+ " 23:59:59";
			}

			Object phone1 = params.get("phone");
			String phone = null;
			if (CommonUtil.isNotEmpty(phone1)) {
				phone = phone1.toString();
			}

			int rowCount = memberMapper.countMember(busId, search, phone, ctId,
					gtId, source, changeCardType, startDate, endDate);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "member/findMember.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);

			List<Map<String, Object>> list = null;
			List<Map<String, Object>> members = null;
			if (CommonUtil.isEmpty(phone)) {
				list = memberMapper.findMemberBybusId(
						Integer.parseInt(params.get("firstResult").toString()),
						pageSize, busId, search, ctId, gtId, source,
						changeCardType, startDate, endDate);
				// 采用数据拼接方式
				List<Integer> mcIds = new ArrayList<Integer>();
				for (Map<String, Object> map : list) {
					if (CommonUtil.isNotEmpty(map.get("mc_id"))) {
						mcIds.add(CommonUtil.toInteger(map.get("mc_id")));
					}
				}
				if (mcIds.size() > 0) {
					members = memberMapper.findMemberBymcIds(busId, mcIds,
							phone);
				}
			} else {
				members = memberMapper.findMemberByPhone(busId, phone);
				if (CommonUtil.isNotEmpty(members) && members.size() > 0) {
					list = memberMapper.findMemberBybusIdAndPhone(busId,
							CommonUtil.toInteger(members.get(0).get("mc_id")));
				}

			}

			List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : list) {
				for (Map<String, Object> member : members) {
					if (CommonUtil.isNotEmpty(map.get("mc_id"))
							&& CommonUtil.isNotEmpty(member.get("mc_id"))
							&& CommonUtil.toInteger(map.get("mc_id")).equals(
									CommonUtil.toInteger(member.get("mc_id")))) {
						map.put("id", member.get("id"));
						map.put("fans_currency", member.get("fans_currency"));
						map.put("flow", member.get("flow"));
						map.put("integral", member.get("integral"));
						map.put("phone", member.get("phone"));
						map.put("nickname", member.get("nickname"));
						map.put("sex", member.get("sex"));
						map.put("totalMoney", member.get("totalMoney"));
						map.put("cardChecked", member.get("cardChecked"));
						map.put("remark", member.get("remark"));
						map.put("mc_id", member.get("mc_id"));
						if (member.containsKey("nickname")) {
							try {
								byte[] bytes = (byte[]) map.get("nickname");
								map.put("nickname", new String(bytes, "UTF-8"));
							} catch (Exception e) {
								map.put("nickname", null);
							}
							memberList.add(map);
						} else {
							memberList.add(map);
						}
					}
				}
			}
			page.setSubList(memberList);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findMember(Integer busId, Integer ctId,
			Integer gtId, String search) {
		List<Map<String, Object>> list = memberMapper.findMember(busId, search,
				ctId, gtId);
		List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			if (map.containsKey("nickname")) {
				try {
					byte[] bytes = (byte[]) map.get("nickname");
					map.put("nickname", new String(bytes, "UTF-8"));
				} catch (Exception e) {
					map.put("nickname", null);
				}
				memberList.add(map);
			} else {
				memberList.add(map);
			}

		}

		return list;
	}

	@Override
	public SXSSFWorkbook errorMember(List<ErrorWorkbook> wbs) {
		SXSSFWorkbook wb = new SXSSFWorkbook();
		try {
			// 创建工作簿 冻结第一行
			Sheet errorSheet = wb.createSheet("错误会员卡信息");
			errorSheet.createFreezePane(0, 1, 0, 1); // 冻结行
			// 设置表头
			Row row1 = errorSheet.createRow(0); // 创建行, 位于第0行
			ExcelStyle.setFirstRow(errorSheet, wb, row1, 0, "卡号（必填）", 15);
			ExcelStyle.setFirstRow(errorSheet, wb, row1, 1, "姓名", 25);
			ExcelStyle.setFirstRow(errorSheet, wb, row1, 2, "性别(0、女1、男 2、未知)",
					25);
			ExcelStyle.setFirstRow(errorSheet, wb, row1, 3, "手机号码（必填）", 20);
			ExcelStyle.setFirstRow(errorSheet, wb, row1, 4, "领卡时间(yyyy-MM-dd)",
					15);
			ExcelStyle.setFirstRow(errorSheet, wb, row1, 5, "余额或次数", 15);
			ExcelStyle.setFirstRow(errorSheet, wb, row1, 6, "积分", 15);
			ExcelStyle.setFirstRow(errorSheet, wb, row1, 7,
					"会员类型(1、积分卡2、折扣卡 3、储值卡4、时效卡5、次卡)", 35);
			ExcelStyle.setFirstRow(errorSheet, wb, row1, 8, "会员等级（1-4等）", 35);
			// 主体样式
			CellStyle cellStyle = ExcelStyle.otherRowFont(wb, (byte) 2);
			CellStyle errorCellStyle = ExcelStyle.footRowFont(wb); // 错误数据样式
			Row row = null;
			int i = 1;
			for (ErrorWorkbook ewb : wbs) {
				row = errorSheet.createRow(i++);

				Cell cell = row.createCell(0);
				cell.setCellValue(ewb.getCardNo()); // 设置内容
				if ("1".equals(ewb.getCardNoStyle())) {
					cell.setCellStyle(errorCellStyle); // 填充样式
				} else {
					cell.setCellStyle(cellStyle); // 填充样式
				}

				cell = row.createCell(1);
				cell.setCellValue(ewb.getCname()); // 设置内容
				if ("1".equals(ewb.getCnameStyle())) {
					cell.setCellStyle(errorCellStyle); // 填充样式
				} else {
					cell.setCellStyle(cellStyle); // 填充样式
				}

				cell = row.createCell(2);
				cell.setCellValue(ewb.getSex()); // 设置内容
				if ("1".equals(ewb.getSexStyle())) {
					cell.setCellStyle(errorCellStyle); // 填充样式
				} else {
					cell.setCellStyle(cellStyle); // 填充样式
				}

				cell = row.createCell(3);
				cell.setCellValue(ewb.getPhone()); // 设置内容
				if ("1".equals(ewb.getPhoneStyle())) {
					cell.setCellStyle(errorCellStyle); // 填充样式
				} else {
					cell.setCellStyle(cellStyle); // 填充样式
				}

				cell = row.createCell(4);
				cell.setCellValue(ewb.getLingquDate()); // 设置内容
				if ("1".equals(ewb.getLingquDateStyle())) {
					cell.setCellStyle(errorCellStyle); // 填充样式
				} else {
					cell.setCellStyle(cellStyle); // 填充样式
				}

				cell = row.createCell(5);
				cell.setCellValue(ewb.getBalance()); // 设置内容
				if ("1".equals(ewb.getBalanceStyle())) {
					cell.setCellStyle(errorCellStyle); // 填充样式
				} else {
					cell.setCellStyle(cellStyle); // 填充样式
				}

				cell = row.createCell(6);
				cell.setCellValue(ewb.getJifen()); // 设置内容
				if ("1".equals(ewb.getJifenStyle())) {
					cell.setCellStyle(errorCellStyle); // 填充样式
				} else {
					cell.setCellStyle(cellStyle); // 填充样式
				}

				cell = row.createCell(7);
				cell.setCellValue(ewb.getMemberType()); // 设置内容
				if ("1".equals(ewb.getMemberTypeStyle())) {
					cell.setCellStyle(errorCellStyle); // 填充样式
				} else {
					cell.setCellStyle(cellStyle); // 填充样式
				}

				cell = row.createCell(8);
				cell.setCellValue(ewb.getMemberGrade()); // 设置内容
				if ("1".equals(ewb.getMemberGradeStyle())) {
					cell.setCellStyle(errorCellStyle); // 填充样式
				} else {
					cell.setCellStyle(cellStyle); // 填充样式
				}
			}
		} catch (Exception e) {
			LOG.error("导出excel异常", e);
			e.printStackTrace();
		}
		return wb;

	}

	*//**
	 * 导入会员信息
	 * 
	 * @throws Exception
	 *//*
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<ErrorWorkbook> upLoadMember(Integer busId, BusUser danbusUser,
			InputStream input) throws Exception {

		List<Card> cards = new ArrayList<Card>();
		List<Member> members = new ArrayList<Member>();
		boolean error = false;
		List<String> cardNos = new ArrayList<String>();

		Workbook workBook = WorkbookFactory.create(input);
		Sheet sheet = workBook.getSheetAt(0);
		boolean flag = false; // 用来判断校验数据
		// 查询商户的卡片表
		List<Map<String, Object>> list = gradeTypeMapper.findBybusId1(busId);
		List<Map<String, Object>> cardList = cardMapper.findCardAll(busId);
		if (cardList != null) {
			for (Map<String, Object> map : cardList) {
				if (CommonUtil.isNotEmpty(map.get("cardNo"))) {
					cardNos.add(map.get("cardNo").toString());
				}
			}
		}
		List<ErrorWorkbook> wbs = new ArrayList<ErrorWorkbook>();
		ErrorWorkbook wb = null;
		Row row = null;
		String phones = "";

		int rowLength = sheet.getPhysicalNumberOfRows();
		// 判断会员领卡数量
		int count = cardMapper.countCardisBinding(busId);

		BusUser busUser = busUserMapper.selectByPrimaryKey(busId);

		List<Map<String, Object>> wxshops = dictService.shopList(danbusUser);

		SortedMap<String, Object> dictMap = dictService.getDict("1093");
		int level = busUser.getLevel();
		for (String dict : dictMap.keySet()) {
			if (level == CommonUtil.toInteger(dict)) {
				if (count + rowLength > CommonUtil.toInteger(dictMap.get(dict))) {
					return wbs;
				}
				break;
			}
		}

		for (int i = 1; i < rowLength; i++) {
			try {
				wb = new ErrorWorkbook();
				row = sheet.getRow(i);
				String cardNo = ExcelStyle.getStringCellValue(row.getCell(0));
				if (cardNos.contains(cardNo)) {
					wb.setCardNo("卡号已存在" + cardNo); // 设置内容
					wb.setCardNoStyle("1");
					flag = true;
				} else if (CommonUtil.isEmpty(cardNo)) {
					wb.setCardNo("不能为空"); // 设置内容
					wb.setCardNoStyle("1");
					flag = true;
				} else {
					wb.setCardNo(cardNo); // 设置内容
					wb.setCardNoStyle("0");
				}
				cardNos.add(cardNo);

				String ct_name = ExcelStyle.getStringCellValue(row.getCell(7));
				if (CommonUtil.isEmpty(ct_name)
						|| !CommonUtil.isInteger(ct_name)) {
					wb.setMemberType("会员卡类型请填写1~5数字:" + ct_name); // 设置内容
					wb.setMemberTypeStyle("1"); // 填充样式
					flag = true;
				} else {
					boolean bool = false;
					for (Map<String, Object> gradeType : list) {
						if (CommonUtil.isNotEmpty(gradeType.get("ctId"))) {
							if (!gradeType.get("ctId").toString()
									.equals(ct_name)) {
								wb.setMemberType(ct_name + "没有此会员或该卡片未发布"); // 设置内容
								wb.setMemberTypeStyle("1"); // 填充样式
								bool = true;
							} else {
								wb.setMemberType(ct_name); // 设置内容
								wb.setMemberTypeStyle("0"); // 填充样式
								bool = false;
								break;
							}
						}
					}
					if (bool) {
						flag = true;
					}
				}

				String gt_grade_name = ExcelStyle.getStringCellValue(row
						.getCell(8));
				Map<String, Object> gradeType = null;
				if (CommonUtil.isEmpty(gt_grade_name)
						|| !CommonUtil.isInteger(gt_grade_name)) {
					wb.setMemberGrade("会员等级不存在：" + gt_grade_name); // 设置内容
					wb.setMemberGradeStyle("1"); // 填充样式
					flag = true;
				} else {
					if (CommonUtil.isInteger(ct_name)) {
						List<Map<String, Object>> gradeTypeList = gradeTypeMapper
								.findBybusIdAndCtId3(busId,
										Integer.parseInt(ct_name));
						if (CommonUtil.isInteger(gt_grade_name)) {
							if (Integer.parseInt(gt_grade_name) <= gradeTypeList
									.size()) {
								gradeType = gradeTypeList.get(Integer
										.parseInt(gt_grade_name) - 1);
								wb.setMemberGrade(gt_grade_name); // 设置内容
								wb.setMemberGradeStyle("0"); // 填充样式
							} else {
								wb.setMemberGrade("没有此会员等级：" + gt_grade_name); // 设置内容
								wb.setMemberGradeStyle("1"); // 填充样式
								flag = true;
							}
						}
					} else {
						wb.setMemberGrade("会员卡类型不对,未查找到想对应等级:" + gt_grade_name); // 设置内容
						wb.setMemberGradeStyle("1"); // 填充样式
						flag = true;
					}
				}

				String nickname = ExcelStyle.getStringCellValue(row.getCell(1));
				wb.setCname(nickname); // 设置内容
				wb.setCnameStyle("0"); // 填充样式

				String sex = ExcelStyle.getStringCellValue(row.getCell(2));
				wb.setSex(sex); // 设置内容
				wb.setSexStyle("0"); // 填充样式
				if (CommonUtil.isNotEmpty(sex) && CommonUtil.isInteger(sex)) {
					if (Integer.parseInt(sex) > 2) {
						sex = "0";
					}
				} else {
					sex = "0";
				}

				String phone = ExcelStyle.getStringCellValue(row.getCell(3));
				if (CommonUtil.isEmpty(phone)
						|| !CommonUtil.isMobileNO(phone.trim())) {
					wb.setPhone("电话号码错误:" + phone); // 设置内容
					wb.setPhoneStyle("1"); // 填充样式
					flag = true;
				} else if (phones.contains(phone)) {
					wb.setPhone("电话号码重复了:" + phone); // 设置内容
					wb.setPhoneStyle("1"); // 填充样式
					flag = true;
				} else {
					phones += phone + ",";
					wb.setPhone(phone); // 设置内容
					wb.setPhoneStyle("0"); // 填充样式
				}

				String receivedate = ExcelStyle.getStringCellValue(row
						.getCell(4));
				Date date = null;
				if (CommonUtil.isEmpty(receivedate)) {
					date = new Date();
				} else {
					if (CommonUtil.isValidDate(receivedate)) {
						date = DateTimeKit.parse(receivedate, "yyyy/MM/dd");
						wb.setLingquDate(receivedate); // 设置内容
						wb.setLingquDateStyle("0"); // 填充样式
					} else {
						wb.setLingquDate("日期不对:" + receivedate); // 设置内容
						wb.setLingquDateStyle("1"); // 填充样式
						flag = true;
					}
				}

				String money = ExcelStyle.getStringCellValue(row.getCell(5));
				if (CommonUtil.isEmpty(money)) {
					money = "0";
				} else {
					if (!CommonUtil.isDouble(money)) {
						wb.setBalance("数据类型不对:" + money); // 设置内容
						wb.setBalanceStyle("1"); // 填充样式
						flag = true;
					} else {
						wb.setBalance(money); // 设置内容
						wb.setBalanceStyle("0"); // 填充样式
					}
				}

				String integral = ExcelStyle.getStringCellValue(row.getCell(6));
				if (CommonUtil.isEmpty(integral)) {
					integral = "0";
				} else {
					if (!CommonUtil.isInteger(integral)) {
						wb.setJifen("积分格式不对：" + integral); // 设置内容
						wb.setJifenStyle("1"); // 填充样式
						flag = true;
					} else {
						wb.setJifen(integral); // 设置内容
						wb.setJifenStyle("0"); // 填充样式
					}
				}

				if (!flag) {
					Card card = new Card();
					card.setCardno(cardNo);
					card.setIsbinding((byte) 0);
					card.setCtId(Integer.parseInt(ct_name));
					card.setGtId(Integer.parseInt(gradeType.get("gt_id")
							.toString()));
					card.setBusid(busId);
					card.setReceivedate(date);
					card.setSource((byte) 1);

					if (Integer.parseInt(ct_name) == 3) {
						card.setMoney(Double.parseDouble(money));
					} else if (Integer.parseInt(ct_name) == 5) {
						card.setFrequency(Integer.parseInt(money));
					}

					if (CommonUtil.isNotEmpty(wxshops) && wxshops.size() > 0) {
						card.setShopid(CommonUtil.toInteger(wxshops.get(0).get(
								"id")));
					}
					card.setOnline((byte) 0);

					Integer grId = giveRuleMapper
							.findBybusIdAndGtId(busId,
									Integer.parseInt(gradeType.get("gt_id")
											.toString()));
					card.setGrId(grId);
					card.setApplytype((byte) 0);

					cards.add(card);

					Member member = new Member();
					member.setBusid(busId);
					member.setIntegral(Integer.parseInt(integral));
					member.setPhone(phone);
					member.setNickname(nickname);
					member.setSex(Integer.parseInt(sex));
					members.add(member);
				} else {
					error = true;
				}
				wbs.add(wb);
			} catch (Exception e) {
				LOG.error("导入会员实体卡异常", e);
				e.printStackTrace();
				throw new Exception();
			}
		}

		try {
			if (!error) {
				Card c = null;
				for (int i = 0; i < cards.size(); i++) {
					// 查询导入实体卡之前 用户是否领取了会员卡，领取会员卡合并
					Member member = members.get(i);
					String phone = member.getPhone();
					Member m = memberMapper.findByPhone(busId, phone); // 已经存在的粉丝信息
					if (CommonUtil.isNotEmpty(m)) {
						if (CommonUtil.isNotEmpty(m.getMcId())) {
							// 已是会员
							Card card = cardMapper.selectByPrimaryKey(m
									.getMcId());
							// 合并数据前 将之前的会员卡数据移到cardOld表
							CardOld old = (CardOld) JSONObject.toBean(
									JSONObject.fromObject(card), CardOld.class);
							cardOldMapper.insertSelective(old);

							c = cards.get(i);
							card.setApplytype((byte) 0);
							card.setCardno(c.getCardno());
							card.setIsbinding((byte) 1);
							card.setCtId(c.getCtId());
							card.setGtId(c.getGtId());
							card.setBusid(busId);
							card.setSource((byte) 1);

							if (c.getCtId() == 3) {
								card.setMoney(card.getMoney() + c.getMoney());
							} else if (c.getCtId() == 5) {
								card.setFrequency(card.getFrequency()
										+ c.getFrequency());
							}

							card.setGrId(c.getGrId());
							cardMapper.updateByPrimaryKeySelective(card);

							Member m1 = new Member();
							m1.setLoginmode((byte) 0);
							m1.setId(m.getId());
							m1.setIntegral(m.getIntegral()
									+ member.getIntegral());
							memberMapper.updateByPrimaryKeySelective(m1);
						} else {
							// 非会员
							c = cards.get(i);
							c.setSource((byte) 1);
							c.setIsbinding((byte) 1);
							c.setApplytype((byte) 0);
							cardMapper.insertSelective(c);

							Member m1 = new Member();
							m1.setId(m.getId());
							m1.setMcId(cards.get(i).getMcId());
							m1.setIntegral(m.getIntegral()
									+ member.getIntegral());
							m1.setLoginmode((byte) 0);
							memberMapper.updateByPrimaryKeySelective(m1);
						}
					} else {
						c = cards.get(i);
						c.setApplytype((byte) 0);
						cardMapper.insertSelective(c);
						member.setMcId(c.getMcId());
						member.setBusid(busId);
						member.setLoginmode((byte) 2);
						memberMapper.insertSelective(member);
					}
				}
				return null;
			}
		} catch (Exception e) {
			LOG.error("导入数据,数据保存异常", e);
			throw new Exception();
		}
		return wbs;

	}

	@Override
	public List<Map<Integer, String>> findAllCity(String cityCode) {
		List list = null;
		try {
			String sql = "SELECT id,city_name,city_code FROM basis_city t  WHERE city_parent=(SELECT id FROM basis_city WHERE city_code='"
					+ cityCode + "' ORDER BY city_level ASC LIMIT 1)  ";
			list = daoUtil.queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Map<Integer, String>> findCityByPid(Integer[] pids) {
		String sql = "SELECT id,city_name,city_code FROM basis_city t ";
		List list = null;
		try {
			if (pids.length > 1) {
				sql += " where t.city_parent in(";
				for (int i = 0; i < pids.length; i++) {
					if (i < pids.length - 1) {
						sql += pids[i] + ",";
					} else {
						sql += pids[i];
					}
				}
				sql += ");";
			} else {
				sql += " where t.city_parent = " + pids[0];
			}
			list = daoUtil.queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Map<String, Object> saveOrUpdateMember(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (CommonUtil.isNotEmpty(param.get("memberId"))) {
			// 微信关注的
			Member member = memberMapper.selectByPrimaryKey(Integer
					.parseInt(param.get("memberId").toString()));
			Card card = cardMapper.selectByPrimaryKey(member.getMcId());

			if (CommonUtil.isEmpty(param.get("cardNo"))) {
				map.put("code", 0);
				map.put("message", "请填写卡号");
				return map;
			}

			// 商家导入卡号信息
			Card cards = cardMapper.findCardByCardNo(member.getBusid(), param
					.get("cardNo").toString());
			if (CommonUtil.isEmpty(cards) || cards.getIsbinding() == 1) {
				map.put("code", 0);
				map.put("message", "不存在该实体卡信息");
				return map;
			}

			// 商家导入会员信息
			Member member1 = memberMapper.findByMcId(member.getBusid(), param
					.get("phone").toString(), cards.getMcId());
			if (CommonUtil.isEmpty(member1)) {
				map.put("code", 0);
				map.put("message", "不存在该实体卡信息");
				return map;
			}
			if (card.getCtId() != cards.getCtId()) {
				map.put("code", 1);
				map.put("message", "会员卡类型与实体卡类型不一致");
				map.put("entityMember", member1.getId());
				return map;
			}

			member.setFansCurrency(member1.getFansCurrency()
					+ member.getFansCurrency());
			member.setFlow(member1.getFlow() + member.getFlow());
			member.setIntegral(member1.getIntegral() + member.getIntegral());
			member.setPhone(member.getPhone());
			member.setLevel(member.getLevel());

			memberMapper.updateByPrimaryKeySelective(member);
			memberMapper.deleteByPrimaryKey(member1.getId());

			card.setCardno(cards.getCardno());
			card.setMoney(card.getMoney() + cards.getMoney());
			card.setFrequency(card.getFrequency() + cards.getFrequency());
			card.setIsbinding((byte) 1);
			cardMapper.updateByPrimaryKeySelective(card);
			cardMapper.deleteByPrimaryKey(cards.getMcId());
			map.put("code", 2);
			map.put("message", "绑定成功");
		}
		return map;
	}

	@Override
	public Map<String, Object> cardTypeChecked(Integer memberId, Byte isChecked) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 微信关注的
			Member member = memberMapper.selectByPrimaryKey(memberId);
			Card card = cardMapper.selectByPrimaryKey(member.getMcId());
			if (isChecked == 1) {
				// 商家导入会员信息
				Member member1 = memberMapper.selectByPrimaryKey(card
						.getEntitymemberid());
				if (CommonUtil.isEmpty(member1)) {
					map.put("result", false);
					map.put("message", "不存在该实体卡信息");
					return map;
				}
				// 商家导入卡号信息
				Card cards = cardMapper.selectByPrimaryKey(member1.getMcId());
				member.setFansCurrency(member1.getFansCurrency()
						+ member.getFansCurrency());
				member.setFlow(member1.getFlow() + member.getFlow());
				member.setIntegral(member1.getIntegral() + member.getIntegral());
				member.setPhone(member.getPhone());
				member.setLevel(member.getLevel());
				memberMapper.updateByPrimaryKeySelective(member);
				memberMapper.deleteByPrimaryKey(member1.getId());
				card.setCardno(cards.getCardno());
				card.setMoney(card.getMoney() + cards.getMoney());
				card.setFrequency(card.getFrequency() + cards.getFrequency());
				card.setIsbinding((byte) 1);
				card.setChangecardtype((byte) 2);
				cardMapper.updateByPrimaryKeySelective(card);
				cardMapper.deleteByPrimaryKey(cards.getMcId());
			} else {
				// 发送消息给用户
				
				 * NoticeUserKey noticeUserKey = new NoticeUserKey();
				 * noticeUserKey.setNoticeid(1);
				 * noticeUserKey.setBusid(memberId); NoticeUser noticeUser =
				 * noticeUserMapper .selectByPrimaryKey(noticeUserKey); if
				 * (CommonUtil.isEmpty(noticeUser)) { noticeUser = new
				 * NoticeUser(); noticeUser.setNoticeid(1);
				 * noticeUser.setBusid(memberId); noticeUser.setMsgtype((byte)
				 * 1); noticeUser.setStatus((byte) 0);
				 * noticeUser.setSenddate(new Date());
				 * noticeUserMapper.insertSelective(noticeUser); } else {
				 * noticeUser.setStatus((byte) 0); noticeUser.setSenddate(new
				 * Date());
				 * noticeUserMapper.updateByPrimaryKeySelective(noticeUser); }
				 * card.setChangecardtype((byte) 0);
				 * cardMapper.updateByPrimaryKeySelective(card);
				 
			}
			map.put("result", true);
			map.put("message", "操作成功");
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "操作失败");
		}
		return map;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void applyMember(Recommend recommend, int applyType, Member member,
			Integer ctId, int ischecked, String nominateCode, String phone,
			String json) throws Exception {
		try {
			if (CommonUtil.isEmpty(member.getMcId())) {
				Card card = new Card();
				card.setIschecked((byte) ischecked);
				card.setCardno(CommonUtil.getCode());
				card.setCtId(ctId);
				if (CommonUtil.isNotEmpty(nominateCode)) {
					card.setNominatecode(nominateCode);
				}
				if (card.getCtId() == 4) {
					card.setExpiredate(new Date());
				}
				card.setSystemcode(CommonUtil.getNominateCode());
				card.setApplytype((byte) applyType);
				if (CommonUtil.isNotEmpty(recommend)) {
					card.setMemberid(recommend.getMemberid());
				}

				// 根据卡片类型 查询第一等级
				List<Map<String, Object>> gradeTypes = gradeTypeMapper
						.findBybusIdAndCtId3(member.getBusid(), ctId);
				if (gradeTypes != null && gradeTypes.size() > 0) {
					card.setGtId(Integer.parseInt(gradeTypes.get(0)
							.get("gt_id").toString()));

					GiveRule giveRule = giveRuleMapper
							.findBybusIdAndGtIdAndCtId(
									member.getBusid(),
									Integer.parseInt(gradeTypes.get(0)
											.get("gt_id").toString()), ctId);
					card.setGrId(giveRule.getGrId());
				}

				card.setBusid(member.getBusid());
				card.setCardno(CommonUtil.getCode());
				card.setPublicId(member.getPublicId());
				card.setReceivedate(new Date());
				card.setIsbinding((byte) 1);

				WxShop wxshop = wxShopMapper.selectMainShopByBusId(member
						.getBusid());
				if (CommonUtil.isNotEmpty(wxshop)) {
					card.setShopid(wxshop.getId());
				} else {
					List<Map<String, Object>> wxshops = wxShopMapper
							.findWxShopbyPublicId1Ver2(member.getBusid());
					if (CommonUtil.isNotEmpty(wxshops) && wxshops.size() > 0) {
						card.setShopid(CommonUtil.toInteger(wxshops.get(0).get(
								"id")));
					}
				}

				card.setOnline((byte) 0);

				cardMapper.insertSelective(card);

				Map<String, Object> mapList = JsonUtil.json2Map(json);
				Member member1 = new Member();
				member1.setPhone(phone);
				member1.setMcId(card.getMcId());
				member1.setId(member.getId());
				if (CommonUtil.isNotEmpty(mapList.get("name"))) {
					member1.setName(mapList.get("name").toString());
				}

				if (CommonUtil.isNotEmpty(mapList.get("sex"))) {
					member1.setSex(CommonUtil.toInteger(mapList.get("sex")));
				}

				if (CommonUtil.isNotEmpty(mapList.get("birth"))) {
					member1.setBirth(DateTimeKit.parseDate(mapList.get("birth")
							.toString()));
				}

				if (CommonUtil.isNotEmpty(mapList.get("email"))) {
					member1.setEmail(mapList.get("email").toString());
				}

				if (CommonUtil.isNotEmpty(mapList.get("cardId"))) {
					member1.setCardid(mapList.get("cardId").toString());
				}

				memberMapper.updateByPrimaryKeySelective(member1);

				MemberParameter mp = memberParameterMapper
						.findByMemberId(member.getId());
				if (CommonUtil.isEmpty(mp)) {
					mp = new MemberParameter();
				}
				boolean flag = false;
				if (CommonUtil.isNotEmpty(mapList.get("provice"))) {
					mp.setProvincecode(mapList.get("provice").toString());
					flag = true;
				}

				if (CommonUtil.isNotEmpty(mapList.get("city"))) {
					mp.setCitycode(mapList.get("city").toString());
					flag = true;
				}
				if (CommonUtil.isNotEmpty(mapList.get("countyCode"))) {
					mp.setCountycode(mapList.get("countyCode").toString());
					flag = true;
				}
				if (CommonUtil.isNotEmpty(mapList.get("address"))) {
					mp.setAddress(mapList.get("address").toString());
					flag = true;
				}

				if (CommonUtil.isNotEmpty(mapList.get("getMoney"))) {
					mp.setGetmoney(CommonUtil.toDouble(mapList.get("getMoney")));
					flag = true;
				}
				if (flag) {
					if (CommonUtil.isEmpty(mp.getId())) {
						mp.setMemberid(member.getId());
						memberParameterMapper.insertSelective(mp);
					} else {
						mp.setId(mp.getId());
						memberParameterMapper.updateByPrimaryKeySelective(mp);
					}
				}
				// 判断是否是否是完善资料 还是修改资料
				member.setMcId(card.getMcId());
				giveMemberGift(member, mp);

				member1.setBusid(member.getBusid());
				// 发送短信通知
				systemMsgService.sendNewMemberMsg(member1);

				// 推荐赠送积分 粉币 流量
				if (CommonUtil.isNotEmpty(recommend)) {
					boolean bool = false;
					Member member2 = memberMapper.selectByPrimaryKey(recommend
							.getMemberid());
					Member member3 = new Member();
					member3.setId(recommend.getMemberid());

					if (CommonUtil.isNotEmpty(recommend.getFlow())
							&& recommend.getFlow() > 0) {
						member3.setFlow(member2.getFlow() + recommend.getFlow());
						memberPayService.saveCardRecordNew(member2.getMcId(),
								(byte) 4, recommend.getFlow() + "MB", "推荐赠送",
								member2.getBusid(), null, 0,
								recommend.getFlow());
						bool = true;
					}

					if (CommonUtil.isNotEmpty(recommend.getFenbi())
							&& recommend.getFenbi() > 0) {
						member3.setFansCurrency(member2.getFansCurrency()
								+ recommend.getFenbi());
						memberPayService.saveCardRecordNew(member2.getMcId(),
								(byte) 3, recommend.getFenbi() + "粉币", "推荐赠送",
								member2.getBusid(), null, 0,
								recommend.getFenbi());
						bool = true;
					}

					if (CommonUtil.isNotEmpty(recommend.getIntegral())
							&& recommend.getIntegral() > 0) {
						member3.setIntegral(member2.getIntegral()
								+ recommend.getIntegral());
						memberPayService.saveCardRecordNew(member2.getMcId(),
								(byte) 2, recommend.getIntegral() + "积分",
								"推荐赠送", member2.getBusid(), null, 0,
								recommend.getIntegral());
						bool = true;
					}
					if (bool) {
						memberMapper.updateByPrimaryKeySelective(member3);
					}
					recommend.setIsuser((byte) 1);

					recommendMapper.updateByPrimaryKeySelective(recommend);

					if (recommend.getMoney() > 0) {
						Card c2 = cardMapper.selectByPrimaryKey(member2
								.getMcId());
						Card newC = new Card();
						newC.setMcId(c2.getMcId());
						newC.setGivemoney(c2.getGivemoney()
								+ recommend.getMoney());
						cardMapper.updateByPrimaryKeySelective(newC);
						memberPayService.saveCardRecordNew(member2.getMcId(),
								(byte) 1, recommend.getMoney() + "元", "推荐赠送",
								0, null, 0, recommend.getMoney());
					}
				}

				// 如果会员卡需要审核发送短信提醒商家审核
				if (ischecked == 0) {
					// ischecked==0时会员卡需要审核
					if (member != null && member.getBusid() != null) {// 判断商家ID不为空时查询该商家的手机号是否填写
						BusUser busUser = busUserMapper
								.selectByPrimaryKey(member.getBusid());
						if (CommonUtil.isNotEmpty(busUser.getPhone())) {// 商家手机号不为空就发送短信提醒商家审核
							// 发送短信
//							Map<String, Object> companyMap = new HashMap<String, Object>();
//							companyMap.put("busId", member.getBusid());
//							companyMap.put("company",
//									busUser.getMerchant_name());
//							companyMap.put("mobiles", busUser.getPhone());
//							companyMap.put("content", "尊敬的商家,手机号：" + phone
//									+ "，已提交会员卡申请，请尽快审核。 ");
//							companyMap.put("model", 3);
//							smsSpendingService.sendSms(companyMap);
						}

					}
				}
			}
			
			

		} catch (Exception e) {
			LOG.error("领取会员卡异常", e);
			e.printStackTrace();
			throw new Exception();
		}
	}

	@Override
	public Map<String, Object> findMemberCard(BusUser busUser,
			String cardNoKey, String cardNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		String cardNodecrypt = "";
		map.put("saomao", 0);
		try {
			// 如果手动输入 会出现异常
			cardNodecrypt = EncryptUtil.decrypt(cardNoKey, cardNo);
			map.put("saomao", 1);
		} catch (Exception e) {
			// 如果不是扫码 判断商家是否允许不扫码
			SortedMap<String, Object> maps = dictService.getDict("A001");
			Object obj = maps.get(busUser.getId().toString());
			if (CommonUtil.isNotEmpty(obj)) {
				map.put("saomao", 1);
			}
		}

		int busId = busUser.getId();
		if (busUser.getPid() != 0) {
			busId = dictService.pidUserId(busUser.getPid());
		}

		if (cardNodecrypt.contains("?time")) {
			// 查询卡号是否存在
			Long time = Long.parseLong(cardNodecrypt.substring(cardNodecrypt
					.indexOf("?time=") + 6));

			cardNo = cardNodecrypt.substring(0, cardNodecrypt.indexOf("?time"));

			Card card1 = cardMapper.findCardByCardNo(busId, cardNo);
			if (card1.getCtId() == 3) {
				// 2分钟后为超时
				if (DateTimeKit.secondBetween(new Date(time), new Date()) > 120) {
					// 二维码已超时
					map.put("result", false);
					map.put("message", "二维码已超时!");
					return map;
				}
			}
		}

		Card card = null;
		try {
			// 判断是否借给他人
			CardLent c = cardLentMapper.findByCode(cardNo);
			if (CommonUtil.isNotEmpty(c)) {
				// 判断时间是否在有效时间内
				// 5分钟后为超时
				map.put("saomao", 1);
				if (DateTimeKit.secondBetween(c.getCreatedate(), new Date()) > 300) {
					// 二维码已超时
					map.put("result", false);
					map.put("message", "二维码已超时!");
					return map;
				}
				card = cardMapper.selectByPrimaryKey(c.getMcid());

				map.put("jie", 1);
				map.put("lentMoney", c.getLentmoney());
				map.put("clId", c.getId());
			}

		} catch (Exception e) {

		}

		try {
			// 查询卡号是否存在
			if (CommonUtil.isEmpty(card)) {
				card = cardMapper.findCardByCardNo(busId, cardNo);
			}

			if (CommonUtil.isEmpty(card)) {
				Member member = memberMapper.findByPhone(busId, cardNo);
				if (CommonUtil.isNotEmpty(member)) {
					card = cardMapper.selectByPrimaryKey(member.getMcId());
				}
			}

			if (CommonUtil.isEmpty(card)) {
				map.put("result", false);
				map.put("message", "卡片不存在!");
				return map;
			} else if (card.getCardstatus() == 1) {
				map.put("result", false);
				map.put("message", "会员已拉黑!");
				return map;
			} else {
				List<Map<String, Object>> cards = cardMapper.findCardById(card
						.getMcId());
				GiveRule giveRule = giveRuleMapper.selectByPrimaryKey(card
						.getGrId());
				Member member = memberMapper.findByMcIdAndbusId(busId,
						card.getMcId());
				map.put("result", true);
				map.put("nickName", member.getNickname());
				map.put("phone", member.getPhone());
				map.put("ctName", cards.get(0).get("ct_name"));
				map.put("gradeName", cards.get(0).get("gt_grade_name"));
				map.put("cardNo", card.getCardno());
				map.put("ctId", card.getCtId());
				map.put("discount", giveRule.getGrDiscount());
				map.put("money", card.getMoney());
				map.put("frequency", card.getFrequency());
				map.put("integral", member.getIntegral());

				Double fenbiMoeny = memberPayService.currencyCount(null,
						member.getFansCurrency());
				map.put("fenbiMoeny", fenbiMoeny);

				Double jifenMoeny = memberPayService.integralCount(null,
						new Double(member.getIntegral()), busId);
				map.put("jifenMoeny", jifenMoeny);

				List<Map<String, Object>> shopIds = null;

//				if (busUser.getPid() == 0) {
//					shopIds = wxShopMapper.findWxShopbyPublicId1Ver2(busUser
//							.getId());
//				} else {
//					shopIds = busUserBranchRelationMapper
//							.findBusUserShop(busUser.getId());
//				}

				WxPublicUsers wxPublicUsers = wxPublicUsersMapper
						.selectByUserId(busId);

				if (CommonUtil.isNotEmpty(wxPublicUsers)
						&& CommonUtil.isNotEmpty(member.getOpenid())) {
					// 查询优惠券信息
					List<Map<String, Object>> cardList = wxCardReceiveMapper
							.findByOpenId1(wxPublicUsers.getId(),
									member.getOpenid());
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					if (CommonUtil.isNotEmpty(cardList) && cardList.size() > 0) {
						for (Map<String, Object> map2 : cardList) {
							// 时间判断
							if (CommonUtil.isNotEmpty(map2
									.get("begin_timestamp"))
									&& CommonUtil.isNotEmpty(map2
											.get("end_timestamp"))) {
								if (DateTimeKit.laterThanNow(DateTimeKit.parse(
										map2.get("begin_timestamp").toString(),
										"yyyy-MM-dd hh:mm:ss"))) {
									continue;
								}
								if (!DateTimeKit.laterThanNow(DateTimeKit
										.parse(map2.get("end_timestamp")
												.toString(),
												"yyyy-MM-dd hh:mm:ss"))) {
									continue;
								}
							} else {
								if (DateTimeKit
										.laterThanNow(DateTimeKit.addDays(
												DateTimeKit.parse(
														map2.get("ctime")
																.toString(),
														"yyyy-MM-dd hh:mm:ss"),
												CommonUtil.toInteger(map2
														.get("fixed_begin_term"))))) {
									continue;
								}
								if (!DateTimeKit.laterThanNow(DateTimeKit
										.addDays(DateTimeKit.parse(
												map2.get("ctime").toString(),
												"yyyy-MM-dd hh:mm:ss"),
												CommonUtil.toInteger(map2
														.get("fixed_term"))))) {
									continue;
								}
							}

							String day = DateTimeKit.getDayToEnglish();
							if (!map2.get("time_limit").toString()
									.contains(day)) {
								continue;
							}

							for (Map<String, Object> shopid : shopIds) {
								if (CommonUtil.isEmpty(shopid.get("poiId"))) {
									continue;
								}
								if (map2.get("location_id_list")
										.toString()
										.contains(
												shopid.get("poiId").toString())) {
									list.add(map2);
									break;
								}
							}

						}
					}
					if (list.size() > 0) {
						map.put("code", 1);
					}
					map.put("cardList", JSONArray.fromObject(list));
				}
			}

			// 查询能使用的多粉优惠券

			MemberDate memberDate = memberPayService.findMemeberDate(busId,
					card.getCtId());
			if (card.getCtId() == 2) {
				if (CommonUtil.isNotEmpty(memberDate)) {
					map.put("memberDiscount", memberDate.getDiscount());
					map.put("memberDate", true);
				}
			}
			map.put("result", true);
			return map;
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "查询异常");
			e.printStackTrace();
		}
		return map;
	}

	*//** 充值查询卡片 *//*
	@Override
	public Map<String, Object> findMemberCardByrecharge(Integer busId,
			String cardNoKey, String cardNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		String cardNodecrypt = "";
		try {
			// 如果手动输入 会出现异常
			cardNodecrypt = EncryptUtil.decrypt(cardNoKey, cardNo);
		} catch (Exception e) {

		}
		if (cardNodecrypt.contains("?time")) {
			Long time = Long.parseLong(cardNodecrypt.substring(cardNodecrypt
					.indexOf("?time=") + 6));
			// 2分钟后为超时
			if (DateTimeKit.secondBetween(new Date(time), new Date()) > 120) {
				// 二维码已超时
				map.put("result", false);
				map.put("message", "二维码已超时!");
				return map;
			} else {
				cardNo = cardNodecrypt.substring(0,
						cardNodecrypt.indexOf("?time"));
			}
		}

		// 查询卡号是否存在
		Card card = cardMapper.findCardByCardNo(busId, cardNo);

		if (CommonUtil.isEmpty(card)) {
			Member member = memberMapper.findByPhone(busId, cardNo);
			if (CommonUtil.isNotEmpty(member)
					&& memberPayService.isMemember(member.getId())) {
				card = cardMapper.selectByPrimaryKey(member.getMcId());
			}
		}

		if (CommonUtil.isEmpty(card)) {
			map.put("result", false);
			map.put("message", "卡片不存在!");
			return map;
		}
		if (card.getCardstatus() == 1) {
			map.put("result", false);
			map.put("message", "会员已拉黑!");
			return map;
		}
		if (card.getCtId() == 1) {
			map.put("result", false);
			map.put("message", "积分卡不能充值!");
			return map;
		}

		if (card.getCtId() == 2) {
			map.put("result", false);
			map.put("message", "折扣卡不能充值!");
			return map;
		}
		List<Map<String, Object>> cards = cardMapper.findCardById(card
				.getMcId());
		GiveRule giveRule = giveRuleMapper.selectByPrimaryKey(card.getGrId());
		Member member = memberMapper.findByMcIdAndbusId(busId, card.getMcId());
		map.put("result", true);
		map.put("nickName", member.getNickname());
		map.put("phone", member.getPhone());
		map.put("ctName", cards.get(0).get("ct_name"));
		map.put("gradeName", cards.get(0).get("gt_grade_name"));
		map.put("cardNo", card.getCardno());
		map.put("ctId", card.getCtId());
		map.put("discount", giveRule.getGrDiscount());
		map.put("money", card.getMoney());
		map.put("frequency", card.getFrequency());
		map.put("integral", member.getIntegral());

		if (card.getCtId() == 3 || card.getCtId() == 5) {
			MemberDate memberdate = memberPayService.findMemeberDate(
					member.getPublicId(), card.getCtId());
			if (CommonUtil.isNotEmpty(memberdate)) {
				List<Map<String, Object>> recharges = rechargeGiveMapper
						.findBybusIdAndGrId(busId, card.getGrId(), 1);
				map.put("recharges", recharges);
			} else {
				List<Map<String, Object>> recharges = rechargeGiveMapper
						.findBybusIdAndGrId(busId, card.getGrId(), 0);
				map.put("recharges", recharges);
			}
		}
		if (card.getCtId() == 4) {
			map.put("expiredate",
					DateTimeKit.format(card.getExpiredate(), "yyyy-MM-dd"));
			List<Map<String, Object>> giveRules = giveRuleMapper
					.findBybusIdAndCtId1(busId, 4);
			map.put("giveRules", giveRules);
		}
		return map;
	}

	*//** 单个审批会员 *//*
	@Override
	public Map<String, Object> cardApplyChecked(Integer memberId,
			Byte ischecked, String phone, Map<String, Object> companyMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Member member = memberMapper.selectByPrimaryKey(memberId);
			Card card = cardMapper.selectByPrimaryKey(member.getMcId());
			if (card.getIschecked() != 1) {
				if (ischecked == 1) {
					card.setIschecked((byte) 1);
					cardMapper.updateByPrimaryKeySelective(card);
				} else {
					cardMapper.deleteByPrimaryKey(member.getMcId());
					member.setMcId(null);
					member.setOldid(null);
					memberMapper.updateByPrimaryKey(member);
				}
			}
			// 发送短信
			String mobiles = "";
			String cont = "";
			if (CommonUtil.isNotEmpty(phone)) {
				mobiles = phone;
			}
			if (ischecked == 0) {
				cont = "尊敬的用户您好：您的" + companyMap.get("company")
						+ "会员卡,商家未审核通过,暂时无法享受会员权益.";
			} else {
				cont = "尊敬的用户您好：您的" + companyMap.get("company")
						+ "会员卡,商家已审核通过,各种会员权益等您来体验！";
			}
			companyMap.put("mobiles", mobiles);
			companyMap.put("content", cont);
			companyMap.put("model", 3);
	//		smsSpendingService.sendSms(companyMap);
			map.put("result", true);
			map.put("message", "操作成功");
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "操作异常");
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> findStore(Integer busId, String lng,
			String lat) {
		List<Map<String, Object>> wxshops = null;
		try {
			if (CommonUtil.isEmpty(lng) || CommonUtil.isEmpty(lat)) {
				// 门店
				wxshops = wxShopMapper.findWxShopByPublicIdVer2(busId, null,
						null);
			} else {
				wxshops = wxShopMapper
						.findWxShopByPublicIdVer2(busId, lat, lng);
			}
		} catch (Exception e) {
			LOG.error("查询门面信息异常", e);
			e.printStackTrace();
		}
		return wxshops;
	}

	@Override
	public List<Map<String, Object>> findStoreJifenAll(Integer busId) {
		// 暂时使用

//		List<Map<String, Object>> shopIdList = wxShopMapper
//				.findWxShopbyPublicId1Ver2(busId);
//
//		List<Integer> shopIds = new ArrayList<Integer>();
//		for (Map<String, Object> map : shopIdList) {
//			shopIds.add(CommonUtil.toInteger(map.get("id")));
//		}
//		if (shopIds.size() == 0) {
//			return null;
//		}
//
//		shopIds = pageService.shopIsJiFen(shopIds);
//		if (shopIds.size() == 0) {
//			return null;
//		}
//
//		List<Map<String, Object>> shopes = new ArrayList<Map<String, Object>>();
//		List<Map<String, Object>> stores = storeMapper.findByShopIds(shopIds);
//		Map<String, Object> maps = null;
//		for (Map<String, Object> map2 : stores) {
//			maps = new HashMap<String, Object>();
//			maps.put("nameStore", map2.get("sto_name"));
//			maps.put("idStore", map2.get("id"));
//			maps.put("phoneStore", map2.get("sto_phone"));
//			maps.put("addrStore", map2.get("sto_address"));
//			shopes.add(maps);
//		}
//		return shopes;
		return null;
	}

	@Override
	public List<Map<String, Object>> findStoreFenbiAll(Integer busId) {

//		List<Map<String, Object>> shopIdList = wxShopMapper
//				.findWxShopbyPublicId1Ver2(busId);
//
//		List<Integer> shopIds = new ArrayList<Integer>();
//		for (Map<String, Object> map : shopIdList) {
//			shopIds.add(CommonUtil.toInteger(map.get("id")));
//		}
//		if (shopIds.size() == 0) {
//			return null;
//		}
//
//		shopIds = pageService.shopIsFenbi(shopIds);
//		if (shopIds.size() == 0) {
//			return null;
//		}
//
//		List<Map<String, Object>> shopes = new ArrayList<Map<String, Object>>();
//		List<Map<String, Object>> stores = storeMapper.findByShopIds(shopIds);
//		Map<String, Object> maps = null;
//		for (Map<String, Object> map2 : stores) {
//			maps = new HashMap<String, Object>();
//			maps.put("nameStore", map2.get("sto_name"));
//			maps.put("idStore", map2.get("id"));
//			maps.put("phoneStore", map2.get("sto_phone"));
//			maps.put("addrStore", map2.get("sto_address"));
//			shopes.add(maps);
//		}
//		return shopes;
		
		return null;
	}

	@Override
	public int updateMemberSale(Integer memberId, Integer saleNumber,
			Integer saleMoney) {
		try {
			String sql = "update t_wx_bus_member set saleNumber=saleNumber+?,saleMoney=saleMoney+? where id=?";
			int count = daoUtil.update(sql, saleNumber, saleMoney, memberId);
			return count;
		} catch (Exception e) {
			LOG.error("修改会员交易统计一次", e);
		}
		return -1;
	}

	@Override
	public Boolean isBusMember(int memberId, int busId) {
		String sql = "SELECT count(1) from t_wx_bus_member t1 LEFT JOIN t_wx_public_users t2 on t1.public_id = t2.id WHERE t2.bus_user_id = "
				+ busId + " and t1.id = " + memberId;
		return daoUtil.queryForInt(sql) > 0 ? true : false;
	}

	@Override
	public Map<String, Object> saveMemberDate(Integer busId, String memberDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			MemberDate memberdate = (MemberDate) JSONObject.toBean(
					JSONObject.fromObject(memberDate), MemberDate.class);
			memberdate.setBusid(busId);
			if (CommonUtil.isNotEmpty(memberdate.getId())) {
				memberDateMapper.updateByPrimaryKeySelective(memberdate);
			} else {
				memberDateMapper.insertSelective(memberdate);
			}
			map.put("result", true);
			map.put("message", "操作成功");
		} catch (Exception e) {
			LOG.error("保存或修改会员日异常", e);
			map.put("result", false);
			map.put("message", "操作异常");
		}
		return map;
	}

	*//**
	 * @param busId
	 * @param ids
	 * @param integral
	 * @param fans_currency
	 * @param type
	 * @return
	 * @throws Exception
	 *//*
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> updateMember(int busId, String ids,
			Integer integral, double fans_currency, Integer type)
			throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			BusUser busUser = busUserMapper.selectByPrimaryKey(busId);

			StringBuffer sb = new StringBuffer(
					"update t_wx_bus_member set fans_currency=fans_currency+"
							+ fans_currency + ",integral=integral+" + integral);
			sb.append(" where id in(" + ids + "-1)");

			byte recordType = 2;
			Double num = integral.doubleValue();
			String itemname = "商家发放积分";
			// 粉币发放
			if (type == 1) {
				String[] str = ids.split(",");
				int j = 0;
				for (int i = 0; i < str.length; i++) {
					if (CommonUtil.isNotEmpty(str[i])) {
						j++;
					}
				}

				if (j > 0) {
					Double fansNumber = j * fans_currency;
					if (busUser.getFansCurrency() < fansNumber) {
						returnMap.put("result", false);
						returnMap.put("message", "粉币不足请充值");
						return returnMap;
					}

					// 新增粉笔和流量分配表
//					FenbiFlowRecord fenbi = new FenbiFlowRecord();
//					fenbi.setBusUserId(busUser.getId());
//					fenbi.setRecType(1);
//					fenbi.setRecCount(fansNumber);
//					fenbi.setRecUseCount(fansNumber);
//					fenbi.setRecDesc("商家发放粉币给会员");
//					fenbi.setRecFreezeType(19);
//					fenbi.setRollStatus(2);
//					fenbi.setFlowType(0);
//					fenbi.setFlowId(0);
//					fenbiFlowRecordMapper.insertSelective(fenbi);

					StringBuffer busSb = new StringBuffer(
							"update bus_user set fans_currency=fans_currency-"
									+ fansNumber + "where id="
									+ busUser.getId());
					daoUtil.update(busSb.toString());

				}
				num = fans_currency;
				recordType = 3;
				itemname = "商家发放粉币";

			}
			daoUtil.update(sb.toString());
			String[] str = ids.split(",");
			StringBuffer memberSb = new StringBuffer(
					"SELECT m.id,m.public_id,m.mc_id,m.integral FROM t_wx_bus_member  m  where m.id in (");
			for (int i = 0; i < str.length; i++) {
				if (CommonUtil.isNotEmpty(str[i])) {
					memberSb.append(str[i] + ",");
				}
			}
			memberSb.append("-1)");
			List<Map<String, Object>> list = daoUtil.queryForList(memberSb
					.toString());

			String itemName = "";
			if (recordType == 3) {
				itemName = num + "粉币";
			} else {
				itemName = num + "积分";
			}
			for (Map<String, Object> map : list) {
				memberPayService.saveCardRecordNew(
						CommonUtil.toInteger(map.get("mc_id")), recordType,
						itemName, itemname, busUser.getId(), null, 0, num);
			}
			returnMap.put("result", true);
			returnMap.put("message", "赠送成功");
		} catch (Exception e) {
			LOG.error("赠送失败", e);
			throw new Exception();
		}
		return returnMap;
	}

	@Override
	public Map<String, Object> changeFlow(Integer id, Integer type) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		Member member = memberMapper.selectByPrimaryKey(id);
//		if (CommonUtil.isEmpty(member.getPhone())) {
//			map.put("result", -2);
//			map.put("message", "请完善手机号码");
//			return map;
//		}
//		if (member.getFlow() < type) {
//			map.put("result", -1);
//			map.put("message", "流量不足,不能兑换");
//			return map;
//		}
//
//		BusUser busUser = busUserMapper.selectByPrimaryKey(member.getBusid());
//
//		Card card = cardMapper.selectByPrimaryKey(member.getMcId());
//
//		// 查询分配的存在流量包
////		FenbiFlowRecord fenbiFlowRecord = fenbiFlowRecordMapper
////				.getFenbiFlowRecordByUserIdAndType(busUser.getId(), 2, 5, type,
////						card.getCtId());
////
////		if (CommonUtil.isEmpty(fenbiFlowRecord)
////				|| fenbiFlowRecord.getRecCount() <= 0) {
////			map.put("result", -1);
////			map.put("message", "分配流量不足,领取失败");
////		}
//
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("busId", busUser.getId());
//		params.put("prizeCount", type);
//		params.put("mobile", member.getPhone());
//		WxPublicUsers wxPublicUsers = wxPublicUsersMapper
//				.selectByPrimaryKey(member.getPublicId());
//		params.put("publicId", wxPublicUsers.getId());
//		params.put("memberId", id);
//		list.add(params);
//		try {
////			Map<String, Object> result = MobileLocationUtil.adcServices(list,
////					3, "");
//		} catch (Exception e) {
//			map.put("result", -1);
//			map.put("message", "领取失败");
//			LOG.error("流量包领取异常", e);
//		}
//
//		// 修改流量包数量
//		fenbiFlowRecordMapper.updateFlowReduce(busUser.getId(), 1, 5, type,
//				card.getCtId());
		return null;
	}

	@Override
	public Page findCommend(Integer busId, Map<String, Object> params) {
		params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
				: CommonUtil.toInteger(params.get("curPage")));
		try {
			int pageSize = 10;
			Object search1 = params.get("search");
			String search = null;
			if (CommonUtil.isNotEmpty(search1)) {
				search = search1.toString();
			}
			Integer recommendType = null;
			if (CommonUtil.isNotEmpty(params.get("recommendType"))) {
				recommendType = CommonUtil.toInteger(params
						.get("recommendType"));
			}

			int rowCount = memberMapper.countCommend(busId, search,
					recommendType);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "member/findcommend.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = memberMapper.findCommend(busId,
					search, CommonUtil.toInteger(params.get("firstResult")),
					pageSize, recommendType);
			List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : list) {
				if (map.containsKey("nickname")) {
					try {
						byte[] bytes = (byte[]) map.get("nickname");
						map.put("nickname", new String(bytes, "UTF-8"));
					} catch (Exception e) {
						map.put("nickname", null);
					}
					memberList.add(map);
				} else {
					memberList.add(map);
				}

			}
			page.setSubList(memberList);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, Object> upgradeMember(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (CommonUtil.isNotEmpty(param.get("memberId"))) {
			// 微信关注的
			Member member = memberMapper.selectByPrimaryKey(Integer
					.parseInt(param.get("memberId").toString()));
			Card card = cardMapper.selectByPrimaryKey(member.getMcId());

			String phone = CommonUtil.toString(param.get("phone"));

			try {
				Map<String, Object> returnMap = mergeMemberCard(
						member.getBusid(), phone, member.getId());
				if ("1".equals(CommonUtil.toString(returnMap.get("isCard")))) {
					map.put("result", true);
					map.put("message", "尊敬用户：你已是本店会员,会员绑定成功");
					return map;
				}
			} catch (Exception e) {
				LOG.error("会员绑定异常", e);
				map.put("result", false);
				map.put("message", "会员绑定异常");
				return map;
			}

			List<Map<String, Object>> gradeTypes = gradeTypeMapper
					.findAllBybusId(member.getBusid(),
							CommonUtil.toInteger(param.get("ctId")));

			Card card1 = new Card();
			card1.setMcId(card.getMcId());
			card1.setGtId(CommonUtil.toInteger(gradeTypes.get(0).get("gt_id")));
			card1.setGrId(CommonUtil.toInteger(gradeTypes.get(0).get("gr_id")));
			card1.setCtId(CommonUtil.toInteger(param.get("ctId")));
			card1.setIschecked(new Byte(gradeTypes.get(0).get("isCheck")
					.toString()));
			card1.setApplytype((byte) 0);

			cardMapper.updateByPrimaryKeySelective(card1);

			MemberGift memberGift = memberGiftMapper.findBybusIdAndmodelCode(
					member.getBusid(), 0);

			Member member1 = new Member();
			if (CommonUtil.isNotEmpty(memberGift)) {

				member1.setId(member.getId());
				member1.setPublicId(member.getPublicId());
				if (memberGift.getJifen() > 0) {
					// 积分操作
					member1.setIntegral(member.getIntegral()
							+ memberGift.getJifen().intValue());
					memberPayService.saveCardRecordNew(member.getMcId(),
							(byte) 2, memberGift.getJifen() + "积分", "完善资料赠送积分",
							0, null, 0, memberGift.getJifen());

				}

				if (memberGift.getFenbi() > 0) {
					// 粉币操作
					member1.setFansCurrency(member.getFansCurrency()
							+ memberGift.getFenbi());
					memberPayService.saveCardRecordNew(member.getMcId(),
							(byte) 3, memberGift.getFenbi() + "粉币", "完善资料赠送粉币",
							0, null, 0, memberGift.getFenbi());

				}

				if (memberGift.getFlow() > 0) {
					// 流量操作
					member1.setFlow(member.getFlow()
							+ memberGift.getFlow().intValue());
					memberPayService.saveCardRecordNew(member.getMcId(),
							(byte) 4, memberGift.getFlow() + "MB", "完善资料赠送流量",
							0, null, 0, memberGift.getFlow());
				}
				memberMapper.updateByPrimaryKeySelective(member1);
			}

			// 升级通知
			systemMsgService.upgradeMemberMsg(member, card.getCardno(),
					CommonUtil.isEmpty(card.getExpiredate()) ? "长期有效"
							: DateTimeKit.format(card.getExpiredate()));

			map.put("result", true);
			map.put("message", "升级成功");
		}
		return map;
	}

	@Override
	public Map<String, Object> loadFlowOrFenbi(Integer ctId, int busId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
//		try {
//			// 查询会员卡粉币和流量冻结数据
//
//			Integer recFreezeType = 0;
//			Integer flowType = 0;
//			switch (ctId) {
//			case 1:
//				recFreezeType = 20;
//				flowType = 25;
//				break;
//			case 2:
//				recFreezeType = 21;
//				flowType = 26;
//				break;
//			case 3:
//				recFreezeType = 22;
//				flowType = 27;
//				break;
//			case 4:
//				recFreezeType = 23;
//				flowType = 28;
//				break;
//			case 5:
//				recFreezeType = 24;
//				flowType = 29;
//				break;
//
//			default:
//				break;
//			}
//			// 粉币
//			List<FenbiFlowRecord> fenbiRecords = fenbiFlowRecordMapper
//					.findByUserIdAndFreezeTypeAndPkid(busId, recFreezeType,
//							ctId);
//			boolean fenbiBool = false;
//			String flowStr = "";
//			for (FenbiFlowRecord fenbiFlowRecord : fenbiRecords) {
//				if (fenbiFlowRecord.getRecType() == 1) {
//					fenbiBool = true;
//				}
//			}
//
//			// 流量
//			List<FenbiFlowRecord> flowRecords = fenbiFlowRecordMapper
//					.findByUserIdAndFreezeTypeAndPkid(busId, flowType, ctId);
//			for (FenbiFlowRecord fenbiFlowRecord : flowRecords) {
//				if (fenbiFlowRecord.getRecType() == 2) {
//					flowStr = flowStr + fenbiFlowRecord.getFlowType() + ",";
//				}
//			}
//
//			SortedMap<String, Object> map = dictService.getDict("1054");
//			for (String str : map.keySet()) {
//				if (flowStr.contains(str)) {
//					continue;
//				}
//				FenbiFlowRecord fenbi = new FenbiFlowRecord();
//				fenbi.setBusUserId(busId);
//				fenbi.setRecType(2);
//				fenbi.setRecDesc("会员赠送流量");
//				fenbi.setRecCount(0.0);
//				fenbi.setRecUseCount(0.0);
//				fenbi.setRecFreezeType(flowType);
//				fenbi.setRecFkId(ctId);
//				fenbi.setRollStatus(1);
//				switch (str) {
//				case "10":
//					fenbi.setFlowType(10);
//					fenbi.setFlowId(1);
//					break;
//				case "30":
//					fenbi.setFlowType(30);
//					fenbi.setFlowId(2);
//					break;
//				case "100":
//					fenbi.setFlowType(100);
//					fenbi.setFlowId(3);
//					break;
//				case "200":
//					fenbi.setFlowType(200);
//					fenbi.setFlowId(4);
//					break;
//				case "500":
//					fenbi.setFlowType(500);
//					fenbi.setFlowId(5);
//					break;
//				}
//				fenbiFlowRecordMapper.insertSelective(fenbi);
//			}
//			if (!fenbiBool) {
//				// 新增粉笔和流量分配表
//				FenbiFlowRecord fenbi = new FenbiFlowRecord();
//				fenbi.setBusUserId(busId);
//				fenbi.setRecType(1);
//				fenbi.setRecCount(0.0);
//				fenbi.setRecUseCount(0.0);
//				fenbi.setRecDesc("会员赠送粉币");
//				fenbi.setRecFreezeType(recFreezeType);
//				fenbi.setRecFkId(ctId);
//				fenbi.setRollStatus(1);
//				fenbi.setFlowType(0);
//				fenbi.setFlowId(0);
//				fenbiFlowRecordMapper.insertSelective(fenbi);
//			}
//			returnMap.put("result", true);
//			returnMap.put("message", "同步成功");
//		} catch (Exception e) {
//			returnMap.put("result", false);
//			returnMap.put("message", "同步失败");
//		}
		return returnMap;
	}

	@Override
	public Map<String, Object> updateMember(Map<String, Object> parma)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Member memberOld = memberMapper.selectByPrimaryKey(Integer
					.parseInt(parma.get("id").toString()));

			Member member = new Member();

			if (CommonUtil.isNotEmpty(parma.get("tel"))) {
				member.setPhone(parma.get("tel").toString());
			}

			if (CommonUtil.isNotEmpty(parma.get("pwd"))) {
				member.setPwd(SHA1.encode(parma.get("pwd").toString()));
			}

			// 修改手机号
			if (CommonUtil.isEmpty(memberOld.getPhone())
					|| !memberOld.getPhone().equals(member.getPhone())) {
				Object vcode = parma.get("vcode");
				if (CommonUtil.isEmpty(vcode)) {
					map.put("result", false);
					map.put("message", "请输入验证码");
					return map;
				}
				String vcode1 = JedisUtil.get(vcode.toString());
				if (CommonUtil.isEmpty(vcode1)) {
					map.put("result", false);
					map.put("message", "验证码超时或错误");
					return map;
				}
				JedisUtil.del(vcode1);
				// 联盟卡操作
				if (CommonUtil.isNotEmpty(memberOld.getPhone())) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("oldPhone", memberOld.getPhone());
					// 零时数据
					param.put("busId", memberOld.getBusid());
					param.put("newPhone", member.getPhone());
				//	unionMobileService.updateMemberPhone(param);
				}

				Map<String, Object> returnMap = mergeMemberCard(
						memberOld.getBusid(), member.getPhone(),
						Integer.parseInt(parma.get("id").toString()));
			}

			MemberParameter memberParameter = new MemberParameter();

			if (CommonUtil.isNotEmpty(parma.get("provincecode"))) {
				memberParameter.setProvincecode(parma.get("provincecode")
						.toString());
			}
			if (CommonUtil.isNotEmpty(parma.get("city"))) {
				memberParameter.setCitycode(parma.get("city").toString());
			}
			if (CommonUtil.isNotEmpty(parma.get("countyCode"))) {
				memberParameter.setCountycode(parma.get("countyCode")
						.toString());
			}

			if (CommonUtil.isNotEmpty(parma.get("address"))) {
				memberParameter.setAddress(parma.get("address").toString());
			}

			if (CommonUtil.isNotEmpty(parma.get("getmoney"))) {
				memberParameter.setGetmoney(CommonUtil.toDouble(parma
						.get("getmoney")));
			}

			MemberParameter memberParameter1 = memberParameterMapper
					.findByMemberId(Integer
							.parseInt(parma.get("id").toString()));

			if (CommonUtil.isEmpty(memberParameter1)) {
				memberParameter.setMemberid(Integer.parseInt(parma.get("id")
						.toString()));
				memberParameterMapper.insertSelective(memberParameter);
			} else {
				memberParameter.setMemberid(Integer.parseInt(parma.get("id")
						.toString()));
				memberParameter.setId(memberParameter1.getId());
				memberParameterMapper
						.updateByPrimaryKeySelective(memberParameter);
			}

			if (parma.get("email") != null) {
				member.setEmail(parma.get("email").toString());
			} else {
				member.setEmail("");
			}
			if (parma.get("name") != null) {
				member.setName(parma.get("name").toString());
			}

			if (CommonUtil.isNotEmpty(parma.get("gender"))) {
				member.setSex(Integer.parseInt(parma.get("gender").toString()));
			}
			if (CommonUtil.isNotEmpty(parma.get("id"))) {
				member.setId(Integer.parseInt(parma.get("id").toString()));
			}
			if (parma.get("birth") != null) {
				member.setBirth(DateTimeKit.parseDate(parma.get("birth")
						.toString()));
			}
			if (CommonUtil.isNotEmpty(parma.get("cardId"))) {
				member.setCardid(parma.get("cardId").toString());
			} else {
				member.setCardid("");
			}

			if (CommonUtil.isNotEmpty(parma.get("imageurls"))) {
				String[] str = parma.get("imageurls").toString().split(",");
				for (int i = 0; i < str.length; i++) {
					if (CommonUtil.isNotEmpty(str[i])) {
						if (CommonUtil.isEmpty(member.getCardimg())) {
							member.setCardimg(str[i]);
						} else if (CommonUtil.isEmpty(member.getCardimgback())) {
							member.setCardimgback(str[i]);
							break;
						}
					}
				}
			}
			memberMapper.updateByPrimaryKeySelective(member);
			map.put("result", true);

			// 判断是否是否是完善资料 还是修改资料
			giveMemberGift(memberOld, memberParameter1);

		} catch (Exception e) {
			LOG.error("修改信息异常", e);
			map.put("result", false);
			map.put("message", "保存失败");
			throw new Exception();
		}
		return map;
	}

	*//**
	 * 泛会员 和正式会员完善资料 赠送物品
	 * 
	 * @param memberOld
	 * @param memberParameter1
	 * @return
	 *//*
	public boolean giveMemberGift(Member memberOld,
			MemberParameter memberParameter1) {
		try {
			MemberOption memberOption = memberOptionMapper
					.findByBusId(memberOld.getBusid());
			if (CommonUtil.isEmpty(memberOption)) {
				return false;
			}

			boolean flag = false;
			if (CommonUtil.isNotEmpty(memberOption.getNameoption())
					&& memberOption.getNameoption() == 1
					&& CommonUtil.isEmpty(memberOld.getName())) {
				flag = true;
			} else if (CommonUtil.isNotEmpty(memberOption.getSexoption())
					&& memberOption.getSexoption() == 1
					&& CommonUtil.isEmpty(memberOld.getSex())) {
				flag = true;
			} else if (CommonUtil.isNotEmpty(memberOption.getPhoneoption())
					&& memberOption.getPhoneoption() == 1
					&& CommonUtil.isEmpty(memberOld.getPhone())) {
				flag = true;
			} else if (CommonUtil
					.isNotEmpty(memberOption.getAddrdetailoption())
					&& memberOption.getAddrdetailoption() == 1
					&& CommonUtil.isNotEmpty(memberParameter1)
					&& CommonUtil.isEmpty(memberParameter1.getAddress())) {
				flag = true;
			} else if (CommonUtil.isNotEmpty(memberOption.getMailoption())
					&& memberOption.getMailoption() == 1
					&& CommonUtil.isEmpty(memberOld.getEmail())) {
				flag = true;
			} else if (CommonUtil.isNotEmpty(memberOption.getBirthoption())
					&& memberOption.getBirthoption() == 1
					&& CommonUtil.isEmpty(memberOld.getBirth())) {
				flag = true;
			} else if (CommonUtil.isNotEmpty(memberOption.getCardoption())
					&& memberOption.getCardoption() == 1
					&& CommonUtil.isEmpty(memberOld.getCardid())) {
				flag = true;
			} else if (CommonUtil.isNotEmpty(memberOption.getAddroption())
					&& memberOption.getAddroption() == 1
					&& CommonUtil.isNotEmpty(memberParameter1)
					&& CommonUtil.isEmpty(memberParameter1.getProvincecode())) {
				flag = true;
			} else if (CommonUtil.isNotEmpty(memberOption.getGetmoneyoption())
					&& memberOption.getGetmoneyoption() == 1
					&& CommonUtil.isNotEmpty(memberParameter1)
					&& CommonUtil.isEmpty(memberParameter1.getGetmoney())) {
				flag = true;
			}
			if (flag) {
				// 赠送礼品
				Card card = cardMapper.selectByPrimaryKey(memberOld.getMcId());
				MemberGift memberGift = null;
				Integer modelCode = null;
				if (card.getApplytype() == 4) {
					// 泛会员完善资料
					modelCode = 1;
				} else {
					modelCode = 2;
				}
				memberGift = memberGiftMapper.findBybusIdAndmodelCode(
						memberOld.getBusid(), modelCode);

				Member member = new Member();
				if (CommonUtil.isNotEmpty(memberGift)) {
					member.setId(memberOld.getId());
					member.setPublicId(memberOld.getPublicId());
					if (memberGift.getJifen() > 0) {
						// 积分操作
						member.setIntegral(memberOld.getIntegral()
								+ memberGift.getJifen().intValue());
						memberPayService.saveCardRecordNew(memberOld.getMcId(),
								(byte) 2, memberGift.getJifen() + "积分",
								"完善资料赠送积分", 0, null, 0, memberGift.getJifen());
					}
					boolean isCutFenbi = false; // 标示粉币可以扣除
					if (memberGift.getFenbi() > 0) {
						BusUser busUser = busUserMapper
								.selectByPrimaryKey(member.getBusid());
						if (busUser.getFansCurrency() > memberGift.getFenbi()) {
							memberPayService.saveCardRecordNew(
									memberOld.getMcId(), (byte) 3,
									memberGift.getFenbi() + "粉币", "完善资料赠送粉币",
									0, null, 0, memberGift.getFenbi());

							// 粉币操作
							member.setFansCurrency(memberOld.getFansCurrency()
									+ memberGift.getFenbi());
							isCutFenbi = true;
						}
					}

					if (memberGift.getFlow() > 0) {
						memberPayService.saveCardRecordNew(memberOld.getMcId(),
								(byte) 4, memberGift.getFlow() + "MB",
								"完善资料赠送流量", 0, null, 0, memberGift.getFlow());
					}

					memberMapper.updateByPrimaryKeySelective(member);

					if (isCutFenbi) {
						// 扣除商家粉币
//						busUserMapper.updateCutFenbi(member.getBusid(),
//								memberGift.getFenbi().intValue());
					}
				}
			}
		} catch (Exception e) {
			LOG.error("会员和泛会员完善资料赠送异常", e);
			return false;
		}
		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> updateMember(Integer memberId, String phone,
			String json) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Member member = memberMapper.selectByPrimaryKey(memberId);

			Card card = cardMapper.selectByPrimaryKey(member.getMcId());
			if (card.getApplytype() == 4 || "4".equals(card.getApplytype())) {
				mergeMemberCard(member.getBusid(), phone, memberId);
			}

			Map<String, Object> mapList = JsonUtil.json2Map(json);
			Member member1 = new Member();
			member1.setPhone(phone);
			member1.setId(memberId);
			if (CommonUtil.isNotEmpty(mapList.get("name"))) {
				member1.setName(mapList.get("name").toString());
			}

			if (CommonUtil.isNotEmpty(mapList.get("sex"))) {
				member1.setSex(CommonUtil.toInteger(mapList.get("sex")));
			}
			if (CommonUtil.isNotEmpty(mapList.get("birth"))) {
				member1.setBirth(DateTimeKit.parseDate(mapList.get("birth")
						.toString()));
			}
			if (CommonUtil.isNotEmpty(mapList.get("email"))) {
				member1.setEmail(mapList.get("email").toString());
			}
			if (CommonUtil.isNotEmpty(mapList.get("cardId"))) {
				member1.setCardid(mapList.get("cardId").toString());
			}
			memberMapper.updateByPrimaryKeySelective(member1);
			MemberParameter mp = memberParameterMapper.findByMemberId(member
					.getId());
			boolean flag = false;
			if (CommonUtil.isNotEmpty(mapList.get("provice"))) {
				mp.setProvincecode(mapList.get("provice").toString());
				flag = true;
			}
			if (CommonUtil.isNotEmpty(mapList.get("city"))) {
				mp.setCitycode(mapList.get("city").toString());
				flag = true;
			}
			if (CommonUtil.isNotEmpty(mapList.get("countyCode"))) {
				mp.setCountycode(mapList.get("countyCode").toString());
				flag = true;
			}
			if (CommonUtil.isNotEmpty(mapList.get("address"))) {
				mp.setAddress(mapList.get("address").toString());
				flag = true;
			}
			if (CommonUtil.isNotEmpty(mapList.get("getMoney"))) {
				mp.setGetmoney(CommonUtil.toDouble(mapList.get("address")));
				flag = true;
			}
			if (flag) {
				if (CommonUtil.isEmpty(mp.getId())) {
					mp.setMemberid(member.getId());
					memberParameterMapper.insertSelective(mp);
				} else {
					mp.setId(mp.getId());
					memberParameterMapper.updateByPrimaryKeySelective(mp);
				}
			}
			// 判断是否是否是完善资料 还是修改资料
			giveMemberGift(member, mp);
			map.put("result", true);
			map.put("message", "完善资料成功");
		} catch (Exception e) {
			throw new Exception();
		}
		return map;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> alipayBuyCard(Integer memberId, Integer ctId,
			Integer gtId) throws Exception {
		if (CommonUtil.isEmpty(memberId) || CommonUtil.isEmpty(ctId)) {
			throw new Exception();
		}
		Member member = memberMapper.selectByPrimaryKey(memberId);

		GradeType gradeTypes = gradeTypeMapper.selectByPrimaryKey(gtId);
		if (CommonUtil.isEmpty(gradeTypes)
				|| CommonUtil.isEmpty(gradeTypes.getBuymoney() <= 0)) {
			throw new Exception();
		}

		try {
			// 添加会员记录
			UserConsume uc = new UserConsume();
			uc.setPublicId(member.getPublicId());
			uc.setMemberid(memberId);
			uc.setCtid(ctId);
			uc.setRecordtype((byte) 2);
			uc.setUctype((byte) 13);
			uc.setTotalmoney(gradeTypes.getBuymoney());
			uc.setCreatedate(new Date());
			uc.setPaystatus((byte) 0);
			uc.setDiscount(100);
			uc.setDiscountmoney(gradeTypes.getBuymoney());
			uc.setPaymenttype((byte) 0);
			String orderCode = CommonUtil.getMEOrderCode();
			uc.setOrdercode(orderCode);
			uc.setGtId(gtId);
			uc.setBususerid(member.getBusid());
			userConsumeMapper.insertSelective(uc);
			// 统一下单调用
			Map<String, Object> params = new HashMap<String, Object>();
			String red_url = "/alipay/79B4DE7C/alipayApi.do?out_trade_no="
					+ orderCode
					+ "&subject=购买会员卡&model=14&businessUtilName=alipayNotifyUrlBuinessServiceBuyCard&total_fee="
					+ gradeTypes.getBuymoney() + "&busId=" + member.getBusid()
					+ "&return_url=" + PropertiesUtil.getWebHomeUrl()
					+ "/phoneMemberController/" + member.getBusid()
					+ "/79B4DE7C/findMember_1.do";
			params.put("result", true);
			params.put("message", "未支付");
			params.put("red_url", red_url);
			return params;
		} catch (Exception e) {
			LOG.error("购买会员卡下单异常", e);
			throw new Exception();
		}
	}

	@Override
	public void aliPayPayBack(String orderCode) {
		LOG.error("支付包支付回调订单单号 ：" + orderCode);
		List<Map<String, Object>> ucs = userConsumeMapper
				.findByOrderCode(orderCode);
		if (CommonUtil.isEmpty(ucs) || ucs.size() == 0 || ucs.size() > 1) {
			LOG.error("支付回调查询订单出现异常");
			return;
		}
		Integer id = Integer.parseInt(ucs.get(0).get("id").toString());
		// 修改订单
		UserConsume uc = new UserConsume();
		uc.setId(id);
		uc.setPaymenttype((byte)0);
		uc.setPaystatus((byte) 1);
		userConsumeMapper.updateByPrimaryKeySelective(uc);

		CardBuy cardbuy = new CardBuy();
		cardbuy.setBuymoney(CommonUtil
				.toDouble(ucs.get(0).get("discountMoney")));
		cardbuy.setCtid(CommonUtil.toInteger(ucs.get(0).get("ctId")));
		cardbuy.setMemberid(CommonUtil.toInteger(ucs.get(0).get("memberId")));
		cardbuy.setBusid(CommonUtil.toInteger(ucs.get(0).get("busUserId")));
		cardBuyMapper.insertSelective(cardbuy);

		// 添加会员卡
		Card card = new Card();
		card.setIschecked((byte) 1);
		card.setCardno(CommonUtil.getCode());
		card.setCtId(CommonUtil.toInteger(ucs.get(0).get("ctId")));

		card.setSystemcode(CommonUtil.getNominateCode());
		card.setApplytype((byte) 3);
		card.setMemberid(CommonUtil.toInteger(ucs.get(0).get("memberId")));
		card.setGtId(CommonUtil.toInteger(ucs.get(0).get("gt_id")));
		GiveRule giveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId(
				CommonUtil.toInteger(ucs.get(0).get("busUserId")),
				card.getGtId(), card.getCtId());
		card.setGrId(giveRule.getGrId());

		card.setCardno(CommonUtil.getCode());
		card.setBusid(CommonUtil.toInteger(ucs.get(0).get("busUserId")));
		card.setReceivedate(new Date());
		card.setIsbinding((byte) 1);

		GradeType gradeType = gradeTypeMapper
				.selectByPrimaryKey(card.getGtId());
		if (card.getCtId() == 5) {
			if (CommonUtil.isNotEmpty(gradeType.getBalance())) {
				card.setFrequency(new Double(gradeType.getBalance()).intValue());
			} else {
				card.setFrequency(0);
			}
		} else {
			if (CommonUtil.isNotEmpty(gradeType.getBalance())) {
				card.setMoney(new Double(gradeType.getBalance()));
			} else {
				card.setMoney(0.0);
			}
		}

		cardMapper.insertSelective(card);

		Member member = new Member();
		member.setId(CommonUtil.toInteger(ucs.get(0).get("memberId")));
		member.setIsbuy((byte) 1);
		member.setMcId(card.getMcId());
		memberMapper.updateByPrimaryKeySelective(member);
		String balance = null;
		if (card.getCtId() == 5) {
			balance = card.getFrequency() + "次";
		} else {
			balance = card.getMoney() + "元";
		}
		memberPayService.saveCardRecordNew(card.getMcId(), (byte) 1, ucs.get(0)
				.get("discountMoney") + "元", "购买会员卡", card.getPublicId(),
				balance, card.getCtId(), 0.0);

		// 新增会员短信通知
		member = memberMapper.selectByPrimaryKey(CommonUtil.toInteger(ucs
				.get(0).get("memberId")));
		systemMsgService.sendNewMemberMsg(member);
		
		socke.sendMessage2("member_" + member.getId(), "");
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> mergeMemberCard(Integer busId, String phone,
			Integer memberId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询老数据
			Member member = memberMapper.findByPhone(busId, phone);

			// 本数据
			Member m1 = memberMapper.selectByPrimaryKey(memberId);
			if (CommonUtil.isEmpty(member)) {
				map.put("isCard", 0);
				map.put("message", "老会员不存在，请做新增操作");
				return map;
			}

			if (!member.getId().equals(m1.getId())) {
				// 合并member数据
				m1.setFlow(m1.getFlow() + member.getFlow());
				m1.setIntegral(m1.getIntegral() + member.getIntegral());
				m1.setFansCurrency(m1.getFansCurrency()
						+ member.getFansCurrency());
				if (CommonUtil.isNotEmpty(member.getPwd())) {
					m1.setPwd(member.getPwd());
				}
				
				if (CommonUtil.isNotEmpty(m1.getOldid())) {
					m1.setOldid(m1.getOldid() + "," + member.getId());
				} else {
					m1.setOldid(m1.getId() + "," + member.getId());
				}

				if (CommonUtil.isNotEmpty(member.getPublicId())
						&& member.getPublicId() > 0) {
					m1.setPublicId(member.getPublicId());
				}
				if (CommonUtil.isNotEmpty(member.getOpenid())
						&& CommonUtil.isEmpty(m1.getOpenid())) {
					m1.setOpenid(member.getOpenid());
				}

				m1.setPhone(phone);
				m1.setMcId(member.getMcId());
				m1.setNickname(member.getNickname());
				m1.setHeadimgurl(member.getHeadimgurl());
				m1.setTotalmoney(member.getTotalmoney() + m1.getTotalmoney());
				m1.setTotalintegral(member.getTotalintegral()
						+ m1.getTotalintegral());
				m1.setRemark(member.getRemark());
				m1.setPwd(member.getPwd());
				m1.setLoginmode((byte) 0);

				// 删除数据做移出到memberold
				MemberOld old = (MemberOld) JSONObject.toBean(
						JSONObject.fromObject(member), MemberOld.class);
				memberOldMapper.insertSelective(old);

				memberMapper.deleteByPrimaryKey(member.getId());

				memberMapper.updateByPrimaryKeySelective(m1);

				MemberParameter mp = memberParameterMapper
						.findByMemberId(member.getId());
				if (CommonUtil.isNotEmpty(mp)) {
					memberParameterMapper.deleteByPrimaryKey(mp.getId());
				}
				
				// 修改小程序之前openId对应的memberId
				memberAppletOpenidMapper.updateMemberId(m1.getId(),
						member.getId());
			}

			if (CommonUtil.isNotEmpty(member.getMcId())) {
				Card card = cardMapper.selectByPrimaryKey(member.getMcId());
				// 如果已存在会员卡
				Card c = new Card();
				c.setMcId(member.getMcId());
				c.setIsbinding((byte) 1);
				c.setSource((byte) 1);
				if (card.getApplytype() == 4) {
					c.setApplytype((byte) 0);
				}
				cardMapper.updateByPrimaryKeySelective(c);
				map.put("isCard", 1);
				map.put("message", "手机号码已经注册,已持有会员卡不需要领取。");

			} else {
				map.put("isCard", 0);
				map.put("message", "老会员不存在，请做新增操作。");
			}
		} catch (Exception e) {
			LOG.error("合并会员信息异常", e);
			throw new Exception();
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> findnewMember(Integer busId) {
		Date date = DateTimeKit.parseDate(DateTimeKit.getDate());
		List<Map<String, Object>> list = memberMapper.findNewMemberByBusId(
				busId, date);
		List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			if (map.containsKey("nickname")) {
				try {
					byte[] bytes = (byte[]) map.get("nickname");
					map.put("nickname", new String(bytes, "UTF-8"));
				} catch (Exception e) {
					map.put("nickname", null);
				}
				memberList.add(map);
			} else {
				memberList.add(map);
			}

		}
		return list;
	}

	*//** pc会员信息会员完善资料 *//*
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> addMemberUser(Map<String, Object> param)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Member memberOld = memberMapper.selectByPrimaryKey(Integer
					.parseInt(param.get("id").toString()));

			Member member = new Member();

			if (CommonUtil.isNotEmpty(param.get("tel"))) {
				member.setPhone(param.get("tel").toString());
			}

			if (CommonUtil.isNotEmpty(param.get("pwd"))) {
				member.setPwd(SHA1.encode(param.get("pwd").toString()));
			}

			// 修改手机号
			Object vcode = param.get("vcode");
			if (CommonUtil.isEmpty(vcode)) {
				map.put("result", false);
				map.put("message", "请输入验证码");
				return map;
			}
			String vcode1 = JedisUtil.get(vcode.toString());
			if (CommonUtil.isEmpty(vcode1)) {
				map.put("result", false);
				map.put("message", "验证码超时或错误");
				return map;
			}
			JedisUtil.del(vcode1);

			Map<String, Object> mergeMap = mergeMemberCard(
					memberOld.getBusid(), param.get("tel").toString(),
					Integer.parseInt(param.get("id").toString()));
			if ("1".equals(CommonUtil.toString(mergeMap.get("isCard")))) {
				map.put("result", false);
				map.put("message", "手机号码已经注册,已持有会员卡不需要领取。");
				return map;
			}
			Integer memberId = Integer.parseInt(param.get("id").toString());
			int count = cardMapper.countCardisBinding(memberOld.getBusid());
			BusUser busUser = busUserMapper.selectByPrimaryKey(memberOld
					.getBusid());
			int level = busUser.getLevel();

			String dictNum = dictService.dictBusUserNum(busUser.getId(), level,
					4, "1093"); // 多粉 翼粉

			if (CommonUtil.toInteger(dictNum) < count) {
				map.put("result", false);
				map.put("message", "会员卡已领取完!");
				return map;
			}

			MemberParameter memberParameter = new MemberParameter();
			if (CommonUtil.isNotEmpty(param.get("provincecode"))) {
				memberParameter.setProvincecode(param.get("provincecode")
						.toString());
			}
			if (CommonUtil.isNotEmpty(param.get("city"))) {
				memberParameter.setCitycode(param.get("city").toString());
			}
			if (CommonUtil.isNotEmpty(param.get("countyCode"))) {
				memberParameter.setCountycode(param.get("countyCode")
						.toString());
			}

			if (CommonUtil.isNotEmpty(param.get("address"))) {
				memberParameter.setAddress(param.get("address").toString());
			}

			if (CommonUtil.isNotEmpty(param.get("getmoney"))) {
				memberParameter.setGetmoney(CommonUtil.toDouble(param
						.get("getmoney")));
			}

			MemberParameter memberParameter1 = memberParameterMapper
					.findByMemberId(Integer
							.parseInt(param.get("id").toString()));

			if (CommonUtil.isEmpty(memberParameter1)) {
				memberParameter.setMemberid(Integer.parseInt(param.get("id")
						.toString()));
				memberParameterMapper.insertSelective(memberParameter);
			} else {
				memberParameter.setMemberid(Integer.parseInt(param.get("id")
						.toString()));
				memberParameter.setId(memberParameter1.getId());
				memberParameterMapper
						.updateByPrimaryKeySelective(memberParameter);
			}
			if (CommonUtil.isNotEmpty(param.get("mcId"))) {
				member.setMcId(Integer.parseInt(param.get("mcId").toString()));
			}
			if (CommonUtil.isNotEmpty(param.get("email"))) {
				member.setEmail(param.get("email").toString());
			} else {
				member.setEmail("");
			}
			if (CommonUtil.isNotEmpty(param.get("name"))) {
				member.setName(param.get("name").toString());
			}

			if (CommonUtil.isNotEmpty(param.get("gender"))) {
				member.setSex(Integer.parseInt(param.get("gender").toString()));
			}
			if (CommonUtil.isNotEmpty(param.get("id"))) {
				member.setId(Integer.parseInt(param.get("id").toString()));
			}
			if (CommonUtil.isNotEmpty(param.get("birth"))) {
				member.setBirth(DateTimeKit.parseDate(param.get("birth")
						.toString()));
			}
			if (CommonUtil.isNotEmpty(param.get("cardId"))) {
				member.setCardid(param.get("cardId").toString());
			} else {
				member.setCardid("");
			}

			member.setId(memberOld.getId());

			// 购买
			String buy = CommonUtil.toString(param.get("buy"));
			Integer ctId = CommonUtil.toInteger(param.get("cardType"));
			if ("0".equals(buy)) {
				// 分配会员卡
				if (CommonUtil.isEmpty(memberOld.getMcId())) {
					Card card = new Card();
					card.setIschecked((byte) 1);
					card.setCardno(CommonUtil.getCode());
					card.setCtId(ctId);
					card.setNominatecode(CommonUtil.getPhoneCode());
					if (card.getCtId() == 4) {
						card.setExpiredate(new Date());
					}
					card.setSystemcode(CommonUtil.getNominateCode());

					card.setMemberid(memberOld.getId());

					if (CommonUtil.isNotEmpty(param.get("gradeTypeId"))) {
						GradeType gt = gradeTypeMapper
								.selectByPrimaryKey(CommonUtil.toInteger(param
										.get("gradeTypeId")));
						card.setApplytype(Byte.valueOf(gt.getApplytype()));
						card.setGtId(CommonUtil.toInteger(param
								.get("gradeTypeId")));

						GiveRule giveRule = giveRuleMapper
								.findBybusIdAndGtIdAndCtId(
										memberOld.getBusid(), CommonUtil
												.toInteger(param
														.get("gradeTypeId")),
										ctId);
						card.setGrId(giveRule.getGrId());
					} else {
						// 根据卡片类型 查询第一等级
						List<Map<String, Object>> gradeTypes = gradeTypeMapper
								.findBybusIdAndCtId3(memberOld.getBusid(), ctId);
						if (gradeTypes != null && gradeTypes.size() > 0) {
							card.setApplytype(Byte.valueOf(gradeTypes.get(0)
									.get("applyType").toString()));

							card.setGtId(Integer.parseInt(gradeTypes.get(0)
									.get("gt_id").toString()));

							GiveRule giveRule = giveRuleMapper
									.findBybusIdAndGtIdAndCtId(
											memberOld.getBusid(),
											Integer.parseInt(gradeTypes.get(0)
													.get("gt_id").toString()),
											ctId);
							card.setGrId(giveRule.getGrId());
						}
					}
					card.setBusid(memberOld.getBusid());
					card.setCardno(CommonUtil.getCode());
					card.setPublicId(memberOld.getPublicId());
					card.setReceivedate(new Date());
					card.setIsbinding((byte) 1);

					BusUser busUser1 = (BusUser) param.get("busUser");
					List<Map<String, Object>> wxshops = dictService
							.shopList(busUser1);
					if (CommonUtil.isNotEmpty(wxshops) && wxshops.size() > 0) {
						card.setShopid(CommonUtil.toInteger(wxshops.get(0).get(
								"id")));
					}
					card.setOnline((byte) 1);

					cardMapper.insertSelective(card);
					member.setMcId(card.getMcId());
					map.put("result", true);
				}

			}

			memberMapper.updateByPrimaryKeySelective(member);

			// 钱包支付
			if ("1".equals(buy)) {
				Integer gradeTypeId = CommonUtil.toInteger(param
						.get("gradeTypeId"));
				GradeType gt = gradeTypeMapper.selectByPrimaryKey(gradeTypeId);
				// 添加会员记录
				UserConsume uc = new UserConsume();
				uc.setPublicId(member.getPublicId());
				uc.setMemberid(memberId);
				uc.setCtid(ctId);
				uc.setRecordtype((byte) 2);
				uc.setUctype((byte) 13);
				uc.setTotalmoney(gt.getBuymoney());
				uc.setCreatedate(new Date());
				uc.setPaystatus((byte) 0);
				uc.setDiscount(100);
				uc.setDiscountmoney(gt.getBuymoney());
				uc.setPaymenttype((byte) 1);
				String orderCode = CommonUtil.getMEOrderCode();
				uc.setOrdercode(orderCode);
				uc.setGtId(gt.getGtId());
				userConsumeMapper.insertSelective(uc);
				// 统一下单调用
				String url = "/cashier/pay_page.do?model=7&out_trade_no="
						+ orderCode + "&total_fee=" + gt.getBuymoney()
						+ "&businessUtilName=BuyCardbusinessService";
				map.put("url", url);
			}
			map.put("result", true);
			map.put("message", "新增成功");

			// 新增会员短信通知
			member.setBusid(memberOld.getBusid());
			systemMsgService.sendNewMemberMsg(member);

		} catch (Exception e) {
			LOG.error("修改信息异常", e);
			map.put("result", false);
			map.put("message", "保存失败");
			throw new Exception();
		}
		return map;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> fansToMemberCard(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 修改手机号
			Object vcode = param.get("vcode");
			if (CommonUtil.isEmpty(vcode)) {
				map.put("result", false);
				map.put("message", "请输入验证码");
				return map;
			}
			String vcode1 = JedisUtil.get(vcode.toString());
			if (CommonUtil.isEmpty(vcode1)) {
				map.put("result", false);
				map.put("message", "验证码超时或错误");
				return map;
			}

			if (CommonUtil.isEmpty(param.get("busId"))) {
				map.put("result", false);
				map.put("message", "请求参数异常");
				return map;
			}

			JedisUtil.del(vcode1);
			Map<String, Object> objectMap = mergeMemberCard(
					param.get("busId") == null ? 0 : Integer.parseInt(param
							.get("busId").toString()),
					param.get("phone") == null ? "" : param.get("phone")
							.toString(),
					param.get("id") == null ? 0 : Integer.parseInt(param.get(
							"id").toString()));
			if (objectMap != null
					&& Integer.parseInt(objectMap.get("isCard").toString()) == 1) {
				map.put("result", false);
				map.put("message", objectMap.get("message"));
				return map;
			}

			Member memberOld = memberMapper.selectByPrimaryKey(Integer
					.parseInt(param.get("id").toString()));
			int count = cardMapper.countCardisBinding(memberOld.getBusid());
			BusUser busUser = busUserMapper.selectByPrimaryKey(memberOld
					.getBusid());
			int level = busUser.getLevel();

			String dictNum = dictService.dictBusUserNum(busUser.getId(), level,
					4, "1093"); // 多粉 翼粉

			if (CommonUtil.toInteger(dictNum) < count) {
				map.put("result", false);
				map.put("message", "会员卡已领取完!");
				return map;
			}
			Card card = new Card();
			card.setBusid(Integer.parseInt(param.get("busId").toString()));
			card.setCardno(CommonUtil.getCode());
			map.put("cardNo", card.getCardno());
			card.setCtId(Integer.parseInt(param.get("cardType").toString()));
			card.setReceivedate(new Date());
			card.setIsbinding((byte) 1);
			card.setIschecked((byte) 1);
			if (CommonUtil.isNotEmpty(param.get("shopId"))) {
				card.setShopid(CommonUtil.toInteger(param.get("shopId")));
			} else {
				Integer danBusid = CommonUtil.toInteger(param.get("busId"));
				List<Map<String, Object>> wxShop = dictService
						.shopList(busUserMapper.selectByPrimaryKey(danBusid));
				if (CommonUtil.isNotEmpty(wxShop) && wxShop.size() > 0) {
					card.setShopid(CommonUtil
							.toInteger(wxShop.get(0).get("id")));
				}
			}
			card.setOnline((byte) 1);
			if (card.getCtId() == 4) {
				card.setExpiredate(new Date());
			}
			// 根据卡片类型 查询第一等级
			List<Map<String, Object>> gradeTypes = gradeTypeMapper
					.findBybusIdAndCtId3(
							Integer.parseInt(param.get("busId").toString()),
							Integer.parseInt(param.get("cardType").toString()));
			if (gradeTypes != null && gradeTypes.size() > 0) {
				card.setApplytype(Byte.valueOf(gradeTypes.get(0)
						.get("applyType").toString()));

				card.setGtId(Integer.parseInt(gradeTypes.get(0).get("gt_id")
						.toString()));

				GiveRule giveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId(
						Integer.parseInt(param.get("busId").toString()),
						Integer.parseInt(gradeTypes.get(0).get("gt_id")
								.toString()),
						Integer.parseInt(param.get("cardType").toString()));
				card.setGrId(giveRule.getGrId());
			}
			int mcId = cardMapper.insertSelective(card);
			if (mcId > 0) {
				param.put("mcId", card.getMcId());
			} else {
				throw new Exception();
			}

			Member member = new Member();
			if (CommonUtil.isNotEmpty(param.get("pwd"))) {
				member.setPwd(SHA1.encode(param.get("pwd").toString()));
			}

			if (CommonUtil.isNotEmpty(param.get("phone"))) {
				member.setPhone(param.get("phone").toString());
			}
			MemberParameter memberParameter = new MemberParameter();
			if (CommonUtil.isNotEmpty(param.get("provincecode"))) {
				memberParameter.setProvincecode(param.get("provincecode")
						.toString());
			}
			if (CommonUtil.isNotEmpty(param.get("city"))) {
				memberParameter.setCitycode(param.get("city").toString());
			}
			if (CommonUtil.isNotEmpty(param.get("countyCode"))) {
				memberParameter.setCountycode(param.get("countyCode")
						.toString());
			}

			if (CommonUtil.isNotEmpty(param.get("address"))) {
				memberParameter.setAddress(param.get("address").toString());
			}

			if (CommonUtil.isNotEmpty(param.get("getmoney"))) {
				memberParameter.setGetmoney(CommonUtil.toDouble(param
						.get("getmoney")));
			}

			MemberParameter memberParameter1 = memberParameterMapper
					.findByMemberId(Integer
							.parseInt(param.get("id").toString()));

			if (CommonUtil.isEmpty(memberParameter1)) {
				memberParameter.setMemberid(Integer.parseInt(param.get("id")
						.toString()));
				memberParameterMapper.insertSelective(memberParameter);
			} else {
				memberParameter.setMemberid(Integer.parseInt(param.get("id")
						.toString()));
				memberParameter.setId(memberParameter1.getId());
				memberParameterMapper
						.updateByPrimaryKeySelective(memberParameter);
			}
			if (CommonUtil.isNotEmpty(param.get("mcId"))) {
				member.setMcId(Integer.parseInt(param.get("mcId").toString()));
			}
			if (CommonUtil.isNotEmpty(param.get("email"))) {
				member.setEmail(param.get("email").toString());
			} else {
				member.setEmail("");
			}
			if (CommonUtil.isNotEmpty(param.get("name"))) {
				member.setName(param.get("name").toString());
			}

			if (CommonUtil.isNotEmpty(param.get("gender"))) {
				member.setSex(Integer.parseInt(param.get("gender").toString()));
			}
			if (CommonUtil.isNotEmpty(param.get("id"))) {
				member.setId(Integer.parseInt(param.get("id").toString()));
			}
			if (CommonUtil.isNotEmpty(param.get("birth"))) {
				member.setBirth(DateTimeKit.parseDate(param.get("birth")
						.toString()));
			}
			if (CommonUtil.isNotEmpty(param.get("cardId"))) {
				member.setCardid(param.get("cardId").toString());
			} else {
				member.setCardid("");
			}

			member.setId(memberOld.getId());
			memberMapper.updateByPrimaryKeySelective(member);
			map.put("result", true);
			map.put("message", "新增成功");
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "保存失败");
			e.printStackTrace();
		}
		return map;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> exchangeFlow(HttpServletRequest request,
			Integer id, String phone, Integer flowCount) throws Exception {
		Map<String, Object> result = null;
		try {
			Member member = memberMapper.selectByPrimaryKey(id);
			Card card = cardMapper.selectByPrimaryKey(member.getMcId());
			BusUser busUser = busUserMapper.selectByPrimaryKey(member
					.getBusid());
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("busId", busUser.getId());
			params.put("prizeCount", flowCount);
			params.put("mobile", phone);
			params.put("memberId", member.getId());
			list.add(params);

			result = MobileLocationUtil.adcServices(list, 3, "");

			if ("1".equals(result.get("code").toString())) {
				Integer flowType = 0;
				switch (card.getCtId()) {
				case 1:
					flowType = 25;
					break;
				case 2:
					flowType = 26;
					break;
				case 3:
					flowType = 27;
					break;
				case 4:
					flowType = 28;
					break;
				case 5:
					flowType = 29;
					break;

				default:
					break;
				}

				// 修改流量包数量
				fenbiFlowRecordMapper.updateFlowReduce(busUser.getId(), 1,
						flowType, flowCount, card.getCtId());

				Member m = new Member();
				m.setId(id);
				m.setFlow(member.getFlow() - flowCount);
				memberMapper.updateByPrimaryKeySelective(m);

				memberPayService.saveCardRecordNew(card.getMcId(), (byte) 4,
						-flowCount + "MB", "流量包兑换", 0, null, 0, -flowCount);

				member.setFlow(member.getFlow() - flowCount);
				CommonUtil.setLoginMember(request, member);

				// 添加会员交易
				UserConsume uc = new UserConsume();
				uc.setBususerid(member.getBusid());
				uc.setMemberid(member.getId());
				uc.setMcid(member.getMcId());
				uc.setUctype((byte) 20);
				uc.setGiveflow(-flowCount);
				uc.setOrdercode(CommonUtil.toString(result.get("orderNo")));
				uc.setFlowstate((byte) 1);
				uc.setIsend((byte) 1);
				uc.setIsenddate(new Date());
				userConsumeMapper.insertSelective(uc);

			} else if ("-1".equals(result.get("code").toString())) {
				result.put("code", -1);
				result.put("msg", "系统升级中...请稍后再兑");
			}
		} catch (Exception e) {
			LOG.error("兑换流量异常", e);
			throw new Exception();
		}
		return result;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> qiandao(HttpServletRequest request, Integer id,
			Integer jifen) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Member member = CommonUtil.getLoginMember(request);
			Member m = new Member();
			m.setId(id);
			m.setIntegral(member.getIntegral() + jifen);
			memberMapper.updateByPrimaryKeySelective(m);

			member.setIntegral(member.getIntegral() + jifen);
			MemberParameter memberParameter = memberParameterMapper
					.findByMemberId(id);

			if (CommonUtil.isNotEmpty(memberParameter.getSigndate())
					&& DateTimeKit.isSameDay(memberParameter.getSigndate(),
							new Date())) {
				map.put("result", false);
			} else {
				if (CommonUtil.isEmpty(memberParameter)) {
					memberParameter = new MemberParameter();
					memberParameter.setMemberid(id);
					memberParameter.setSigndate(new Date());
					memberParameterMapper.insertSelective(memberParameter);
				} else {
					MemberParameter mp = new MemberParameter();
					mp.setId(memberParameter.getId());
					mp.setSigndate(new Date());
					memberParameterMapper.updateByPrimaryKeySelective(mp);
				}
				CommonUtil.setLoginMember(request, member);
				memberPayService.saveCardRecordNew(member.getMcId(), (byte) 2,
						jifen + "积分", "签到送积分", 0, null, 0, jifen);
				map.put("result", true);
			}

		} catch (Exception e) {
			map.put("result", false);
			LOG.error("签到失败", e);
			throw new Exception();
		}
		return map;
	}

	@Override
	public Map<String, Object> uploadMemberHeadImg(HttpServletRequest request,
			Map<String, Object> parma) {
		Integer id = Integer.parseInt(parma.get("id").toString());
		Map<String, Object> map = new HashMap<String, Object>();
		Member member = memberMapper.selectByPrimaryKey(id);
		BusUser busUser = busUserMapper.selectByPrimaryKey(member.getBusid());
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> userfile = multipartRequest.getFiles("file");
			if (CommonUtil.isNotEmpty(userfile)) {
				try {
					map = CommonUtil.fileUploadByBusUser(userfile.get(0),
							busUser);
					MemberParameter mp = memberParameterMapper
							.findByMemberId(id);
					if (CommonUtil.isNotEmpty(mp)) {
						mp.setHeadimg(map.get("message") + "");
						memberParameterMapper.updateByPrimaryKeySelective(mp);
					}

					map.put("headimg", mp.getHeadimg());
				} catch (Exception e) {
					map.put("message", "上传失败");
					map.put("result", false);
				}
			}
		}

		return map;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void clearJifen(List<Integer> busIds) throws Exception {
		try {
			Date date = DateTimeKit.addMonths(new Date(), -12);
			Date startdate = DateTimeKit.addMonths(new Date(), -24);
			List<Map<String, Object>> upperYear = cardRecordMapper.sumByBusId(
					busIds, startdate, date); // 前一年数据统计
			List<Map<String, Object>> currentYear = cardRecordMapper
					.sumCurrentByBusId(busIds, date, new Date());
			for (Map<String, Object> map : upperYear) {
				Integer amounts = CommonUtil.toDouble(map.get("amounts"))
						.intValue();
				Integer busId = CommonUtil.toInteger(map.get("busId"));
				Integer cardId = CommonUtil.toInteger(map.get("cardId"));
				boolean flag = false;
				if (CommonUtil.isNotEmpty(amounts) && amounts > 0) {
					if (currentYear.size() == 0) {
						// 执行清楚计划
						memberPayService.saveCardRecordNew(cardId, (byte) 2,
								amounts + "积分", "清除积分", busId, "", 0, 0);
						// 修改粉丝的积分
						memberMapper.updateJifen(busId, cardId, amounts);
					}

					for (Map<String, Object> map2 : currentYear) {
						Integer cardId1 = CommonUtil.toInteger(map2
								.get("cardId"));
						Integer amounts1 = -CommonUtil.toDouble(
								map2.get("amounts")).intValue();
						if (cardId.equals(cardId1)) {
							flag = true;
							if (amounts > amounts1) {
								Integer amount = amounts - amounts1;
								// 执行清楚计划
								memberPayService.saveCardRecordNew(cardId,
										(byte) 2, amount + "积分", "清除积分", busId,
										"", 0, 0);
								// 修改粉丝的积分
								memberMapper.updateJifen(busId, cardId, amount);
								break;

							}
						}
					}

					if (!flag) {
						// 执行清楚计划
						memberPayService.saveCardRecordNew(cardId, (byte) 2,
								amounts + "积分", "清除积分", busId, "", 0, 0);
						// 修改粉丝的积分
						memberMapper.updateJifen(busId, cardId, amounts);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("webservice积分清零0异常", e);
			throw new Exception();
		}
	}

	*//**
	 * @param scene_id
	 * @param wxPublicUsers
	 * @return
	 *//*
	public String insertTwoCode(String scene_id, WxPublicUsers wxPublicUsers) {
		String component_appid = WxConstants.COMPONENT_APPID;
		ComponentAccessToken component_access_token = componentAPI
				.api_component_token();
		AuthorizerAccessToken token = componentAPI.api_authorizer_token(
				component_access_token.getComponent_access_token(),
				component_appid, wxPublicUsers.getAppid(),
				wxPublicUsers.getAuthRefreshToken());
		QrcodeTicket qrcodeTicket = QrcodeAPI.qrcodeCreateFinal(
				token.getAuthorizer_access_token(), scene_id);
		return qrcodeTicket.getTicket();
	}

	*//**
	 * 批量审批会员
	 * 
	 * @param memberId
	 * @param ischecked
	 * @param phone
	 * @param companyName
	 * @return
	 * @throws Exception
	 *//*

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> cardApplyCheckeds(String[] memberId,
			Byte ischecked, String[] phones, Map<String, Object> companyMap)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			StringBuffer memberIds = new StringBuffer();// 会员Id集合
			for (int a = 0; a < memberId.length; a++) {
				if (a == 0) {
					memberIds.append(memberId[a]);
				} else {
					memberIds.append("," + memberId[a]);
				}
			}
			List<Member> memberList = memberMapper
					.selectByPrimaryKeys(memberIds.toString());
			StringBuffer mcIds = new StringBuffer();
			StringBuffer phoneSb = new StringBuffer();

			for (int i = 0; i < memberList.size(); i++) {
				if (i == 0) {
					mcIds.append(memberList.get(i).getMcId());
					phoneSb.append(memberList.get(i).getPhone());
				} else {
					mcIds.append("," + memberList.get(i).getMcId());
					phoneSb.append("," + memberList.get(i).getPhone());
				}
			}

			List<Card> cardList = cardMapper.selectByPrimaryKeys(mcIds
					.toString());
			memberIds = new StringBuffer();
			for (int b = 0; b < cardList.size(); b++) {
				Card card = cardList.get(b);
				if (card.getIschecked() != 1) {
					if (ischecked == 1) {
						card.setIschecked((byte) 1);
						cardMapper.updateByPrimaryKeySelective(card);
					} else {
						cardMapper.deleteByPrimaryKey(card.getMcId());
						if (CommonUtil.isNotEmpty(card.getMcId())) {
							memberMapper.updateMemberByMcId(card.getBusid(),
									card.getMcId());
						}

					}
				}
			}
			// 发送短信
			String cont = "";
			if (ischecked == 0) {
				cont = "尊敬的用户您好：您的" + companyMap.get("company")
						+ "会员卡,商家未审核通过,暂时无法享受会员权益.";
			} else {
				cont = "尊敬的用户您好：您的" + companyMap.get("company")
						+ "会员卡,商家已审核通过,各种会员权益等您来体验！";
			}
			companyMap.put("content", cont);
			companyMap.put("model", 3);
			companyMap.put("mobiles", phoneSb.toString());
			smsSpendingService.sendSms(companyMap);
			map.put("result", true);
			map.put("message", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("会员卡审核异常", e);
			throw new Exception();
		}
		return map;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> pickMoney(Integer memberId, Integer busId)
			throws Exception {
		Map<String, Object> map1 = new HashMap<String, Object>();
		try {
			WxPublicUsers wxPublicUsers = wxPublicUsersMapper
					.selectByUserId(busId);
			Member member = memberMapper.selectByPrimaryKey(memberId);
			Card card = cardMapper.selectByPrimaryKey(member.getMcId());

			GradeType gt = gradeTypeMapper.findGiveMoney(busId, card.getCtId());
			if (card.getGivemoney() < gt.getPickmoney()) {
				map1.put("result", false);
				map1.put("message", "提取金额不足，必须要大于" + gt.getPickmoney()
						+ "元才能提取");
				return map1;
			}

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("appid", wxPublicUsers.getAppid());
			params.put("desc", "会员推荐佣金发放");
			params.put("model", 14);
			params.put("busId", wxPublicUsers.getBusUserId());
			params.put("openid", member.getOpenid());
			params.put("amount", card.getGivemoney());
			params.put("partner_trade_no", "YJ" + new Date().getTime());
			Map<String, Object> returnMap = wxPayService
					.enterprisePayment(params);
			if ("1".equals(returnMap.get("code").toString())) {
				// 新增佣金提交记录
				MemberPickLog mpl = new MemberPickLog();
				mpl.setBusid(busId);
				mpl.setMemberid(memberId);
				mpl.setPickdate(new Date());
				mpl.setPickmoney(card.getGivemoney());
				memberPickLogMapper.insertSelective(mpl);
				Card c = new Card();
				c.setMcId(card.getMcId());
				c.setGivemoney(0.0);
				cardMapper.updateByPrimaryKeySelective(c);

				map1.put("result", true);
				map1.put("message", returnMap.get("msg"));
			} else {
				LOG.error("用户领取推荐佣金调用微信企业支付失败:" + returnMap.get("msg"));
				map1.put("result", false);
				map1.put("message", returnMap.get("msg"));
			}
		} catch (Exception e) {
			LOG.error("用户领取推荐佣金", e);
			throw new Exception();
		}
		return map1;
	}

	@Override
	public Page findPickLog(Integer busId, Map<String, Object> params) {
		params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
				: CommonUtil.toInteger(params.get("curPage")));
		try {
			int pageSize = 10;
			Object search1 = params.get("search");
			String search = null;
			if (CommonUtil.isNotEmpty(search1)) {
				search = search1.toString();
			}
			int rowCount = memberPickLogMapper.countPickLog(busId, search);
			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
					pageSize, rowCount, "member/findPickLog.do");
			params.put("firstResult", pageSize
					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
			params.put("maxResult", pageSize);
			List<Map<String, Object>> list = memberPickLogMapper.findPickLog(
					busId, search,
					CommonUtil.toInteger(params.get("firstResult")), pageSize);
			List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : list) {
				if (map.containsKey("nickname")) {
					try {
						byte[] bytes = (byte[]) map.get("nickname");
						map.put("nickname", new String(bytes, "UTF-8"));
					} catch (Exception e) {
						map.put("nickname", null);
					}
					memberList.add(map);
				} else {
					memberList.add(map);
				}

			}
			page.setSubList(memberList);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> findShiti(Integer memberId, Integer busId,
			String phone, String vcode) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (CommonUtil.isEmpty(vcode)) {
				map.put("result", false);
				map.put("message", "请输入验证码");
				return map;
			}
			String vcode1 = JedisUtil.get(vcode.toString());
			if (CommonUtil.isEmpty(vcode1)) {
				map.put("result", false);
				map.put("message", "验证码超时或错误");
				return map;
			}
			Member member = memberMapper.findLoginMode2ByPhone(busId, phone);
			if (CommonUtil.isNotEmpty(member)
					&& CommonUtil.isNotEmpty(member.getMcId())) {
				JedisUtil.del(vcode1); // 实体卡存在就删除验证码
				mergeMemberCard(busId, phone, memberId); // 数据合并
				map.put("result", 2);
				map.put("message", "粉丝已经绑定了实体卡");
			} else {
				if(CommonUtil.isNotEmpty(member)){
					mergeMemberCard(busId, phone, memberId); // 数据合并
				}
				map.put("result", 1);
				map.put("message", "领取会员卡");
			}
		} catch (Exception e) {
			LOG.error("领取会员卡绑定实体卡异常", e);
			throw new Exception();
		}
		return map;
	}

	@Override
	public void memberTongJi(HttpServletRequest request, Integer ctId,
			String startTime) {
		try {
			BusUser busUser = CommonUtil.getLoginUser(request);
			Integer busId = busUser.getId();
			if (busUser.getPid() != 0) {
				busId = dictService.pidUserId(busUser.getId());
			}
			List<Map<String, Object>> grades = gradeTypeMapper
					.findGradeTyeBybusId(busId);
			if (CommonUtil.isEmpty(grades) || grades.size() == 0) {
				return;
			}
			if (CommonUtil.isEmpty(ctId) || ctId == 0) {
				ctId = CommonUtil.toInteger(grades.get(0).get("ctId"));
			}
			request.setAttribute("ctId", ctId);
			request.setAttribute("grades", grades);

			// 会员总数
			Integer countMember = cardMapper.countCard1(busId, ctId);
			request.setAttribute("countMember", countMember);
			// 查询推荐总数
			Integer tuijian = recommendMapper.countTuiJian(busId, ctId);
			request.setAttribute("tuijian", tuijian);
			// 订单统计
			Integer countOrder = userConsumeMapper.countOrder(busId, ctId);
			request.setAttribute("countOrder", countOrder);
			// 性别分组统计
			List<Map<String, Object>> sexMap = memberMapper.findSexGroupBySex(
					busId, ctId);
			request.setAttribute("sexMap", JSONArray.fromObject(sexMap));
			// 等级分组统计
			List<Map<String, Object>> countCard = cardMapper.findGroupBygtId(
					busId, ctId);
			request.setAttribute("countCard", JSONArray.fromObject(countCard));
			// 查询消费总和 和售卡总和
			Double sumXiaofei = userConsumeMapper.sumXiaofei(busId, ctId);
			Double buyCard = userConsumeMapper.buyCard(busId, ctId);
			request.setAttribute("sumXiaofei", sumXiaofei);
			request.setAttribute("buyCard", buyCard);
			// 表格 7销售总额和售卡总额
			Date date = new Date();
			if (CommonUtil.isNotEmpty(startTime)) {
				date = DateTimeKit.parse(startTime, "yyyy/MM/dd");
				request.setAttribute("startTime", startTime);
			}
			List<Map<String, Object>> sumdayOrder = userConsumeMapper
					.sum7DayOrder(busId, ctId, date);

			List<Map<String, Object>> sumbuyCard = userConsumeMapper
					.sum7DayBuyCard(busId, ctId, date);
			List<Map<String, Object>> sumMemberOrder = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map1 : sumdayOrder) {
				boolean flag = false;
				for (Map<String, Object> map : sumbuyCard) {
					if (CommonUtil.toString(map.get("a1")).equals(
							CommonUtil.toString(map1.get("a3")))) {
						map1.put("a2", CommonUtil.toString(map.get("a2")));
						sumMemberOrder.add(map1);
						flag = true;
					}
				}
				if (!flag) {
					map1.put("a2", 0);
					sumMemberOrder.add(map1);
				}
			}
			request.setAttribute("sumMemberOrder", sumMemberOrder);
			switch (ctId) {
			case 1:
				// 积分卡
				// 查询积分兑换情况
				List<Map<String, Object>> jifenMap = userConsumeMapper
						.countJifenDuiHan(busId);
				Collections.reverse(jifenMap);
				request.setAttribute("jifenMap", JSONArray.fromObject(jifenMap));

				// 剩余积分统计
				Integer countJifen = memberMapper.countJifen(busId);
				request.setAttribute("countJifen", countJifen);

				// 消耗积分统计
				Integer countUserJifen = userConsumeMapper.countUseJifen(busId);
				request.setAttribute("countUserJifen", countUserJifen);
				break;
			case 2: // 折扣卡
				// 会员折扣总和
				List<Map<String, Object>> discountOrder = userConsumeMapper
						.sum7DayDiscount(busId);
				Collections.reverse(discountOrder);
				request.setAttribute("discountOrder",
						JSONArray.fromObject(discountOrder));

				// 折扣总额
				Double disCountMoney = userConsumeMapper.sumDisCount(busId);
				request.setAttribute("disCountMoney", disCountMoney);

				break;
			case 3:
				// 储值卡
				// 查询会员卡中剩余总额
				Double yueMoney = cardMapper.sumMoney(busId);
				request.setAttribute("yueMoney", yueMoney);

				// 会员累计充值总额
				Double chongzhiMoney = userConsumeMapper.sumChongzhi(busId);
				request.setAttribute("chongzhiMoney", chongzhiMoney);
				// 会员充值走势
				List<Map<String, Object>> chongzhiMap = userConsumeMapper
						.sumChongzhi7Day(busId);
				Collections.reverse(chongzhiMap);
				request.setAttribute("chongzhiMap",
						JSONArray.fromObject(chongzhiMap));
				break;
			case 4:
				// 时效卡

				break;
			case 5:
				// 次卡
				// 查询次卡7天消费次数
				List<Map<String, Object>> cikaMap = userConsumeMapper
						.sumCiKa(busId);
				Collections.reverse(cikaMap);
				request.setAttribute("cikaMap", JSONArray.fromObject(cikaMap));

				// 会员剩余次数
				Integer sumfrequency = cardMapper.sumfrequency(busId);
				request.setAttribute("sumfrequency", sumfrequency);
				// 会员消费次数
				Integer userCiKa = userConsumeMapper.userCiKa(busId);
				request.setAttribute("userCiKa", userCiKa);

				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Map<String, Object> saveRecommend(Map<String, Object> parma) {
		Integer id = Integer.parseInt(parma.get("id").toString());
		Map<String, Object> map = new HashMap<String, Object>();

		Integer count = recommendMapper.findRecommendByphone(
				CommonUtil.toString(parma.get("phone")), id);
		if (count > 0) {
			map.put("result", false);
			map.put("message", "您已经推荐过当前拥有的手机号码用户");
			return map;
		}
		Recommend r = new Recommend();
		r.setMemberid(id);
		r.setName(parma.get("name").toString());
		r.setPhone(parma.get("phone").toString());
		r.setIscheck(Byte.valueOf(parma.get("ischeck").toString()));
		r.setDatetime(new Date());
		if ("1".equals(parma.get("ischeck"))) {
			String code = CommonUtil.getNominateCode();
			r.setCode(code);
			map.put("code", code);
		}
		map.put("ischeck", parma.get("ischeck"));
		r.setCtid(CommonUtil.toInteger(parma.get("ctId")));
		r.setIsuser((byte) 0);
		r.setIntegral(CommonUtil.toInteger(parma.get("giveIntegral")));
		r.setFlow(CommonUtil.toInteger(parma.get("giveflow")));
		r.setFenbi(CommonUtil.toInteger(parma.get("givefenbi")));
		r.setMoney(CommonUtil.toDouble(parma.get("givemoney")));
		r.setRetype(Byte.parseByte("0"));
		try {
			recommendMapper.insertSelective(r);
			map.put("result", true);
			map.put("message", "推荐成功");
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "推荐失败");
		}
		return map;
	}

//	@Override
//	public Page findMemberIsNotCard(Integer busId, Map<String, Object> params) {
//		try {
//			params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1
//					: CommonUtil.toInteger(params.get("curPage")));
//			int pageSize = 12;
//
//			int rowCount = memberMapper.countMemberIsNotCard(busId);
//			Page page = new Page(CommonUtil.toInteger(params.get("curPage")),
//					pageSize, rowCount, "/memberERP/erpMember.do");
//
//			params.put("firstResult", pageSize
//					* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
//
//			List<Map<String, Object>> members = memberMapper
//					.findMemberIsNotCard(busId,
//							CommonUtil.toInteger(params.get("firstResult")),
//							pageSize);
//
//			List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
//			for (Map<String, Object> map : members) {
//				try {
//					byte[] bytes = (byte[]) map.get("nickname");
//					map.put("nickname", new String(bytes, "UTF-8"));
//				} catch (Exception e) {
//					map.put("nickname", null);
//				}
//				memberList.add(map);
//			}
//			page.setSubList(memberList);
//			return page;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * uc端注册并领取会员卡
//	 * @throws Exception 
//	 */
//	@Transactional(rollbackFor=Exception.class)
//	@Override
//	public Map<String, Object> linquMemberCard(BusUser busUser,
//			Map<String, Object> params) throws Exception {
//		Map<String, Object> returnMap = new HashMap<>();
//		try {
//
//			int count = cardMapper.countCardisBinding(busUser.getId());
//			String dictNum = dictService.dictBusUserNum(busUser.getId(),
//					busUser.getLevel(), 4, "1093"); // 多粉 翼粉
//			if (CommonUtil.toInteger(dictNum) < count) {
//				returnMap.put("code", -1);
//				returnMap.put("message", "会员卡已领取完!");
//				return returnMap;
//			}
//
//			String phone = CommonUtil.toString(params.get("phone"));
//			
//			Member member=memberMapper.findByPhone(busUser.getId(), phone);
//			if(CommonUtil.isEmpty(member)){
//				// 新增用户
//				member = new Member();
//				member.setPhone(phone);
//				member.setBusid(busUser.getId());
//				member.setLoginmode((byte) 1);
//				member.setNickname("Fans_" + phone.substring(4));
//				memberMapper.insertSelective(member);
//				MemberParameter memberParameter = memberParameterMapper
//						.findByMemberId(member.getId());
//				if (CommonUtil.isEmpty(memberParameter)) {
//					MemberParameter mp = new MemberParameter();
//					mp.setMemberid(member.getId());
//					memberParameterMapper.insertSelective(mp);
//				}
//			}
//
//			Integer applyType=CommonUtil.toInteger(params.get("applyType"));
//			Integer ctId = CommonUtil.toInteger(params.get("ctId"));
//			Integer gtId=CommonUtil.toInteger(params.get("gtId"));
//			Integer shopId = CommonUtil.toInteger(params.get("shopId"));
//			if (applyType != 3) {
//				//非购买会员卡  直接分配会员卡
//				Card card = new Card();
//				card.setIschecked((byte) 1);
//				card.setCardno(CommonUtil.getCode());
//				card.setCtId(ctId);
//				if (card.getCtId() == 4) {
//					card.setExpiredate(new Date());
//				}
//
//				card.setSystemcode(CommonUtil.getNominateCode());
//				card.setApplytype((byte) 0);
//
//				// 根据卡片类型 查询第一等级
//				List<Map<String, Object>> gradeTypes = gradeTypeMapper
//						.findBybusIdAndCtId3(member.getBusid(), ctId);
//
//				if (gradeTypes != null && gradeTypes.size() > 0) {
//					card.setGtId(Integer.parseInt(gradeTypes.get(0)
//							.get("gt_id").toString()));
//					GiveRule giveRule = giveRuleMapper
//							.findBybusIdAndGtIdAndCtId(
//									member.getBusid(),
//									Integer.parseInt(gradeTypes.get(0)
//											.get("gt_id").toString()), ctId);
//					card.setGrId(giveRule.getGrId());
//				}
//				card.setBusid(member.getBusid());
//				card.setCardno(CommonUtil.getCode());
//				card.setReceivedate(new Date());
//				card.setIsbinding((byte) 1);
//
//				card.setShopid(shopId);
//				card.setOnline((byte) 0);
//				cardMapper.insertSelective(card);
//
//				Member member1 = new Member();
//				member1.setMcId(card.getMcId());
//				member1.setId(member.getId());
//				memberMapper.updateByPrimaryKeySelective(member1);
//				returnMap.put("code", 1);
//				returnMap.put("message", "领取成功");
//				
//			}else{
//				returnMap.put("memberId", member.getId());
//				returnMap.put("code", 2);
//				returnMap.put("message", "未支付");
//			}
//
//		} catch (Exception e) {
//			LOG.error("erp 领取会员卡异常", e);
//			throw new Exception();
//		}
//		return returnMap;
//	}
//
//	@Transactional(rollbackFor=Exception.class)
//	@Override
//	public Map<String, Object> buyMemberCard(BusUser busUser,
//			Map<String, Object> params) throws Exception {
//		Map<String, Object> returnMap=new HashMap<>();
//		
//		Integer memberId=CommonUtil.toInteger(params.get("memberId"));
//		Integer ctId=CommonUtil.toInteger(params.get("ctId"));
//		Integer gtId=CommonUtil.toInteger(params.get("gtId"));
//		Integer shopId=CommonUtil.toInteger(params.get("shopId"));
//		Integer payType=CommonUtil.toInteger(params.get("payType"));
//		//购买会员卡
//		GradeType gradeType = gradeTypeMapper.selectByPrimaryKey(gtId);
//		if (CommonUtil.isEmpty(gradeType)
//				|| CommonUtil.isEmpty(gradeType.getBuymoney() <= 0)) {
//			throw new Exception();
//		}
//		// 添加会员记录
//		UserConsume uc = new UserConsume();
//		uc.setMemberid(memberId);
//		uc.setCtid(ctId);
//		uc.setRecordtype((byte) 2);
//		uc.setUctype((byte) 13);
//		uc.setTotalmoney(gradeType.getBuymoney());
//		uc.setCreatedate(new Date());
//		uc.setPaystatus((byte) 0);
//		uc.setDiscount(100);
//		uc.setDiscountmoney(gradeType.getBuymoney());
//		String orderCode = CommonUtil.getMEOrderCode();
//		uc.setOrdercode(orderCode);
//		uc.setGtId(gtId);
//		uc.setBususerid(busUser.getId());
//		uc.setStoreid(shopId);
//		uc.setStoreid(shopId);
//		if(payType==1){
//			//现金
//			uc.setPaystatus((byte) 1);
//			uc.setPaymenttype((byte)10);
//			userConsumeMapper.insertSelective(uc);
//
//			CardBuy cardbuy = new CardBuy();
//			cardbuy.setBuymoney(gradeType.getBuymoney());
//			cardbuy.setCtid(ctId);
//			cardbuy.setMemberid(memberId);
//			cardbuy.setBusid(busUser.getId());
//			cardBuyMapper.insertSelective(cardbuy);
//			
//
//			// 添加会员卡
//			Card card = new Card();
//			card.setIschecked((byte) 1);
//			card.setCardno(CommonUtil.getCode());
//			card.setCtId(ctId);
//
//			card.setSystemcode(CommonUtil.getNominateCode());
//			card.setApplytype((byte) 3);
//			card.setMemberid(memberId);
//			card.setGtId(gtId);
//			GiveRule giveRule = giveRuleMapper.findBybusIdAndGtIdAndCtId(
//					busUser.getId(),
//					card.getGtId(), card.getCtId());
//			card.setGrId(giveRule.getGrId());
//
//			card.setCardno(CommonUtil.getCode());
//			card.setBusid(busUser.getId());
//			card.setReceivedate(new Date());
//			card.setIsbinding((byte) 1);
//
//			if (card.getCtId() == 5) {
//				if (CommonUtil.isNotEmpty(gradeType.getBalance())) {
//					card.setFrequency(new Double(gradeType.getBalance()).intValue());
//				} else {
//					card.setFrequency(0);
//				}
//			} else {
//				if (CommonUtil.isNotEmpty(gradeType.getBalance())) {
//					card.setMoney(new Double(gradeType.getBalance()));
//				} else {
//					card.setMoney(0.0);
//				}
//			}
//
//			cardMapper.insertSelective(card);
//
//			Member member = new Member();
//			member.setId(memberId);
//			member.setIsbuy((byte) 1);
//			member.setMcId(card.getMcId());
//			memberMapper.updateByPrimaryKeySelective(member);
//			String balance = null;
//			if (card.getCtId() == 5) {
//				balance = card.getFrequency() + "次";
//			} else {
//				balance = card.getMoney() + "元";
//			}
//			memberPayService.saveCardRecordNew(card.getMcId(), (byte) 1,  gradeType.getBuymoney()+ "元", "购买会员卡", card.getPublicId(),
//					balance, card.getCtId(), 0.0);
//
//			// 新增会员短信通知
//			member = memberMapper.selectByPrimaryKey(memberId);
//			systemMsgService.sendNewMemberMsg(member);
//			
//			returnMap.put("code", 1);
//			returnMap.put("message","领取成功");
//		}else if(payType==2){
//			//扫码支付
//			userConsumeMapper.insertSelective(uc);
//			
//			returnMap.put("orderid", orderCode);
//			returnMap.put("businessUtilName", "alipayNotifyUrlBuinessServiceBuyCard");
//			returnMap.put("totalFee", gradeType.getBuymoney());
//			returnMap.put("model", 7);
//			returnMap.put("busId", busUser.getId());
//			returnMap.put("appidType", 0);
//			returnMap.put("orderNum", orderCode);
//			returnMap.put("code", 2);
//			returnMap.put("message","领取成功");
//		}
//		return returnMap;
//	}}
