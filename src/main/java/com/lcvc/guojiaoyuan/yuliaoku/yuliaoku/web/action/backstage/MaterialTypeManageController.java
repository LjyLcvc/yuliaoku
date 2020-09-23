package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.action.backstage;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialType;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.JsonCode;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.MaterialTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员管理模块，用于整站的管理员管理
 */
@RestController
@RequestMapping(value = "/api/backstage/materialtype")
public class MaterialTypeManageController {

    @Autowired
    private MaterialTypeService materialTypeService;

    /**
     * 展示所有的物资类别列表，按照优先级升序排序
     */
    @GetMapping("/all")
    public Map<String, Object> materialtypeManage(){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        map.put(Constant.JSON_DATA,materialTypeService.queryAll());
        return map;
    }

    /**
     * 批量删除物资类别记录
     * @param ids 物资类别记录的标志符集合，前端传递格式：1,2,3
     */
    @DeleteMapping("/{ids}")
    public Map<String, Object> deleteMaterialTypes(@PathVariable("ids")Integer[] ids){
        Map<String, Object> map=new HashMap<String, Object>();
        materialTypeService.deletes(ids);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

    /**
     * 添加物资类别
     * @param materialType
     */
    @PostMapping
    public Map<String, Object> addMaterialType(@RequestBody MaterialType materialType){
        Map<String, Object> map=new HashMap<String, Object>();
        materialTypeService.add(materialType);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

    /**
     * 根据标志符读取指定物资类别记录，如果为NULL表示不存在
     * @param id 指定物资类别的标志符
     * @return
     */
    @GetMapping("/{id}")
    public Map<String, Object>  getMaterialType(@PathVariable Integer id){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        map.put(Constant.JSON_DATA,materialTypeService.get(id));
        return map;
    }

    /**
     * 编辑指定物资类别的信息
     * @param materialType 编辑指定的物资类别的信息
     * @return
     */
    @PutMapping
    public Map<String, Object> updateMaterialType(@RequestBody MaterialType materialType){
        Map<String, Object> map=new HashMap<String, Object>();
        materialTypeService.update(materialType);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

}
