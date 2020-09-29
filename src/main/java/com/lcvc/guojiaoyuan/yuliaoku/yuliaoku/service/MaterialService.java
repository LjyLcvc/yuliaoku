package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.AdminDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialEnglishHistoryDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialTypeDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialType;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyServiceException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.opi.material.MaterialReadFromExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
@Validated//表示开启sprint的校检框架，会自动扫描方法里的@Valid（@Valid注解一般写在接口即可）
@Service
public class MaterialService {
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


    /**
     * 读取物料的所有信息
     * 键值：物料类别-》物料类别下的所有物料信息。
     * @return
     */
    public Map<MaterialType, List<Material>> getAllMaterials (){
        Map<MaterialType, List<Material>> map=new LinkedHashMap<>();
        List<MaterialType> materialTypes=materialTypeDao.readAll(null);//读取所有的栏目
        for(MaterialType materialType:materialTypes){
            MaterialQuery materialQuery=new MaterialQuery();
            materialQuery.setMaterialType(materialType);
            List<Material> materials=materialDao.readAll(materialQuery);//读取指定类别的物料
            map.put(materialType,materials);
        }
        return map;
    }

    /**
     * 导入电子表格
     * @param inputStream
     * @return 返回成功导入的数量
     * @throws Exception
     */
    public int addMaterialsFromExcel(InputStream inputStream) throws Exception {
        AtomicInteger count = new AtomicInteger(0);//记录成功导入的记录数.lambda要用这个形式来处理
        //从上传的excel中得到表格的数据
        Map<MaterialType, List<Material>> map= MaterialReadFromExcel.getExcel(inputStream);
        if(map.size()>0){
            map.forEach((materialType, materials) ->{
                //对物资类别进行处理
                if(materialType.getId()!=null){//如果该物资类别已经存在
                    MaterialType materialTypeOfOrigin=materialTypeDao.get(materialType.getId());//判断该记录是否存在
                    if(materialTypeOfOrigin==null){
                        throw new MyServiceException("操作失败：工作表"+materialType.getName()+"在数据库不存在，请联系技术员核对");
                    }
                    if(!materialType.getName().equals(materialTypeOfOrigin.getName())){//如果名字已经变更
                        materialTypeOfOrigin.setName(materialTypeOfOrigin.getName());
                        materialTypeDao.update(materialType);//存储变更后的名字
                    }
                }else{//如果该物资类别不存在
                    materialType.setSort(100);//优先级默认100
                    materialTypeDao.save(materialType);
                }
                //对物资进行处理
                for(Material material:materials){
                    count.incrementAndGet();//计数增加
                    material.setMaterialType(materialType);//附上所属的物资类别
                }
                materialDao.saves(materials);
            });
        }
        return count.intValue();
    }

}
