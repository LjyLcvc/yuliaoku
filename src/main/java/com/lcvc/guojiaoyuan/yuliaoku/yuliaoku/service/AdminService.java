package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.AdminDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Admin;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyServiceException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyWebException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
@Validated//表示开启sprint的校检框架，会自动扫描方法里的@Valid（@Valid注解一般写在接口即可）
@Service
public class AdminService {
    @Autowired
    private AdminDao adminDao;
    /**
     * 登录方法
     *
     * @param username 账户名，不能为空
     * @param password 密码，不能为空
     * @return true表示登陆成功，false表示登陆失败
     */
    public boolean login(String username, String password){
        boolean judge=false;
        if(StringUtils.isEmpty(username)){
            throw new MyWebException("登陆失败：账户名不能为空");
        }else  if(StringUtils.isEmpty(password)){
            throw new MyWebException("登陆失败：密码不能为空");
        }else{
            if(adminDao.login(username, SHA.getResult(password))==1){
                judge=true;
            }
        }
        return judge;
    }

    /**
     * 根据账户名读取指定标识符
     * @param username
     * @return
     */
    public Admin getAdmin(@NotNull String username) {
        Admin admin=null;
        if(username!=null){
            admin=adminDao.get(username);
        }
        return admin;
    }

    /**
     * 修改密码
     * 说明：
     * 1.本方法不对原密码、新密码和确认密码的规则进行验证，请在web层验证后再传入
     * @param username 必填
     * @param password 必填
     * @param newPass 必填
     * @param rePass 必填
     */
    public void updatePassword(String username,String password,String newPass,String rePass){
        //在web层已对密码字段进行验证
        if(!newPass.equals(rePass)){
            throw new MyWebException("密码修改失败：确认密码与新密码必须相同");
        }
        if(this.login(username, password)){//说明原密码正确
            Admin admin=adminDao.get(username);
            admin.setPassword(SHA.getResult(newPass));
            adminDao.update(admin);
        }else{
            throw new MyServiceException("密码修改失败：原密码错误");
        }
    }

}
