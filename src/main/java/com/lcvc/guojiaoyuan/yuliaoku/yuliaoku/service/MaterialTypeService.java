package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.AdminDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialTypeDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialType;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyServiceException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyWebException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
@Validated//表示开启sprint的校检框架，会自动扫描方法里的@Valid（@Valid注解一般写在接口即可）
@Service
public class MaterialTypeService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private MaterialTypeDao materialTypeDao;
    @Autowired
    private MaterialDao materialDao;

    public List<MaterialType> queryAll(){
        List<MaterialType> list=materialTypeDao.readAll(null);
        for(MaterialType materialType:list){
            //获取每个类别下的物资数量
            MaterialQuery materialQuery=new MaterialQuery();
            materialQuery.setMaterialType(materialType);
            materialType.setMaterialNumber(materialDao.querySize(materialQuery));
        }
        return list;
    }

    /**
     * 根据标志符读取指定记录
     * @param id
     * @return
     */
    public MaterialType get(@NotNull Integer id) {
        return materialTypeDao.get(id);
    }


    /**
     * 批量删除指定记录
     * 说明：
     * 1.如果该栏目下有物资，则不允许删除
     * @param ids 多个记录的主键集合
     */
    public void deletes(Integer[] ids){
        //先进行验证
        if(ids.length>0){//只有集合大于0才执行删除
            for(Integer id:ids){
                //检查该栏目下是否有物资，如果有则不允许删除
                MaterialQuery materialQuery=new MaterialQuery();
                materialQuery.setMaterialType(new MaterialType(id));
                int number=materialDao.querySize(materialQuery);//获取该类别下的物资总数
                if(number>0){//如果该类别下有物资
                    MaterialType materialType=materialTypeDao.get(id);//读取该类别记录
                    throw new MyServiceException("操作失败：物资类别（"+materialType.getName()+")下还有"+number+"个物资");
                }
            }
            materialTypeDao.deletes(ids);
        }
    }

    /**
     *
     * 添加记录
     * 说明：
     * 1.名称和排序属性均不能为空
     * @param materialType
     */
    public void add(@Valid @NotNull(message = "表单没有传值到服务端") MaterialType materialType){
        //前面必须经过spring验证框架的验证
        if(materialType.getSort()==null){//优先级单独验证
            throw new MyWebException("操作失败：物料的排序参数不能为空");
        }
        materialTypeDao.save(materialType);
    }

    /**
     *
     * 编辑记录
     * 说明：
     * 1.参数的标志符不能为空
     * @param materialType
     */
    public void update(@Valid @NotNull(message = "表单没有传值到服务端") MaterialType materialType) {
        //前面必须经过spring验证框架的验证
        if(materialType.getId()!=null){
            MaterialType materialTypeEdit = materialTypeDao.get(materialType.getId());//获取操作账户的原始信息
            if(materialTypeEdit!=null){
                materialTypeDao.update(materialType);//将操作后的数据保存到数据库
            }else{
                throw new MyWebException("操作失败：该记录不存在");
            }
        }else{
            throw new MyWebException("操作失败：没有传递标志符");
        }
    }

}
