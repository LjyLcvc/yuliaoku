package com.lcvc.guojiaoyuan.yuliaoku.dao;

import com.lcvc.guojiaoyuan.yuliaoku.YuliaokuApplicationTests;
import com.lcvc.guojiaoyuan.yuliaoku.model.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AdminDaoTest extends YuliaokuApplicationTests {
    @Autowired
    private AdminDao adminDao;

    @Test
    public void testReadAll(){
        List<Admin> list=adminDao.readAll(null);
        for(Admin admin:list){
            System.out.println(admin.getUsername());
        }
    }

    @Test
    public void testLogin(){
        System.out.println(adminDao.login("lcvc","fh58q2ea6thauof5ikg98fe2ciafh50r"));
    }

    @Test
    public void testTotal(){
        System.out.println(adminDao.total());
    }
}
