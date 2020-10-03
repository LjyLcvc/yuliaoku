package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.base.IBaseDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialPhoto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//为了不让idea报错加上
public interface MaterialDao extends IBaseDao<Material> {

    /**
     * 获取指定的物料图片
     * @param id
     * @return
     */
    MaterialPhoto getMaterialPhoto(int id);

    /**
     * 批量保存物料图片的名字到数据库中
     * @param meterialId 对应的物料
     * @param fileNames 文件集合
     * @return
     */
    int saveMaterialPhotos(int meterialId,List<String> fileNames);

    /**
     * 批量物料对应的图片数量
     * @param meterialId 对应的物料
     * @return
     */
    int getMaterialPhotosNumber(int meterialId);

    /**
     * 批量物料对应的图片集合
     * @param meterialId 对应的物料
     * @return
     */
    List<MaterialPhoto> getMaterialPhotos(int meterialId);

    /**
     * 删除物料对应的图片数量
     * @param meterialId 对应的物料
     * @return
     */
    int deleteMaterialPhotosByMaterial(int meterialId);

    /**
     * 根据物料图片标志符集合删除对应的记录信息集合，长度如果为0则不进行任何处理
     * @param ids id集合
     * @return  删除的记录数，>=1表示删除成功，0表示删除失败
     */
    int deleteMaterialPhotos(java.io.Serializable[] ids);


}
