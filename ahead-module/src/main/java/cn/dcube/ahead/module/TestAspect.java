package cn.dcube.ahead.module;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @date：2021-12-27 10:05<br>
 * @author：yangfei<br>
 * @version: v1.0
 */
@Aspect
@Component
@Slf4j
public class TestAspect {

    @Pointcut("@annotation(cn.dcube.ahead.dynamicDS.DynamicDS)")
    public void cutPoint(){}

    @Before("cutPoint()")
    public void sysNotic(JoinPoint joinPoint){
        System.out.println("》》》》》》》》》》》》》");
    }
}