package com.gt.member.export;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.gt.member.util.CommonUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * Excel样式设置
 */
public class ExcelStyle {

    /**
     * 第一行设置
     *
     * @param sheet 页簿
     * @param wb    对象
     * @param row   行
     * @param num   列
     * @param value 内容
     * @param width 宽度
     */
    public static void setFirstRow( Sheet sheet, SXSSFWorkbook wb, Row row, int num, String value, int width ) {
	sheet.setColumnWidth( num, 200 * width );
	CellStyle setBorder = wb.createCellStyle();
	setBorder.setFillForegroundColor(IndexedColors.GREEN.index ); // 设置背景色
	setBorder.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	setBorder.setAlignment( HorizontalAlignment.CENTER); // 居中
	setBorder.setVerticalAlignment(VerticalAlignment.CENTER); // 设置垂直居中
	setBorder.setWrapText( true );

	Font font = wb.createFont();
	font.setFontName( "Arial" );
	font.setFontHeightInPoints( (short) 9 );// 设置字体大小
	//	font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 粗体显示
	//font.setBold(true);
	font.setColor( IndexedColors.BLACK.index );
	setBorder.setFont( font );
	Cell cell = row.createCell( num );
	cell.setCellValue( value );
	cell.setCellStyle( setBorder );
    }

    /**
     * 字体设置
     *
     * @param wb
     * @param bool 字体位置 1左 2中 3右
     *
     * @return
     */
    public static CellStyle otherRowFont( SXSSFWorkbook wb, Byte bool ) {
	CellStyle setBorder = wb.createCellStyle();
	switch ( bool ) {
	    case 1:
					setBorder.setAlignment(HorizontalAlignment.LEFT); // 居左
					setBorder.setVerticalAlignment(VerticalAlignment.CENTER);// 设置垂直居左
		break;
	    case 2:
					setBorder.setAlignment(HorizontalAlignment.CENTER); // 居中
					setBorder.setVerticalAlignment(VerticalAlignment.CENTER);// 设置垂直居中
		break;
	    case 3:
					setBorder.setAlignment(HorizontalAlignment.RIGHT); // 居右
					setBorder.setVerticalAlignment(VerticalAlignment.CENTER);// 设置垂直居右
		break;
	}

	Font font = wb.createFont();
	font.setFontHeightInPoints( (short) 9 );// 设置字体大小
	font.setFontName( "Arial" );
	setBorder.setFont( font );
	return setBorder;
    }

