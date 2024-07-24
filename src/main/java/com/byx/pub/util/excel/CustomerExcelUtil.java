package com.byx.pub.util.excel;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.byx.pub.bean.bo.ImportChangeDetailExcelBo;
import com.byx.pub.bean.vo.ConsultantIncomeVo;
import com.byx.pub.bean.vo.PageSettlementDetailListVo;
import com.byx.pub.util.StringUtil;
import lombok.Data;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ICE
 * @date 2021/9/3 17:16
 */
@Log
@Data
public class CustomerExcelUtil {
    /** Excel 2007 的后缀名 */
    private static final String EXECL_FILE_SUFFIX_2007="xlsx";
    /** Excel 2003 的后缀名 */
    private static final String EXECL_FILE_SUFFIX_2003="xls";





    /**
     * 加载结算详情数据
     * @param list
     * @return
     */
    public static List<List<Object>> loadValueRowSettlementDetail(List<PageSettlementDetailListVo> list){
        List<List<Object>> rowList = new ArrayList<>();
        for(PageSettlementDetailListVo vo : list){
            List<Object> rows = new ArrayList<>();
            if(StringUtil.notEmpty(vo.getId())){
                rows.add(vo.getId());
            }else{
                rows.add("");
            }
            if(StringUtil.notEmpty(vo.getOrderSn())){
                rows.add(vo.getOrderSn());
            }else{
                rows.add("");
            }
            if(StringUtil.notEmpty(vo.getBusinessName())){
                rows.add(vo.getBusinessName());
            }else{
                rows.add("");
            }
            if(StringUtil.notEmpty(vo.getSettlerAdminName())){
                rows.add(vo.getSettlerAdminName());
            }else{
                rows.add(vo.getSettlerUserName());
            }
            if(1 == vo.getShareType()){
                rows.add("拉新");
            }else if(2 == vo.getShareType()){
                rows.add("促成");
            }else{
                rows.add("其他");
            }
            rows.add(vo.getSettlementAmount());
            rows.add(vo.getCreateTime());
            rowList.add(rows);
        }
        return rowList;
    }

    /**
     * 导出支付流水列表值装载
     * @param list
     * @return
     */
    public static List<List<Object>> loadValueRowPayFlow(List<ConsultantIncomeVo> list){
        List<List<Object>> rowList = new ArrayList<>();
        for(ConsultantIncomeVo vo : list){
            List<Object> rows = new ArrayList<>();
            if(StringUtil.notEmpty(vo.getPaySn())){
                rows.add(vo.getPaySn());
            }else{
                rows.add("");
            }
            if(StringUtil.notEmpty(vo.getOrderSn())){
                rows.add(vo.getOrderSn());
            }else{
                rows.add("");
            }
            if(StringUtil.notEmpty(vo.getUserName())){
                rows.add(vo.getUserName());
            }else{
                rows.add("");
            }
            if(StringUtil.notEmpty(vo.getAdminName())){
                rows.add(vo.getAdminName());
            }else{
                rows.add("");
            }
            if(StringUtil.notEmpty(vo.getPayment())){
                rows.add(vo.getPayment());
            }else{
                rows.add("");
            }
            if(StringUtil.notEmpty(vo.getAmount())){
                rows.add(vo.getAmount());
            }else{
                rows.add("");
            }
            if(StringUtil.notEmpty(vo.getSuccessTime())){
                rows.add(vo.getSuccessTime());
            }else{
                rows.add("");
            }
            rowList.add(rows);
        }
        return rowList;
    }


}
