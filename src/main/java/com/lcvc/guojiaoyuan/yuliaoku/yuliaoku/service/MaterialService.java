package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.AdminDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialHistoryDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialTypeDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.*;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.Constant;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyServiceException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyWebException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialHistoryQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.file.MyFileOperator;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.opi.material.MaterialReadFromExcel;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.string.MyStringUtil;
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
import java.util.*;
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
    private MaterialHistoryDao materialHistoryDao;

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
        MaterialHistoryQuery materialEnglishHistoryQuery=new MaterialHistoryQuery();
        materialEnglishHistoryQuery.setMaterial(material);
        material.setMaterialEnglishHistoryNumber(materialHistoryDao.querySize(materialEnglishHistoryQuery));
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
     * 设置物料的关联属性，用于前台翻译
     * @param material 必须有数据库该表的完整字段
     * @param baseUrl 项目根目录网址，用于图片地址处理
     */
    private void setMaterialTranslationParam(Material material,String baseUrl){
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
     * 分页查询记录，专用于前台翻译
     * @param page 当前页面
     * @param limit  每页最多显示的记录数
     * @param materialQuery 查询条件类
     * @param baseUrl 项目根目录网址，用于图片地址处理
     */
    public PageObject queryForTranslation(Integer page, Integer limit, MaterialQuery materialQuery,String baseUrl){
        if(materialQuery!=null){
            if(materialQuery.getChinese()!=null){
                materialQuery.setChinese(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(materialQuery.getChinese()));//清除前后空格，并保持中间空格最多一个
            }
            if(materialQuery.getEnglish()!=null){
                materialQuery.setEnglish(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(materialQuery.getEnglish()));//清除前后空格，并保持中间空格最多一个
            }
            if(materialQuery.getSpanish()!=null){
                materialQuery.setSpanish(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(materialQuery.getSpanish()));//清除前后空格，并保持中间空格最多一个
            }
        }
        PageObject pageObject = new PageObject(limit,page,materialDao.querySizeForTranslation(materialQuery));
        List<Material> materials=materialDao.queryForTranslation(pageObject.getOffset(),pageObject.getLimit(),materialQuery);
        pageObject.setList(materials);
        for(Material material:materials){
            this.setMaterialTranslationParam(material,baseUrl);//设置关联属性
        }
        return pageObject;
    }

    /**
     * 分页查询记录，查询所有
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
     * 根据标志符读取指定记录(被移除的物资不显示)
     * @param id
     * @param baseUrl 项目根目录网址，用于图片地址处理
     * @return
     */
    public Material get(@NotNull Integer id,String baseUrl) {
        Material material=materialDao.get(id);
        if(material!=null){
            if(!material.getRemoveStatus()){
                this.setMaterialParam(material,baseUrl);//设置关联属性
            }else{
                throw new MyServiceException("操作失败：该物资已经被移除");
            }

        }
        return materialDao.get(id);
    }

    /**
     * 批量更改物料的所属栏目
     * 说明：
     * @param ids 多个记录的主键集合
     * @param materailTypeId 要变更的物料类别
     */
    public void updatesOfMaterialType(Integer[] ids,@NotNull(message = "必须填写要变更的物料类别")Integer materailTypeId){
        //先进行验证
        if(ids.length>0){//只有集合大于0才执行删除
            MaterialType materialType=materialTypeDao.get(materailTypeId);//读取栏目
            if(materialType!=null){//如果该栏目存在
                materialDao.updatesOfMaterialType(ids,materailTypeId);
            }else{
                throw new MyServiceException("操作失败：要变更的物料类别不存在");
            }
        }
    }

    /**
     * 批量更改物料的逻辑删除指状态
     * 说明：逻辑删除，该物料对应的图片和物料提议保留
     * @param ids 多个记录的主键集合
     */
    public void updateOfRemoves(Integer[] ids,@NotNull(message = "必须填写要更改的物料移除状态") Boolean removeStatus){
        //先进行验证
        if(ids.length>0){//只有集合大于0才执行删除
            materialDao.updateOfRemoves(ids,removeStatus);//逻辑删除物料集合
        }
    }


    /**
     * 批量删除指定记录
     * 说明：
     * 1.如果该物资下有提议，则一起删除
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
                materialHistoryDao.deleteByMaterial(id);
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
     * 2.中文、英文、西文都将进行处理，保证前后没有空格，词组的间隔只有最多一个空格
     * 3.添加词库的同时，物料提议也添加进去
     * @param material
     */
    public void add(@Valid @NotNull(message = "表单没有传值到服务端") Material material, @NotNull(message = "清先登陆")Admin operator){
        //前面必须经过spring验证框架的验证
        if(material.getChinese()!=null){
            material.setChinese(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(material.getChinese()));//清除前后空格，并保持中间空格最多一个
        }
        if(material.getEnglish()!=null){
            material.setEnglish(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(material.getEnglish()));//清除前后空格，并保持中间空格最多一个
        }
        if(material.getSpanish()!=null){
            material.setSpanish(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(material.getSpanish()));//清除前后空格，并保持中间空格最多一个
        }
        if(operator.isSuperAdmin()) {//如果是管理员，则该物资默认已经审核
            material.setAuditor(operator);//审核者为自己
            material.setAudit(true);//审核通过
            material.setAuditTime(Calendar.getInstance().getTime());//审核时间
        }
        materialDao.save(material);//保存物料到数据库
        /**
         * 进行处理，将物料提议作为该词库的第一次记录提交
         */
        MaterialHistory materialHistory =new MaterialHistory();
        materialHistory.setMaterial(material);
        materialHistory.setOperator(operator);
        materialHistory.setHistoryType(0);//设置为全修改
        materialHistory.setChinese(material.getChinese());
        materialHistory.setEnglish(material.getEnglish());
        materialHistory.setSpanish(material.getSpanish());
        if(operator.isSuperAdmin()){//如果是管理员，则该物资默认已经审核
            materialHistory.setAuditor(operator);//审核者为自己
            materialHistory.setAudit(true);//审核通过
            materialHistory.setAuditTime(Calendar.getInstance().getTime());//审核时间
        }
        materialHistoryDao.save(materialHistory);//记录该次物料提议
    }

     /**
     * 批量将词库改为指定的审核状态
     * 说明：
     * 1.必须是管理员才可以操作
     * 2.已经审核通过的物资不允许再修改状态
      *3.已经被移除的物资不允许修改
      *4.如果物料最终审核通过，则原来的提议也审核通过
     * @param auditor 操作管理员
     * @param ids 多个记录的主键集合
     */
    public void updatesOfAudit(@NotNull Integer[] ids,Admin auditor,@NotNull(message = "必须填写审核结果") Boolean audit){
        List<MaterialHistory> materialHistories=new ArrayList<MaterialHistory>();//要审核的物料提议数（在创建词库时产生）
        if(!auditor.isSuperAdmin()){//如果不是管理员，则不允许审核
            throw new MyWebException("操作失败：必须是管理员才能审核");
        }else{//如果满足审核条件
            for(Integer id:ids){
                //查询该记录的审核状态是否已经通过
                Material material = materialDao.get(id);//读取原来的记录
                if(material !=null){//如果该记录存在
                    if(material.getAudit()!=null&& material.getAudit()){//如果该词库已经审核通过
                        throw new MyServiceException("操作失败："+material.getChinese()+"已经审核通过。审核通过的物料不能再更改状态。如果不需要该词库可以选择移除");
                    }
                    if(material.getRemoveStatus()){//如果该物资已经被移除
                        throw new MyServiceException("操作失败："+material.getChinese()+"已经被移除，无法更改审核状态");
                    }
                    //获取物料对应的日志
                    MaterialHistoryQuery materialHistoryQuery=new MaterialHistoryQuery();
                    materialHistoryQuery.setMaterial(material);
                    materialHistories.addAll(materialHistoryDao.readAll(materialHistoryQuery));//虽然是查询集合，但按照业务规则，一个词库未审核前只有一个相关提议
                }
            }
            materialDao.updatesOfAudit(ids,auditor.getUsername(),Calendar.getInstance().getTime(),audit);//批量修改审核结果
            //将物料提议批量改为与物料相同的审批状态
            if(materialHistories.size()>0){
                Integer[] materialHistorieIds=new Integer[materialHistories.size()];
                int i=0;
                for(MaterialHistory materialHistory:materialHistories){
                    materialHistory.setAuditor(auditor);//审核者为自己
                    materialHistory.setAudit(audit);//审核结果
                    materialHistory.setAuditTime(Calendar.getInstance().getTime());//审核时间
                    materialHistorieIds[i++]=materialHistory.getId();
                }
                materialHistoryDao.updateOfAudit(materialHistorieIds,auditor.getUsername(),audit,Calendar.getInstance().getTime());//批量修改物料提议
            }
        }
    }

    /**
     *
     * 编辑记录
     * 说明：
     * 1.物料标志符不能为空
     * 2.只能修改所属物料类别
     * @param material
     */
  /*  public void update(@Valid @NotNull(message = "表单没有传值到服务端") Material material){
        //前面必须经过spring验证框架的验证
        if(material.getId()==null){
            throw new MyServiceException("操作失败：物料标志符不能为空");
        }
       *//* if(material.getChinese()!=null){
            material.setChinese(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(material.getChinese()));//清除前后空格，并保持中间空格最多一个
        }*//*
        //下述字段不允许修改
        material.setEnglish(null);
        material.setSpanish(null);
        materialDao.update(material);
    }*/

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
     * 读取物料的所有信息（不包括被移除的物资）
     * 键值：物料类别-》物料类别下的所有物料信息。
     * @return
     */
    public Map<MaterialType, List<Material>> getAllMaterials (){
        Map<MaterialType, List<Material>> map=new LinkedHashMap<>();
        List<MaterialType> materialTypes=materialTypeDao.readAll(null);//读取所有的栏目
        for(MaterialType materialType:materialTypes){
            MaterialQuery materialQuery=new MaterialQuery();
            materialQuery.setRemoveStatus(false);//加上条件，移除的物资不显示
            materialQuery.setMaterialType(materialType);
            List<Material> materials=materialDao.readAll(materialQuery);//读取指定类别的物料
            map.put(materialType,materials);
        }
        return map;
    }

    /**
     * 导入电子表格
     * 1.导入的新词将导入系统中
     * 2.导入的新词也将作为提议存入系统中
     * @param inputStream
     * @param operator 操作的管理员
     * @return 返回成功导入的数量
     * @throws Exception
     */
    public int addMaterialsFromExcel(InputStream inputStream,Admin operator) throws Exception {
        AtomicInteger count = new AtomicInteger(0);//记录成功导入的记录数.lambda要用这个形式来处理
        //从上传的excel中得到表格的数据
        Map<MaterialType, List<Material>> map= MaterialReadFromExcel.getExcel(inputStream);
        List<MaterialHistory> materialHistories=new ArrayList<MaterialHistory>();//创建提议历史
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
                for(Material material:materials){//将词库遍历为提议
                    /**
                     * 进行处理，将物料提议作为该词库的第一次记录提交
                     */
                    MaterialHistory materialHistory =new MaterialHistory();
                    materialHistory.setMaterial(material);
                    materialHistory.setOperator(operator);
                    materialHistory.setHistoryType(0);//设置为全修改
                    materialHistory.setChinese(material.getChinese());
                    materialHistory.setEnglish(material.getEnglish());
                    materialHistory.setSpanish(material.getSpanish());
                    if(operator.isSuperAdmin()){//如果是管理员，则该物资默认已经审核
                        materialHistory.setAuditor(operator);//审核者为自己
                        materialHistory.setAudit(true);//审核通过
                        materialHistory.setAuditTime(Calendar.getInstance().getTime());//审核时间
                    }
                    materialHistories.add(materialHistory);
                }
            });
        }
        materialHistoryDao.saves(materialHistories);//存储提议日志
        return count.intValue();
    }

}
