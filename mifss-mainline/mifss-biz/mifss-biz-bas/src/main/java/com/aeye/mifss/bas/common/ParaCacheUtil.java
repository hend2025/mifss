package com.aeye.mifss.bas.common;

import cn.hsa.ims.common.cache.AeyeCacheManager;
import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import cn.hutool.core.bean.BeanUtil;
import com.aeye.mifss.bas.biz.entity.ParaDO;
import com.aeye.mifss.bas.biz.service.ParaService;
import com.aeye.mifss.bas.dto.ParaDTO;

import java.util.ArrayList;
import java.util.List;

public class ParaCacheUtil {

    public static void reloadCache() {
        ParaService paraService = AeyeSpringContextUtils.getBean("paraService");
        paraService.reloadCache();
    }

    public static List<ParaDTO> getDicAll() {
        List<ParaDO> list = AeyeCacheManager.getList("paraList", ParaDO.class);
        if(list==null||list.size()==0){
            return null;
        }
        return  BeanUtil.copyToList(list, ParaDTO.class);
    }

    public static ParaDTO getDicById(String id) {
        ParaDO paraDO = AeyeCacheManager.get(id, ParaDO.class);
        if(paraDO==null){
            return null;
        }
        return BeanUtil.toBean(paraDO, ParaDTO.class);
    }

    public static List<ParaDTO> getDicByParaType(String paraType) {
        return new ArrayList<>();
    }

}
