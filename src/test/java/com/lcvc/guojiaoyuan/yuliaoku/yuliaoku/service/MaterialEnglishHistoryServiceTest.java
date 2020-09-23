package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.service;

import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialEnglishHistory;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialEnglishHistoryQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MaterialEnglishHistoryServiceTest extends YuliaokuApplicationTests {
    @Autowired
    private MaterialEnglishHistoryService materialEnglishHistoryService;

    @Test
    public void testQuery(){
        MaterialEnglishHistoryQuery materialEnglishHistoryQuery=new MaterialEnglishHistoryQuery();
        materialEnglishHistoryQuery.setMaterial(new Material(2));
        List<MaterialEnglishHistory> list=materialEnglishHistoryService.query(1,10,materialEnglishHistoryQuery).getList();
        for(MaterialEnglishHistory materialEnglishHistory:list){
            System.out.print(materialEnglishHistory.getOperator().getName()+"\t");
            System.out.print(materialEnglishHistory.getMaterial().getChinese()+"\t");
            System.out.print(materialEnglishHistory.getEnglish()+"\t");
            System.out.println(materialEnglishHistory.getAuditor());
        }
    }
}
