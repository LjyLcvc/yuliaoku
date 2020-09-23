package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.action.backstage;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.JsonCode;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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



}
