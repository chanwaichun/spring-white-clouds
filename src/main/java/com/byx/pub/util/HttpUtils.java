package com.byx.pub.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.byx.pub.exception.ApiException;
import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Jump
 * @date 2023/5/11 22:50:28
 */
public class HttpUtils {

    private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    static public String requestGet(String url, Map<String, String> params, Map<String, String> heads) {


        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        if (params != null) {
            for (String key : params.keySet()) {
                urlBuilder.addQueryParameter(key, params.get(key));
            }
        }

        reqBuild.url(urlBuilder.build());


        if (heads != null) {
            for (String key : heads.keySet()) {

                reqBuild.addHeader(key, heads.get(key));
            }
        }


        Request req = reqBuild.build();

        try {
            Response response = okHttpClient.newCall(req).execute();


            //判断请求是否成功
            if (response.isSuccessful()) {
                String s = response.body().string();
                return s;
            }

        } catch (IOException e) {

            e.printStackTrace();


            logger.error(e.toString());


        }

        return null;


    }

    static public String requestPostBody(String url, String json, Map<String, String> heads) {

        MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSONType, json);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (heads != null) {
            for (String key : heads.keySet()) {
                builder.addHeader(key, heads.get(key));
            }
        }

        builder.post(body);
        Request req = builder.build();
        try {
            Response response = okHttpClient.newCall(req).execute();

            if (response.isSuccessful()) {
                return response.body().string();
                //打印服务端返回结果
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回流的
     * @param url
     * @param json
     * @param heads
     * @return
     */
    static public byte[] reqPostBodyByByte(String url, String json, Map<String, String> heads) {
        MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSONType, json);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (heads != null) {
            for (String key : heads.keySet()) {
                builder.addHeader(key, heads.get(key));
            }
        }
        builder.post(body);
        Request req = builder.build();
        try {
            Response response = okHttpClient.newCall(req).execute();

            if (response.isSuccessful()) {
                return response.body().bytes();
                //打印服务端返回结果
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取数据流
     * @param url
     * @param paraMap
     * @return
     */
    public static byte[] doImgPost(String url, Map<String, Object> paraMap) {
        byte[] result = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        try {
            // 设置请求的参数
            JSONObject postData = new JSONObject();
            for (Map.Entry<String, Object> entry : paraMap.entrySet()) {
                postData.put(entry.getKey(), entry.getValue());
            }
            httpPost.setEntity(new StringEntity(postData.toString(), "UTF-8"));
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toByteArray(entity);
        } catch (ConnectionPoolTimeoutException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return result;
    }




    static public String requestPostForm(String url, Map<String, String> params, Map<String, String> heads) {

//        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);

        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                bodyBuilder.add(key, params.get(key));
            }
        }
        RequestBody body = bodyBuilder.build();
        Request.Builder builder = new Request.Builder();

        if (heads != null) {
            for (String key : heads.keySet()) {
                builder.addHeader(key, heads.get(key));
            }
        }
        builder.url(url);
        builder.post(body);
        Request req = builder.build();

        try {
            Response response = okHttpClient.newCall(req).execute();
            if (response.code() != 200) {
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                String message = jsonObject.getString("message");
                Long code = jsonObject.getLong("status");
                throw new ApiException(code, message);
            }
            return response.body().string();

            //打印服务端返回结果
        } catch (IOException e) {
            e.printStackTrace();

        }

        return null;

    }


}
