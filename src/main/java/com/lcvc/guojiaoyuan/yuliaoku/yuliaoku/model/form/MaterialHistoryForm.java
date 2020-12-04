package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 物料提议表单
 */
public class MaterialHistoryForm {
    @NotNull(message = "请输入物料提议标志符")
    private Integer materialHistoryId;

    @Length(min = 1, max = 50, message = "物资的中文名长度必须在 {min} - {max} 之间")
    private String chinese;//物料的中文名
    @Length(min = 1, max = 200, message = "物资的英文长度必须在 {min} - {max} 之间")
    private String english;//修订的物料英文名
    @Length(max = 200,message = "物资的西语长度不能超过 {max} ")
    private String spanish;//物料的西班牙语

    public Integer getMaterialHistoryId() {
        return materialHistoryId;
    }

    public void setMaterialHistoryId(Integer materialHistoryId) {
        this.materialHistoryId = materialHistoryId;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getSpanish() {
        return spanish;
    }

    public void setSpanish(String spanish) {
        this.spanish = spanish;
    }
}
