package com.aeye.mifss.bas.controller;

import com.aeye.mifss.bas.dto.MedinsInfoDTO;
import com.aeye.mifss.bas.service.MedinsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "医疗机构信息管理")
@RestController
@RequestMapping("/bas/medinsInfo")
public class MedinsInfoController {

//    @Autowired
//    private MedinsInfoService medinsInfoBo;
//
//    @ApiOperation("根据ID查询医疗机构")
//    @GetMapping("/{id}")
//    public MedinsInfoDTO getById(@ApiParam("医疗机构ID") @PathVariable("id") String id) {
//        MedinsInfoDTO bean = medinsInfoBo.getById(id);
//        return bean;
//    }

}
