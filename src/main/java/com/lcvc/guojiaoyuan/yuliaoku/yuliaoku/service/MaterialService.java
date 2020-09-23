package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.AdminDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialEnglishHistoryDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
@Validated//表示开启sprint的校检框架，会自动扫描方法里的@Valid（@Valid注解一般写在接口即可）
@Service
public class MaterialService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private MaterialDao materialDao;
    @Autowired
    private MaterialEnglishHistoryDao materialEnglishHistoryDao;

    /**
     * 分页查询记录
     * @param page 当前页面
     * @param limit  每页最多显示的记录数
     * @param materialQuery 查询条件类
     */
    public PageObject query(Integer page, Integer limit, MaterialQuery materialQuery){
        PageObject pageObject = new PageObject(limit,page,materialDao.querySize(materialQuery));
        pageObject.setList(materialDao.query(pageObject.getOffset(),pageObject.getLimit(),materialQuery));
        return pageObject;
    }

    /**
     * 根据标志符读取指定记录
     * @param id
     * @return
     */
    public Material get(@NotNull Integer id) {
        return materialDao.get(id);
    }


    /**
     * 批量删除指定记录
     * 说明：
     * 1.如果该物资下有操作记录，则一起删除
     * @param ids 多个记录的主键集合
     */
    public void deletes(Integer[] ids){
        //先进行验证
        if(ids.length>0){//只有集合大于0才执行删除
            for(Integer id:ids){
                //检查该物资下是否有操作记录，如果有则删除所有操作记录
                materialEnglishHistoryDao.deleteByMaterial(id);
            }
            materialDao.deletes(ids);
        }
    }

    /**
     *
     * 添加记录
     * 说明：
     * 1.名称和排序属性均不能为空
     * @param material
     */
    public void add(@Valid @NotNull(message = "表单没有传值到服务端") Material material){
        //前面必须经过spring验证框架的验证
        materialDao.save(material);
    }

}
