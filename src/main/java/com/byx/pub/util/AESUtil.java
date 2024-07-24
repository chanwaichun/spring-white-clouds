package com.byx.pub.util;

import com.alibaba.fastjson.JSONException;
import com.byx.pub.exception.ApiException;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @author Jamin
 * @Date 2023/2/3 11:20
 */

public class AESUtil {

    /**
     * 设置加密字符集
     * 本例采用UTF-8字符集
     */
    private static final String CHARACTER = "UTF-8";

    /**
     * 用SHA1算法生成安全签名
     *
     * @param token     票据
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param encrypt   密文
     * @return 安全签名
     */
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt){
        try {
            String[] array = new String[] { token, timestamp, nonce, encrypt };
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("生成安全签名失败");
        }
    }

    /**
     * 对密文进行解密.
     *
     * @param text      需要解密的密文
     * @param key       密钥
     * @param receiveId 不同场景含有不同，企业应用的回调：corpid,第三方事件的回调：suiteid,个人主体的第三方应用的回调：receiveId为空字符串
     * @return 解密得到的明文
     */
    public static String weComDecrypt(String text, String key, String receiveId) {
        byte[] original;
        byte[] aesKey = Base64.decodeBase64(key + "=");
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            // 使用BASE64对密文进行解码
            byte[] encrypted = Base64.decodeBase64(text);

            // 解密
            original = cipher.doFinal(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String jsonContent, from_receiveid;
        try {
            // 去除补位字符
            byte[] bytes = PKCS7Encoder.decode(original);

            // 分离16位随机字符串,网络字节序和receiveid
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

            int jsonLength = recoverNetworkBytesOrder(networkOrder);

            jsonContent = new String(Arrays.copyOfRange(bytes, 20, 20 + jsonLength), CHARACTER);
            from_receiveid = new String(Arrays.copyOfRange(bytes, 20 + jsonLength, bytes.length), CHARACTER);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // receiveid不相同的情况
        if (!from_receiveid.equals(receiveId)) {

        }
        if (jsonContent != null && jsonContent.contains("<xml>")) {
            try {
                JSONObject json = XML.toJSONObject(jsonContent.replace("<xml>", "").replace("</xml>", ""));
                jsonContent = json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonContent;

    }

    /**
     * 提取出xml数据包中的加密消息
     *
     * @param xmltext 待提取的xml字符串
     * @return 提取出的加密消息字符串
     */
    public static Object[] extractXML(String xmltext) {
        Object[] result = new Object[3];
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            String FEATURE = null;
            // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
            // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
            FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
            dbf.setFeature(FEATURE, true);

            // If you can't completely disable DTDs, then at least do the following:
            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
            // JDK7+ - http://xml.org/sax/features/external-general-entities
            FEATURE = "http://xml.org/sax/features/external-general-entities";
            dbf.setFeature(FEATURE, false);

            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
            // JDK7+ - http://xml.org/sax/features/external-parameter-entities
            FEATURE = "http://xml.org/sax/features/external-parameter-entities";
            dbf.setFeature(FEATURE, false);

            // Disable external DTDs as well
            FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
            dbf.setFeature(FEATURE, false);

            // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);

            // And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then
            // ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
            // (http://cwe.mitre.org/data/definitions/918.html) and denial
            // of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."

            // remaining parser logic
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xmltext);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Encrypt");
            result[0] = 0;
            result[1] = nodelist1.item(0).getTextContent();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提取出 JSON 包中的加密消息
     *
     * @param jsontext 待提取的json字符串
     * @return 提取出的加密消息字符串
     */
    public static Object[] extractJSON(String jsontext) {
        Object[] result = new Object[3];
        try {

            JSONObject json = new JSONObject(jsontext);
            String encrypt_msg = json.getString("encrypt");
            String tousername = json.getString("tousername");
            String agentid = json.getString("agentid");

            result[0] = tousername;
            result[1] = encrypt_msg;
            result[2] = agentid;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 还原4个字节的网络字节序
    private static int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    public static void main(String[] args) {
        String url = "自己的域名" + "/wecom/callback/weCom/oauth"; // 二维码中的redirect_uri
        String encodeUrl = URLEncoder.encode(url);
        System.out.println(encodeUrl);
    }
}
