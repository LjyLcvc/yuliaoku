package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.base.IBaseDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Admin;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository//为了不让idea报错加上
public interface AdminDao extends IBaseDao<Admin> {

    /**
     * 处理数据库的登录方法
     * @param username 账户名
     * @param password 密码
     * @return 返回匹配的账户总数
     */
    @Select("SELECT count(1) FROM admin where username=#{username} and password=#{password}")
    int login(String username, String password);
}
