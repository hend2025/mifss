package com.aeye.mifss.common.dto;

import com.aeye.mifss.common.mybatis.wrapper.RpcQueryWrapper;
import com.aeye.mifss.common.utils.AeyePageInfo;
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
