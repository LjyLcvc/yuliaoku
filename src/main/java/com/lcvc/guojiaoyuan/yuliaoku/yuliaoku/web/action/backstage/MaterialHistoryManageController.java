package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.action.backstage;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Admin;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.JsonCode;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.form.MaterialAddForm;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialHistoryQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.MaterialHistoryService;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员管理模块，用于整站的管理员管理
 */
@RestController
@RequestMapping(value = "/api/backstage/materialHistory")
public class MaterialHistoryManageController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialHistoryService materialHistoryService;

    /**
     * 展示所有的物资操作记录列表，按照优先级升序排序
     */
    @GetMapping("/query")
    public Map<String, Object> manage(Integer page, Integer limit, MaterialHistoryQuery materialHistoryQuery){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        //加上条件，移除的物资不显示
        if(materialHistoryQuery==null){
            materialHistoryQuery=new MaterialHistoryQuery();//创建查询条件
        }
        //加上被移除物资的条件
        Material material=new Material();
        material.setRemoveStatus(false);
        materialHistoryQuery.setMaterial(material);
        PageObject pageObject = materialHistoryService.query(page,limit,materialHistoryQuery);
        map.put(Constant.JSON_TOTAL,pageObject.getTotalRecords());
        map.put(Constant.JSON_DATA,pageObject.getList());
        return map;
    }

    /**
     * 批量删除物资的提议记录
     * @param ids 物资记录的标志符集合，前端传递格式：1,2,3
     */
    @DeleteMapping("/{ids}")
    public Map<String, Object> deletes(@PathVariable("ids")Integer[] ids,HttpSession session){
        Map<String, Object> map=new HashMap<String, Object>();
        Admin admin=((Admin) session.getAttribute("admin"));
        materialHistoryService.deletes(admin,ids);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

    /**
     * 添加物料提议,用于添加指定物料的中文提议
     * @param materialAddForm 提议添加表单
     */
    @PostMapping("/chinese")
    public Map<String, Object> addChinese(@RequestBody @NotNull MaterialAddForm materialAddForm, HttpSession session){
        Map<String, Object> map=new HashMap<String, Object>();
        Admin admin=((Admin) session.getAttribute("admin"));
        materialHistoryService.add(materialAddForm.getMaterialId(),1,materialAddForm.getContent(),admin);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

    /**
     * 添加物料提议,用于添加指定物料的英文提议
     * @param materialAddForm 提议添加表单
     */
    @PostMapping("/english")
    public Map<String, Object> addEnglish(@RequestBody @NotNull MaterialAddForm materialAddForm, HttpSession session){
        Map<String, Object> map=new HashMap<String, Object>();
        Admin admin=((Admin) session.getAttribute("admin"));
        materialHistoryService.add(materialAddForm.getMaterialId(),2,materialAddForm.getContent(),admin);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }


    /**
     * 添加物料提议,用于添加指定物料的西文提议
     * @param materialAddForm 提议添加表单
     */
    @PostMapping("/spanish")
    public Map<String, Object> addSpanish(@RequestBody @NotNull MaterialAddForm materialAddForm, HttpSession session){
        Map<String, Object> map=new HashMap<String, Object>();
        Admin admin=((Admin) session.getAttribute("admin"));
        materialHistoryService.add(materialAddForm.getMaterialId(),3,materialAddForm.getContent(),admin);
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
        map.put(Constant.JSON_DATA, materialHistoryService.get(id));
        return map;
    }

    /*============================下面设计是只有管理员才能操作=================================*/

    /**
     * 审核指定提议
     * @param id 提议
     * @parm audit 审核状态
     * @return
     */
    @PutMapping("/manage/audit/{id}")
    public Map<String, Object>  updateOfAudit(@PathVariable Integer id,Boolean audit,HttpSession session){
        Map<String, Object> map=new HashMap<String, Object>();
        Admin admin=(Admin)session.getAttribute("admin");
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        materialHistoryService.updateOfAudit(id,audit,admin);
        return map;
    }

    /**
     * 批量拒绝指定的多个提议
     * @param ids 提议id集合
     * @return
     */
    @PutMapping("/manage/audit_refuse/{ids}")
    public Map<String, Object>  updateOfAudit(@PathVariable("ids")Integer[] ids,HttpSession session){
        Map<String, Object> map=new HashMap<String, Object>();
        Admin admin=(Admin)session.getAttribute("admin");
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        materialHistoryService.updateOfAuditRefuse(ids,admin);
        return map;
    }
}
