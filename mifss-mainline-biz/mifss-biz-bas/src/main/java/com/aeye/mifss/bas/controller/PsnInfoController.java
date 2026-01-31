package com.aeye.mifss.bas.controller;

import com.aeye.mifss.bas.dto.PsnInfoDTO;
import com.aeye.mifss.bas.service.PsnInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "人员信息管理")
@RestController
@RequestMapping("/bas/psnInfo")
public class PsnInfoController {

//    @Autowired
//    private PsnInfoService psnInfoService;
//
//    @ApiOperation("根据ID查询人员")
//    @GetMapping("/{id}")
//    public PsnInfoDTO getById(@ApiParam("人员模板号") @PathVariable("id") String id) {
//        PsnInfoDTO bean = psnInfoService.getById(id);
//        return bean;
//    }

}
