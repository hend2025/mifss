package com.aeye.mifss.ipt.rpc.impl;

import com.aeye.mifss.bio.dto.FaceFturDTO;
import com.aeye.mifss.bio.service.FaceFturFeignClient;
import com.aeye.mifss.ipt.rpc.FaceFturRpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 人脸特征 Feign 实现的 RPC 客户端
 */
@Component
@ConditionalOnProperty(name = "mifss.rpc.type", havingValue = "feign", matchIfMissing = true)
public class FaceFturFeignRpcClient implements FaceFturRpcClient {

    @Autowired
    private FaceFturFeignClient faceFturFeignClient;

    @Override
    public FaceFturDTO getById(String id) {
        System.out.println("===== Feign getById ======");
        return faceFturFeignClient.getById(id);
    }

    @Override
    public List<FaceFturDTO> queryList(FaceFturDTO dto) {
        System.out.println("===== Feign queryList ======");
        return faceFturFeignClient.queryList(dto);
    }
}
