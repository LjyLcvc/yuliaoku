package com.lcvc.guojiaoyuan.yuliaoku.util.opi.material;

import com.lcvc.guojiaoyuan.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.model.MaterialType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;
import java.util.Map;

/**
 * 将物资记录在Excel导出
 */
public class MaterialWriteForExcel {

    /**
     * 物料单元格记录的样式
     * @return
     */
    public static XSSFCellStyle getMaterialRecordStyle( XSSFWorkbook workbook){
        // 设置单元格表单标题样式
        XSSFCellStyle style = workbook.createCellStyle();
        //设置字体
        XSSFFont titlefont = workbook.createFont();
        titlefont.setFontHeightInPoints((short) 12);//设置字体大小
        style.setFont(titlefont);
        style.setBorderBottom(BorderStyle.THIN);//底部细线边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER); //垂直居中
        style.setWrapText(true);//自动换行
        return style;
    }

    /**
     * 将物料记录输出到excel
     * 说明：
     * @param map
     * @return
     */
    public static XSSFWorkbook writeExcel(Map<MaterialType, List<Material>> map) throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook();//创建建工作簿对象
        XSSFCellStyle materialRecordStyle=getMaterialRecordStyle(workbook);//设置单元格样式
        //创建每个工作表的标题，因为都相同，这里统一设定
        String[] titles = { "序号\r\nS/N", "物料名称\r\nMaterial Description","物料名称\r\nEnglish Description","物料名称\r\nSpanish Description"};
        map.forEach((materialType, materials) ->{
            XSSFSheet sheet = workbook.createSheet(materialType.getId()+"-"+materialType.getName());//生成工作表的名称：id-物料类别名
            /**
             *  在工作表第一行创建标题集合
             */
            XSSFRow titleRow = sheet.createRow(0);
            //根据集合长度创建第一行单元格数组
            XSSFCell[] titleRowCells = new XSSFCell[titles.length];
            //根据字段，在第一行创建相应的字段值
            for (int i = 0; i < titles.length; i++) {
                titleRowCells[i] = titleRow.createCell(i);
                //用字符串表示
                titleRowCells[i].setCellValue(new XSSFRichTextString(titles[i]));
                titleRowCells[i].setCellStyle(materialRecordStyle);//统一设置单元格样式
            }
            //设置列宽
            sheet.setColumnWidth(0, 10 * 256);//设置第1列的宽度是10个字符宽度
            sheet.setColumnWidth(1, 30 * 256);
            sheet.setColumnWidth(2, 60 * 256);
            sheet.setColumnWidth(3, 60 * 256);
            /**
             * 添加记录行
             */
            int rowIndex=1;//从第二行开始
            for(Material material:materials){//遍历物料
                XSSFRow row = sheet.createRow(rowIndex++);
                for (int ii = 0; ii < titles.length; ii++) {//设置各列的值
                    XSSFCell cell = row.createCell(ii);//获取第ii列的单元格
                    cell.setCellStyle(materialRecordStyle);//统一设置单元格样式
                    if(ii==0){//设置第一列的值
                        cell.setCellValue(material.getId());
                    }else if(ii==1){//设置第2列的值
                        cell.setCellValue(material.getChinese());
                    }else if(ii==2){//设置第3列的值
                        cell.setCellValue(material.getEnglish());
                    }else if(ii==3){//设置第4列的值
                        cell.setCellValue(material.getSpanish());
                    }
                }
            }
        });
        return workbook;
    }
}
