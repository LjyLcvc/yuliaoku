package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query;

import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;

/**
 * 物资名称
 */
public class MaterialQuery extends Material {
    private Integer queryType;//1表示中文翻译成英文/西语；2表示英语翻译成中文/西语；3表示西语翻译成中文/英语

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }
}
