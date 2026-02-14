package com.aeye.mifss.bas.biz.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.ims.common.contoller.AeyeAbstractController;
import cn.hsa.ims.common.utils.AeyePageResult;
import com.aeye.mifss.bas.biz.entity.CrtfDO;
import com.aeye.mifss.bas.biz.service.CrtfService;
import com.aeye.mifss.bas.dto.CrtfDTO;
import com.aeye.mifss.common.utils.AeyeBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证记录Controller
 */
@Api(tags = "认证记录管理")
@RestController
@RequestMapping("/bas/crtf")
public class CrtfController extends AeyeAbstractController {

    @Autowired
    private CrtfService crtfService;

    @ApiOperation("列表查询")
    @PostMapping("/list")
    public WrapperResponse<List<CrtfDTO>> list() throws Exception {
        AeyePageResult pageData = crtfService.page(buildPageInfo(), new LambdaQueryWrapper<CrtfDO>());
        return (WrapperResponse) WrapperResponse.success(pageData);
    }

    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    public WrapperResponse<CrtfDTO> info(@PathVariable("id") String id) throws Exception {
        CrtfDO entity = crtfService.getById(id);
        return WrapperResponse.success(AeyeBeanUtils.copyBean(entity, CrtfDTO.class));
    }
}
