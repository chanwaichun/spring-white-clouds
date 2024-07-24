package com.byx.pub.util.excel;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Excel对象
 * @author Jump
 */
@Data
public class ExcelData implements Serializable {
    /**
     * 表头
     */
    private List<String> titles;

    /**
     * 数据
     */
    private List<List<Object>> rows;

    /**
     * 页签名称
     */
    private String name;
}
