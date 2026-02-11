package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.ims.common.contoller.AeyeAbstractController;
import cn.hsa.ims.common.utils.AeyePageInfo;
import com.aeye.mifss.bas.dto.ParaDTO;
import com.aeye.mifss.bas.service.RpcParaService;
import com.aeye.mifss.common.mybatis.wrapper.RpcQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
            @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "int", paramType = "header"),
            @ApiImplicitParam(name = "pageSize", value = "每页条目", dataType = "int", paramType = "header"),
            @ApiImplicitParam(name = "orderField", value = "排序字段", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "orderType", value = "排序类型", dataType = "string", paramType = "header", example = "asc或者desc")
    })
    public WrapperResponse<List<ParaDTO>> list() throws Exception {
        AeyePageInfo pageInfo = buildPageInfo();
        RpcQueryWrapper<ParaDTO> queryWrapper = new RpcQueryWrapper<ParaDTO>();
        Page<ParaDTO> page = new Page<>();
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageSize());
        queryWrapper.like(ParaDTO::getParaName,"check");
        queryWrapper.setLastSql("limit 10");
        Page<ParaDTO> list = (Page<ParaDTO>) paraService.pageRpc(page,queryWrapper);
        return (WrapperResponse) WrapperResponse.success(list);
    }

}
