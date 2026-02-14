package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.ims.common.contoller.AeyeAbstractController;
import cn.hsa.ims.common.utils.AeyePageInfo;
import cn.hsa.ims.common.utils.AeyePageResult;
import cn.hutool.core.util.StrUtil;
import com.aeye.mifss.bas.dto.CrtfDTO;
import com.aeye.mifss.bas.service.RpcCrtfService;
import com.aeye.mifss.common.dto.RpcMergeDTO;
import com.aeye.mifss.common.mybatis.wrapper.RpcQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "认证记录管理")
@RestController
@RequestMapping("/crtf")
public class CrtfController extends AeyeAbstractController {

    @Autowired(required = false)
    private RpcCrtfService crtfService;

    @ApiOperation("查询列表")
    @PostMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "int", paramType = "header", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条目", dataType = "int", paramType = "header", example = "10"),
            @ApiImplicitParam(name = "orderField", value = "排序字段", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "orderType", value = "排序类型", dataType = "string", paramType = "header", example = "asc或者desc")
    })
    public WrapperResponse<List<CrtfDTO>> list(@RequestBody CrtfDTO crtfDTO) throws Exception {
        AeyePageInfo pageInfo = buildPageInfo();
        RpcMergeDTO<CrtfDTO> rpcMergeDTO = new RpcMergeDTO(pageInfo, new RpcQueryWrapper<CrtfDTO>()
                .eq(StrUtil.isNotBlank(crtfDTO.getPsnName()), CrtfDTO::getPsnName, crtfDTO.getPsnName())
                .ge(crtfDTO.getStartTime()!=null,CrtfDTO::getCrtfTime,crtfDTO.getStartTime())
                .le(crtfDTO.getEndTime()!=null,CrtfDTO::getCrtfTime,crtfDTO.getEndTime())
                .like(StrUtil.isNotBlank(crtfDTO.getMedinsName()), CrtfDTO::getMedinsName, crtfDTO.getMedinsName())
        );
        AeyePageResult<CrtfDTO> result = crtfService.pageRpc(rpcMergeDTO);
        return (WrapperResponse) WrapperResponse.success(result);
    }

}
