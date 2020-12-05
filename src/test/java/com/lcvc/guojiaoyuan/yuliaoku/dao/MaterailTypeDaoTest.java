package com.lcvc.guojiaoyuan.yuliaoku.dao;

import com.lcvc.guojiaoyuan.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.model.MaterialType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MaterailTypeDaoTest extends YuliaokuApplicationTests {
    @Autowired
    private MaterialTypeDao materialTypeDao;

    @Test
    public void testReadAll(){
        List<MaterialType> list=materialTypeDao.readAll(null);
        for(MaterialType materialType:list){
            System.out.println(materialType.getName());
        }
    }
}
