package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.AdminDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialEnglishHistoryDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialTypeDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Admin;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialEnglishHistory;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyServiceException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyWebException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialEnglishHistoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
@Validated//表示开启sprint的校检框架，会自动扫描方法里的@Valid（@Valid注解一般写在接口即可）
@Service
public class MaterialEnglishHistoryService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private MaterialTypeDao materialTypeDao;
    @Autowired
    private MaterialDao materialDao;
    @Autowired
    private MaterialEnglishHistoryDao materialEnglishHistoryDao;

    /**
     * 分页查询记录
     * @param page 当前页面
     * @param limit  每页最多显示的记录数
     * @param materialEnglishHistoryQuery 查询条件类
     */
    public PageObject query(Integer page, Integer limit, MaterialEnglishHistoryQuery materialEnglishHistoryQuery){
        PageObject pageObject = new PageObject(limit,page,materialEnglishHistoryDao.querySize(materialEnglishHistoryQuery));
        pageObject.setList(materialEnglishHistoryDao.query(pageObject.getOffset(),pageObject.getLimit(),materialEnglishHistoryQuery));
        return pageObject;
    }

    /**
     * 根据标志符读取指定记录
     * @param id
     * @return
     */
    public MaterialEnglishHistory get(@NotNull Integer id) {
        return materialEnglishHistoryDao.get(id);
    }


    /**
     * 批量删除指定记录
     * 说明：
     * 1.管理员可以直接删除
     * 2.普通用户
     * （1）只能删除自己创建的词条
     * （2）不能删除已经审核过的词条
     * @param admin 操作管理员
     * @param ids 多个记录的主键集合
     */
    public void deletes(Admin admin,Integer[] ids){
        if(admin.isSuperAdmin()){//如果是超级管理员，可以删除
            if(ids.length>0){//只有集合大于0才执行删除
                materialEnglishHistoryDao.deletes(ids);
            }
        }else{//如果不是管理员，则只能删除自己的操作记录
            for(Integer id:ids){
                //查询该记录是否是操作用户创建的
                MaterialEnglishHistory materialEnglishHistory=materialEnglishHistoryDao.get(id);
                if(materialEnglishHistory!=null){//如果该记录存在
                    if(!materialEnglishHistory.getOperator().equals(admin)){
                        throw new MyServiceException("操作失败：您不能删除别人的操作记录");
                    }
                    if(materialEnglishHistory.getAudit()!=null){
                        throw new MyServiceException("操作失败：已经审核通过的不允许删除");
                    }
                }
            }
            materialEnglishHistoryDao.deletes(ids);
        }
    }

    /**
     * 添加操作记录
     * 说明：
     * @param materialId 物料的主键，不能为NULL
     * @param english 英文，必须符合规则
     * @param operator 操作员，不能为NULL
     */
    public void add(@NotNull(message = "请选择相应的物料") Integer materialId, @NotBlank(message = "请输入相应的英语")String english, Admin operator){
        //前面必须经过spring验证框架的验证
        if(english.length()==0&&english.length()>50){
            throw new MyWebException("操作失败：物资英语的长度必须在1-50之间");
        }
        Material material=materialDao.get(materialId);//获取物料记录，判断是否存在
        if(material!=null){
            MaterialEnglishHistory materialEnglishHistory=new MaterialEnglishHistory();
            materialEnglishHistory.setMaterial(material);
            materialEnglishHistory.setOperator(operator);
            materialEnglishHistory.setEnglish(english);
            if(operator.isSuperAdmin()){//如果是管理员，则该物资默认已经审核
                materialEnglishHistory.setAuditor(operator);//审核者为自己
                materialEnglishHistory.setAudit(true);//审核通过
                materialEnglishHistory.setAuditTime(Calendar.getInstance().getTime());//审核时间
                //因为审核通过，所以直接将英文存储到物资表中
                material.setEnglish(english);
                materialDao.update(material);
            }
            materialEnglishHistoryDao.save(materialEnglishHistory);//保存到操作记录中
        }else{
            throw new MyWebException("操作失败：该物资不存在");
        }
    }

    /**
     * 修改自己的操作记录
     * 说明：
     * @param materialEnglishHistoryId 操作记录主键
     * @param materialId 物料主键，如果为NULL则不进行修改
     * @param english 英文，如果为NULL或空字符串，则不进行修改
     * @param operator 不能为NULL，如果不是操作记录的创建者，则不允许修改
     */
    public void updateByMyself(@NotNull(message = "缺少主键") Integer materialEnglishHistoryId,Integer materialId, String english, Admin operator){
        //前面必须经过spring验证框架的验证
        MaterialEnglishHistory materialEnglishHistory=materialEnglishHistoryDao.get(materialEnglishHistoryId);//读取原来的记录
        if(materialEnglishHistory!=null){
            //检查操作的是否是创建这
            if(!materialEnglishHistory.getOperator().equals(operator)){//如果不是操作者自己
                throw new MyWebException("操作失败：不能修改他人的操作记录");
            }
            //检查物资编号
            if(materialId!=null&&!materialEnglishHistory.getMaterial().getId().equals(materialId)){//如果物资编号存在且和原来的不同
                Material material=materialDao.get(materialId);//获取物料记录，判断是否存在
                if(material!=null){
                    materialEnglishHistory.setMaterial(material);
                }else{
                    throw new MyWebException("操作失败：该物资不存在");
                }
            }
            //检查英文
            if(StringUtils.isEmpty(english)){//如果不为空
                if(english.length()==0&&english.length()>50){
                    throw new MyWebException("操作失败：物资英语的长度必须在1-50之间");
                }
                if(english.equals(materialEnglishHistory.getEnglish())){//如果和原来的单词不同
                    materialEnglishHistory.setEnglish(english);
                }
            }
            materialEnglishHistoryDao.update(materialEnglishHistory);//保存到操作记录中
        }else{
            throw new MyWebException("操作失败：该物资操作记录不存在");
        }
    }

}
