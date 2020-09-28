package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.opi;

import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialType;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;
import java.util.Map;

/**
 * 将物资记录在Excel导出
 */
public class MaterialWriteForExcel {

    /**
     * 将物料记录输出到excel
     * 说明：
     * @param map
     * @return
     */
    public static XSSFWorkbook writeExcel(Map<MaterialType, List<Material>> map) throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook();//创建建工作簿对象
        //创建每个工作表的标题，因为都相同，这里统一设定
        String[] titles = { "序号\nS/N","物料图号\nMaterial Figure No.", "物料名称\nMaterial Description","物料名称\nEnglish Description","物料名称\nSpanish Description"};
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
            }
            /**
             * 添加记录行
             */
            int rowIndex=1;//从第二行开始
            for(Material material:materials){//遍历物料
                XSSFRow row = sheet.createRow(rowIndex++);
                //设置第一列的值
                XSSFCell cell = row.createCell(0);
                cell.setCellValue(material.getId());
                //设置第二列的值
                cell = row.createCell(1);
                cell.setCellValue(material.getChinese());
                //设置第三列的值
                cell = row.createCell(2);
                cell.setCellValue(material.getEnglish());
                //设置第四列的值
                cell = row.createCell(3);
                cell.setCellValue(material.getSpanish());
            }
        });
        return workbook;
    }
}
