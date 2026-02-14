package com.aeye.mifss.bas.service;

import com.aeye.mifss.bas.dto.CrtfDTO;
import com.aeye.mifss.common.mybatis.service.RpcService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;

/**
 * 认证记录RPC服务接口
 */
@FeignClient(value = "${feign_bas_application.name:mifss-bas}", path = "${feign_bas_application.context:/mifss/bas}/cloud/crtf", configuration = FeignClientsConfiguration.class)
public interface RpcCrtfService extends RpcService<CrtfDTO> {

}