    /**
     * 字体设置
     *
     * @param wb
     *
     * @return
     */
    public static CellStyle footRowFont( SXSSFWorkbook wb ) {
	CellStyle setBorder = wb.createCellStyle();
	setBorder.setAlignment(HorizontalAlignment.CENTER); // 居中
	setBorder.setVerticalAlignment(VerticalAlignment.CENTER);//设置垂直居中

	Font font = wb.createFont();
	font.setFontHeightInPoints( (short) 8 );//设置字体大小
	font.setFontName( "Arial" );
	font.setColor( IndexedColors.RED.index );
	//font.setBold(true);
	setBorder.setFont( font );
	return setBorder;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     *
     * @return String 单元格数据内容
     */
    public static String getStringCellValue( Cell cell ) {
	if ( CommonUtil.isEmpty( cell ) ) {
	    return "";
	}
	String strCell = "";
	switch ( cell.getCellType() ) {
	    case XSSFCell.CELL_TYPE_STRING:
		strCell = cell.getStringCellValue();
		break;
	    case SXSSFCell.CELL_TYPE_NUMERIC:
		if ( HSSFDateUtil.isCellDateFormatted( cell ) ) {
		    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		    return sdf.format( HSSFDateUtil.getJavaDate( cell.getNumericCellValue() ) ).toString();
		}
		BigDecimal bigDecimal = new BigDecimal( cell.getNumericCellValue() );
		strCell = bigDecimal.toString();
		break;
	    case SXSSFCell.CELL_TYPE_BOOLEAN:
		strCell = String.valueOf( cell.getBooleanCellValue() );
		break;
	    case SXSSFCell.CELL_TYPE_BLANK:
		strCell = "";
		break;
	    default:
		strCell = "";
		break;
	}
	if ( strCell.equals( "" ) || strCell == null ) {
	    return "";
	}
	return strCell;
    }

    public static void GenerateExcelStyle( SXSSFWorkbook workbook, CellStyle titleStyle, CellStyle headStyle ) {

	//标题字体
	Font titleFont = workbook.createFont();
	titleFont.setColor( HSSFFont.SS_NONE ); //字体颜色
	titleFont.setFontName( "黑体" ); //字体
	titleFont.setFontHeightInPoints( (short) 16  );
	titleFont.setBold( true );

	//头部样式
	titleStyle.setAlignment(HorizontalAlignment.CENTER);  //内容排列的方式
	titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	titleStyle.setFont(titleFont);
	titleStyle.setBorderBottom(BorderStyle.MEDIUM); //下边框
	titleStyle.setBorderLeft(BorderStyle.MEDIUM);//左边框
	titleStyle.setBorderTop(BorderStyle.MEDIUM);//上边框
	titleStyle.setBorderRight(BorderStyle.MEDIUM);//右边框

	//头部字体
	Font headFont = workbook.createFont();
	headFont.setColor( HSSFFont.SS_NONE ); //字体颜色
	headFont.setFontName( "黑体" ); //字体
	headFont.setFontHeightInPoints( (short) 11  );

	//头部样式
	headStyle.setAlignment(HorizontalAlignment.CENTER);  //内容排列的方式
	headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	headStyle.setFont(headFont);
	headStyle.setBorderBottom(BorderStyle.MEDIUM); //下边框
	headStyle.setBorderLeft(BorderStyle.MEDIUM);//左边框
	headStyle.setBorderTop(BorderStyle.MEDIUM);//上边框
	headStyle.setBorderRight(BorderStyle.MEDIUM);//右边框

	//内容字体
	Font contentFont = workbook.createFont();
	contentFont.setColor( HSSFFont.SS_NONE);

	/*//内容(文字类型)样式
	contentStrStyle.setAlignment(HorizontalAlignment.CENTER); //内容排列的方式
	contentStrStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	contentStrStyle.setFont(contentFont);
	contentStrStyle.setBorderBottom(BorderStyle.MEDIUM); //下边框
	contentStrStyle.setBorderLeft(BorderStyle.MEDIUM);//左边框
	contentStrStyle.setBorderTop(BorderStyle.MEDIUM);//上边框
	contentStrStyle.setBorderRight(BorderStyle.MEDIUM);//右边框

	//内容(数字类型-金额)样式
	contentMoneyStyle.setAlignment(HorizontalAlignment.CENTER);  //内容排列的方式
	contentMoneyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	contentMoneyStyle.setFont(contentFont);
	contentMoneyStyle.setBorderBottom(BorderStyle.MEDIUM); //下边框
	contentMoneyStyle.setBorderLeft(BorderStyle.MEDIUM);//左边框
	contentMoneyStyle.setBorderTop(BorderStyle.MEDIUM);//上边框
	contentMoneyStyle.setBorderRight(BorderStyle.MEDIUM);//右边框
	DataFormat fmt = workbook.createDataFormat();
	contentMoneyStyle.setDataFormat(fmt.getFormat("#,##0.00"));

	//内容(数字类型-数量)样式
	contentNumStyle.setAlignment(HorizontalAlignment.CENTER);  //内容排列的方式
	contentNumStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	contentNumStyle.setFont(contentFont);
	contentNumStyle.setBorderBottom(BorderStyle.MEDIUM); //下边框
	contentNumStyle.setBorderLeft(BorderStyle.MEDIUM);//左边框
	contentNumStyle.setBorderTop(BorderStyle.MEDIUM);//上边框
	contentNumStyle.setBorderRight(BorderStyle.MEDIUM);//右边框*/
    }
}
