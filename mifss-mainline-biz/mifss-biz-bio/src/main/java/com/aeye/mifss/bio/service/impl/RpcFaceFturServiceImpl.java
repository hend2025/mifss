package com.aeye.mifss.bio.service.impl;

import com.aeye.mifss.bio.bo.FaceFturBo;
import com.aeye.mifss.bio.dto.FaceFturDTO;
import com.aeye.mifss.bio.entity.FaceFturDO;
import com.aeye.mifss.bio.service.FaceFturDubboService;
import com.aeye.mifss.common.core.service.AbstractBaseServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 人脸特征服务实现
 * 同时作为本地 Spring 服务和 Dubbo RPC 服务
 */
@Service
@DubboService(interfaceClass = FaceFturDubboService.class)
public class RpcFaceFturServiceImpl extends AbstractBaseServiceImpl<FaceFturDTO, FaceFturDO, FaceFturBo>
        implements FaceFturDubboService {

    @Autowired
    private FaceFturBo faceFturBo;

    @Override
    protected FaceFturBo getBo() {
        return faceFturBo;
    }

}
