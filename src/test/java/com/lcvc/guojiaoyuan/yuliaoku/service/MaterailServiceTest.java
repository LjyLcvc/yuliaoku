package com.lcvc.guojiaoyuan.yuliaoku.service;

import com.lcvc.guojiaoyuan.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.model.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.InputStream;

public class MaterailServiceTest extends YuliaokuApplicationTests {
    @Autowired
    private MaterialService materialService;

    @Test
    public void testAddMaterialsFromExcel() throws Exception{
        InputStream is = new FileInputStream("d:/test.xlsx");
        materialService.addMaterialsFromExcel(is,new Admin("lcvc"));
    }

    @Test
    public void testRemoves(){
        Integer[] ids=new Integer[]{1,3};
        materialService.updateOfRemoves(ids,true);
    }
}
