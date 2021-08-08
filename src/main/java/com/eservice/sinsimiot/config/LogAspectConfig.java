package com.eservice.sinsimiot.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: silent
 * @date: Created in 2019/11/28 9:53
 * @description: 描述
 */

@Aspect
@Component
@Slf4j
public class LogAspectConfig {


    /**
     * @Description: 定义切入点
     * @Title: pointCut
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:17
     */
    @Pointcut("execution(public * com.hankun.*.web.*.*(..))")
    public void pointCut() {
        log.info(" 【注解：PointCut】 不会进！！！ ");
    }

    /**
     * @param joinPoint
     * @throws Throwable
     * @Description: 定义前置通知
     * @Title: before
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:23
     */
    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        String logClassName = joinPoint.getSignature().getDeclaringTypeName();
        logClassName = logClassName.substring(logClassName.lastIndexOf(".") + 1);
        String method = joinPoint.getSignature().getName();
        if (method.contains("search") || method.contains("list")) {
            return;
        }
        logClassName = String.format("%s.%s", logClassName.substring(logClassName.lastIndexOf(".") + 1), method);
        log.info("【{}】 ----------开始执行---------------", logClassName);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("【{}】URL : {}", logClassName, request.getRequestURL().toString());
        log.info("【{}】HTTP_METHOD : {}", logClassName, request.getMethod());
        log.info("【{}】IP : {}", logClassName, request.getRemoteAddr());
        log.info("【{}】CLASS_METHOD : {}", logClassName, method);
        log.info("【{}】ARGS : {}", logClassName, joinPoint.getArgs());

    }

    /**
     * @param ret
     * @throws Throwable
     * @Description: 后置返回通知
     * @Title: afterReturning
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:30
     */
    @AfterReturning(returning = "ret", pointcut = "pointCut()")
    public void afterReturning(JoinPoint joinPoint, Object ret) {
        String logClassName = joinPoint.getSignature().getDeclaringTypeName().replaceAll("com.hankun.master.", "");
        logClassName = String.format("%s.%s", logClassName.substring(logClassName.lastIndexOf(".") + 1), joinPoint.getSignature().getName());
        log.info("【{}】Result : {}", logClassName, ret);
    }

    /**
     * @param joinPoint
     * @Description: 后置异常通知
     * @Title: afterThrowing
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:37
     */
    @AfterThrowing(value = "pointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        String logClassName = joinPoint.getSignature().getDeclaringTypeName().replaceAll("com.hankun.master.", "");
        logClassName = String.format("%s.%s", logClassName.substring(logClassName.lastIndexOf(".") + 1), joinPoint.getSignature().getName());
        log.info("【{}】error : {}", logClassName, e.getMessage());
    }

    /**
     * @param joinPoint
     * @Description: 后置最终通知, final增强，不管是抛出异常或者正常退出都会执行
     * @Title: after
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:48
     */
    @After("pointCut()")
    public void after(JoinPoint joinPoint) {
        String logClassName = joinPoint.getSignature().getDeclaringTypeName().replaceAll("com.hankun.master.", "");
        logClassName = String.format("%s.%s", logClassName.substring(logClassName.lastIndexOf(".") + 1), joinPoint.getSignature().getName());
        log.info("【{}】 ----------执行完毕---------------", logClassName);
    }


}
