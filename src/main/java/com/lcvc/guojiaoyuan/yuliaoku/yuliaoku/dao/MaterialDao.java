package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao;


import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao.base.IBaseDao;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialPhoto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//为了不让idea报错加上
public interface MaterialDao extends IBaseDao<Material> {

    /**
     * 物料翻译，专门用于前台翻译，
     * 说明：
     * 1.如果是中文查询，则按照模糊查询方式查找
     * 2.如果是英文查询
     * (1)如果用英文词组查询，则按照模糊查询
     * (2)如果用英文单词查询，按照空格（目前只支持一个空格）切割为多个单词，按照单词进行匹配
     * 3.如果是西文查询
     * (1)如果用西文词组查询，则按照模糊查询
     * (2)如果用西文单词查询，按照空格（目前只支持一个空格）切割为多个单词，按照单词进行匹配
     * 4.如果没有查询条件，则不返回任何词组
     * @return
     */
    int querySizeForTranslation(@Param(value = "objectQuery")Object objectQuery);

    /**
     * 物料翻译，专门用于前台
     * 说明：
     * 1.如果是中文查询，则按照模糊查询方式查找
     * 2.如果是英文查询，则将英文词组按照空格（目前只支持一个空格）切割为多个单词，按照单词进行匹配
     * 3.如果是西文查询，则将英文词组按照空格（目前只支持一个空格）切割为多个单词，按照单词进行匹配
     * 4.如果没有查询条件，则不返回任何词组
     * 5.排序将按照匹配程度的比例由大到小排序
     * @return
     */
    List<Material> queryForTranslation(@Param(value = "offset") final int offset, @Param(value = "length") final int length, @Param(value = "objectQuery") Object objectQuery);

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
