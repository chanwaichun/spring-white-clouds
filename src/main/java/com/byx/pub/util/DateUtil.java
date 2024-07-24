package com.byx.pub.util;
import org.joda.time.DateTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Calendar操作Date
 */
public class DateUtil {

    /**
     * Date-->String 转换
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(date);
    }

    public static String dateTimeToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String dateTimeToTimeString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return sdf.format(date);
    }

    /**
     * localDateTime转str
     * @param dateTime
     * @return
     */
    public static String LocalDateTimeToStr(LocalDateTime dateTime){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(fmt);
    }

    /**
     * LocalDateTime--> Date 转换
     * @param dateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime dateTime){
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
        Instant instant2 = zonedDateTime.toInstant();
        return Date.from(instant2);
    }

    /**
     * date转localDate
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }


    /**
     * 获取指定日期是月中的第几天
     * @param date
     */
    public static void getDayOfMonth(Date date){
        int day = date.getDate();
        System.out.println(day);
    }

    public static String yearString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    public static String timeString(Date date)
    {
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }
    /**
     * 对日期的【秒】进行加/减
     *
     * @param date    日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date    日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date  日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date  日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date   日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date  日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }





    public static String dayString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(date);
    }

    public static String monthString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(date);
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static Date getNextDay(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DAY_OF_MONTH, 1);//这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        return date;
    }


    public static Date dayStartTime(Date date) {
        String formatString = dateFormat(date);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(formatString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stringToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stringTToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String newDateToString() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String dayAndTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(date);

    }

    public static String dayAndTimes(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        return sdf.format(date);

    }

    public static String getDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        return sdf.format(date);
    }
    /**
     * 根据时间的毫秒值来获取当天的日期 日期格式 :yyyy-MM-dd
     * @param timeMillies(时间的毫秒值)
     * @return
     */
    public static String getStringDate(Long timeMillies)
    {
        Date date = new Date();
        date.setTime(timeMillies);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param createTime
     * @return
     */
    public static String convertTimeToFormat(Date createTime) {
        long curTime = System.currentTimeMillis() / 1000;
        long newTime = createTime.getTime() / 1000;
        long time = curTime - newTime;
        String s;
        if (time < 60 && time >= 0) {
            s = "立即";
        } else if (time >= 60 && time < 3600) {
            s = time / 60 + "分钟";
        } else if (time >= 3600 && time < 3600 * 24) {
            s = time / 3600 + "小时";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            s = time / 3600 / 24 + "天";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            s = time / 3600 / 24 / 30 + "个月";
        } else if (time >= 3600 * 24 * 30 * 12) {
            s = time / 3600 / 24 / 30 / 12 + "年";
        } else {
            s = "立即";
        }

        return s + "回复";
    }

    /**
     * 获取指定日期 前或后 的日期集合
     * @param date
     * @param num
     * @return
     */
    public static List<Date> getListDates(Date date,int num){

        List<Date> list = new ArrayList<Date>();
        Date targetDate = addDateDays(date, num);
        Date startDate = date;
        int index = num;
        if (num<0){
            startDate= targetDate;
            index = -num;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        list.add(cal.getTime());
        while (list.size()<index){
            cal.add(Calendar.DATE,1);
            list.add(cal.getTime());
        }
        return list;
    }

    /**
     * 获取今天的开始时间
     *
     * @return
     */

    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();

    }

    /**
     * 获取今天的结束时间
     *
     * @return
     */

    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /**
     * 获取昨天的开始时间
     *
     * @return
     */
    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getStartTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取昨天的结束时间
     *
     * @return
     */

    public static Date getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getEndTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }


    /**
     * 获取两个日期相差的月数
     */
    public static int getMonthDiff(Date dateMin, Date dateMax) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(dateMin);
        c2.setTime(dateMax);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值
        int yearInterval = year2 - year1;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
        return monthsDiff;
    }

    /**
     * 获取当月第一天开始时间
     * @return
     */
    public static  String getMonthFirstTime(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time) + " 00:00:00";
    }

    /**
     * 获取指定月份字符
     * @param num ±数
     * @return
     */
    public static String getUpMonthStr(Integer num){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, num);
        String format1 = format.format(calendar.getTime());
        return format1;
    }

    /**
     * 获取指定月份第一天的开始时间
     * @param dateStr 指定月份(2023-01)
     * @return
     */
    public static String getZhiDingMonthFirstTime(String dateStr){
        return dateStr + "-01 00:00:00";
    }

    public static String getZhiDingMonthLastTime(String dateStr){
        int year = Integer.parseInt(dateStr.split("-")[0]);
        int month = Integer.parseInt(dateStr.split("-")[1]);
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份 设置当前月的上一个月
        cal.set(Calendar.MONTH, month);
        // 获取某月最大天数 获取月份中的最小值，即第一天
        int lastDay = cal.getMinimum(Calendar.DATE);
        // 设置日历中月份的最大天数 上月的第一天减去1就是当月的最后一天
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1);
        //将小时至23
        cal.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        cal.set(Calendar.MINUTE, 59);
        //将秒至59
        cal.set(Calendar.SECOND,59);
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sf.format(cal.getTime());
        return format;

    }


    /**
     * 获取上一个月1号0点0分0秒的时间
     */
    public static String getBeforeFirstMonthDate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至00
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        //将分钟至00
        calendar.set(Calendar.MINUTE, 00);
        //将秒至00
        calendar.set(Calendar.SECOND,00);
        String format1 = format.format(calendar.getTime());
        return format1;
    }

    /**
     * 获取上个月的最后一天23点59分59秒的时间
     */
    public static String getBeforeLastMonthDate(){
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        calendar.set(Calendar.MINUTE, 59);
        //将秒至59
        calendar.set(Calendar.SECOND,59);
        String format = sf.format(calendar.getTime());
        return format;
    }

    /**
     * 时间戳转日期时分秒
     * @param timestamp
     * @return
     */
    public static String timestampToDateStr(long timestamp){
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取30分钟前的秒数
     * @return
     */
    public static long get30MinuteSeconds(){
        Date now = new Date();
        return DateUtil.addDateMinutes(now,-28).getTime() / 1000;
    }

    /**
     * 获取1小时5分钟前的秒数
     * @return
     */
    public static long get1Hour5MinuteSeconds(){
        Date now = new Date();
        return DateUtil.addDateMinutes(DateUtil.addDateHours(now, -1),-5).getTime() / 1000;
    }


    public static void main(String[] args) throws ParseException {
        long s = 1631008238000L;
        System.out.println(timestampToDateStr(s * 1000));


    }



}
