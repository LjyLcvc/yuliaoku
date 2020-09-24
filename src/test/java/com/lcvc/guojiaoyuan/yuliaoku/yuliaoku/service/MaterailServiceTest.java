package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service;

import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.YuliaokuApplicationTests;
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
        materialService.addMaterialsFromExcel(is);
    }
}
