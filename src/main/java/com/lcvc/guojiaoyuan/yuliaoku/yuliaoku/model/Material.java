package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
    @Length(max = 200, message = "物资的英文长度不能超过 {max} ")
    private String english;//物料的英文名
    @Length(max = 200,message = "物资的西语长度不能超过 {max} ")
    private String spanish;//物料的西班牙语
    private Boolean audit;//表示该表单是否审核通过,true表示通过，false表示不通过
    private Admin auditor;//审计此次记录的人，如果是管理员自己编辑的词条则直接默认是审计
    private Date auditTime;//审核的时间
    private Boolean removeStatus;//逻辑删除，true表示被删除

    //非数据库字段
    private List<MaterialPhoto> materialPhotos;//物料对应的图片集合
    private Integer materialEnglishHistoryNumber;//物料对应的英语提议数量


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

    public List<MaterialPhoto> getMaterialPhotos() {
        return materialPhotos;
    }

    public void setMaterialPhotos(List<MaterialPhoto> materialPhotos) {
        this.materialPhotos = materialPhotos;
    }

    public Integer getMaterialEnglishHistoryNumber() {
        return materialEnglishHistoryNumber;
    }

    public void setMaterialEnglishHistoryNumber(Integer materialEnglishHistoryNumber) {
        this.materialEnglishHistoryNumber = materialEnglishHistoryNumber;
    }

    public Boolean getAudit() {
        return audit;
    }

    public void setAudit(Boolean audit) {
        this.audit = audit;
    }

    public Admin getAuditor() {
        return auditor;
    }

    public void setAuditor(Admin auditor) {
        this.auditor = auditor;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Boolean getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(Boolean removeStatus) {
        this.removeStatus = removeStatus;
    }
}
