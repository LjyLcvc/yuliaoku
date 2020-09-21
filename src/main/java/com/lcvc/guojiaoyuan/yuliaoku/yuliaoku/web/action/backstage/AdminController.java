package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.web.action.backstage;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Admin;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.JsonCode;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.form.AdminPasswordEditForm;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/backstage/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PatchMapping("/password")
    public Map<String, Object> updatePassword(@RequestBody @Validated AdminPasswordEditForm adminPasswordEditForm, HttpSession session) {
        Admin admin=((Admin) session.getAttribute("admin"));
        Map<String, Object> map = new HashMap<String, Object>();
        adminService.updatePassword(admin.getUsername(),adminPasswordEditForm.getPassword(),adminPasswordEditForm.getNewPass(),adminPasswordEditForm.getRePass());
        map.put(Constant.JSON_CODE, JsonCode.SUCCESS.getValue());
        map.put(Constant.JSON_MESSAGE, "密码修改成功");
        return map;
    }

}
