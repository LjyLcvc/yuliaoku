package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.action.backstage;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.JsonCode;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyWebException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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
    public Map<String, Object> manage(Integer page, Integer limit, MaterialQuery materialQuery){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        PageObject pageObject =materialService.query(page,limit,materialQuery);
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
    public Map<String, Object>  getMaterialType(@PathVariable Integer id){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        map.put(Constant.JSON_DATA,materialService.get(id));
        return map;
    }


    /*============================下面设计是只有管理员才能操作=================================*/

    /**
     * 批量删除物资记录
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
     * 上传物资表格，并导入到项目中
     * @param file 要上传的excel
     */
    @PostMapping("/manage/excel")
    public Map<String, Object> uploadExcel(MultipartFile file) throws Exception{
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.ERROR.getValue());//默认失败
        if(file!=null&&!file.isEmpty()){
            //String filePath=uploadFolder+Constant.MATERIAL_EXCEL_UPLOAD_PATH;//获取excel上传后保存的物理路径
            materialService.addMaterialsFromExcel(file.getInputStream());
            map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        }else{
            throw new MyWebException("操作失败：请选择上传文件");
        }
        return map;
    }




}
