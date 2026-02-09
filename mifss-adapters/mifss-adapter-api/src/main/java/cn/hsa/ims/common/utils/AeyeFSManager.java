package cn.hsa.ims.common.utils;

import cn.hsa.hsaf.core.fsstore.FSEntity;
import cn.hsa.hsaf.core.fsstore.FSManager;

public class AeyeFSManager {

    public static FSEntity getObject(String var1, String var2){
        FSManager fsManager = AeyeSpringContextUtils.getBean(FSManager.class);
        FSEntity entity = fsManager.getObject(var1, var2);
        return entity;
    }

    public static FSEntity putObject(String var1, FSEntity var2){
        FSManager fsManager = AeyeSpringContextUtils.getBean(FSManager.class);
        FSEntity entity = fsManager.putObject(var1,var2);
        return entity;
    }

    public static boolean deleteObject(String var1, String var2){
        FSManager fsManager = AeyeSpringContextUtils.getBean(FSManager.class);
        return fsManager.deleteObject(var1, var2);
    }

}
