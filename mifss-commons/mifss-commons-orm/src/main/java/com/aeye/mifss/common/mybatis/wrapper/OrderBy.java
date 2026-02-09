package com.aeye.mifss.common.mybatis.wrapper;

import java.io.Serializable;

/**
 * 排序条件
 */
public class OrderBy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 排序字段
     */
    private String column;

    /**
     * 是否升序
     */
    private boolean asc;

    public OrderBy() {
    }

    public OrderBy(String column, boolean asc) {
        this.column = column;
        this.asc = asc;
    }

    public static OrderBy asc(String column) {
        return new OrderBy(column, true);
    }

    public static OrderBy desc(String column) {
        return new OrderBy(column, false);
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
