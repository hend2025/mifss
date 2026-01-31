package com.aeye.mifss.ipt.rpc;

import com.aeye.mifss.bio.dto.FaceFturDTO;

import java.util.List;

/**
 * 人脸特征 RPC 客户端接口
 * 统一的 RPC 调用抽象，支持 Feign 和 Dubbo 两种实现
 */
public interface FaceFturRpcClient {

    /**
     * 根据ID查询人脸特征
     * 
     * @param id 人脸特征ID
     * @return 人脸特征DTO
     */
    FaceFturDTO getById(String id);

    /**
     * 查询人脸特征列表
     * 
     * @param dto 查询条件
     * @return 人脸特征列表
     */
    List<FaceFturDTO> queryList(FaceFturDTO dto);
}
