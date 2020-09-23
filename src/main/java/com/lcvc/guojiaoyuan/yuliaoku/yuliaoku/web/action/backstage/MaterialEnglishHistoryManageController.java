package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.action.backstage;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Admin;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.JsonCode;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialEnglishHistoryQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.MaterialEnglishHistoryService;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员管理模块，用于整站的管理员管理
 */
@RestController
@RequestMapping(value = "/api/backstage/materialenglishhistory")
public class MaterialEnglishHistoryManageController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialEnglishHistoryService materialEnglishHistoryService;

    /**
     * 展示所有的物资操作记录列表，按照优先级升序排序
     */
    @GetMapping("/query")
    public Map<String, Object> manage(Integer page, Integer limit, MaterialEnglishHistoryQuery materialEnglishHistoryQuery){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        PageObject pageObject =materialEnglishHistoryService.query(page,limit,materialEnglishHistoryQuery);
        map.put(Constant.JSON_TOTAL,pageObject.getTotalRecords());
        map.put(Constant.JSON_DATA,pageObject.getList());
        return map;
    }

    /**
     * 批量删除物资的操作记录
     * @param ids 物资记录的标志符集合，前端传递格式：1,2,3
     */
    @DeleteMapping("/{ids}")
    public Map<String, Object> deletes(@PathVariable("ids")Integer[] ids,HttpSession session){
        Map<String, Object> map=new HashMap<String, Object>();
        Admin admin=((Admin) session.getAttribute("admin"));
        materialEnglishHistoryService.deletes(admin,ids);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

    /**
     * 添加物资操作记录
     * @param materialId 物资id
     * @param english 英文
     */
    @PostMapping
    public Map<String, Object> add(Integer materialId, String english, HttpSession session){
        Map<String, Object> map=new HashMap<String, Object>();
        Admin admin=((Admin) session.getAttribute("admin"));
        materialEnglishHistoryService.add(materialId,english,admin);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

    /**
     * 根据标志符读取指定物资操作记录，如果为NULL表示不存在
     * @param id 指定物资的标志符
     * @return
     */
    @GetMapping("/{id}")
    public Map<String, Object>  get(@PathVariable Integer id){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        map.put(Constant.JSON_DATA,materialEnglishHistoryService.get(id));
        return map;
    }

    /*============================下面设计是只有管理员才能操作=================================*/


}
