package com.ecnu.handler;

import com.ecnu.common.R;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 一般参数（form-data）校验绑定异常处理
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public R bindException(BindException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();   // 获取所有的错误信息
        StringBuilder msg = new StringBuilder();
        allErrors.forEach(e -> {
            msg.append(e.getDefaultMessage()).append(" ");
            System.out.println(e.getDefaultMessage());
        });
        System.out.println("bindException");
        return R.error(String.valueOf(msg));
    }

    /**
     * JSON参数校验绑定异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public R methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors(); // 获取所有的错误信息
        StringBuilder msg = new StringBuilder();
        allErrors.forEach(e -> {
            msg.append(e.getDefaultMessage()).append(" ");
            System.out.println(e.getDefaultMessage());
        });
        return R.error(String.valueOf(msg));
    }

    /**
     * 单个参数异常处理
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public R constraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();  // 获取具体的错误信息
        StringBuilder str = new StringBuilder();
        violations.forEach(e -> {
            str.append(e.getMessage()).append(" ");
            System.out.println(e.getMessage());
        });    // 打印数据
        return R.error(String.valueOf(str));
    }


    /**
     * 其他异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R otherException(Exception ex) {
        ex.printStackTrace();
        return R.error("服务器内部异常错误!\n"+ex.getMessage());
    }
}