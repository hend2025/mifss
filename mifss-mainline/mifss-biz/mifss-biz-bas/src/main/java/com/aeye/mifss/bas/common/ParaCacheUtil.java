package com.aeye.mifss.bas.common;

import cn.hsa.ims.common.cache.AeyeCacheManager;
import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import cn.hutool.core.bean.BeanUtil;
import com.aeye.mifss.bas.biz.entity.ParaDO;
import com.aeye.mifss.bas.biz.service.ParaService;
import com.aeye.mifss.bas.dto.ParaDTO;

import java.util.List;

public class ParaCacheUtil {

    public static void reloadCache() {
        ParaService paraService = AeyeSpringContextUtils.getBean("paraServiceImpl");
        paraService.reloadCache();
    }

    public static List<ParaDTO> getDicAll() {
        return AeyeCacheManager.getList("paraList",ParaDTO.class);
    }

    public static ParaDTO getDicById(String id) {
        ParaDO bean = AeyeCacheManager.get("para",id,ParaDO.class);
        if(bean == null){
            return null;
        }
        return BeanUtil.toBean(bean,ParaDTO.class);
    }

    public static List<ParaDTO> getDicByParaType(String paraType) {
        return AeyeCacheManager.getList("paraList:"+paraType, ParaDTO.class);
    }

}
