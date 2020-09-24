package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 物资类别
 */
public class MaterialType {
    private Integer id;
    @NotNull(message = "物资类别名称不能为空")
    @Length(min = 1, max = 30, message = "物资类别名称的长度必须在 {min} - {max} 之间")
    private String name;//物资类别名称，用于工作子表，分类管理
    //@NotNull(message = "物料的排序参数不能为空")
    private Integer sort;//排序，升序W

    public MaterialType() {
    }

    public MaterialType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
