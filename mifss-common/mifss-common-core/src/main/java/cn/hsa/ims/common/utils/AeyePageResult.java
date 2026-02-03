package cn.hsa.ims.common.utils;

import cn.hsa.hsaf.core.framework.util.PageResult;
import java.util.Map;

public class AeyePageResult<T> extends PageResult<T> {
    private Map<Object, Object> extraDataMap;

    public Map<Object, Object> getExtraDataMap() {
        return this.extraDataMap;
    }

    public void setExtraDataMap(Map<Object, Object> extraDataMap) {
        this.extraDataMap = extraDataMap;
    }
}
