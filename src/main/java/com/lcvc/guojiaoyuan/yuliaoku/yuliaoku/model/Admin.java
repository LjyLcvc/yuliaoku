package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model;

import java.util.Date;

/**
 * 管理账户表
 */
public class Admin {
    private String username;
    private String password;
    private String name;//姓名
    private Integer role;//角色，1表示超级管理员，2表示普通用户
    private Date createTime;//创建时间

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
