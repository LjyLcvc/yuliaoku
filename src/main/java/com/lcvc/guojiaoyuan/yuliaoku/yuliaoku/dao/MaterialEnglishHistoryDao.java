package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.base.IBaseDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialEnglishHistory;
import org.springframework.stereotype.Repository;

@Repository//为了不让idea报错加上
public interface MaterialEnglishHistoryDao extends IBaseDao<MaterialEnglishHistory> {

    /**
     * 删除物资id下的所有操作记录
     * @param materialId
     * @return
     */
    int deleteByMaterial(int materialId);
}
