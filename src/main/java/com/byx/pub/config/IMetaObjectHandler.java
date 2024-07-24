
package com.byx.pub.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.byx.pub.enums.ClientTypeEnum;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Objects;

/**
 * 公共字段填充
 * @Author: Jump
 * @Date: 2023年5月8日
 */
@Slf4j
public class IMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(Objects.isNull(servletRequestAttributes)) {
            setFieldValByName("creator", "SYSTEM", metaObject);
            return;
        }
        setFieldValByName("creator", getOperator(servletRequestAttributes), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(null == servletRequestAttributes) {
            setFieldValByName("updator", "SYSTEM", metaObject);
            return;
        }
        setFieldValByName("updator", getOperator(servletRequestAttributes), metaObject);
    }

    public String getOperator(ServletRequestAttributes servletRequestAttributes){
        String id = "SYSTEM";
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String source = request.getHeader("x-source");
        if(ClientTypeEnum.BYX_MANEGE.getValue().equals(source)){
            id = request.getHeader("admin-id");
        }else if(ClientTypeEnum.BYX_FRONT.getValue().equals(source)){
            id = request.getHeader("user-id");
        }
        return id;
    }

}
