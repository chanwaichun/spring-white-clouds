package com.byx.pub.filter;


import com.alibaba.fastjson.JSONObject;
import com.byx.pub.bean.vo.LoginHeadBean;
import com.byx.pub.enums.ClientTypeEnum;
import com.byx.pub.enums.ResultCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.service.RedisService;
import com.byx.pub.util.MD5;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@WebFilter(filterName = "accountFilter", urlPatterns = "/*")
public class AccountFilter implements Filter {

    @Autowired
    private ApplicationContext applicationContext;
    @Value("${token.check.status}")
    private Boolean checkStatus;
    //直接放行的url
    private List<String> pathList= Arrays.asList(
            "/",
           // "/actuator", //服务状态检测
            "/csrf",
            "/v2/api-docs",
            "/white/clouds/manage/v1/admin/account/login",
            "/white/clouds/manage/v1/admin/get/login/qrCode",
            "/white/clouds/manage/v1/admin/WeChat/callback",
            "/white/clouds/manage/v1/admin/get/login/crop/qrCode",
            "/white/clouds/manage/v1/crop/inner/wechat/login/qrCode",
            "/white/clouds/manage/v1/admin/crop/callback",
            "/white/clouds/manage/v1/crop/inner/wechat/crop/callback",
            "/white/clouds/front/user/v1/wx/pay/notify",
            "/white/clouds/front/user/v1/wx/refund/notify",
            "/white/clouds/front/user/v1/user/login",
            "/doc",
            "/white/clouds/manage/v1/view/relay/list",
            "/white/clouds/manage/v1/crop/callback/msg",
            "/white/clouds/manage/v1/crop/callback/command",
            "/white/clouds/manage/v1/xhs/callback"
    );
    //添加白名单注解的url
    private List<String> filterPassList;

    @Resource
    RedisService redisService;

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        WhiteCloudsRequestWrapper request = new WhiteCloudsRequestWrapper((HttpServletRequest) servletRequest);
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        this.logSession(session,servletPath);

        //以下直接放行
        if (!checkStatus || this.filterPassList.contains(servletPath) || this.pathList.contains(servletPath) ||
        servletPath.contains("swagger") || servletPath.contains("doc") || servletPath.contains("webjars") ){
            filterChain.doFilter(request,servletResponse);
            return;
        }

        //静态资源路径白名单
        if( servletPath.contains("/WW_verify_jZGe5zcuOYBj3Hex")
                || servletPath.contains("/favicon.ico")
                || servletPath.contains("/WW_verify_YoJSgfVFgIffzTpE.txt")
                || servletPath.contains("/productUploadImg")
                || servletPath.contains("/protocolUploadImg")){
            filterChain.doFilter(request,servletResponse);
            return;
        }
        //校验路由
        String xSource = "";
        if(servletPath.contains("/white/clouds/manage/")){
            xSource = ClientTypeEnum.BYX_MANEGE.getValue();
        }else if(servletPath.contains("/white/clouds/front/")){
            xSource = ClientTypeEnum.BYX_FRONT.getValue();
        }else{
            log.error("路由规则错误：{}",servletPath);
            error503(servletResponse);
            return;
        }
        //校验token
        String xKey = request.getHeader("x-token");
        if(StringUtil.isEmpty(xKey) || StringUtil.isEmpty(xSource)){
            log.error("token的key为空,key:{}，xSource:{}",xKey,xSource);
            error401(servletResponse);
            return;
        }
        //获取token的值
        String xToken = redisService.get(MD5.md5(xSource)+xKey);
        if (this.checkStatus && StringUtil.isEmpty(xToken)) {
            this.error401(servletResponse);
            return;
        }

