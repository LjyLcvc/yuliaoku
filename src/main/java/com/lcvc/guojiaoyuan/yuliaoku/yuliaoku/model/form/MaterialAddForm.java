package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.form;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 物料提议添加表单
 */
public class MaterialAddForm {
    @NotNull(message = "请选择相应的物料")
    Integer materialId;
    @NotNull(message = "请输入物料提议类型")
   /* @Range(min = 1, max = 3, message = "物资的提议类型数值范围必须在 {min} - {max} 之间")
    private Integer historyType;//表示提议记录的类型，记录修改的是谁。0表示修改所有字段（即修改所有），1表示中文提议，2表示英文提议，3表示西语提议*/
    @Length(min = 1, max = 200, message = "物资的英文长度必须在 {min} - {max} 之间")
    private String content;//物料提议的内容

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
