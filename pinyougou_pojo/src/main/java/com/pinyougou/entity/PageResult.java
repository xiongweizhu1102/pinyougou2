package com.pinyougou.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author bear
 * @version 1.0
 * @description com.pinyougou.manager.controller.sellergoods.service.entity
 * @date 2018/3/23
 */
public class PageResult implements Serializable {
    private long total;
    private List rows;

    public PageResult(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }
    public PageResult() {

    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
