package com.lcvc.guojiaoyuan.yuliaoku.service;

import com.lcvc.guojiaoyuan.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.model.query.MaterialHistoryQuery;
import com.lcvc.guojiaoyuan.yuliaoku.model.MaterialHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MaterialHistoryServiceTest extends YuliaokuApplicationTests {
    @Autowired
    private MaterialHistoryService materialHistoryService;

    @Test
    public void testQuery(){
        MaterialHistoryQuery materialEnglishHistoryQuery=new MaterialHistoryQuery();
        materialEnglishHistoryQuery.setMaterial(new Material(2));
        List<MaterialHistory> list= materialHistoryService.query(1,10,materialEnglishHistoryQuery).getList();
        for(MaterialHistory materialHistory :list){
            System.out.print(materialHistory.getOperator().getName()+"\t");
            System.out.print(materialHistory.getMaterial().getChinese()+"\t");
            System.out.print(materialHistory.getEnglish()+"\t");
            System.out.println(materialHistory.getAuditor());
        }
    }
}
