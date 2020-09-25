package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.action.frontdesk;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/frontdesk/material_translation")
public class MaterialTranslationController {

    @Autowired
    private MaterialService materialService;

    /**
     * 中译英
     * @param page
     * @param limit
     * @param chinese
     * @return
     */
    @GetMapping(value = "/chineseToEngLish")
    public Map<String, Object> chineseToEngLish( Integer page, Integer limit,@NotBlank(message = "必须输入中文")String chinese){
        Map<String, Object> map=new HashMap<String, Object>();
        MaterialQuery materialQuery=new MaterialQuery();//创建查询条件
        materialQuery.setChinese(chinese);
        PageObject pageObject =materialService.query(page,limit,materialQuery);
        map.put(Constant.JSON_TOTAL,pageObject.getTotalRecords());
        map.put(Constant.JSON_DATA,pageObject.getList());
        return map;
    }


    /**
     * 英译中
     * @param page
     * @param limit
     * @param english
     * @return
     */
    @GetMapping(value = "/engLishToChinese")
    public Map<String, Object> engLishToChinese(Integer page, Integer limit,@NotBlank(message = "必须输入英文")String english){
        Map<String, Object> map=new HashMap<String, Object>();
        MaterialQuery materialQuery=new MaterialQuery();//创建查询条件
        materialQuery.setEnglish(english);
        PageObject pageObject =materialService.query(page,limit,materialQuery);
        map.put(Constant.JSON_TOTAL,pageObject.getTotalRecords());
        map.put(Constant.JSON_DATA,pageObject.getList());
        return map;
    }
}
