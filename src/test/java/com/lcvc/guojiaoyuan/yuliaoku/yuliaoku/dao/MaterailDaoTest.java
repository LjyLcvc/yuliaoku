package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao;

import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
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
}
