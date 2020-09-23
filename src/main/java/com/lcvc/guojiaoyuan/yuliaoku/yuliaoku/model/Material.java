package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 物资名称
 */
public class Material {
    private Integer id;
    @NotNull(message = "物资类别不能为空")
    private MaterialType materialType;//该物资所属的类别
    @NotNull(message = "物资的中文名不能为空")
    @Length(min = 1, max = 50, message = "物资的中文名长度必须在 {min} - {max} 之间")
    private String chinese;//物料的中文名
    @Length(max = 50, message = "物资的英文长度不能超过 {max} ")
    private String english;//物料的英文名
    @Length(max = 50,message = "物资的西语长度不能超过 {max} ")
    private String spanish;//物料的西班牙语


    public Material() {
    }

    public Material(Integer id) {
        this.id = id;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getSpanish() {
        return spanish;
    }

    public void setSpanish(String spanish) {
        this.spanish = spanish;
    }
}
