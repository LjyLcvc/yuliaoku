package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.action.frontdesk;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/frontdesk/material_translation")
public class MaterialTranslationController {

    @Autowired
    private MaterialService materialService;

    /**
     * 获取项目根目录的绝对网址
     * @param request
     * @return
     */
    private String getBaseUrl(HttpServletRequest request){
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";//获取项目根目录网址
    }

    /**
     * 中文翻译
     * @param page
     * @param limit
     * @param chinese
     * @return
     */
    @GetMapping(value = "/chinese")
    public Map<String, Object> chinese(Integer page, Integer limit, @NotNull(message = "必须输入中文")String chinese, HttpServletRequest request){
        Map<String, Object> map=new HashMap<String, Object>();
        MaterialQuery materialQuery=new MaterialQuery();//创建查询条件
        materialQuery.setAudit(true);
        materialQuery.setRemoveStatus(false);
        materialQuery.setQueryType(1);
        materialQuery.setChinese(chinese);
        PageObject pageObject =materialService.queryForTranslation(page,limit,materialQuery,getBaseUrl(request));
        map.put(Constant.JSON_TOTAL,pageObject.getTotalRecords());
        map.put(Constant.JSON_DATA,pageObject.getList());
        return map;
    }


    /**
     * 英文翻译
     * @param page
     * @param limit
     * @param english
     * @return
     */
    @GetMapping(value = "/english")
    public Map<String, Object> engLish(Integer page, Integer limit,@NotBlank(message = "必须输入英文")String english,HttpServletRequest request){
        Map<String, Object> map=new HashMap<String, Object>();
        MaterialQuery materialQuery=new MaterialQuery();//创建查询条件
        materialQuery.setAudit(true);
        materialQuery.setRemoveStatus(false);
        materialQuery.setQueryType(2);
        materialQuery.setEnglish(english);
        PageObject pageObject =materialService.queryForTranslation(page,limit,materialQuery,getBaseUrl(request));
        map.put(Constant.JSON_TOTAL,pageObject.getTotalRecords());
        map.put(Constant.JSON_DATA,pageObject.getList());
        return map;
    }

    /**
     * 西文翻译
     * @param page
     * @param limit
     * @param spanish
     * @return
     */
    @GetMapping(value = "/spanish")
    public Map<String, Object> engLishToChinese(Integer page, Integer limit,@NotBlank(message = "必须输入西文")String spanish,HttpServletRequest request){
        Map<String, Object> map=new HashMap<String, Object>();
        MaterialQuery materialQuery=new MaterialQuery();//创建查询条件
        materialQuery.setAudit(true);
        materialQuery.setRemoveStatus(false);
        materialQuery.setQueryType(3);
        materialQuery.setSpanish(spanish);
        PageObject pageObject =materialService.queryForTranslation(page,limit,materialQuery,getBaseUrl(request));
        map.put(Constant.JSON_TOTAL,pageObject.getTotalRecords());
        map.put(Constant.JSON_DATA,pageObject.getList());
        return map;
    }
}
