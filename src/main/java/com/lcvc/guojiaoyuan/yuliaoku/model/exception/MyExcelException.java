package com.lcvc.guojiaoyuan.yuliaoku.model.exception;

import com.google.gson.Gson;
import com.lcvc.guojiaoyuan.yuliaoku.model.base.ExcelException;

import java.util.List;

/**
 * 自定义异常：用于处理excel上传的异常
 * 说明：用于收集excel表中错误的所有行信息
 */
public class MyExcelException extends RuntimeException {

    public MyExcelException() {
        super();
    }

   public MyExcelException(List<ExcelException> excelExceptions) {
        super(new Gson().toJson(excelExceptions));
    }

    public MyExcelException(String message, Throwable cause) {
        super(message, cause);
    }



    public MyExcelException(Throwable cause) {
        super(cause);
    }

}

