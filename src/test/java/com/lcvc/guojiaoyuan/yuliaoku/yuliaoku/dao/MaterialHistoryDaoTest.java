package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao;

import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialHistory;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialHistoryQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MaterialHistoryDaoTest extends YuliaokuApplicationTests {
    @Autowired
    private MaterialHistoryDao materialHistoryDao;

    @Test
    public void testReadAll(){
        List<MaterialHistory> list= materialHistoryDao.readAll(null);
        for(MaterialHistory materialHistory :list){
            System.out.print(materialHistory.getOperator().getName()+"\t");
            System.out.print(materialHistory.getMaterial().getChinese()+"\t");
            System.out.print(materialHistory.getEnglish()+"\t");
            System.out.println(materialHistory.getAuditor());
        }
    }

    @Test
    public void testQuerySize(){
        MaterialHistoryQuery materialEnglishHistoryQuery=new MaterialHistoryQuery();
        materialEnglishHistoryQuery.setMaterial(new Material(1));
        int number= materialHistoryDao.querySize(materialEnglishHistoryQuery);
        System.out.println(number);
    }
}
