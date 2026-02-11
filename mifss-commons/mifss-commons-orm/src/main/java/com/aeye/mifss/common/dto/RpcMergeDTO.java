package com.aeye.mifss.common.dto;

import cn.hsa.ims.common.utils.AeyePageInfo;
import com.aeye.mifss.common.mybatis.wrapper.RpcQueryWrapper;
import lombok.Data;

import java.io.Serializable;

@Data
public class RpcMergeDTO<DTO> implements Serializable {
    private AeyePageInfo pageParam;
    private RpcQueryWrapper<DTO> queryWrapper;
    private DTO dto;

    public RpcMergeDTO(AeyePageInfo pageParam, RpcQueryWrapper<DTO> queryWrapper) {
        this.pageParam = pageParam;
        this.queryWrapper = queryWrapper;
    }

}
