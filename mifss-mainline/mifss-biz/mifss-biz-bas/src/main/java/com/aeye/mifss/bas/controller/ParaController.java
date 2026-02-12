package com.aeye.mifss.bas.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.ims.common.contoller.AeyeAbstractController;
import cn.hsa.ims.common.utils.AeyePageInfo;
import cn.hsa.ims.common.utils.AeyePageResult;
import com.aeye.mifss.bas.dto.ParaDTO;
import com.aeye.mifss.bas.entity.ParaDO;
import com.aeye.mifss.bas.service.ParaService;
import com.aeye.mifss.common.utils.AeyeBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
            @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "int", paramType = "header",  example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条目", dataType = "int", paramType = "header", example = "10"),
            @ApiImplicitParam(name = "orderField", value = "排序字段", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "orderType", value = "排序类型", dataType = "string", paramType = "header", example = "asc或者desc")
    })
    public WrapperResponse<List<ParaDTO>> list() throws Exception {
        AeyePageInfo pageInfo = buildPageInfo();
        AeyePageResult pageData = paraService.page(pageInfo, new LambdaQueryWrapper<ParaDO>()
                .like(ParaDO::getParaName, "check")
                .last("limit 10"));
        return (WrapperResponse) WrapperResponse.success(pageData);
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "/info/{KeyId}")
    public WrapperResponse<ParaDTO> info(@PathVariable("KeyId") String KeyId) throws Exception {
        ParaDO bean = paraService.getById(KeyId);
        return WrapperResponse.success(AeyeBeanUtils.copyBean(bean, ParaDTO.class));
    }

}
