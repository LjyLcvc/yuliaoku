package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.opi;

import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialType;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyServiceException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.opi.base.MethorOfPOI;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 从Excel中读取物资记录
 */
public class MaterialReadFromExcel {

    /**
     * 从输入流中读取excel的内容
     * 说明：
     * @param inputStream
     * @return
     */
    public static Map<MaterialType,List<Material>> getExcel(InputStream inputStream) throws Exception{
        Map<MaterialType,List<Material>> map= new LinkedHashMap<MaterialType,List<Material>>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);//根据输入流创建工作簿对象
        int number=workbook.getNumberOfSheets();//获取工作簿中工作表数量
        for (int i = 0; i < number; i++) {//遍历所有工作表
            XSSFSheet sheet = workbook.getSheetAt(i);
            String name = sheet.getSheetName().trim();//获取工作表的名称
            String[] names=name.split("\\-");//按照规则进行拆分，如1-车灯，表示1为已存在的主键
            //创建物料类别对象
            MaterialType materialType=new MaterialType();
            if(names.length==2){//如果有2个，则表示该主键存在
                materialType.setId(Integer.valueOf(names[0]));
                materialType.setName(names[1]);
            }else{//如果无法拆分，则表示没有主键，即该产品类别不存在
                materialType.setName(name);
            }
            List<Material> materials=new ArrayList<Material>();//创建物料对象
            map.put(materialType,materials);//将物料类别-物料的键值存储
            //根据表格的实际记录行数，逐行读取记录——第一行是标题行，不进行读取
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                XSSFRow row = sheet.getRow(rowNum);//获取指定的行
                String chinese=MethorOfPOI.getValue(row.getCell(0));//第一列是中文名
                if(StringUtils.isEmpty(chinese)){//如果中文名不存在，则说明该表格没有下一行。因为通过getLastRowNum可能因电子表格不一致导致不准确
                    break;
                }
                chinese=chinese.trim();
                if(chinese.length()>50){
                    throw new MyServiceException("操作失败：导入的表格的工作簿（"+name+"）第"+(rowNum+1)+"行的中文列长度超过50个字符");
                }
                Material material=new Material();
                material.setChinese(chinese);//设置中文名
                String english=MethorOfPOI.getValue(row.getCell(1));
                if(!StringUtils.isEmpty(english)){//如果不为空
                    english=english.trim();
                    if(english.length()>200){
                        throw new MyServiceException("操作失败：导入的表格的工作簿（"+name+"）第"+(rowNum+1)+"行的英文文列长度超过200个字符");
                    }
                    material.setEnglish(english);
                }
                String spanish=MethorOfPOI.getValue(row.getCell(2));
                if(!StringUtils.isEmpty(spanish)){//如果不为空
                    spanish=spanish.trim();
                    if(spanish.length()>200){
                        throw new MyServiceException("操作失败：导入的表格的工作簿（"+name+"）第"+(rowNum+1)+"行的西文文列长度超过200个字符");
                    }
                    material.setSpanish(spanish);
                }
                materials.add(material);//将该记录添加到集合中
            }
        }
        return map;
    }

    public static void main(String[] args) throws Exception{
        InputStream is = new FileInputStream("d:/test.xlsx");
        Map<MaterialType,List<Material>> map=getExcel(is);
        System.out.println(map.size());
        map.forEach((materialType, materials) ->{
            System.out.println("=============================================================");
            System.out.println(materialType.getName());
            for(Material material:materials){
                System.out.print(material.getChinese()+"\t");
                System.out.print(material.getEnglish()+"\t");
                System.out.println(material.getSpanish());
            }
        });
    }
}
