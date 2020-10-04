package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.action.backstage;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialType;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.JsonCode;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyWebException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.MaterialService;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.file.IoFile;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.file.MyFileOperator;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.file.MyFileUpload;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.opi.material.MaterialWriteForExcel;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员管理模块，用于整站的管理员管理
 */
@RestController
@RequestMapping(value = "/api/backstage/material")
public class MaterialManageController {

    @Autowired
    private MaterialService materialService;
    @Value("${myFile.uploadFolder}")
    private String uploadFolder;//注入系统默认的上传路径

    /**
     * 展示所有的物资列表，按照优先级升序排序
     * @param page 当前页
     * @param limit 每页记录数，如果为null则默认为20
     * @param materialQuery 查询条件
     */
    @GetMapping("/query")
    public Map<String, Object> manage(Integer page, Integer limit, MaterialQuery materialQuery,HttpServletRequest request){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";//获取项目根目录网址
        PageObject pageObject =materialService.query(page,limit,materialQuery,baseUrl);
        map.put(Constant.JSON_TOTAL,pageObject.getTotalRecords());
        map.put(Constant.JSON_DATA,pageObject.getList());
        return map;
    }

    /**
     * 根据标志符读取指定物资记录，如果为NULL表示不存在
     * @param id 指定物资的标志符
     * @return
     */
    @GetMapping("/{id}")
    public Map<String, Object>  get(@PathVariable Integer id,HttpServletRequest request){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";//获取项目根目录网址
        map.put(Constant.JSON_DATA,materialService.get(id,baseUrl));
        return map;
    }


    /*============================下面设计是只有管理员才能操作=================================*/

    /**
     * 批量删除物资记录，并删除对应的图片
     * @param ids 物资记录的标志符集合，前端传递格式：1,2,3
     */
    @DeleteMapping("/manage/{ids}")
    public Map<String, Object> deletes(@PathVariable("ids")Integer[] ids){
        Map<String, Object> map=new HashMap<String, Object>();
        materialService.deletes(ids);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

    /**
     * 添加物资
     * @param material
     */
    @PostMapping("/manage")
    public Map<String, Object> add(@RequestBody Material material){
        Map<String, Object> map=new HashMap<String, Object>();
        materialService.add(material);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }


    /**
     * 上传物资对应的图片
     * 1.如果上传失败，则删除已经上传成功的其他图片
     * @param id 指定物料的关键字
     * @param files 要上传的图片集合，如果没有图片则弹出异常
     * @return data里面会有回显的图片网址
     */
    @PostMapping("/manage/photo/{id}")
    public Map<String, Object> uploadPicture(@PathVariable Integer id, MultipartFile[] files, HttpServletRequest request) throws Exception{
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());//默认成功
        List<String> fileNames=new ArrayList<String>();//获取成功上传的图片文件名（实际上传到服务器后的文件名）
        if(files!=null&&files.length>0){
            String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";//获取项目根目录网址
            String basePath=uploadFolder;//注入系统默认的上传路径:例如保存在服务器C盘
            String fileUploadPath=basePath+Constant.MATERIAL_PHOTO_UPLOAD_PATH;//获取图片上传后保存的完整物理路径
            for(MultipartFile file:files){//遍历上传的图片
                Material material=materialService.get(id,baseUrl);//获取物料对象
                if(material!=null){//如果该物料存在，则执行上传
                    MyFileOperator.createDir(fileUploadPath);//创建存储目录
                    String fileName=file.getOriginalFilename();//获取文件名
                    String extensionName=MyFileOperator.getExtensionName(fileName);//获取文件扩展名
                    try {
                        MyFileUpload.validateExtByDir(extensionName,null);// 验证上传图片后缀名是否符合网站要求
                        fileName= material.getId()+"_"+IoFile.gainFileNameOfNewOfUUID(extensionName);//获取按JAVA的UUID规则生成的新文件名
                        file.transferTo(new File(fileUploadPath+fileName));
                        fileNames.add(fileName);//将文件名称存入
                    } catch (Exception e) {
                        map.put(Constant.JSON_MESSAGE, "上传失败："+e.getMessage());
                        map.put(Constant.JSON_CODE, JsonCode.ERROR.getValue());//失败状态码
                    }
                }
            }
            if(map.get(Constant.JSON_CODE)==JsonCode.SUCCESS.getValue()) {//如果图片保存成功
                try{
                    //将新的图片信息存入数据库
                    materialService.addMaterialPhotos(id,fileNames);
                }catch (Exception e) {
                    map.put(Constant.JSON_MESSAGE, "上传失败："+e.getMessage());
                    map.put(Constant.JSON_CODE, JsonCode.ERROR.getValue());//失败状态码
                }
            }
            //如果图片上传和数据库保存均成功，只要有一个失败就删除上传的图片
            if(map.get(Constant.JSON_CODE)==JsonCode.SUCCESS.getValue()){//如果保存成功
                map.put(Constant.JSON_MESSAGE, "上传成功上传"+fileNames.size()+"张图片");
                List<String> picUrls=new ArrayList<String>();
                for(String fileName:fileNames){//遍历上传成功的图片
                    picUrls.add(materialService.getPictureUrl(baseUrl,fileName));
                }
                map.put(Constant.JSON_DATA, picUrls);//将上传成功后的图片地址返回
            }else{//如果保存失败
                //删除已经上传的文件
                for(String fileName:fileNames){//遍历上传成功的图片
                    String filePath=fileUploadPath+fileName;//获取图片完整路径
                    MyFileOperator.deleteFile(filePath);//删除文件
                }
            }
        }else{
            throw new MyWebException("操作失败：请选择上传文件");
        }
        return map;
    }

    /**
     * 移除物资对应的图片
     * @param ids 指定物料图片的关键字集合
     */
    @DeleteMapping("/manage/photo/{ids}")
    public Map<String, Object> removePicture(@PathVariable Integer[] ids) throws Exception{
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        materialService.deleteMaterialPhotos(ids);
        return map;
    }



    /**
     * 上传物资表格，并导入到项目中
     * @param file 要上传的excel
     */
    @PostMapping("/manage/excel")
    public Map<String, Object> uploadExcel(MultipartFile file) throws Exception{
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.ERROR.getValue());//默认失败
        if(file!=null&&!file.isEmpty()){
            //String filePath=uploadFolder+Constant.MATERIAL_EXCEL_UPLOAD_PATH;//获取excel上传后保存的物理路径
            int number=materialService.addMaterialsFromExcel(file.getInputStream());
            map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
            map.put(Constant.JSON_DATA,number);
        }else{
            throw new MyWebException("操作失败：请选择上传文件");
        }
        return map;
    }

    /**
     * 将数据库中的物资记录以物资表格供前端下载
     */
    @GetMapping("/manage/excel")
    public String getTable5(HttpServletResponse response)  throws Exception{
        //读取数据库所有记录
        Map<MaterialType, List<Material>> map=materialService.getAllMaterials();
        //导出表格
        XSSFWorkbook book= MaterialWriteForExcel.writeExcel(map);//根据记录，生成excel表格
        //创建文件对象，导出
        this.outExcelStream(response,book,"物料翻译系统词库");
        return "SUCCESS";//这里其实就是随意返回一个字符串
    }

    /**
     * 通过流的方式输出excle到页面，每个文件都要下载
     * @param response 响应
     * @param workbook 表对象
     * @param fileName 文件名，下载时显示的文件名
     */
    private void outExcelStream(HttpServletResponse response, Workbook workbook, String fileName){
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xlsx");
            workbook.write(os);
            os.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
