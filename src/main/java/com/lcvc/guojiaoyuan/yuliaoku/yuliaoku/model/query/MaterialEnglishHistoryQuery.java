package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query;

import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialEnglishHistory;

/**
 * 物资名称
 */
public class MaterialEnglishHistoryQuery extends MaterialEnglishHistory {

    private Integer sortType;//排序类型：1表示发布时间升序，2表示发布时间降序

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }
}
