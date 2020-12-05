package com.lcvc.guojiaoyuan.yuliaoku.service;


import com.lcvc.guojiaoyuan.yuliaoku.dao.AdminDao;
import com.lcvc.guojiaoyuan.yuliaoku.dao.MaterialHistoryDao;
import com.lcvc.guojiaoyuan.yuliaoku.dao.MaterialTypeDao;
import com.lcvc.guojiaoyuan.yuliaoku.model.Admin;
import com.lcvc.guojiaoyuan.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.model.exception.MyServiceException;
import com.lcvc.guojiaoyuan.yuliaoku.model.exception.MyWebException;
import com.lcvc.guojiaoyuan.yuliaoku.model.query.AdminQuery;
import com.lcvc.guojiaoyuan.yuliaoku.model.query.MaterialHistoryQuery;
import com.lcvc.guojiaoyuan.yuliaoku.util.SHA;
import com.lcvc.guojiaoyuan.yuliaoku.dao.MaterialDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
@Validated//表示开启sprint的校检框架，会自动扫描方法里的@Valid（@Valid注解一般写在接口即可）
@Service
public class AdminService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private MaterialTypeDao materialTypeDao;
    @Autowired
    private MaterialDao materialDao;
    @Autowired
    private MaterialHistoryDao materialHistoryDao;

    /**
     * 判断是否是超级管理员
     * @return
     */
    public boolean isSuperAdmin(Admin admin){
        boolean judge=false;//默认不是
        if(admin!=null){
            if(admin.getRole()==1){
                judge=true;
            }
        }
        return judge;
    }
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
     * 根据账户名读取指定账户
     * @param username
     * @return
     */
    public Admin get(@NotNull String username) {
        Admin admin=null;
        if(username!=null){
            admin=adminDao.get(username);
        }
        return admin;
    }

    /**
     * 修改密码，用于个人修改
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

    /**
     * 分页查询管理员表
     * @param page 当前页面
     * @param limit  每页最多显示的记录数
     * @param adminQuery 查询条件类
     */
    public PageObject query(Integer page, Integer limit, AdminQuery adminQuery){
        PageObject pageObject = new PageObject(limit,page,adminDao.querySize(adminQuery));
        pageObject.setList(adminDao.query(pageObject.getOffset(),pageObject.getLimit(),adminQuery));
        return pageObject;
    }

    /**
     * 批量删除指定账户
     * 说明：
     * 1.不能自己删除自己
     * 2.如果该账户发表过物料提议，或是审核过物料提议，均不允许删除
     * 3.删除该账户的同时，移除相应的所有角色关系
     * @param adminOfOperator 执行删除的管理员账户
     * @param usernames 多个账户的主键集合
     */
    public void deletes(Admin adminOfOperator,String[] usernames){
        //先进行验证
        if(usernames.length>0){//只有集合大于0才执行删除
            for(String username:usernames){
                Admin admin=new Admin(username);
                if(adminOfOperator.getUsername().equals(username)) {//如果登录账户的主键与被删除账户的主键一致
                    throw new MyServiceException("删除失败：不允许删除自己的账户");
                }
                //获取被删除账户是否有物料提议记录，如果有不允许删除
                MaterialHistoryQuery materialEnglishHistoryQuery=new MaterialHistoryQuery();
                materialEnglishHistoryQuery.setOperator(admin);
                int numberOfOperator= materialHistoryDao.querySize(materialEnglishHistoryQuery);//获取要删除的记录
                if(numberOfOperator>0){
                    throw new MyServiceException("删除失败：账户"+username+"发布有"+numberOfOperator+"个提议");
                }
                //获取被删除账户是否有审核物料提议的记录，如果有不允许删除
                materialEnglishHistoryQuery=new MaterialHistoryQuery();
                materialEnglishHistoryQuery.setAuditor(admin);
                int numberOfAudit= materialHistoryDao.querySize(materialEnglishHistoryQuery);//获取要删除的记录
                if(numberOfAudit>0){
                    throw new MyServiceException("删除失败：账户"+username+"审核过"+numberOfAudit+"个提议");
                }
            }
            adminDao.deletes(usernames);
        }
    }

    /**
     *
     * 添加用户，用于后台添加，不用于注册
     * 说明：
     * 1.账户名、姓名、角色不能为空
     * 2.账户名不能重名
     * 3.密码默认是123456
     * @param admin
     */
    public void add(@Valid @NotNull Admin admin){
        //前面必须经过spring验证框架的验证
        if(admin!=null){
            if(admin.getUsername()==null){
                throw new MyWebException("账户添加失败：账户名不能为空");
            }
            /*if(admin.getPassword()==null){
                throw new MyWebException("账户添加失败：密码不能为空");
            }*/
            if(admin.getName()==null){
                throw new MyWebException("账户添加失败：姓名不能为空");
            }
            Integer role=admin.getRole();
            if(role==null){//角色信息不能为空
                throw new MyWebException("账户添加失败：请选择用户身份");
            }else{
                if(role>=3||role<=0){
                    throw new MyWebException("账户添加失败：用户身份只能是普通用户或是管理员");
                }
            }
            //查询该账户是否已经在数据库存在
            AdminQuery adminQuery=new AdminQuery(admin.getUsername());
            if(adminDao.querySize(adminQuery)==0){//如果该用户在数据库中不存在，即可以添加
                admin.setPassword(SHA.getResult("123456"));//将密码加密
                admin.setCreateTime(Calendar.getInstance().getTime());//获取当前时间为创建时间
                adminDao.save(admin);
            }else{
                throw new MyServiceException("账户添加失败：账户名重名");
            }
        }else{
            throw new MyWebException("账户添加失败：表单没有传值到服务端");
        }
    }

    /**
     *
     * 编辑用户，用于个人账户编辑
     * 说明：
     * 1.账户名不能为空
     * 2.密码字段，角色信息不修改
     * @param admin
     */
    public void update(@Valid @NotNull Admin admin) {
        //前面必须经过spring验证框架的验证
        if (admin.getUsername() == null) {
            throw new MyWebException("账户编辑失败：账户名不能为空");
        }
        Admin adminEdit = adminDao.get(admin.getUsername());//获取编辑账户的原始信息
        if(adminEdit!=null){
            Integer role = admin.getRole();
            if (role != null) {//如果前台传递了该字段
                if (role >= 3 || role <= 0) {
                    throw new MyWebException("账户编辑失败：用户身份只能是普通用户或是管理员");
                }
            }
            admin.setPassword(null);//不修改密码字段
        }else{
            throw new MyWebException("账户编辑失败：该账户不存在");
        }
        adminDao.update(admin);
    }

    /**
     *
     * 编辑用户
     * 说明：
     * 1.账户名不能为空
     * 2.密码字段不修改
     * @param adminOfOperator 操作的账户，后期改为在过滤器统一验证
     * @param admin 被操作的账户
     */
    public void update(@NotNull Admin adminOfOperator,@Valid @NotNull Admin admin) {
        //前面必须经过spring验证框架的验证
        if (admin.getUsername() == null) {
            throw new MyWebException("账户编辑失败：账户名不能为空");
        }
        Admin adminEdit = adminDao.get(admin.getUsername());//获取编辑账户的原始信息
        if(adminEdit!=null){
            Integer role = admin.getRole();
            if (role != null) {//如果前台传递了该字段
                if (role >= 3 || role <= 0) {
                    throw new MyWebException("账户编辑失败：用户身份只能是普通用户或是管理员");
                }
            }
            admin.setPassword(null);//不修改密码字段
            adminDao.update(admin);//编辑账户
        }else{
            throw new MyWebException("账户编辑失败：该账户不存在");
        }
    }

}
