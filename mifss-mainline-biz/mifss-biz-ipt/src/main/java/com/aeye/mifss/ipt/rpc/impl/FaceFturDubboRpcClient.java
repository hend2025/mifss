package com.aeye.mifss.ipt.rpc.impl;

import com.aeye.mifss.bio.dto.FaceFturDTO;
import com.aeye.mifss.bio.service.FaceFturDubboService;
import com.aeye.mifss.ipt.rpc.FaceFturRpcClient;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 人脸特征 Dubbo3 实现的 RPC 客户端
 */
@Component
@ConditionalOnProperty(name = "mifss.rpc.type", havingValue = "dubbo")
public class FaceFturDubboRpcClient implements FaceFturRpcClient {

    @DubboReference
    private FaceFturDubboService faceFturDubboService;

    @Override
    public FaceFturDTO getById(String id) {
        System.out.println("===== Dubbo getById ======");
        return faceFturDubboService.getById(id);
    }

    @Override
    public List<FaceFturDTO> queryList(FaceFturDTO dto) {
        System.out.println("===== Dubbo queryList ======");
        return faceFturDubboService.queryList(dto);
    }

}
