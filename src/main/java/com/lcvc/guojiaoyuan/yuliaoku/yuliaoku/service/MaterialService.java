package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.AdminDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialEnglishHistoryDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialTypeDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialPhoto;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialType;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyServiceException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialEnglishHistoryQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.file.MyFileOperator;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.opi.material.MaterialReadFromExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
@Validated//表示开启sprint的校检框架，会自动扫描方法里的@Valid（@Valid注解一般写在接口即可）
@Service
public class MaterialService {
    @Value("${myFile.uploadFolder}")
    private String uploadFolder;//注入系统默认的上传路径
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private MaterialTypeDao materialTypeDao;
    @Autowired
    private MaterialDao materialDao;
    @Autowired
    private MaterialEnglishHistoryDao materialEnglishHistoryDao;

    /**
     * 获取图片上传到服务器后的完整物理路径，可用于文件的操作，如删除
     * @param filename 图片的文件名
     * @return
     */
    public String getPicturePath(String filename){
        String fileUploadPath=uploadFolder+Constant.MATERIAL_PHOTO_UPLOAD_PATH;//获取图片上传后保存的完整物理路径
        return fileUploadPath+filename;
    }

    /**
     * 获取图片上传到服务器后的完整网址，用于图片的显示
     * @param filename 图片的文件名
     * @param baseUrl 项目根目录网址，用于图片地址处理
     * @return
     */
    public String getPictureUrl(String baseUrl,String filename){
        return baseUrl+Constant.MATERIAL_PHOTO_UPLOAD_URL+filename;
    }

    /**
     * 设置物料的关联属性
     * @param material 必须有数据库该表的完整字段
     * @param baseUrl 项目根目录网址，用于图片地址处理
     */
    private void setMaterialParam(Material material,String baseUrl){
        /**
         * 获取物料对应的提议数量
         */
        MaterialEnglishHistoryQuery materialEnglishHistoryQuery=new MaterialEnglishHistoryQuery();
        materialEnglishHistoryQuery.setMaterial(material);
        material.setMaterialEnglishHistoryNumber(materialEnglishHistoryDao.querySize(materialEnglishHistoryQuery));
        /**
         *  获取物料对应的图片集合
         */
        List<MaterialPhoto> materialPhotos=materialDao.getMaterialPhotos(material.getId());//获取数据库里的数据集合
        //设置图片的网址
        for(MaterialPhoto materialPhoto:materialPhotos){
            materialPhoto.setPicUrl(getPictureUrl(baseUrl,materialPhoto.getPicUrl()));
        }
        material.setMaterialPhotos(materialPhotos);
    }

    /**
     * 分页查询记录
     * @param page 当前页面
     * @param limit  每页最多显示的记录数
     * @param materialQuery 查询条件类
     * @param baseUrl 项目根目录网址，用于图片地址处理
     */
    public PageObject query(Integer page, Integer limit, MaterialQuery materialQuery,String baseUrl){
        PageObject pageObject = new PageObject(limit,page,materialDao.querySize(materialQuery));
        List<Material> materials=materialDao.query(pageObject.getOffset(),pageObject.getLimit(),materialQuery);
        pageObject.setList(materials);
        for(Material material:materials){
            this.setMaterialParam(material,baseUrl);//设置关联属性
        }
        return pageObject;
    }

    /**
     * 根据标志符读取指定记录
     * @param id
     * @param baseUrl 项目根目录网址，用于图片地址处理
     * @return
     */
    public Material get(@NotNull Integer id,String baseUrl) {
        Material material=materialDao.get(id);
        if(material!=null){
            this.setMaterialParam(material,baseUrl);//设置关联属性
        }

        return materialDao.get(id);
    }


