package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 管理账户表
 */
public class Admin {

    @Length(min = 2, max = 30, message = "账户名长度必须在 {min} - {max} 之间")
    private String username;
    @Length(min = 6, max = 20, message = "原密码长度必须在 {min} - {max} 之间")
    private String password;
    @Length(min = 1, max = 10, message = "姓名长度必须在 {min} - {max} 之间")
    private String name;//姓名
    private Integer role;//角色，1表示管理员，2表示普通用户
    private Date createTime;//创建时间

    public Admin() {
    }

    public Admin(String username) {
        this.username = username;
    }

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
