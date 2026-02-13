package com.aeye.mifss.bas.service;

import com.aeye.mifss.bas.dto.DicDTO;
import com.aeye.mifss.common.mybatis.service.RpcService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;

@FeignClient(
        value = "${feign_bas_application.name:mifss-bas}",
        path = "${feign_bas_application.context:/mifss/bas}/cloud/dic",
        configuration = FeignClientsConfiguration.class)
public interface RpcDicService extends RpcService<DicDTO> {

}
