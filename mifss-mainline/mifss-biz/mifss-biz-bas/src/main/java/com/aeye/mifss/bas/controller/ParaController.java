package com.aeye.mifss.bas.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.ims.common.contoller.AeyeAbstractController;
import cn.hsa.ims.common.utils.AeyePageInfo;
import com.aeye.mifss.bas.dto.ParaDTO;
import com.aeye.mifss.bas.entity.ParaDO;
import com.aeye.mifss.bas.service.ParaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "场景监管参数管理")
@RestController
@RequestMapping("/bas/para")
public class ParaController extends AeyeAbstractController {

    @Autowired
    private ParaService paraService;

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
        LambdaQueryWrapper<ParaDO> queryWrapper = new LambdaQueryWrapper<ParaDO>();
        queryWrapper.like(ParaDO::getParaName,"check");
        queryWrapper.last("limit 10");
        Page<ParaDO> page = new Page<>();
        page.setCurrent(pageInfo.getPageNum());
        page.setSize(pageInfo.getPageSize());
        Page<ParaDO> list = (Page<ParaDO>) paraService.page(page,queryWrapper);
        return (WrapperResponse) WrapperResponse.success(list);
    }

}
