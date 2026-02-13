package com.aeye.mifss.bas.common;

import cn.hsa.ims.common.cache.AeyeCacheManager;
import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import cn.hutool.core.bean.BeanUtil;
import com.aeye.mifss.bas.biz.entity.DicDO;
import com.aeye.mifss.bas.biz.service.DicService;
import com.aeye.mifss.bas.dto.DicDTO;

import java.util.List;

public class DicCacheUtil {

    public static void reloadCache() {
        DicService dicService = AeyeSpringContextUtils.getBean("dicServiceImpl");
        dicService.reloadCache();
    }

    public static List<DicDTO> getDicAll() {
        return AeyeCacheManager.getList("dicList",DicDTO.class);
    }

    public static DicDTO getDicById(String id) {
        DicDO bean = AeyeCacheManager.get("dic",id,DicDO.class);
        if(bean == null){
            return null;
        }
        return BeanUtil.toBean(bean,DicDTO.class);
    }

    public static List<DicDTO> getDicByType(String dicType) {
        return AeyeCacheManager.getList("dicList:"+dicType,DicDTO.class);
    }

}
