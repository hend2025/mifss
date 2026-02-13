package com.aeye.mifss.bas.common;

import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import com.aeye.mifss.bas.biz.service.DicService;
import com.aeye.mifss.bas.dto.DicDTO;

import java.util.ArrayList;
import java.util.List;

public class DicCacheUtil {

    public static void reloadCache() {
        DicService dicService = AeyeSpringContextUtils.getBean("dicService");
        dicService.reloadCache();
    }

    public static List<DicDTO> getDicAll() {
        return new ArrayList<>();
    }

    public static DicDTO getDicById(String id) {
        return new DicDTO();
    }

    public static List<DicDTO> getDicByDicType(String dicType) {
        return new ArrayList<>();
    }

}