        if (StringUtils.isBlank(xToken)){
            this.error401(servletResponse);
            return;
        }
        LoginHeadBean headBean = JSONObject.parseObject(xToken, LoginHeadBean.class);
        //针对不同客户端请求头设值
        Map<String,String> setHeaders = new HashMap<>();
        if(ClientTypeEnum.BYX_MANEGE.getValue().equals(headBean.getClientType())){
            String adminId = Objects.nonNull(headBean.getAdminId()) ? headBean.getAdminId():"";
            String adminRole = Objects.nonNull(headBean.getAdminRole()) ? headBean.getAdminRole():"";
            String businessId = Objects.nonNull(headBean.getBusinessId()) ? headBean.getBusinessId():"";
            setHeaders.put("x-source",ClientTypeEnum.BYX_MANEGE.getValue());
            setHeaders.put("admin-id",adminId);
            setHeaders.put("admin-role",adminRole);
            setHeaders.put("business-id",businessId);
            setHeaders.put("x-key",MD5.md5(xSource)+xKey);
        }else if(ClientTypeEnum.BYX_FRONT.getValue().equals(headBean.getClientType())){
            String adminId = Objects.nonNull(headBean.getAdminId()) ? headBean.getAdminId():"";
            String userId = Objects.nonNull(headBean.getUserId()) ? headBean.getUserId():"" ;
            String userRole = Objects.nonNull(headBean.getUserRole()) ? headBean.getUserRole():"";
            String businessId = Objects.nonNull(headBean.getBusinessId()) ? headBean.getBusinessId():"";
            setHeaders.put("x-source",ClientTypeEnum.BYX_FRONT.getValue());
            setHeaders.put("admin-id",adminId);
            setHeaders.put("user-id",userId);
            setHeaders.put("user-role",userRole);
            setHeaders.put("business-id",businessId);
            setHeaders.put("x-key",MD5.md5(xSource)+xKey);
        }else{
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"用户信息错误");
        }
        //设置请求头参数的值
        if (Objects.nonNull(setHeaders)){
            request.setHeaders(setHeaders);
        }
        filterChain.doFilter(request,servletResponse);
        return;
    }

    private void logSession(HttpSession session, String path){
        Enumeration<String> attributeNames = session.getAttributeNames();
        List<String> attributeNameList=new ArrayList<>();
        while (attributeNames.hasMoreElements()){
            attributeNameList.add(attributeNames.nextElement());
        }
        //log.info("session attributeNameList has "+JSONObject.toJSONString(attributeNameList)+" ,request path :"+path);
    }

    /**
     * 返回401(token过期)
     * @param servletResponse
     * @throws IOException
     */
    private void error401(ServletResponse servletResponse) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(401);
        PrintWriter writer = response.getWriter();
        JSONObject data=new JSONObject();
        data.put("code",401);
        data.put("msg","token已过期,请重新登录。");
        writer.print(data.toJSONString());
        writer.flush();
    }

    /**
     * 返回503(路由格式错误)
     * @param servletResponse
     * @throws IOException
     */
    private void error503(ServletResponse servletResponse) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(503);
        PrintWriter writer = response.getWriter();
        JSONObject data=new JSONObject();
        data.put("code",503);
        data.put("msg","路由错误，请联系管理员");
        writer.print(data.toJSONString());
        writer.flush();
    }

    /**
     * 返回403(白名单机器)
     * @param servletResponse
     * @param remoteHost
     * @throws IOException
     */
    private void error403(ServletResponse servletResponse, String remoteHost) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(403);
        PrintWriter writer = response.getWriter();
        JSONObject data=new JSONObject();
        data.put("code",403);
        data.put("msg","未授权的访问设备,ip:"+remoteHost);
        writer.print(data.toJSONString());
        writer.flush();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.initFilterPathList();
    }
    //初始化拦截器(把白名单注解的接口路径放入到list)
    private void initFilterPathList(){
        log.info("开始初始化AccountFilter");
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(Controller.class);
        List<String> filterPassList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
            //tips：超链的版本需要拿到超类，这边不用entry.getValue().getClass().getSuperclass();
            Class<?> clazz = entry.getValue().getClass();
            RequestMapping requestMappingClass = clazz.getAnnotation(RequestMapping.class);
            String basePath="";
            if (requestMappingClass!=null){
                String s = requestMappingClass.value()[0];
                if (!s.startsWith("/")){s="/"+s;}
                if (s.endsWith("/")){s=s.substring(0,s.length()-1);}
                basePath=s;
            }
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                FilterPass filterPass = declaredMethod.getAnnotation(FilterPass.class);
                if (filterPass==null){continue;}
                String path = this.getRequestPath(declaredMethod);
                if (path==null){continue;}
                if (!path.startsWith("/")){path="/"+path;}
                filterPassList.add(basePath+path);
            }
        }
        log.info("filterPassList:{}",filterPassList);
        this.filterPassList=filterPassList;
    }

    //各种各样的请求方式的controller
    private String getRequestPath(Method method){
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping!=null){
            return requestMapping.value()[0];
        }
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping!=null){
            return getMapping.value()[0];
        }
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping!=null){
            return postMapping.value()[0];
        }
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (putMapping!=null){
            return putMapping.value()[0];
        }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (deleteMapping!=null){
            return deleteMapping.value()[0];
        }
        PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
        if (patchMapping!=null){
            return patchMapping.value()[0];
        }
        return null;
    }

    public String getRemoteHost(HttpServletRequest request){
        String realIp = request.getHeader("x-real-ip");
        if (StringUtils.isNotBlank(realIp)){
            return realIp;
        }

        return request.getRemoteHost();
    }


}
