package cn.hsa.ims.common.utils;

import cn.hsa.hsaf.core.framework.util.PageInfo;

public class AeyePageInfo<T> extends PageInfo {

    private boolean searchCount = true;

    private T paramsObj;

    public T getParamsObj() {
        return paramsObj;
    }

    public void setParamsObj(T paramsObj) {
        this.paramsObj = paramsObj;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }
}
