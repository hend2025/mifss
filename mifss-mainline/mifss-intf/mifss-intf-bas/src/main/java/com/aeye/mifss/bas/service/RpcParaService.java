package com.aeye.mifss.bas.service;

import com.aeye.mifss.bas.dto.ParaDTO;
import com.aeye.mifss.common.mybatis.service.RpcService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;

@FeignClient(
        value = "${feign_bio_application.name:mifss-bas}",
        path = "${feign_bio_application.context:/mifss/bas}/cloud/para",
        configuration = FeignClientsConfiguration.class
)
public interface RpcParaService extends RpcService<ParaDTO> {

}
