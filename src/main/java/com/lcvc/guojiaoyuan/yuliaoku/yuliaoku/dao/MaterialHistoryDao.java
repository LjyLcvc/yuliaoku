package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.base.IBaseDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialHistory;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 词库提议的数据层
 */
@Repository//为了不让idea报错加上
public interface MaterialHistoryDao extends IBaseDao<MaterialHistory> {

    /**
     * 删除物资id下的所有操作记录
     * @param materialId
     * @return
     */
    int deleteByMaterial(int materialId);

    /**
     * 根据标志符集合批量将对应的提议改为审核不通过状态，长度如果为0则不进行任何处理
     * @param ids id集合
     * @param auditorId 审核人
     * @param auditTime 审核时间
     * @return  删除的记录数，>=1表示删除成功，0表示删除失败
     */
    int updateOfAuditRefuse(java.io.Serializable[] ids, java.io.Serializable auditorId, Date auditTime);

    /**
     * 根据标志符集合批量将对应的提议进行批量修改，长度如果为0则不进行任何处理
     * @param ids id集合
     * @param auditorId 审核人
     * @param auditTime 审核时间
     * @return  删除的记录数，>=1表示删除成功，0表示删除失败
     */
    int updateOfAudit(java.io.Serializable[] ids, java.io.Serializable auditorId,boolean audit, Date auditTime);
}
