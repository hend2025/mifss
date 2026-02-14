package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.ims.common.contoller.AeyeAbstractController;
import cn.hsa.ims.common.utils.AeyePageInfo;
import cn.hsa.ims.common.utils.AeyePageResult;
import com.aeye.mifss.bas.dto.ParaDTO;
import com.aeye.mifss.bas.service.RpcParaService;
import com.aeye.mifss.common.dto.RpcMergeDTO;
import com.aeye.mifss.common.mybatis.wrapper.RpcQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "场景监管参数管理")
@RestController
@RequestMapping("/bas/para")
public class ParaController extends AeyeAbstractController {

    @Autowired(required = false)
    private RpcParaService paraService;

    @ApiOperation("查询列表")
    @PostMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "int", paramType = "header", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条目", dataType = "int", paramType = "header", example = "10"),
            @ApiImplicitParam(name = "orderField", value = "排序字段", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "orderType", value = "排序类型", dataType = "string", paramType = "header", example = "asc或者desc")
    })
    public WrapperResponse<List<ParaDTO>> list(@RequestBody ParaDTO paraDTO) throws Exception {
        AeyePageInfo pageInfo = buildPageInfo();
        RpcMergeDTO<ParaDTO> rpcMergeDTO = new RpcMergeDTO(pageInfo, new RpcQueryWrapper<ParaDTO>()
                .like(ParaDTO::getParaName, paraDTO.getParaName())
                .like(ParaDTO::getParaDscr, paraDTO.getParaDscr()));
        AeyePageResult<ParaDTO> result = paraService.pageRpc(rpcMergeDTO);
        return (WrapperResponse) WrapperResponse.success(result);
    }

}
