package com.aeye.mifss.bio.service.impl;

import com.aeye.mifss.bio.bo.FaceFturBo;
import com.aeye.mifss.bio.dto.FaceFturDTO;
import com.aeye.mifss.bio.entity.FaceFturDO;
import com.aeye.mifss.bio.service.FaceFturDubboService;
import com.aeye.mifss.bio.service.FaceFturService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cn.hutool.core.bean.BeanUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 人脸特征服务实现
 * 同时作为本地 Spring 服务和 Dubbo RPC 服务
 */
@Service
@DubboService(interfaceClass = FaceFturDubboService.class)
public class FaceFturServiceImpl implements FaceFturService, FaceFturDubboService {

    @Autowired
    FaceFturBo faceFturBo;

    @Override
    public FaceFturDTO getById(String id) {
        FaceFturDO bean = faceFturBo.getById(id);
        if (bean == null) {
            return null;
        }
        return BeanUtil.copyProperties(bean, FaceFturDTO.class);
    }

    @Override
    public List<FaceFturDTO> queryList(FaceFturDTO dto) {
        List<FaceFturDO> list = faceFturBo.list(new LambdaQueryWrapper<FaceFturDO>().last("limit 10"));
        if (list == null || list.isEmpty()) {
            return null;
        }
        return BeanUtil.copyToList(list, FaceFturDTO.class);
    }

}
