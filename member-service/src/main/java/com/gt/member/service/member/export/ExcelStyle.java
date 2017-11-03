package com.gt.member.service.member.export;//package com.gt.member.service.old.member.export;
//
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.xssf.streaming.SXSSFCell;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//
//import com.gt.util.CommonUtil;
//
///**
// * Excel样式设置
// *
// */
//public class ExcelStyle {
//
//	/**
//	 * 第一行设置
//	 *
//	 * @param sheet 页簿
//	 * @param wb 对象
//	 * @param row 行
//	 * @param num 列
//	 * @param value 内容
//	 * @param width 宽度
//	 */
//	public static void setFirstRow(Sheet sheet, SXSSFWorkbook wb, Row row, int num, String value,
//			int width) {
//		sheet.setColumnWidth(num, 200 * width);
//		CellStyle setBorder = wb.createCellStyle();
//		// HSSFColor.GREEN.index
//		setBorder.setFillForegroundColor(HSSFColor.GREEN.index); // 设置背景色
//		setBorder.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		setBorder.setAlignment(CellStyle.ALIGN_CENTER); // 居中
//		setBorder.setVerticalAlignment(CellStyle.ALIGN_CENTER); // 设置垂直居中
//		setBorder.setWrapText(true);
//
//		Font font = wb.createFont();
//		font.setFontName("Arial");
//		font.setFontHeightInPoints((short) 9);// 设置字体大小
//		font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 粗体显示
//		font.setBold(true);
//		font.setColor(HSSFColor.WHITE.index);
//		setBorder.setFont(font);
//		Cell cell = row.createCell(num);
//		cell.setCellValue(value);
//		cell.setCellStyle(setBorder);
//	}
//
//	/**
//	 * 字体设置
//	 *
//	 * @param wb
//	 * @param bool 字体位置 1左 2中 3右
//	 * @return
//	 */
//	public static CellStyle otherRowFont(SXSSFWorkbook wb, Byte bool) {
//		CellStyle setBorder = wb.createCellStyle();
//		switch (bool) {
//		case 1:
//			setBorder.setAlignment(CellStyle.ALIGN_LEFT); // 居左
//			setBorder.setVerticalAlignment(CellStyle.ALIGN_LEFT);// 设置垂直居左
//			break;
//		case 2:
//			setBorder.setAlignment(CellStyle.ALIGN_CENTER); // 居中
//			setBorder.setVerticalAlignment(CellStyle.ALIGN_CENTER);// 设置垂直居中
//			break;
//		case 3:
//			setBorder.setAlignment(CellStyle.ALIGN_RIGHT); // 居右
//			setBorder.setVerticalAlignment(CellStyle.ALIGN_RIGHT);// 设置垂直居右
//			break;
//		}
//
//		Font font = wb.createFont();
//		font.setFontHeightInPoints((short) 9);// 设置字体大小
//		font.setFontName("Arial");
//		setBorder.setFont(font);
//		return setBorder;
//	}
//
//
//
//	/**
//	 * 字体设置
//	 * @param wb
//	 * @return
//	 */
//	public static CellStyle footRowFont(SXSSFWorkbook wb) {
//		CellStyle setBorder = wb.createCellStyle();
//		setBorder.setAlignment(CellStyle.ALIGN_CENTER); // 居中
//		setBorder.setVerticalAlignment(CellStyle.ALIGN_CENTER);//设置垂直居中
//
//		Font font = wb.createFont();
//		font.setFontHeightInPoints((short) 8);//设置字体大小
//		font.setFontName("Arial");
//		font.setColor(HSSFColor.RED.index);
//		font.setBold(true);
//		setBorder.setFont(font);
//		return setBorder;
//	}
//
//
//	  /**
//     * 获取单元格数据内容为字符串类型的数据
//     *
//     * @param cell Excel单元格
//     * @return String 单元格数据内容
//     */
//    public static String getStringCellValue(Cell cell) {
//    	if(CommonUtil.isEmpty(cell)){
//    		return "";
//    	}
//        String strCell = "";
//        switch (cell.getCellType()) {
//        case XSSFCell.CELL_TYPE_STRING:
//            strCell = cell.getStringCellValue();
//            break;
//        case SXSSFCell.CELL_TYPE_NUMERIC:
//        	if(HSSFDateUtil.isCellDateFormatted(cell)){
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
//			}
//        	BigDecimal bigDecimal=new BigDecimal(cell.getNumericCellValue());
//        	strCell=bigDecimal.toString();
//            break;
//        case SXSSFCell.CELL_TYPE_BOOLEAN:
//            strCell = String.valueOf(cell.getBooleanCellValue());
//            break;
//        case SXSSFCell.CELL_TYPE_BLANK:
//            strCell = "";
//            break;
//        default:
//            strCell = "";
//            break;
//        }
//        if (strCell.equals("") || strCell == null) {
//            return "";
//        }
//        return strCell;
//    }
//
//    public static void main(String[] args) {
//		String aa="1.3544338596E10";
//		aa=aa.replace("E10", "");
//		aa=aa.replace(".","");
//		System.out.println(aa);
//
//	}
//}
