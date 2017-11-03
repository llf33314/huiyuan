package com.gt.member.service.member.export;//package com.gt.member.service.old.member.export;
//
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.springframework.stereotype.Service;
//
//import com.gt.util.CommonUtil;
//
//
///**
// * 生成excel
// * @author pengjiangli
// * @version
// * 创建时间:2016年4月19日
// *
// */
//@Service
//public final class ExportExcel {
//
//	private static final Logger LOG = Logger.getLogger(ExportExcel.class);
//
//	/**
//	 * 数据导出入口
//	 *
//	 * @param type
//	 *            报表类型
//	 * @param map
//	 *            数据
//	 * @param tenantId
//	 *            租户
//	 * @return
//	 */
//	public SXSSFWorkbook todo(Byte type,
//			List<Map<String, Object>> map) {
//		// Excel 最大行数
//		if (map.size() > 1048570) {
//			return null;
//		}
//		switch (type) {
//		case 1:
//			// 导出会员卡
//			return exportMember(map);
//		case 2:
//			return exportSoldLog(map);
//		case 3:
//			return exportPickLog(map);
//		case 4:
//			return exportTaskFinishLog(map);
//		default:
//			break;
//		}
//		return null;
//	}
//
//
//	/**
//	 * 会员卡下载
//	 *
//	 * @param tenantId
//	 * @param map
//	 * @return
//	 */
//	public SXSSFWorkbook exportMember(
//			List<Map<String, Object>> map) {
//
//		SXSSFWorkbook wb = null;
//		try {
//			wb = new SXSSFWorkbook();
//			// 创建工作簿 冻结第一行
//			Sheet sheet = wb.createSheet("会员卡");
//			sheet.createFreezePane(0, 1, 0, 1); // 冻结行
//
//			// 设置表头
//			Row row = sheet.createRow(0); // 创建行, 位于第0行
//			ExcelStyle.setFirstRow(sheet, wb, row, 0, "卡号", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 1, "姓名", 25);
//			ExcelStyle.setFirstRow(sheet, wb, row, 2, "性别", 25);
//			ExcelStyle.setFirstRow(sheet, wb, row, 3, "手机号码", 20);
//			ExcelStyle.setFirstRow(sheet, wb, row, 4, "领卡时间", 15);
//			int rowNum=4;
//			Integer ctId=0;
//			if(map.size()>0){
//				ctId=CommonUtil.toInteger(map.get(0).get("ct_id"));
//			}
//			switch (ctId) {
//			case 1:
//				ExcelStyle.setFirstRow(sheet, wb, row, 5, "积分", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 6, "粉币", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 7, "流量", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 8, "会员类型", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 9, "会员等级", 15);
//				break;
//			case 2:
//				ExcelStyle.setFirstRow(sheet, wb, row, 5, "积分", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 6, "粉币", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 7, "流量", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 8, "会员类型", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 9, "会员等级", 15);
//				break;
//			case 3:
//				ExcelStyle.setFirstRow(sheet, wb, row, 5, "余额", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 6, "积分", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 7, "粉币", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 8, "流量", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 9, "会员类型", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 10, "会员等级", 15);
//				break;
//			case 4:
//				ExcelStyle.setFirstRow(sheet, wb, row, 5, "有效截止时间", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 6, "积分", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 7, "粉币", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 8, "流量", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 9, "会员类型", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 10, "会员等级", 15);
//				break;
//			case 5:
//				ExcelStyle.setFirstRow(sheet, wb, row, 5, "次数", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 6, "积分", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 7, "粉币", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 8, "流量", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 9, "会员类型", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 10, "会员等级", 15);
//				break;
//
//			default:
//				ExcelStyle.setFirstRow(sheet, wb, row, 5, "积分", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 6, "粉币", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 7, "流量", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 8, "会员类型", 15);
//				ExcelStyle.setFirstRow(sheet, wb, row, 9, "会员等级", 15);
//				break;
//			}
//
//
//			// 主体变量
//			String cardNo = "";
//			String nickname = "";
//			String sex = "";
//			String phone = "";
//			String receivedate = "";
//			String money = "";
//			String integral = "";
//			String fans_currency = "";
//			String flow = "";
//			String ct_name = "";
//			String gt_grade_name = "";
//			String expireDate="";
//			String frequency="";
//
//			// 主体样式
//			CellStyle cellStyle = ExcelStyle.otherRowFont(wb, (byte) 2);
//
//			// 填充数据的内容
//			int i = 1;
//			for (Map<String, Object> log : map) {
//
//				cardNo = CommonUtil.getStr(log.get("cardNo"));
//				nickname = CommonUtil.getStr(log.get("nickname"));
//				if(CommonUtil.isEmpty(log.get("sex"))){
//					sex="";
//				}else if ("1".equals(CommonUtil.getStr(log.get("sex")))) {
//					sex = "男";
//				} else if ("0".equals(CommonUtil.getStr(log.get("sex")))) {
//					sex = "女";
//				}
//				phone = CommonUtil.getStr(log.get("phone"));
//				receivedate = CommonUtil.getStr(log.get("receivedate"));
//				money = CommonUtil.getStr(log.get("money"));
//				integral = CommonUtil.getStr(log.get("integral"));
//				fans_currency = CommonUtil.getStr(log.get("fans_currency"));
//				flow = CommonUtil.getStr(log.get("flow"));
//				ct_name = CommonUtil.getStr(log.get("ct_name"));
//				gt_grade_name = CommonUtil.getStr(log.get("gt_grade_name"));
//				expireDate=CommonUtil.getStr(log.get("expireDate"));
//				frequency=CommonUtil.getStr(log.get("frequency"));
//
//				// 添加到wb中
//				row = sheet.createRow(i++);
//
//				Cell cell = row.createCell(0);
//				cell.setCellValue(cardNo); // 设置内容
//				cell.setCellStyle(cellStyle); // 填充样式
//
//				cell = row.createCell(1);
//				cell.setCellValue(nickname);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(2);
//				cell.setCellValue(sex);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(3);
//				cell.setCellValue(phone);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(4);
//				cell.setCellValue(receivedate);
//				cell.setCellStyle(cellStyle);
//
//
//				if(ctId==1 || ctId==2){
//					cell = row.createCell(5);
//					cell.setCellValue(integral);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(6);
//					cell.setCellValue(fans_currency);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(7);
//					cell.setCellValue(flow);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(8);
//					cell.setCellValue(ct_name);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(9);
//					cell.setCellValue(gt_grade_name);
//					cell.setCellStyle(cellStyle);
//				}else if(ctId==3){
//					cell = row.createCell(5);
//					cell.setCellValue(money);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(6);
//					cell.setCellValue(integral);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(7);
//					cell.setCellValue(fans_currency);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(8);
//					cell.setCellValue(flow);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(9);
//					cell.setCellValue(ct_name);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(10);
//					cell.setCellValue(gt_grade_name);
//					cell.setCellStyle(cellStyle);
//				}else if(ctId==4){
//					cell = row.createCell(5);
//					cell.setCellValue(expireDate);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(6);
//					cell.setCellValue(integral);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(7);
//					cell.setCellValue(fans_currency);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(8);
//					cell.setCellValue(flow);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(9);
//					cell.setCellValue(ct_name);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(10);
//					cell.setCellValue(gt_grade_name);
//					cell.setCellStyle(cellStyle);
//				}else if(ctId==5){
//					cell = row.createCell(5);
//					cell.setCellValue(frequency);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(6);
//					cell.setCellValue(integral);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(7);
//					cell.setCellValue(fans_currency);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(8);
//					cell.setCellValue(flow);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(9);
//					cell.setCellValue(ct_name);
//					cell.setCellStyle(cellStyle);
//
//					cell = row.createCell(10);
//					cell.setCellValue(gt_grade_name);
//					cell.setCellStyle(cellStyle);
//				}
//			}
//
//		} catch (Exception e) {
//			LOG.warn("生成excel异常", e);
//			return null;
//		}
//		return wb;
//
//	}
//
//
//
//
//
//	/**
//	 * 导出模板
//	 * @return
//	 */
//	public SXSSFWorkbook exportMemberModel(){
//		SXSSFWorkbook wb = null;
//		try {
//			wb = new SXSSFWorkbook();
//			// 创建工作簿 冻结第一行
//			Sheet sheet = wb.createSheet("会员卡");
//			sheet.createFreezePane(0, 1, 0, 1); // 冻结行
//
//			// 设置表头
//			Row row = sheet.createRow(0); // 创建行, 位于第0行
//			ExcelStyle.setFirstRow(sheet, wb, row, 0, "卡号", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 1, "姓名", 25);
//			ExcelStyle.setFirstRow(sheet, wb, row, 2, "性别", 25);
//			ExcelStyle.setFirstRow(sheet, wb, row, 3, "手机号码", 20);
//			ExcelStyle.setFirstRow(sheet, wb, row, 4, "领卡时间", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 5, "余额", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 6, "积分", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 7, "粉币", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 8, "流量", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 9, "会员类型", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 10, "会员等级", 15);
//
//		}catch(Exception e){
//			LOG.warn("生成excel异常", e);
//		}
//		return wb;
//	}
//
//
//
//	/**
//	 * 佣金结算
//	 *
//	 * @param tenantId
//	 * @param map
//	 * @return
//	 */
//	public SXSSFWorkbook exportSoldLog(
//			List<Map<String, Object>> map) {
//
//		SXSSFWorkbook wb = null;
//		try {
//			wb = new SXSSFWorkbook();
//			// 创建工作簿 冻结第一行
//			Sheet sheet = wb.createSheet("佣金结算统计");
//			sheet.createFreezePane(0, 1, 0, 1); // 冻结行
//
//			// 设置表头
//			Row row = sheet.createRow(0); // 创建行, 位于第0行
//			ExcelStyle.setFirstRow(sheet, wb, row, 0, "分销员账户", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 1, "商品名称", 25);
//			ExcelStyle.setFirstRow(sheet, wb, row, 2, "销售金额", 25);
//			ExcelStyle.setFirstRow(sheet, wb, row, 3, "佣金比例(%)", 20);
//			ExcelStyle.setFirstRow(sheet, wb, row, 4, "佣金", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 5, "销售时间", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 6, "结算状态", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 7, "是否结算", 15);
//
//
//			// 主体变量
//			String soldname = "";
//			String soldmoney = "";
//			String soldDate = "";
//			String commissionMoney = "";
//			String ratio = "";
//			String state = "";
//			String settletype = "";
//			String account = "";
//
//			//总计
//			Double soldmoneys=0.0;
//			Double commissionMoneys=0.0;
//			// 主体样式
//			CellStyle cellStyle = ExcelStyle.otherRowFont(wb, (byte) 2);
//
//			// 填充数据的内容
//			int i = 1;
//			for (Map<String, Object> log : map) {
//				account = CommonUtil.getStr(log.get("account"));
//				soldname = CommonUtil.getStr(log.get("soldname"));
//				soldmoney=CommonUtil.getStr(log.get("soldmoney"));
//				soldmoneys=CommonUtil.add(soldmoneys, soldmoney);
//
//				soldDate = CommonUtil.getStr(log.get("soldDate"));
//				commissionMoney = CommonUtil.getStr(log.get("commissionMoney"));
//				commissionMoneys=CommonUtil.add(commissionMoneys, commissionMoney);
//
//				ratio = CommonUtil.getStr(log.get("ratio"));
//
//				state = CommonUtil.getStr(log.get("state"));
//				if("0".equals(state)){
//					state="未结算";
//				}else{
//					state="已结算";
//				}
//				settletype = CommonUtil.getStr(log.get("settletype"));
//				if("0".equals(settletype)){
//					settletype="人工结算";
//				}else{
//					settletype="自动结算";
//				}
//
//
//				// 添加到wb中
//				row = sheet.createRow(i++);
//
//				Cell cell = row.createCell(0);
//				cell.setCellValue(account); // 设置内容
//				cell.setCellStyle(cellStyle); // 填充样式
//
//				cell = row.createCell(1);
//				cell.setCellValue(soldname);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(2);
//				cell.setCellValue(soldmoney);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(3);
//				cell.setCellValue(ratio);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(4);
//				cell.setCellValue(commissionMoney);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(5);
//				cell.setCellValue(soldDate);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(6);
//				cell.setCellValue(state);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(7);
//				cell.setCellValue(settletype);
//				cell.setCellStyle(cellStyle);
//			}
//			// 添加到wb中
//			row = sheet.createRow(i++);
//			Cell cell = row.createCell(0);
//			cell.setCellValue("总计"); // 设置内容
//			cell.setCellStyle(cellStyle); // 填充样式
//
//			cell = row.createCell(2);
//			cell.setCellValue(soldmoneys);
//			cell.setCellStyle(cellStyle);
//
//			cell = row.createCell(4);
//			cell.setCellValue(commissionMoneys);
//			cell.setCellStyle(cellStyle);
//
//
//
//		} catch (Exception e) {
//			LOG.warn("生成excel异常", e);
//			return null;
//		}
//		return wb;
//
//	}
//
//
//
//	/**
//	 * 佣金提取
//	 *
//	 * @param tenantId
//	 * @param map
//	 * @return
//	 */
//	public SXSSFWorkbook exportPickLog(
//			List<Map<String, Object>> map) {
//
//		SXSSFWorkbook wb = null;
//		try {
//			wb = new SXSSFWorkbook();
//			// 创建工作簿 冻结第一行
//			Sheet sheet = wb.createSheet("佣金提取统计");
//			sheet.createFreezePane(0, 1, 0, 1); // 冻结行
//
//			// 设置表头
//			Row row = sheet.createRow(0); // 创建行, 位于第0行
//			ExcelStyle.setFirstRow(sheet, wb, row, 0, "分销员账户", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 1, "分销员", 25);
//			ExcelStyle.setFirstRow(sheet, wb, row, 2, "提取金额", 25);
//			ExcelStyle.setFirstRow(sheet, wb, row, 3, "时间", 20);
//			ExcelStyle.setFirstRow(sheet, wb, row, 4, "剩余佣金", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 5, "提取状态", 15);
//
//			// 主体变量
//			String account = "";
//			String nickname = "";
//			String pickMoney = "";
//			String pickDate = "";
//			String surplusMoney = "";
//			String state = "";
//
//			//总计
//			Double pickMoneys=0.0;
//			Double surplusMoneys=0.0;
//			// 主体样式
//			CellStyle cellStyle = ExcelStyle.otherRowFont(wb, (byte) 2);
//
//			// 填充数据的内容
//			int i = 1;
//			for (Map<String, Object> log : map) {
//				account = CommonUtil.getStr(log.get("account"));
//				nickname = CommonUtil.getStr(log.get("nickname"));
//				pickMoney=CommonUtil.getStr(log.get("pickMoney"));
//				pickMoneys=CommonUtil.add(pickMoneys, pickMoney);
//
//				pickDate = CommonUtil.getStr(log.get("pickDate"));
//				surplusMoney = CommonUtil.getStr(log.get("surplusMoney"));
//				surplusMoneys=CommonUtil.add(surplusMoneys, surplusMoney);
//
//
//				state = CommonUtil.getStr(log.get("state"));
//				if("0".equals(state)){
//					state="未提取";
//				}else{
//					state="已提取";
//				}
//
//
//				// 添加到wb中
//				row = sheet.createRow(i++);
//
//				Cell cell = row.createCell(0);
//				cell.setCellValue(account); // 设置内容
//				cell.setCellStyle(cellStyle); // 填充样式
//
//				cell = row.createCell(1);
//				cell.setCellValue(nickname);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(2);
//				cell.setCellValue(pickMoney);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(3);
//				cell.setCellValue(pickDate);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(4);
//				cell.setCellValue(surplusMoney);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(5);
//				cell.setCellValue(state);
//				cell.setCellStyle(cellStyle);
//
//			}
//			// 添加到wb中
//			row = sheet.createRow(i++);
//			Cell cell = row.createCell(0);
//			cell.setCellValue("总计"); // 设置内容
//			cell.setCellStyle(cellStyle); // 填充样式
//
//			cell = row.createCell(2);
//			cell.setCellValue(pickMoneys);
//			cell.setCellStyle(cellStyle);
//
//			cell = row.createCell(4);
//			cell.setCellValue(surplusMoneys);
//			cell.setCellStyle(cellStyle);
//
//
//
//		} catch (Exception e) {
//			LOG.warn("生成excel异常", e);
//			return null;
//		}
//		return wb;
//
//	}
//
//
//	/**
//	 * 佣金提取
//	 *
//	 * @param tenantId
//	 * @param map
//	 * @return
//	 */
//	public SXSSFWorkbook exportTaskFinishLog(
//			List<Map<String, Object>> map) {
//
//		SXSSFWorkbook wb = null;
//		try {
//			wb = new SXSSFWorkbook();
//			// 创建工作簿 冻结第一行
//			Sheet sheet = wb.createSheet("任务统计");
//			sheet.createFreezePane(0, 1, 0, 1); // 冻结行
//
//			// 设置表头
//			Row row = sheet.createRow(0); // 创建行, 位于第0行
//			ExcelStyle.setFirstRow(sheet, wb, row, 0, "任务名称", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 1, "开始时间", 25);
//			ExcelStyle.setFirstRow(sheet, wb, row, 2, "结束时间", 25);
//			ExcelStyle.setFirstRow(sheet, wb, row, 3, "分销账户", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 4, "分销商", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 5, "任务销售金额", 20);
//			ExcelStyle.setFirstRow(sheet, wb, row, 6, "实际销售金额", 15);
//			ExcelStyle.setFirstRow(sheet, wb, row, 7, "奖励佣金", 15);
//
//			//f.soldMoney,t.saleMoney,t.rewardMoney,t.startDate,t.endDate,m.nickname,d.account
//			// 主体变量
//			String title = "";
//			String startDate = "";
//			String endDate = "";
//			String saleMoney = "";
//			String rewardMoney = "";
//			String nickname = "";
//			String soldMoney = "";
//			String account="";
//
//			//总计
//			Double soldMoneys=0.0;
//			Double rewardMoneys=0.0;
//			// 主体样式
//			CellStyle cellStyle = ExcelStyle.otherRowFont(wb, (byte) 2);
//
//			// 填充数据的内容
//			int i = 1;
//			for (Map<String, Object> log : map) {
//				title = CommonUtil.getStr(log.get("title"));
//				startDate = CommonUtil.getStr(log.get("startDate"));
//				endDate = CommonUtil.getStr(log.get("endDate"));
//				saleMoney = CommonUtil.getStr(log.get("saleMoney"));
//				account = CommonUtil.getStr(log.get("account"));
//				nickname = CommonUtil.getStr(log.get("nickname"));
//
//
//				soldMoney=CommonUtil.getStr(log.get("soldMoney"));
//				soldMoneys=CommonUtil.add(soldMoneys, soldMoney);
//
//				rewardMoney = CommonUtil.getStr(log.get("rewardMoney"));
//				rewardMoneys=CommonUtil.add(rewardMoneys, rewardMoney);
//
//
//				// 添加到wb中
//				row = sheet.createRow(i++);
//
//				Cell cell = row.createCell(0);
//				cell.setCellValue(title); // 设置内容
//				cell.setCellStyle(cellStyle); // 填充样式
//
//				cell = row.createCell(1);
//				cell.setCellValue(startDate);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(2);
//				cell.setCellValue(endDate);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(3);
//				cell.setCellValue(account);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(4);
//				cell.setCellValue(nickname);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(5);
//				cell.setCellValue(saleMoney);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(6);
//				cell.setCellValue(soldMoney);
//				cell.setCellStyle(cellStyle);
//
//				cell = row.createCell(7);
//				cell.setCellValue(rewardMoney);
//				cell.setCellStyle(cellStyle);
//
//			}
//			// 添加到wb中
//			row = sheet.createRow(i++);
//			Cell cell = row.createCell(0);
//			cell.setCellValue("总计"); // 设置内容
//			cell.setCellStyle(cellStyle); // 填充样式
//
//			cell = row.createCell(6);
//			cell.setCellValue(soldMoneys);
//			cell.setCellStyle(cellStyle);
//
//			cell = row.createCell(7);
//			cell.setCellValue(rewardMoneys);
//			cell.setCellStyle(cellStyle);
//
//
//
//		} catch (Exception e) {
//			LOG.warn("生成excel异常", e);
//			return null;
//		}
//		return wb;
//
//	}
//
//}
