package com.byx.pub.util;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 * Created by xiaoyue on 2017/12/20.
 * <p>
 * AES-128-CBC 加密方式
 * 注：
 * AES-128-CBC可以自己定义“密钥”和“偏移量“。
 * AES-128是jdk自动生成的“密钥”。
 */
public class AesCbcUtil {

    //加密方式
    public static String KEY_ALGORITHM = "AES";
    //数据填充方式
    String algorithmStr = "AES/CBC/PKCS7Padding";
    //避免重复new生成多个BouncyCastleProvider对象，因为GC回收不了，会造成内存溢出
    //只在第一次调用decrypt()方法时才new 对象
    public static boolean initialized = false;


    /**
     * AES解密
     * 填充模式AES/CBC/PKCS7Padding
     * 解密模式128
     * @param content
     *            目标密文
     * @return
     * @throws Exception
     * @throws InvalidKeyException
     * @throws NoSuchProviderException
     */
    public static byte[] decrypt(byte[] content, byte[] aesKey, byte[] ivByte) {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(aesKey, "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**BouncyCastle作为安全提供，防止我们加密解密时候因为jdk内置的不支持改模式运行报错。**/
    public static void initialize() {
        if (initialized)
            return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    // 生成iv
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }


}