    /**
     * 批量删除指定记录
     * 说明：
     * 1.如果该物资下有操作记录，则一起删除
     * 2.如果该物料有图片，则将数据库记录和相应的图片一起删除
     * @param ids 多个记录的主键集合
     */
    public void deletes(Integer[] ids){
        //先进行验证
        if(ids.length>0){//只有集合大于0才执行删除
            List<MaterialPhoto> materialPhotos=new ArrayList<MaterialPhoto>();//获取物料图片集合，用于后续批量删除
            for(Integer id:ids){
                /**
                 * 检查该物资下是否有操作记录，如果有则删除所有操作记录
                 */
                materialEnglishHistoryDao.deleteByMaterial(id);
                /**
                 * 检查该物资是否有图片，如果有则删除所有图片
                 */
                materialPhotos.addAll(materialDao.getMaterialPhotos(id)); //获取物料对应的图片集合
                //先删除数据库中的记录（因为有事务），后面再删除图片
                materialDao.deleteMaterialPhotosByMaterial(id);
            }
            materialDao.deletes(ids);//删除物料集合
            /**
             * 删除物料对应的所有图片
             */
            for(MaterialPhoto materialPhoto:materialPhotos){//最后删除图片
                MyFileOperator.deleteFile(getPicturePath(materialPhoto.getPicUrl()));//删除文件
            }
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
     *
     * 编辑记录
     * 说明：
     * 1.物料标志符不能为空
     * 2.只能修改所属物料类别和物料中文名
     * @param material
     */
    public void update(@Valid @NotNull(message = "表单没有传值到服务端") Material material){
        //前面必须经过spring验证框架的验证
        if(material.getId()==null){
            throw new MyServiceException("操作失败：物料标志符不能为空");
        }
        //下述字段不允许修改
        material.setEnglish(null);
        material.setSpanish(null);
        materialDao.update(material);
    }

    /**
     *
     * 添加物料照片的集合
     * 说明：
     * 1.名称和排序属性均不能为空
     * @param materialId
     * @param fileNames 文件名集合
     */
    public void addMaterialPhotos(@Valid @NotNull(message = "必须选择要对应的物料") Integer materialId,@NotEmpty List<String> fileNames){
        //前面必须经过spring验证框架的验证
        if(fileNames.size()>Constant.MATERIAL_PHOTO_UPLOAD_NUMBER){
            throw new MyServiceException("操作失败：上传的图片数量不能超过"+Constant.MATERIAL_PHOTO_UPLOAD_NUMBER+"张");
        }
        int numberOrigin=materialDao.getMaterialPhotosNumber(materialId);//获取该物料在数据库原有的图片数量
        if(numberOrigin+fileNames.size()>Constant.MATERIAL_PHOTO_UPLOAD_NUMBER){
            throw new MyServiceException("操作失败：该物料已经拥有"+numberOrigin+"张图片，每个物料能拥有的图片数量不能超过"+Constant.MATERIAL_PHOTO_UPLOAD_NUMBER+"张");
        }
        materialDao.saveMaterialPhotos(materialId,fileNames);
    }

    /**
     * 批量删除指定的照片集合
     * 说明：连同图片文件一起删除
     * @param ids 多个记录的主键集合
     */
    public void deleteMaterialPhotos(@NotEmpty(message = "请选择要删除的图片")Integer[] ids){
        //先进行验证
        if(ids.length>0){//只有集合大于0才执行删除
            List<MaterialPhoto> materialPhotos=new ArrayList<MaterialPhoto>();//获取物料图片集合，用于后续批量删除
            for(Integer id:ids){
                MaterialPhoto materialPhoto=materialDao.getMaterialPhoto(id);//获取对应的图片对象
                if(materialPhoto!=null){//如果记录存在
                    materialPhotos.add(materialDao.getMaterialPhoto(id));
                }
            }
            materialDao.deleteMaterialPhotos(ids);//删除物料图片集合
            /**
             * 删除物料对应的所有图片
             */
            String fileUploadPath=uploadFolder+Constant.MATERIAL_PHOTO_UPLOAD_PATH;//获取图片上传后保存的完整物理路径
            for(MaterialPhoto materialPhoto:materialPhotos){//最后删除图片
                MyFileOperator.deleteFile(fileUploadPath+materialPhoto.getPicUrl());//删除文件
            }
        }
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
