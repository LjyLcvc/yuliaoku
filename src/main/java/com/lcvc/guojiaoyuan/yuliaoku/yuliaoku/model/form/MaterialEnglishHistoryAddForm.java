package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.form;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 物料提议添加表单
 */
public class MaterialEnglishHistoryAddForm {
    @NotNull(message = "请选择相应的物料")
    Integer materialId;
    @Length(min = 1, max = 200, message = "物资的英文长度必须在 {min} - {max} 之间")
    private String english;//修订的物料英文名

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
