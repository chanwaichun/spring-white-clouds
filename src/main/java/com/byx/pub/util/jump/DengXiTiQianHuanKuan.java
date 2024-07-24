package com.byx.pub.util.jump;

import java.math.BigDecimal;

/**
 * @author Jump
 * @date 2023/9/14 11:31:15
 */
public class DengXiTiQianHuanKuan {



    public static void main(String[] args) {
        //定义参数
        BigDecimal invest = new BigDecimal(1140000); // 本金
        int month = 360;
        BigDecimal yearRate = new BigDecimal(0.056); // 年利率
        //每月月供
        BigDecimal monthCost = getPerMonthPrincipalInterest(invest, yearRate, month);
        System.out.println("每月还款本息：" + monthCost+"，需还款："+month + "月，年利率：" +
                yearRate.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP) + "%");
        //循环计算
        for(int i = 1; i < month+1;i++){
            //月利息
            BigDecimal monthInterest = getPerMonthInterest(invest, yearRate, month, i);
            //月本金
            BigDecimal monthPrincipal = monthCost.subtract(monthInterest).setScale(2,BigDecimal.ROUND_HALF_UP);
            System.out.println(i+"月-->还款本金：" + monthPrincipal+"，还款利息：" + monthInterest);
        }
    }





    /**
     * 等额本息的每月偿还利息
     * <p>
     * 公式：每月偿还利息 = 贷款本金 × 月利率 ×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
     * @param invest     总借款额（贷款本金,分）
     * @param yearRate   年利率
     * @param totalMonth 还款总月数
     * @return 每月偿还利息(入1 单位分)
     */
    public static BigDecimal getPerMonthInterest(BigDecimal invest, BigDecimal yearRate, int totalMonth,int nowMonth) {
        //月利率
        BigDecimal monthRate = yearRate.divide(new BigDecimal(12),10,BigDecimal.ROUND_HALF_UP);
        //贷款本金 × 月利率
        BigDecimal multiply = invest.multiply(monthRate);
        //〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕
        BigDecimal sub = monthRate.add(new BigDecimal(1)).pow(totalMonth).subtract(monthRate.add(new BigDecimal(1)).pow(totalMonth-1));
        //〔(1+月利率)^还款月数-1〕
        return multiply.multiply(sub).divide(monthRate.add(new BigDecimal(1)).pow(nowMonth - 1),10,BigDecimal.ROUND_HALF_UP)
                .setScale(2,BigDecimal.ROUND_HALF_UP);
    }





    /**
     * 每月偿还本金和利息
     * <p>
     * 公式：每月偿还本息= 贷款本金×〔月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
     * @param invest     总借款额（贷款本金,单位分）
     * @param yearRate   年利率
     * @param totalMonth 还款总月数
     * @return 每月偿还本金和利息(入1 单位分)
     */
    public static BigDecimal getPerMonthPrincipalInterest(BigDecimal invest, BigDecimal yearRate, int totalMonth) {
        BigDecimal monthRate = yearRate.divide(new BigDecimal(12),30,BigDecimal.ROUND_HALF_UP);
        //〔月利率×(1＋月利率)＾还款月数〕
        BigDecimal pow1 = monthRate.multiply(monthRate.add(new BigDecimal(1)).pow(totalMonth));
        //〔(1＋月利率)＾还款月数-1〕
        BigDecimal pow2 = monthRate.add(new BigDecimal(1)).pow(totalMonth - 1);
        //贷款本金×〔月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
        return invest.multiply(pow1).divide(pow2,2,BigDecimal.ROUND_HALF_UP);
    }
}
