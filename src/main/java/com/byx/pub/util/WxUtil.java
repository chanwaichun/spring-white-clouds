package com.byx.pub.util;

import cn.hutool.json.JSONUtil;
import com.byx.pub.bean.vo.WxJspaiPayVo;
import com.byx.pub.enums.WeChatUrlConstants;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

/**
 * 微信工具类
 *
 * @program: pub-pay
 * @description: 微信工具类
 * @author: Jim
 * @create: 2021-12-07
 */
@Slf4j
public class WxUtil {
    /**
     * 获取私钥(文档放在项目下的方式)
     * @param filename 私钥文件路径  (required)
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    /**
     * v3支付异步通知验证签名
     * @param body 异步通知密文
     * @param key  api 密钥
     * @return 异步通知明文
     * @throws Exception 异常信息
     */
    public static String verifyNotify(String body, String key) throws Exception {
        // 获取平台证书序列号
        cn.hutool.json.JSONObject resultObject = JSONUtil.parseObj(body);
        cn.hutool.json.JSONObject resource = resultObject.getJSONObject("resource");
        String cipherText = resource.getStr("ciphertext");
        String nonceStr = resource.getStr("nonce");
        String associatedData = resource.getStr("associated_data");
        AesUtil aesUtil = new AesUtil(key.getBytes(StandardCharsets.UTF_8));
        // 密文解密
        return aesUtil.decryptToString(
                associatedData.getBytes(StandardCharsets.UTF_8),
                nonceStr.getBytes(StandardCharsets.UTF_8),
                cipherText
        );
    }

    /**
     * 组装签名加载
     * @param method    请求方式
     * @param url       请求地址
     * @param timestamp 请求时间
     * @param nonceStr  请求随机字符串
     * @param body      请求体
     * @return 组装的字符串
     */
    public static String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }
        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }

    /**
     * 生成签名(私钥文件)
     * @param message            请求体
     * @param privateKeyFilePath 私钥的路径
     * @return 生成base64位签名信息
     * @throws Exception
     */
    public static String sign(byte[] message, String privateKeyFilePath) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(getPrivateKey(privateKeyFilePath));
        sign.update(message);
        return Base64.getEncoder().encodeToString(sign.sign());
    }

    /**
     * 生成签名(私钥配置)
     * @param message 请求体
     * @param privateKey 私钥
     * @return 生成base64位签名信息
     * @throws Exception
     */
    public static String sign(byte[] message, PrivateKey privateKey) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(message);
        return Base64.getEncoder().encodeToString(sign.sign());
    }



    /**
     * 微信调起支付参数
     * 返回参数如有不理解 请访问微信官方文档
     * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter4_1_4.shtml
     * @param prepayId 预支付id(微信下单返回)
     * @param appId 应用ID(appid)
     * @param privateKey 私钥
     * @return 前端调起支付所需的参数A
     * @throws Exception
     */
    public static WxJspaiPayVo WxJsApiTuneUp(String prepayId, String appId, PrivateKey privateKey) throws Exception {
        String time = System.currentTimeMillis() / 1000 + "";
        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        String packageStr = "prepay_id=" + prepayId;
        ArrayList<String> list = new ArrayList<>();
        list.add(appId);
        list.add(time);
        list.add(nonceStr);
        list.add(packageStr);
        //加载签名
        String packageSign = sign(buildSignMessage(list).getBytes(), privateKey);
        WxJspaiPayVo wxJspaiPayVo = new WxJspaiPayVo();
        wxJspaiPayVo.setAppid(appId)
                .setTimeStamp(time)
                .setNonceStr(nonceStr)
                .setPackageValue(packageStr)
                .setSignType(WeChatUrlConstants.SIGNTYPE)
                .setPaySign(packageSign);
        return wxJspaiPayVo;
    }

    /**
     * 构造签名串
     *
     * @param signMessage 待签名的参数
     * @return 构造后带待签名串
     */
    static String buildSignMessage(ArrayList<String> signMessage) {
        if (signMessage == null || signMessage.size() <= 0) {
            return null;
        }
        StringBuilder sbf = new StringBuilder();
        for (String str : signMessage) {
            sbf.append(str).append("\n");
        }
        return sbf.toString();
    }


}
