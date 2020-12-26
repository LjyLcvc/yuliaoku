package test;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcvc.guojiaoyuan.yuliaoku.model.base.ExcelException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Test1 {


    @Test
    public void test1(){
        String s="adf-adf";
        System.out.println(s.split("-"));
    }

    @Test
    public void test2(){
        String message=" hello,a b    c    d ";
        System.out.println(message.trim().replaceAll("\\s{1,}", " "));
    }

    @Test
    public void test3() throws Exception{
        List<ExcelException> excelExceptions=new ArrayList<ExcelException>();
        excelExceptions.add(new ExcelException("物料表1",1,3,"轮胎","字符长度超出"));
        excelExceptions.add(new ExcelException("物料表1",2,3,"扳手","字符长度超出"));
        Gson gson = new Gson();
        String json=gson.toJson(excelExceptions);
        System.out.println(json);
        List<ExcelException> excelExceptionsFromJson=gson.fromJson(json, new TypeToken<List<ExcelException>>(){}.getType());
    }

}
