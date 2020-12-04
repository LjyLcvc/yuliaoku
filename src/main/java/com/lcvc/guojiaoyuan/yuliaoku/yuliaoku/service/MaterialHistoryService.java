package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.AdminDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialHistoryDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.MaterialTypeDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Admin;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialHistory;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.base.PageObject;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyServiceException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.exception.MyWebException;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.form.MaterialHistoryForm;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialHistoryQuery;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.util.string.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
@Validated//表示开启sprint的校检框架，会自动扫描方法里的@Valid（@Valid注解一般写在接口即可）
@Service
public class MaterialHistoryService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private MaterialTypeDao materialTypeDao;
    @Autowired
    private MaterialDao materialDao;
    @Autowired
    private MaterialHistoryDao materialHistoryDao;

    /**
     * 分页查询记录
     * @param page 当前页面
     * @param limit  每页最多显示的记录数
     * @param materialHistoryQuery 查询条件类
     */
    public PageObject query(Integer page, Integer limit, MaterialHistoryQuery materialHistoryQuery){
        PageObject pageObject = new PageObject(limit,page, materialHistoryDao.querySize(materialHistoryQuery));
        pageObject.setList(materialHistoryDao.query(pageObject.getOffset(),pageObject.getLimit(),materialHistoryQuery));
        return pageObject;
    }

    /**
     * 根据标志符读取指定记录
     * @param id
     * @return
     */
    public MaterialHistory get(@NotNull Integer id) {
        return materialHistoryDao.get(id);
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
        for(Integer id:ids){
            //查询该记录是否是操作用户创建的
            MaterialHistory materialHistory = materialHistoryDao.get(id);
            if(materialHistory !=null){//如果该记录存在
                if(materialHistory.getAudit()!=null){
                    throw new MyServiceException("操作失败：已经审核通过的不允许删除");
                }
                if(!admin.isSuperAdmin()){//如果不是超级管理员，则只能删除自己的
                    if(!materialHistory.getOperator().equals(admin)){
                        throw new MyServiceException("操作失败：您不能删除别人的操作记录");
                    }
                }
            }
            materialHistoryDao.deletes(ids);
        }
    }

    /**
     * 审核物料提议，适合单条记录审核或是审核通过使用
     * 1.必须是管理员才能审核
     * 2.审核过的提议不能再次审核
     * 3.如果要提议的物料已经被移除或是未审核通过，则不允许审核通过
     * 4.审核通过提议后，根据提议的类型将内容更新到物料库
     * @param materialEnglishHistoryId
     * @param audit 审核状态
     * @param auditor 审核人
     */
    public void updateOfAudit(@NotNull(message = "缺少主键") Integer materialEnglishHistoryId,@NotNull(message = "必须填写审核结果") Boolean audit,Admin auditor){
        MaterialHistory materialHistory = materialHistoryDao.get(materialEnglishHistoryId);//读取原来的记录
        if(materialHistory !=null){
            if(!auditor.isSuperAdmin()){//只有管理员能够审核
                throw new MyWebException("操作失败：必须是管理员才能审核");
            }else if(materialHistory.getAudit()!=null){
                throw new MyServiceException("操作失败：已经审核过的提议不能再次审核");
            }else{
                materialHistory.setAuditor(auditor);//赋值审核管理员
                materialHistory.setAudit(audit);//赋值审核状态
                materialHistory.setAuditTime(Calendar.getInstance().getTime());//赋予审核时间
                if(materialHistory.getAudit()==true){//如果是true，则更改词库的记录
                    Material material=materialDao.get(materialHistory.getMaterial().getId());//获取原有记录
                    if(material!=null){
                        if(material.getAudit()==null||!material.getAudit()){
                            throw new MyWebException("操作失败：还未审核或是审核不通过的物料，不能审核提议");
                        }
                        if(material.getRemoveStatus()){
                            throw new MyWebException("操作失败：已经移除的物料不能审核提议");
                        }
                        if(materialHistory.getHistoryType()==0){//如果是要修改所有记录
                            material.setChinese(materialHistory.getChinese());
                            material.setEnglish(materialHistory.getEnglish());
                            material.setSpanish(materialHistory.getSpanish());
                        }else if(materialHistory.getHistoryType()==1){//如果是要修改中文
                            material.setChinese(materialHistory.getChinese());
                        }else if(materialHistory.getHistoryType()==2){//如果是要修改英文
                            material.setEnglish(materialHistory.getEnglish());
                        }else if(materialHistory.getHistoryType()==3){//如果是要修改西语
                            material.setSpanish(materialHistory.getSpanish());
                        }
                        materialDao.update(material);
                    }else{
                        throw new MyWebException("操作失败：该物料不存在");
                    }
                }
            }
            materialHistoryDao.update(materialHistory);//将审核状态保存
        }else{
            throw new MyWebException("操作失败：该物资提议不存在");
        }
    }


    /**
     * 批量将提议改为不通过状态
     * 说明：
     * 1.必须是管理员才可以操作
     * 2.已经审核过的帖子不允许再修改状态
     * @param auditor 操作管理员
     * @param ids 多个记录的主键集合
     */
    public void updateOfAuditRefuse(@NotNull Integer[] ids,Admin auditor){
        if(!auditor.isSuperAdmin()){//如果不是管理员，则不允许审核
            throw new MyWebException("操作失败：必须是管理员才能审核");
        }else{//如果满足审核条件
            for(Integer id:ids){
                //查询该记录的审核状态是否已经通过
                MaterialHistory materialHistory = materialHistoryDao.get(id);
                if(materialHistory !=null){//如果该记录存在
                    if(materialHistory.getAudit()!=null&& materialHistory.getAudit()){//如果已经通过审核
                        throw new MyServiceException("操作失败：已经审核通过的帖子不能再更改状态");
                    }
                }
            }
            materialHistoryDao.updateOfAuditRefuse(ids,auditor.getUsername(),Calendar.getInstance().getTime());
        }
    }

    /**
     * 添加提议
     * 说明：
     * 1.如果要提议的物料已经被移除或是未审核通过，则不允许添加提议
     * 2.如果是管理员添加，则直接通过审核，审核人就是自己
     * 3.如果是普通用户添加，则审核部分信息为空
     * @param materialId 物料的主键，不能为NULL
     * @param historyType 提议类型，只能1-3.
     * @param content 要更改的内容，必须符合规则,在录入后，将去掉头尾的空格，并且词组中间间隔最多一个空格
     * @param operator 操作员，不能为NULL
     */
    public void add(@NotNull(message = "请选择相应的物料") Integer materialId, @NotNull(message = "请选择要添加的提议类型") Integer historyType,@NotBlank(message = "请输入相应的英语")String content, Admin operator){
        //前面必须经过spring验证框架的验证
        Integer lengthLimit=0;//内容默认限制
        if(historyType==1){//中文提议
            lengthLimit=50;
        }else if(historyType==2||historyType==3){//英文提议
            lengthLimit=200;
        }else{//西语提议
            throw new MyWebException("操作失败：提议类型数值非法");
        }
        content=MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(content);//去掉头尾的空格，并且词组中间间隔最多一个空格
        if(content.length()==0&&content.length()>lengthLimit){
            throw new MyWebException("操作失败：物资的长度必须在1-"+lengthLimit+"之间");
        }
        Material material=materialDao.get(materialId);//获取物料记录，判断是否存在
        if(material!=null){
            if(material.getAudit()==null||!material.getAudit()){
                throw new MyWebException("操作失败：还未审核或是审核不通过的物料，不能添加提议");
            }if(material.getRemoveStatus()){
                throw new MyWebException("操作失败：已经移除的物料不能添加提议");
            }
            MaterialHistory materialHistory =new MaterialHistory();
            materialHistory.setMaterial(material);
            materialHistory.setOperator(operator);
            materialHistory.setChinese(material.getChinese());//设置原来的内容
            materialHistory.setEnglish(material.getEnglish());//设置原来的内容
            materialHistory.setSpanish(material.getSpanish());//设置原来的内容
            //将此次修改的内容放入提议中
            if(historyType==1){//中文提议
                materialHistory.setChinese(content);
            }else if(historyType==2){//英文提议
                materialHistory.setEnglish(content);
            }else if(historyType==3){//西语提议
                materialHistory.setSpanish(content);
            }
            if(operator.isSuperAdmin()){//如果是管理员，则该物资默认已经审核
                materialHistory.setAuditor(operator);//审核者为自己
                materialHistory.setAudit(true);//审核通过
                materialHistory.setAuditTime(Calendar.getInstance().getTime());//审核时间
                //因为审核通过，所以直接将相应内存存储到物资表中
                if(historyType==1){//中文提议
                    material.setChinese(content);
                }else if(historyType==2){//英文提议
                    material.setEnglish(content);
                }else if(historyType==3){//西语提议
                    material.setSpanish(content);
                }
                materialDao.update(material);//将此次提议的内容存入物料中进行更新
            }
            materialHistoryDao.save(materialHistory);//保存到提议中
        }else{
            throw new MyWebException("操作失败：该物资不存在");
        }
    }

    /**
     * 修改自己的提议
     * 说明：
     * 1.已经审核过的记录不能修改
     * 2.不能修改他人的记录
     * @param operator 不能为NULL，如果不是提议的创建者，则不允许修改
     */
    public void updateByMyself(@NotNull(message = "缺少表单对象")MaterialHistoryForm materialHistoryForm, Admin operator){
        //前面必须经过spring验证框架的验证
        MaterialHistory materialHistory = materialHistoryDao.get(materialHistoryForm.getMaterialHistoryId());//读取原来的提议记录
        if(materialHistory !=null){
            if(materialHistory.getAudit()!=null){
                throw new MyServiceException("操作失败：已经审核过的提议不能修改");
            }
            //检查操作的是否是创建者
            if(!materialHistory.getOperator().equals(operator)){//如果不是操作者自己
                throw new MyWebException("操作失败：不能修改他人的提议");
            }
            Material material=materialDao.get(materialHistory.getMaterial().getId());//获取物料记录，判断是否存在
            if(material.getAudit()==null||!material.getAudit()){
                throw new MyWebException("操作失败：还未审核或是审核不通过的物料，不能添加提议");
            }if(material.getRemoveStatus()){
                throw new MyWebException("操作失败：已经移除的物料不能添加提议");
            }
            if(materialHistory.getHistoryType()==0){//如果是要修改所有记录
                materialHistory.setChinese(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(materialHistoryForm.getChinese()));
                materialHistory.setEnglish(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(materialHistoryForm.getEnglish()));
                materialHistory.setSpanish(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(materialHistoryForm.getSpanish()));
            }else if(materialHistory.getHistoryType()==1){//如果是要修改中文
                materialHistory.setChinese(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(materialHistoryForm.getChinese()));
            }else if(materialHistory.getHistoryType()==2){//如果是要修改英文
                materialHistory.setEnglish(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(materialHistoryForm.getEnglish()));
            }else if(materialHistory.getHistoryType()==3){//如果是要修改西语
                materialHistory.setSpanish(MyStringUtil.trimBeginEndAndRetainOneSpaceInMiddle(materialHistoryForm.getSpanish()));
            }
            materialHistoryDao.update(materialHistory);//保存到提议中
        }else{
            throw new MyWebException("操作失败：该物资提议不存在");
        }
    }

}
