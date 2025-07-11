package com.sky.ascept;


import com.sky.annoation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.apache.ibatis.ognl.OgnlRuntime.setFieldValue;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {




    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annoation.AutoFill)")
    public void autoFill(){


    }


    @Before("autoFill()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        log.info("开始进行数据填充");


        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//获取方法签名
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);//获取方法参数的注解
        OperationType value = autoFill.value();//获取注解的属性


        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object object = args[0];
        log.info("对对象{}进行属性填充",object.getClass());

        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();



        if(value == OperationType.INSERT){

            Method setCreateTime = object.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setUpdateTime = object.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setCreateUser = object.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setUpdateUser = object.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setCreateTime.invoke(object,now);
            setUpdateTime.invoke(object,now);
            setCreateUser.invoke(object,currentId);
            setUpdateUser.invoke(object,currentId);
        }
        else if(value == OperationType.UPDATE){

            Method setUpdateTime = object.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = object.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdateTime.invoke(object,now);
            setUpdateUser.invoke(object,currentId);
        }




    }
}
