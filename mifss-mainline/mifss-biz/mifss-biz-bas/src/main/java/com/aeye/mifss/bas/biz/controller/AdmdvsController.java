package com.aeye.mifss.bas.biz.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.ims.common.contoller.AeyeAbstractController;
import cn.hsa.ims.common.utils.AeyePageResult;
import com.aeye.mifss.bas.common.DicCacheUtil;
import com.aeye.mifss.bas.dto.AdmdvsDTO;
import com.aeye.mifss.bas.biz.entity.AdmdvsDO;
import com.aeye.mifss.bas.biz.service.AdmdvsService;
import com.aeye.mifss.bas.dto.DicDTO;
import com.aeye.mifss.common.utils.AeyeBeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "医保区划信息表管理")
@RestController
@RequestMapping("/bas/admdvs")
public class AdmdvsController extends AeyeAbstractController {

    @Autowired
    private AdmdvsService admdvsService;

    @ApiOperation("列表")
    @PostMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "int", paramType = "header", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条目", dataType = "int", paramType = "header", example = "10"),
            @ApiImplicitParam(name = "orderField", value = "排序字段", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "orderType", value = "排序类型", dataType = "string", paramType = "header", example = "asc或者desc")
    })
    public WrapperResponse<List<AdmdvsDTO>> list() throws Exception {
        AeyePageResult pageData = admdvsService.page(buildPageInfo(), new LambdaQueryWrapper<AdmdvsDO>());

        return (WrapperResponse) WrapperResponse.success(pageData);
    }

    @ApiOperation(value = "详细")
    @GetMapping(value = "/info/{keyId}")
    public WrapperResponse<AdmdvsDTO> info(@PathVariable("keyId") String keyId) throws Exception {
        System.out.println();
        List<DicDTO>  list  = DicCacheUtil.getDicAll();
        System.out.println(" list size = "+list.size());

        list  = DicCacheUtil.getDicByDicType("AGNTER_RLTS");
        System.out.println(" list size = "+list.size());

        DicDTO dicDTO = DicCacheUtil.getDicById("AGNTER_RLTS_1");
        System.out.println(dicDTO);

        AdmdvsDO bean = admdvsService.getById(keyId);
        return WrapperResponse.success(AeyeBeanUtils.copyBean(bean, AdmdvsDTO.class));
    }

}
