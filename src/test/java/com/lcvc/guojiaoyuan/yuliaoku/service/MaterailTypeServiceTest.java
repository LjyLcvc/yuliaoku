package com.lcvc.guojiaoyuan.yuliaoku.service;

import com.lcvc.guojiaoyuan.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.model.MaterialType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MaterailTypeServiceTest extends YuliaokuApplicationTests {
    @Autowired
    private MaterialTypeService materialTypeService;

    @Test
    public void testAdd(){
        MaterialType materialType=new MaterialType();
        materialType.setName("测试1");
        materialType.setSort(10);
        materialTypeService.add(materialType);
    }
}
