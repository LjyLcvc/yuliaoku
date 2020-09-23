package com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.dao;

import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.Material;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.MaterialEnglishHistory;
import com.lcvc.guojiaoyuan.yuliaoku.yuliaoku.model.query.MaterialEnglishHistoryQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MaterialEnglishHistoryDaoTest extends YuliaokuApplicationTests {
    @Autowired
    private MaterialEnglishHistoryDao materialEnglishHistoryDao;

    @Test
    public void testReadAll(){
        List<MaterialEnglishHistory> list=materialEnglishHistoryDao.readAll(null);
        for(MaterialEnglishHistory materialEnglishHistory:list){
            System.out.print(materialEnglishHistory.getOperator().getName()+"\t");
            System.out.print(materialEnglishHistory.getMaterial().getChinese()+"\t");
            System.out.print(materialEnglishHistory.getEnglish()+"\t");
            System.out.println(materialEnglishHistory.getAuditor());
        }
    }

    @Test
    public void testQuerySize(){
        MaterialEnglishHistoryQuery materialEnglishHistoryQuery=new MaterialEnglishHistoryQuery();
        materialEnglishHistoryQuery.setMaterial(new Material(1));
        int number=materialEnglishHistoryDao.querySize(materialEnglishHistoryQuery);
        System.out.println(number);
    }
}
