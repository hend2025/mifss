package com.aeye.mifss.bas.common;

import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import com.aeye.mifss.bas.biz.service.AdmdvsService;
import com.aeye.mifss.bas.dto.AdmdvsDTO;

import java.util.ArrayList;
import java.util.List;

public class AdmdvsCacheUtil {

    public static void reloadCache() {
        AdmdvsService admdvsService = AeyeSpringContextUtils.getBean("admdvsService");
        admdvsService.reloadCache();
    }

    public static List<AdmdvsDTO> getAdmdvsAll() {
        return new ArrayList<>();
    }

    public static AdmdvsDTO getDicById(String id) {
        return new AdmdvsDTO();
    }

    public static List<AdmdvsDTO> getAdmdvsByPId(String parentId) {
        return new ArrayList<>();
    }

    public static List<AdmdvsDTO> getAllChildByPId(String parentId) {
        return new ArrayList<>();
    }

}
