package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用于保存物资的词汇提议
 *
 */
public class MaterialHistory {
    private Integer id;
    private Material material;//对应的物料表信息
    private Admin operator;//操作此次记录的人
    private Integer historyType;//表示提议记录的类型，记录修改的是谁。0表示修改所有字段（即修改所有），1表示中文提议，2表示英文提议，3表示西语提议
    @NotNull(message = "物资的中文名不能为空")
    @Length(min = 1, max = 50, message = "物资的中文名长度必须在 {min} - {max} 之间")
    private String chinese;//物料的中文名
    @Length(min = 1, max = 200, message = "物资的英文长度必须在 {min} - {max} 之间")
    private String english;//修订的物料英文名
    @Length(max = 200,message = "物资的西语长度不能超过 {max} ")
    private String spanish;//物料的西班牙语
    private Date createTime;//创建时间
    private Boolean audit;//表示该表单是否审核通过,true表示通过，false表示不通过
    private Admin auditor;//审计此次记录的人，如果是管理员自己编辑的词条则直接默认是审计
    private Date auditTime;//审核的时间

    public MaterialHistory() {
    }

    public MaterialHistory(Integer id) {
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

    public Integer getHistoryType() {
        return historyType;
    }

    public void setHistoryType(Integer historyType) {
        this.historyType = historyType;
    }
}
