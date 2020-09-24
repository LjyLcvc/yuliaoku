package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 用于保存物资的英文库操作记录
 *
 */
public class MaterialEnglishHistory {
    private Integer id;
    private Material material;//对应的物料表信息
    private Admin operator;//操作此次记录的人
    @Length(min = 1, max = 200, message = "物资的英文长度必须在 {min} - {max} 之间")
    private String english;//修订的物料英文名
    private Date createTime;//创建时间，该词条是否审核通过
    private Boolean audit;//表示该表单是否审核通过,true表示通过，false表示不通过
    private Admin auditor;//审计此次记录的人，如果是管理员自己编辑的词条则直接默认是审计
    private Date auditTime;//审核的时间

    public MaterialEnglishHistory() {
    }

    public MaterialEnglishHistory(Integer id) {
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

    public Admin getOperator() {
        return operator;
    }

    public void setOperator(Admin operator) {
        this.operator = operator;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
}
