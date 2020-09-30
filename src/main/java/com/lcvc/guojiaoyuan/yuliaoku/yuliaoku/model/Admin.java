package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.Objects;

/**
 * 管理账户表
 */
public class Admin implements java.io.Serializable{

    @Length(min = 2, max = 20, message = "账户名长度必须在 {min} - {max} 之间")
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

    //获取当前账户的密码
    @JsonIgnore
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(username, admin.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * 判断是否是超级管理员
     * @return
     */
    public boolean isSuperAdmin(){
        boolean judge=false;//默认不是
        if(this.role==1){
            judge=true;
        }
        return judge;
    }
}
