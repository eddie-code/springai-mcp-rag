package org.dromara.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 监控方法执行时间并记录日志
 *
 * @author lee
 * @description
 */
@Component
@Slf4j
@Aspect
public class ServiceLogAspect {

    /**
     * 环绕通知切点，拦截org.dromara.service.impl包及其子包下所有类的所有方法执行
     * <p>
     * 该切点表达式匹配：
     * - 包路径：org.dromara.service.impl包及其所有子包
     * - 类型：所有类
     * - 方法：所有方法（任意参数和返回值）
     * <p>
     * 用于对服务层实现类的方法执行进行统一的环绕增强处理
     */
    @Around("execution(* org.dromara.service.impl..*.*(..))")
    public Object recordTimesLogAspect(ProceedingJoinPoint joinPoint) throws Throwable {

        long begin = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        // 构造目标方法的完整签名信息
        String point = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();

        long end = System.currentTimeMillis();

        // 计算方法执行耗时
        long time = end - begin;

        // 根据执行耗时长短，使用不同级别日志记录监控信息
        if (time > 3000) {
            log.error("{} 耗时偏长 {} 毫秒", point, time);
        } else if (time > 2000) {
            log.warn("{} 耗时中等 {} 毫秒", point, time);
        } else {
            log.info("{} 耗时 {} 毫秒", point, time);
        }

        return proceed;

    }

}
