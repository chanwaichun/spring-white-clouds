package com.byx.pub.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.MimeHeaders;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

/**
 * @author jump
 * @date 2020/11/27 14:52
 */
@Slf4j
public class WhiteCloudsRequestWrapper extends HttpServletRequestWrapper {
    private HttpServletRequest request;

    public WhiteCloudsRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request=request;
        this.initHeads();
    }

    private MimeHeaders headers=new MimeHeaders();

    private void initHeads(){
        Enumeration<String> headerNames = super.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = super.getHeader(name);
            this.headers.addValue(name).setString(value);
        }
    }

    @Override
    public String getHeader(String s){
        return this.headers.getHeader(s);
    }

    @Override
    public Enumeration<String> getHeaderNames(){
        return headers.names();
    }

    @Override
    public Enumeration<String> getHeaders(String s){
        return headers.values(s);
    }

    public void setHeader(String name,String value){
        this.headers.setValue(name).setString(value);
    }

    public void setHeaders(Map<String,String> headers){
        if (Objects.isNull(headers)){
            return;
        }
        for (Map.Entry<String, String> header:headers.entrySet()){
            this.setHeader(header.getKey(),header.getValue());
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream stream = super.getInputStream();
        String contentEncoding = super.getHeader("Content-Encoding");
        log.debug("contentEncoding = "+contentEncoding);
        if (StringUtils.isEmpty(contentEncoding) || !contentEncoding.toLowerCase().contains("gzip")){
            return stream;
        }
        // 处理gzip压缩的body
        return new ServletInputStream() {
            private GZIPInputStream gzipInputStream=new GZIPInputStream(stream);
            @Override
            public int read() throws IOException {
                return this.gzipInputStream.read();
            }
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setReadListener(ReadListener arg0) {}
            @Override
            public void close() throws IOException {
                super.close();
                this.gzipInputStream.close();
            }
        };
    }

    @Override
    public HttpSession getSession(boolean var1){
        return this.request.getSession(var1);
    }

    @Override
    public HttpSession getSession(){
        return this.request.getSession();
    }

    @Override
    public String changeSessionId(){
        return this.request.changeSessionId();
    }

    @Override
    public boolean isRequestedSessionIdValid(){
        return this.request.isRequestedSessionIdValid();
    }

    @Override
    public boolean isRequestedSessionIdFromCookie(){
        return this.request.isRequestedSessionIdFromCookie();
    }

    @Override
    public boolean isRequestedSessionIdFromURL(){
        return this.request.isRequestedSessionIdFromURL();
    }
}
