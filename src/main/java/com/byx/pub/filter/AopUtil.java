package com.byx.pub.filter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * @author cyj
 **/
@Slf4j
public class AopUtil {

    /**
     * 根据表达式获取值
     * @param el SpirngEL表达式
     * @param pjp 切面对象
     * @return SpringEL表达式的值
     */
    public static Object springElParser(String el, ProceedingJoinPoint pjp) {
        // 创建解析器
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser. parseExpression (el);
        //设置解析上下文(有哪些占位荷,以及每种占位符的值-根据方法的参数名和参数值
        EvaluationContext context = new StandardEvaluationContext();

        // 获取方法参数
        Object [] args = pjp.getArgs();
        String [] parameterNames= new DefaultParameterNameDiscoverer().getParameterNames(getMethod(pjp));
        Assert.notEmpty(parameterNames, "方法入参不能为空");
        for (int i = 0; i < parameterNames.length; i++){
            log.info("参数名:{}, 参数值:{}", parameterNames[i], args[i]);
            context.setVariable(parameterNames[i],args[i]);
        }

        // 解析表达式,生成最终的key
        return expression.getValue(context);
    }

    public static Method getMethod(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }
}
