package com.byx.pub.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 统一日志处理切面
 * Created by zyjk on 2018/4/26.
 */
@Slf4j
@Aspect
@Configuration
public class WebLogAop {

    public WebLogAop() {
        log.info("WebLogAspect 初始化");
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void webLog() {
    }

    @Around("webLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isInfoEnabled()) {
            StopWatch stopWatch = new StopWatch(String.valueOf(System.currentTimeMillis()));
            stopWatch.start("解析请求url");
            ImmutablePair<String, String> pair = getUrlAndMethod(joinPoint);

            //log 请求内容
            String className = joinPoint.getSignature().getDeclaringTypeName();
            log.info("执行的类名: ({} 方法：{})",
                    className.substring(className.lastIndexOf(".") + 1) + ".java", joinPoint.getSignature().getName());

            // 去除Request或者Response对象
            Object[] joinPointArgs = joinPoint.getArgs();
            Stream<?> stream = joinPointArgs == null ? Stream.empty() : Arrays.stream(joinPointArgs);
            List<Object> logArgs = stream.filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
                    .collect(Collectors.toList());
            log.info("请求url:  {} {}， 参数: {}", pair.left, pair.right,
                    JSON.toJSONString(logArgs, SerializerFeature.IgnoreNonFieldGetter));

            stopWatch.stop();
            stopWatch.start("执行业务逻辑");
            Object result = joinPoint.proceed(joinPoint.getArgs());
            stopWatch.stop();
            String retStr = JSON.toJSONString(result, SerializerFeature.IgnoreNonFieldGetter);
            log.info("请求url: {}， 返回结果：{}, 耗时：\n{}", pair.left, retStr, stopWatch.prettyPrint());
            return result;
        } else {
            return joinPoint.proceed(joinPoint.getArgs());
        }
    }

    private ImmutablePair<String, String> getUrlAndMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        /* 以下为有feign的情况
        Class<?> declaringClass = method.getDeclaringClass();
        FeignClient annotation = declaringClass.getAnnotation(FeignClient.class);
        if (annotation == null) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            // 异步线程获取ServletRequestAttributes是null
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return ImmutablePair.of(request.getRequestURL().toString(), request.getMethod());
            }
        } else {
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (Annotation declaredAnnotation : declaredAnnotations) {
                InvocationHandler handler = Proxy.getInvocationHandler(declaredAnnotation);
                Class fieldValue = (Class) getFieldValue(handler);

                if (PostMapping.class.getName().equals(fieldValue.getName())) {
                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    return ImmutablePair.of(Arrays.toString(postMapping.value()), "POST");
                } else if (GetMapping.class.getName().equals(fieldValue.getName())) {
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    return ImmutablePair.of(Arrays.toString(getMapping.value()), "GET");
                } else if (RequestMapping.class.getName().equals(fieldValue.getName())) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String requestMethod = Arrays.stream(requestMapping.method())
                            .findFirst()
                            .map(Enum::name)
                            .orElse(StringUtils.EMPTY);
                    return ImmutablePair.of(Arrays.toString(requestMapping.value()), requestMethod);
                }
            }
        }*/
        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
        for (Annotation declaredAnnotation : declaredAnnotations) {
            InvocationHandler handler = Proxy.getInvocationHandler(declaredAnnotation);
            Class fieldValue = (Class) getFieldValue(handler);

            if (PostMapping.class.getName().equals(fieldValue.getName())) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                return ImmutablePair.of(Arrays.toString(postMapping.value()), "POST");
            } else if (GetMapping.class.getName().equals(fieldValue.getName())) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                return ImmutablePair.of(Arrays.toString(getMapping.value()), "GET");
            } else if (RequestMapping.class.getName().equals(fieldValue.getName())) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String requestMethod = Arrays.stream(requestMapping.method())
                        .findFirst()
                        .map(Enum::name)
                        .orElse(StringUtils.EMPTY);
                return ImmutablePair.of(Arrays.toString(requestMapping.value()), requestMethod);
            }
        }
        return ImmutablePair.of(StringUtils.EMPTY, StringUtils.EMPTY);
    }

    @SneakyThrows
    private Object getFieldValue(InvocationHandler handler) {
        Class<? extends InvocationHandler> handlerClass = handler.getClass();
        Field field = handlerClass.getDeclaredField("type");
        field.setAccessible(true);
        return field.get(handler);
    }
}
