package com.aeye.mifss.common.config;

import cn.hsa.hsaf.auth.security.entity.PortalUserDetails;
import com.alibaba.fastjson.JSON;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Map;

public class AeyeFixedPrincipalExtractor implements PrincipalExtractor {
    private static final String[] PRINCIPAL_KEYS = new String[]{"principal"};

    public AeyeFixedPrincipalExtractor() {
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        String[] var2 = PRINCIPAL_KEYS;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String key = var2[var4];
            if (map.containsKey(key)) {

                return JSON.parseObject(JSON.toJSONString(map.get(key)), PortalUserDetails.class);
            }
        }

        return null;
    }
}
