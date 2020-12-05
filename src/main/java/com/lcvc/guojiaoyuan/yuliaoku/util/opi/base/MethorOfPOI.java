package com.lcvc.guojiaoyuan.yuliaoku.util.opi.base;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import java.text.DecimalFormat;

public class MethorOfPOI {
	
	/*
	 * 读取单元格的内容
	 * 说明：针对excel2007版本之后
	 */
	@SuppressWarnings("static-access")
    public static String getValue(XSSFCell cell) {
		String value=null;
		if(cell!=null){
			if (cell.getCellType() == CellType.BOOLEAN) {
				value=String.valueOf(cell.getBooleanCellValue());
	        } else if (cell.getCellType() == CellType.NUMERIC) {
	        	//如果是数字类型，为了防止出现小数点先将小数转换为整数——本项目中不会
	        	DecimalFormat df = new DecimalFormat("#");
	        	double number=cell.getNumericCellValue();
	        	value=String.valueOf(df.format(number));
	        } else {
	            value=String.valueOf(cell.getStringCellValue());
	        }
		}
		return value;
    }

	/*
	 * 读取单元格的内容
	 * 说明：针对excel2003版本之前
	 */
    @SuppressWarnings("static-access")
    public static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    
    /*
	 * 设置单元格样式1
	 * @param book 工作簿
	 */
	public static XSSFCellStyle getCellStyle1(XSSFWorkbook book){
		XSSFCellStyle style1 = book.createCellStyle();
		style1.setBorderTop(BorderStyle.THIN);
		style1.setBorderRight(BorderStyle.THIN);
		style1.setBorderBottom(BorderStyle.THIN);
		style1.setBorderLeft(BorderStyle.THIN);
		
		XSSFDataFormat format = book.createDataFormat();
		style1.setDataFormat(format.getFormat("@"));
		return style1;
	}
	
	
	/*
	 * 设置单元格样式1
	 * @param sheet 工作表
	 * @param commentContent 批注内容
	 * @param author 批注的作者
	 */
	public static XSSFComment getCellComment1(XSSFSheet sheet,String commentContent,String author){
		 //创建绘图对象
		XSSFDrawing p = sheet.createDrawingPatriarch();
		// 获取批注对象
        // (int dx1, int dy1, int dx2, int dy2, short col1, int row1, short
        // col2, int row2)
        // 前四个参数是坐标点,后四个参数是编辑和显示批注时的大小.
        XSSFComment comment = p.createCellComment(new XSSFClientAnchor(0, 0, 0,
                0, (short) 3, 3, (short) 7, 8));
        // 输入批注信息
        comment.setString(new XSSFRichTextString(commentContent));
        // 添加作者
        comment.setAuthor(author);
		return comment;
	}
}
