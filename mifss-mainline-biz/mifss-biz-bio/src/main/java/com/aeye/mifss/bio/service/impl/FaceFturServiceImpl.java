package com.aeye.mifss.bio.service.impl;

import com.aeye.mifss.bio.bo.FaceFturBo;
import com.aeye.mifss.bio.dto.FaceFturDTO;
import com.aeye.mifss.bio.entity.FaceFturDO;
import com.aeye.mifss.bio.service.FaceFturService;
import com.aeye.mifss.bio.service.RpcFaceFturService;
import com.aeye.mifss.common.mybatis.service.BaseServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 人脸特征服务实现
 * 同时作为本地 Spring 服务和 Dubbo RPC 服务
 */
@Service
@DubboService(interfaceClass = RpcFaceFturService.class)
public class FaceFturServiceImpl extends BaseServiceImpl<FaceFturDTO, FaceFturDO, FaceFturBo>
        implements FaceFturService, RpcFaceFturService {

}
