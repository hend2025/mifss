package cn.hsa.ims.common.utils;

import cn.hsa.hsaf.idgenerator.IdGenerator;

import java.util.UUID;

public class AeyeIdGeneratorUtil {

    public static Long nextSF(){
        IdGenerator idGenerator = AeyeSpringContextUtils.getBean(IdGenerator.class);
        return idGenerator.next("");
    }

    public static Long nextRedis(String seqKey){
        IdGenerator idGenerator = AeyeSpringContextUtils.getBean(IdGenerator.class);
        return idGenerator.next(seqKey);
    }

    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
