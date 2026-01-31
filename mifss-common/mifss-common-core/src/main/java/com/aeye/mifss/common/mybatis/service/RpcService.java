package com.aeye.mifss.common.mybatis.service;

import com.aeye.mifss.common.mybatis.wrapper.RpcQueryWrapper;

import java.io.Serializable;
import java.util.List;

public interface RpcService<DTO> {

    /**
     * 根据 ID 查询
     */
    DTO getByIdRpc(Serializable id);

    /**
     * 根据条件查询一条记录
     */
    DTO getOneRpc(RpcQueryWrapper<DTO> queryWrapper);

    /**
     * 根据条件查询列表
     */
    List<DTO> listRpc(RpcQueryWrapper<DTO> queryWrapper);
    
}
