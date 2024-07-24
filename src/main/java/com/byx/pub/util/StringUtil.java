package com.byx.pub.util;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.byx.pub.bean.vo.notify.NotifyVo;
import com.byx.pub.enums.RoleTypeEnum;
import com.byx.pub.exception.ApiException;
import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by huangxiaoliang on 2017/6/21.
 */
@Slf4j
public class StringUtil {
    /**
     * 匹配是否包含数字
     *
     * @param str 可能为中文，也可能是-19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
     * @return
     */
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String content) {
        if (content == null || content.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean notEmpty(String content) {
        if (content == null || content.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static String getRandomSMS() {
        int max = 9999;
        int min = 1000;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return "" + s;
    }

    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static String encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return BaseEncoding.base64().encode(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static String decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return BaseEncoding.base64().encode(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static boolean checkPicFormat(String format) {
        if (format != null) {
            if ("png".equals(format.toLowerCase())) {
                return true;
            }
            if ("bmp".equals(format.toLowerCase())) {
                return true;
            }
            if ("jpeg".equals(format.toLowerCase())) {
                return true;
            }
            if ("jpg".equals(format.toLowerCase())) {
                return true;
            }
            if ("gif".equals(format.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPicFormat2(String format) {
        if (format != null) {
            if ("png".equals(format.toLowerCase())) {
                return true;
            }
            if ("jpeg".equals(format.toLowerCase())) {
                return true;
            }
            if ("jpg".equals(format.toLowerCase())) {
                return true;
            }
            if ("gif".equals(format.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkVoiceFormat(String format) {
        if (format != null) {
            if ("mp3".equals(format.toLowerCase())) {
                return true;
            }
            if ("wma".equals(format.toLowerCase())) {
                return true;
            }
            if ("wav".equals(format.toLowerCase())) {
                return true;
            }
            if ("amr".equals(format.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkApkFormat(String format) {
        if (format != null) {
            if ("apk".equals(format.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String getFileSize(File file) {
        String size = "";
        if (file.exists() && file.isFile()) {
            long fileS = file.length();
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                size = df.format((double) fileS) + "BT";
            } else if (fileS < 1048576) {
                size = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                size = df.format((double) fileS / 1048576) + "MB";
            } else {
                size = df.format((double) fileS / 1073741824) + "GB";
            }
        } else if (file.exists() && file.isDirectory()) {
            size = "";
        } else {
            size = "0BT";
        }
        return size;
    }

    public static boolean checkIsEmail(String email) {
        if (email == null) {
            return false;
        }
        String regex = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}";
        return email.matches(regex);
    }

    public static boolean checkIsMobile(String mobile) {
        String check = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(mobile);
        return matcher.matches();
    }

    public static boolean checkIsUrl(String url) {
        if (url == null) {
            return false;
        }
        return url.startsWith("http://");
    }


    public static Timestamp getDayBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }

    public static Timestamp getMonthBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }

    public static Timestamp getYearBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }

    public static Timestamp getDayBeginTimestamp() {
        Date date = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        Date date2 = new Date(date.getTime() - gc.get(gc.HOUR_OF_DAY) * 60 * 60
                * 1000 - gc.get(gc.MINUTE) * 60 * 1000 - gc.get(gc.SECOND)
                * 1000);
        return new Timestamp(date2.getTime());
    }

    public static String getErrorInfoFromException(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e2) {
            return "bad getErrorInfoFromException";
        }
    }

    public static String transactSQLInjection(String str) {
        if (str != null) {
            return str.replaceAll("([';])+|(--)+", "");
        }
        return "";
    }

    public static int getDayOffset(long date1, long date2) {
        return getDayOffset(new Date(date1 * 1000), new Date(date2 * 1000));
    }

    public static int getDayOffset(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {
            return day2 - day1;
        }
    }

    /**
     * 获取日期年份
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static int getYear(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取日期月份
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static int getMonth(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        return (calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * 获取日期号
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static int getDay(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取月份起始日期
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getMinMonthDate(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    /**
     * 获取月份起始日期时间戳
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static long getMinMonthDateTimestamp(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获取月份最后日期
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getMaxMonthDate(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    /**
     * 获取月份最后日期时间戳
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static long getMaxMonthDateTimestamp(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTimeInMillis() / 1000;
    }

    public static String getSha1(String str) {
        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    public static Long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    public static String truncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;
        if (StringUtil.isEmpty(strURL)) {
            return strAllParam;
        }
        strURL = strURL.trim();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    public static Map<String, String> getParamsMap(String url) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = truncateUrlPage(url);
        if (strUrlParam == null) {
            return mapRequest;
        }
        // 每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            // 解析出键值
            if (arrSplitEqual.length > 1) {
                // 正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    // 只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate2(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long timestamp = Long.valueOf(s) * 1000;
        res = simpleDateFormat.format(new Date(timestamp));
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static Date stampToDate2(long timestamp) {
        return new Date(timestamp * 1000);
    }

    /**
     * n年前时间
     *
     * @param timestamp
     * @param n
     * @return
     * @throws ParseException
     */
    public static long yearAgo(long timestamp, int n) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        //过去一年
        c.setTime(StringUtil.stampToDate2(timestamp));
        c.add(Calendar.YEAR, -n);
        Date y = c.getTime();
        String year = format.format(y);
        return dateToStamp(year);
    }

    public static String getStringDateForyyyymmdd() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getStringTimeForHHmmss() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getStringTimeForyyyyMMddHHmmss() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String stampToDate3(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Long timestamp = Long.valueOf(s) * 1000;
        res = simpleDateFormat.format(new Date(timestamp));
        return res;
    }

    public static String stampToDate4(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Long timestamp = Long.valueOf(s) * 1000;
        res = simpleDateFormat.format(new Date(timestamp));
        return res;
    }

    /**
     * 失效时间 时间+min
     *
     * @param dataStr
     * @return
     */
    public static String expriredDate(String dataStr, int min) {
        String aDataStr = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dataStr);
            date.setTime(date.getTime() + min * 60 * 1000);
            aDataStr = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return aDataStr;
    }

    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) throws ParseException {
        long res = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(s);
            res = date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 判断时间是否在时间段内
     *
     * @param date         当前时间 yyyy-MM-dd HH:mm:ss
     * @param strDateBegin 开始时间 00:00:00
     * @param strDateEnd   结束时间 00:05:00
     * @return
     */
    public static boolean isInDate(Date date, String strDateBegin, String strDateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String strDate = sdf.format(date);   //2016-12-16 11:53:54
        // 截取当前时间时分秒 转成整型
        int tempDate = Integer.parseInt(strDate.substring(11, 13) + strDate.substring(14, 16) + strDate.substring(17, 19));
        // 截取开始时间时分秒  转成整型
        int tempDateBegin = Integer.parseInt(strDateBegin.substring(0, 2) + strDateBegin.substring(3, 5) + strDateBegin.substring(6, 8));
        // 截取结束时间时分秒  转成整型
        int tempDateEnd = Integer.parseInt(strDateEnd.substring(0, 2) + strDateEnd.substring(3, 5) + strDateEnd.substring(6, 8));
        if ((tempDate >= tempDateBegin && tempDate <= tempDateEnd)) {
            return true;
        } else {
            return false;
        }
    }




    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    /**
     * @param ids
     * @return
     */
    public static List<Integer> changeIdsToList(String ids) {

        return changeIdsToList(ids, ",");
    }

    /**
     * @param ids
     * @param regex
     * @return
     */
    public static List<Integer> changeIdsToList(String ids, String regex) {

        List<Integer> result = new ArrayList<>();

        if (StringUtils.isEmpty(ids)) {
            return null;
        }
        if (ids.indexOf(regex) == -1) {
            result.add(Integer.parseInt(ids));
            return result;
        }

        String[] strs = ids.split(regex);
        for (String str : strs) {
            result.add(Integer.parseInt(str));
        }
        return result;
    }


    /**
     * @param ids
     * @return
     */
    public static List<String> changeIdsToStrList(String ids) {

        return changeIdsToStrList(ids, ",");
    }

    /**
     * @param ids
     * @param regex
     * @return
     */
    public static List<String> changeIdsToStrList(String ids, String regex) {

        List<String> result = new ArrayList<>();

        if (StringUtils.isEmpty(ids)) {
            return null;
        }
        if (ids.indexOf(regex) == -1) {
            result.add(ids);
            return result;
        }

        String[] strs = ids.split(regex);
        for (String str : strs) {
            result.add(str);
        }
        return result;
    }


    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException ex) {
        }
        return false;
    }


    /**生成英文加数字组合
     * @param num 位数
     * @return
     */
    public static String genRandom(int num){
        int  maxNum = 36;
        int i;
        int count = 0;
        char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < num){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    /**生成纯数字
     * @param num 位数
     * @return
     */
    public static String genRandomNum(int num){
        int  maxNum = 10;
        int i;
        int count = 0;
        char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < num){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    /**生成纯大写英文
     * @param num 位数
     * @return
     */
    public static String genRandomString(int num){
        int  maxNum = 26;
        int i;
        int count = 0;
        char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < num){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    /***
     * 去除String数组中的空值
     */
    public static String[] deleteArrayNull(String string[]) {
        String strArr[] = string;

        // step1: 定义一个list列表，并循环赋值
        ArrayList<String> strList = new ArrayList<String>();
        for (int i = 0; i < strArr.length; i++) {
            strList.add(strArr[i]);
        }

        // step2: 删除list列表中所有的空值
        while (strList.remove(null));
        while (strList.remove(""));

        // step3: 把list列表转换给一个新定义的中间数组，并赋值给它
        String strArrLast[] = strList.toArray(new String[strList.size()]);

        return strArrLast;
    }

    /**
     * 获取前4个拼音
     * @param str
     * @return

    public static String getPinyin(String str){
        if(str.length() > 4){
            str = str.substring(0,4);
        }
        return ChineseToHanYuPYTest.convertChineseToPinyin(str);
    }*/

    /**
     * 获取前4个拼音
     * @param str
     * @return

    public static String getPinyin(String str){
        if(str.length() > 4){
            str = str.substring(0,4);
        }
        String resStr = str;
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //小写
        //format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //不带声调
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            resStr = PinyinHelper.toHanYuPinyinString(str,format,"",Boolean.TRUE);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            resStr = PinyinUtil.getPinYin(str);
        }
        return resStr.toLowerCase();
    }*/

    /**
     * 转换整个字符List的特殊字符
     * @param list
     * @return
     */
    public static List<String> escapeList(List<String> list){
        List<String> resList = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return resList;
        }
        for(String str : list){
            resList.add(escape(str));
        }
        return resList;
    }



    // 转义特殊字符
    public static String escape(String str){
        if (isEmpty(str)){
            return str;
        }
        str = str.replaceAll("\\\\", "\\\\\\\\");
        str = str.replaceAll("_", "\\\\_");
        str = str.replaceAll("%", "\\\\%");
        return str;
    }

    public static String stripNumber(String str){
        //去掉标点符号
        str = str.replaceAll("[\\pP+~$`^=|<>～`$^+=|<>￥×]", "");
        // 匹配数字
        String reg = "-?[0-9]+\\.?[0-9]*";
        Pattern patten = Pattern.compile(reg);//编译正则表达式
        Matcher matcher = patten.matcher(str);// 指定要匹配的字符串
        // String[] split = patten.split(str);
        return matcher.replaceAll("");
    }



    public static void main(String[] args) {
        /*String imageUrl = "https://white-clouds-dev.dongliuxinli.com/productUploadImg/0ff7f41748fb418ba3cd8ecbe1fe88bb16938821303085534.jpg";
        try {
            byte[] imageData = downloadImage(imageUrl);
            System.out.println("Downloaded " + imageData.length + " bytes");
            System.out.println("Base64： " +Base64.getEncoder().encodeToString(imageData));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println(countZwNum("aaa肖先生您好，1123,"));

        /*List<String> a = new ArrayList<>();
        a.add("2");a.add("3");//新参数
        System.out.println("参数集合:"+a.toString());
        List<String> b = new ArrayList<>();
        b.add("3");b.add("6");b.add("9");b.add("10");//数据库参数
        System.out.println("数据库已有集合:"+b.toString());

        System.out.println("-------------------------------");
        System.out.println("需要插入的(参数有，数据库没有):"+subList(a,b));
        System.out.println("-------------------------------");
        System.out.println("需要删除的(参数没有，数据库有):"+subList(b,a));

        System.out.println("-------------------------------");
        System.out.println("不需要动的(参数有，数据有):"+subList(a,subList(a,b)));
        System.out.println(MD5.md5("1670707369149329410"));*/


    }

    /**
     * 将线上图片转字节数组
     * @param imageUrl
     * @return
     * @throws IOException
     */
    public static byte[] downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int bytesRead;
            byte[] data = new byte[1024];
            while ((bytesRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            return buffer.toByteArray();
        }
    }


    /**
     * 差集(基于java8新特性)优化解法2 适用于大数据量
     * 求List1中有的但是List2中没有的元素
     */
    public static List<String> subList(List<String> list1, List<String> list2) {
        Map<String, String> tempMap = list2.parallelStream().collect(Collectors.toMap(Function.identity(), Function.identity(), (oldData, newData) -> newData));
        return list1.parallelStream().filter(str->{
            return !tempMap.containsKey(str);
        }).collect(Collectors.toList());
    }

    /**
     * 交集(基于java8新特性)优化解法2 适用于大数据量
     * 求List1和List2中都有的元素
     */
    public static List<String> intersectList2(List<String> list1, List<String> list2){
        Map<String, String> tempMap = list2.parallelStream().collect(Collectors.toMap(Function.identity(), Function.identity(), (oldData, newData) -> newData));
        return list1.parallelStream().filter(str->{
            return tempMap.containsKey(str);
        }).collect(Collectors.toList());
    }

    /**
     * 并集(去重) 基于Java8新特性 适用于大数据量
     * 合并list1和list2 去除重复元素
     */
    public static List<String> distinctMergeList1(List<String> list1, List<String> list2){
        //第一步 先求出list1与list2的差集
        list1 = subList(list1,list2);
        //第二部 再合并list1和list2
        list1.addAll(list2);
        return list1;
    }

/*    public static String getFirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }*/

    /**
     * 替换字符串中的中文符号
     * @param str
     * @return
     */
    public static String replaceChineseSymbols(String str){
        StringBuffer resStr = new StringBuffer();

        if(StringUtil.isEmpty(str)){
            return resStr.toString();
        }
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char ch = arr[i];
            if(isChineseSymbol(String.valueOf(ch))){
                resStr.append("");
            }else{
                resStr.append(ch);
            }
        }
        return resStr.toString();
    }


    /**
     * 判断是否中文标点符号
     * @param str
     * @return
     */
    public static Boolean isChineseSymbol(String str){
        Pattern p = Pattern.compile("[\\uFF01]|[\\uFF0C-\\uFF0E]|[\\uFF1A-\\uFF1B]|[\\uFF1F]|[\\uFF08-\\uFF09]|[\\u3001-\\u3002]|[\\u3010-\\u3011]|[\\u201C-\\u201D]|[\\u2013-\\u2014]|[\\u2018-\\u2019]|[\\u2026]|[\\u3008-\\u300F]|[\\u3014-\\u3015]");
        Matcher m = p.matcher(String.valueOf(str));
        return m.find();
    }

    /**
     * 判断是否纯字母
     * @param str
     * @return
     */
    public static Boolean isLetter(String str){
        Pattern pattern2 = Pattern.compile("[a-zA-Z]+$");
        Matcher matcher2 = pattern2.matcher(str);
        return matcher2.matches();
    }

    /**
     * 字符对象串转map
     * @param content
     * @return
     */
    public static HashMap<String, Object> stringToMap(String content) {

        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        HashMap<String, Object> map = JSON.parseObject(content, new TypeReference<HashMap<String, Object>>() {
        });

        return map;

    }

    /**
     * 是否只看自己数据
     * 商家管理员 和 商家导师 可以看商家 所有订单 数据
     * 其他商家角色 只看自己订单数据
     * @param adminRole
     * @return
     */
    public static Boolean isSeeSelfData(String adminRole){
        if(isPlatformAdmin(adminRole)){
           return Boolean.FALSE;
        }
        if(RoleTypeEnum.SJ_MANEGE.getValue().equals(adminRole)
                || RoleTypeEnum.SJ_MENTOR.getValue().equals(adminRole)){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    /**
     * 是否商家
     * @param adminRole
     * @return
     */
    public static Boolean isBusiness(String adminRole){
        if(RoleTypeEnum.FRONT_CUSTOM.getValue().equals(adminRole)){
            return Boolean.FALSE;
        }
        if(isPlatformAdmin(adminRole)){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 是否平台人员
     * @param adminRole
     * @return
     */
    public static Boolean isPlatformAdmin(String adminRole){
        if(RoleTypeEnum.BYX_MANEGE.getValue().equals(adminRole)
                || RoleTypeEnum.BYX_OPERATE.getValue().equals(adminRole) ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 是否有账户服务权限
     * @param adminRole
     * @return
     */
    public static Boolean isAdminServiceRole(String adminRole){
        //如果是平台角色
        if(isPlatformAdmin(adminRole)){
            return Boolean.TRUE;
        }
        //如果是商家管理员
        if(RoleTypeEnum.SJ_MANEGE.getValue().equals(adminRole)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 结算员(平台角色、商家管理员、商家导师)
     * @param role
     * @return
     */
    public static Boolean isSettlementRole(String role){
        //如果是平台角色
        if(isPlatformAdmin(role)){
            return Boolean.TRUE;
        }
        //如果是商家管理员 、导师、老师
        if(RoleTypeEnum.SJ_MANEGE.getValue().equals(role)
                || RoleTypeEnum.SJ_MENTOR.getValue().equals(role)
                || RoleTypeEnum.SJ_TEACHER.getValue().equals(role)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 重命名上传文件名称
     * @param oldName
     * @return
     */
    public static String getFileReName(String oldName){
        return UUID.randomUUID().toString().replace("-", "")
                + System.currentTimeMillis()
                + RandomUtils.nextInt(0, 9999)
                + oldName.substring(oldName.lastIndexOf("."));
    }

    /**
     * 获取业务简单编码
     * @param yw
     * @return
     */
    public static String getSystemYwCode(String yw){
        return yw + System.currentTimeMillis() + RandomUtils.nextInt(0, 999);
    }

    /**
     * 校验企微返回状态
     * @param jsonObject
     * @return
     */
    public static boolean cropResStatus(JSONObject jsonObject) {
        // 没返回errcode视为调用成功
        if (!jsonObject.containsKey("errcode")) {
            return true;
        }
        boolean bool = jsonObject.getString("errcode").equals("0");
        if (!bool) {
            log.error(jsonObject.getString("errmsg"));
        }
        return bool;
    }



    /**
     * 自定义除法
     * @param fz
     * @param fm
     * @return
     */
    public static BigDecimal customDivision(BigDecimal fz, BigDecimal fm, Integer scale){
        if(BigDecimal.ZERO.compareTo(fz) == 0){
            return BigDecimal.ZERO;
        }
        if(BigDecimal.ZERO.compareTo(fm) == 0){
            return BigDecimal.valueOf(1);
        }
        return fz.divide(fm,scale,BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 获取微信默认昵称
     * @return
     */
    public static String getDefaultNickname(){
        return  "微信用户" + RandomUtils.nextInt(0, 99999999);
    }


    /**
     * 分转元
     * @param cent
     * @return
     */
    public static BigDecimal centToYuan(Integer cent){
        if(0 == cent){
            return BigDecimal.ZERO;
        }
        return customDivision(BigDecimal.valueOf(cent),BigDecimal.valueOf(100),2);
    }

    /**
     * 统计中文数量
     * @param str
     * @return
     */
    public static Integer countZwNum(String str){
        String regEx = "[\\u4e00-\\u9fa5]";
        String term = str.replaceAll(regEx, "aa");
        return term.length()-str.length();
    }


}
