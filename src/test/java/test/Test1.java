package test;


import org.junit.jupiter.api.Test;

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
}
