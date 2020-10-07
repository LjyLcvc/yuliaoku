package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao;

import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialPhoto;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MaterailDaoTest extends YuliaokuApplicationTests {
    @Autowired
    private MaterialDao materialDao;

    @Test
    public void testReadAll(){
        List<Material> list=materialDao.readAll(null);
        for(Material material:list){
            System.out.print(material.getMaterialType().getName()+"\t");
            System.out.print(material.getChinese()+"\t");
            System.out.println(material.getEnglish());
        }
    }

    @Test
    public void testGetMaterialPhotos(){
        List<MaterialPhoto> list=materialDao.getMaterialPhotos(1);
        for(MaterialPhoto materialPhoto:list){
            System.out.println(materialPhoto.getPicUrl());
        }
    }

    @Test
    public void testQuerySizeForTranslation(){
        int number=materialDao.querySizeForTranslation(null);
        System.out.println(number);
        MaterialQuery materialQuery=new MaterialQuery();
        materialQuery.setChinese("轮胎");
        materialQuery.setQueryType(1);
        System.out.println(materialDao.querySizeForTranslation(materialQuery));
    }

    @Test
    public void testQueryForTranslation(){
        MaterialQuery materialQuery=new MaterialQuery();
        materialQuery.setQueryType(2);
        materialQuery.setEnglish("getting");
        List<Material> list=materialDao.queryForTranslation(0,5,materialQuery);
        for(Material material:list){
            System.out.print(material.getMaterialType().getName()+"\t");
            System.out.print(material.getChinese()+"\t");
            System.out.println(material.getEnglish());
        }
    }
}
