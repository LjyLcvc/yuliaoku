package com.lcvc.guojiaoyuan.yuliaoku.util.opi.material;

import com.lcvc.guojiaoyuan.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.model.MaterialType;
import com.lcvc.guojiaoyuan.yuliaoku.model.base.ExcelException;
import com.lcvc.guojiaoyuan.yuliaoku.model.exception.MyExcelException;
import com.lcvc.guojiaoyuan.yuliaoku.util.opi.base.MethorOfPOI;
import com.lcvc.guojiaoyuan.yuliaoku.util.string.MyStringUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 从Excel中读取物资记录
 */
public class MaterialReadFromExcel {

    /**
     * 从输入流中读取excel的内容
     * 说明：
     * 1.已经有id的记录不进行读取
     * 2.中文、英文、西文都将进行处理，保证前后没有空格，词组的间隔只有最多一个空格
     * @param inputStream
     * @return
     */
    public static Map<MaterialType,List<Material>> getExcel(InputStream inputStream) throws Exception{
        Map<MaterialType,List<Material>> map= new LinkedHashMap<MaterialType,List<Material>>();
        List<ExcelException> excelExceptions=new ArrayList<ExcelException>();//定义异常集合
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
                Material material=new Material();
                XSSFRow row = sheet.getRow(rowNum);//获取指定的行
                String idString= MethorOfPOI.getValue(row.getCell(0));//第一列是id，序号
                if(!StringUtils.isEmpty(idString)){//如果不为空
                    try {
                        Integer.parseInt(idString);//转化为数字
                        continue;//如果转换成功，则终止此次循环，即该条记录不导入
                    } catch (NumberFormatException e) {
                        excelExceptions.add(new ExcelException(name,rowNum+1,1,idString,"序号必须是整数"));
                        //throw new MyServiceException("操作失败：导入的表格的工作表（"+name+"）第"+(rowNum+1)+"行的序号列必须是整数");
                    }
                }
                String chinese=MethorOfPOI.getValue(row.getCell(1));//第2列是中文名
                if(StringUtils.isEmpty(chinese)){//如果中文名不存在，则说明该表格没有下一行。因为通过getLastRowNum可能因电子表格不一致导致不准确
                    break;
                }
                chinese= MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(chinese);//清除前后空格，并保持中间空格最多一个
                if(chinese.length()>50){
                    excelExceptions.add(new ExcelException(name,rowNum+1,2,chinese,"中文长度不能超过50个字符"));
                    //throw new MyServiceException("操作失败：导入的表格的工作簿（"+name+"）第"+(rowNum+1)+"行的中文列长度超过50个字符");
                }else{//如果验证通过
                    material.setChinese(chinese);//设置中文名
                }
                String english=MethorOfPOI.getValue(row.getCell(2));
                if(!StringUtils.isEmpty(english)){//如果不为空
                    english=MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(english);//清除前后空格，并保持中间空格最多一个
                    if(english.length()>200){
                        excelExceptions.add(new ExcelException(name,rowNum+1,3,english,"英文长度不能超过200个字符"));
                        //throw new MyServiceException("操作失败：导入的表格的工作簿（"+name+"）第"+(rowNum+1)+"行的英文文列长度超过200个字符");
                    }else{//如果验证通过
                        material.setEnglish(english);
                    }
                }
                String spanish=MethorOfPOI.getValue(row.getCell(3));
                if(!StringUtils.isEmpty(spanish)){//如果不为空
                    spanish=MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(spanish);//清除前后空格，并保持中间空格最多一个
                    if(spanish.length()>200){
                        excelExceptions.add(new ExcelException(name,rowNum+1,4,spanish,"西文长度不能超过200个字符"));
                        //throw new MyServiceException("操作失败：导入的表格的工作簿（"+name+"）第"+(rowNum+1)+"行的西文文列长度超过200个字符");
                    }else{//如果验证通过
                        material.setSpanish(spanish);
                    }
                }
                if(material.getChinese()!=null||material.getEnglish()!=null||material.getSpanish()!=null){//如果该行验证通过
                    materials.add(material);//将该记录添加到集合中
                }
            }
        }
        if(excelExceptions.size()>0){//如果存在异常
            throw new MyExcelException(excelExceptions);
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
