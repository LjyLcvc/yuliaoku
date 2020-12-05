package com.lcvc.guojiaoyuan.yuliaoku.model;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 用于保存物资的英文库操作记录
 *
 */
public class MaterialPhoto {
    private Integer id;
    private Material material;//对应的物料表信息
    @Length(min = 1, max = 200, message = "物资的英文长度必须在 {min} - {max} 之间")
    private String picUrl;//图片地址
    private Date createTime;//创建时间

    public MaterialPhoto() {
    }

    public MaterialPhoto(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
