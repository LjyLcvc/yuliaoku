package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.action.backstage;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Admin;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.JsonCode;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.AdminQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员管理模块，用于整站的管理员管理
 */
@RestController
@RequestMapping(value = "/api/backstage/admin/manage")
public class AdminManageController {

    @Autowired
    private AdminService adminService;

    /**
     * 分页展示管理员列表
     * @param page 当前页
     * @param limit 每页记录数，如果为null则默认为20
     * @param adminQuery 查询条件
     */
    @GetMapping("/query")
    public Map<String, Object> adminManage(Integer page, Integer limit, AdminQuery adminQuery){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        PageObject pageObject =adminService.query(page,limit,adminQuery);
        map.put(Constant.JSON_TOTAL,pageObject.getTotalRecords());
        map.put(Constant.JSON_DATA,pageObject.getList());
        return map;
    }

    /**
     * 批量删除管理员
     * @param usernames 管理员的账户名，前端传递格式：user1,user2,user3
     */
    @DeleteMapping("/{usernames}")
    public Map<String, Object> deleteAdmins(@PathVariable("usernames")String[] usernames, HttpSession session){
        Map<String, Object> map=new HashMap<String, Object>();
        Admin admin=(Admin)session.getAttribute("admin");
        adminService.deletes(admin,usernames);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

    /**
     * 添加管理员
     * @param admin
     */
    @PostMapping
    public Map<String, Object> addAdmin(@RequestBody Admin admin){
        Map<String, Object> map=new HashMap<String, Object>();
        adminService.add(admin);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

    /**
     * 根据账户名读取指定账户信息，如果为NULL表示不存在
     * @param username 指定账户名
     * @return
     */
    @GetMapping("/{username}")
    public Map<String, Object>  getAdmin(@PathVariable String username){
        Map<String, Object> map=new HashMap<String, Object>();
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        map.put(Constant.JSON_DATA,adminService.get(username));
        return map;
    }

    /**
     * 编辑指定账户信息
     * @param user 编辑的管理账户信息
     * @return
     */
    @PutMapping
    public Map<String, Object> updateAdmin(@RequestBody Admin user, HttpSession session){
        Map<String, Object> map=new HashMap<String, Object>();
        Admin admin=(Admin)session.getAttribute("admin");//获取当前管理员
        adminService.update(admin,user);
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        return map;
    }

}
