package com.aeye.mifss.ipt.controller;

import com.aeye.mifss.bio.dto.FaceFturDTO;
import com.aeye.mifss.bio.service.FaceFturDubboService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "RPC测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    @DubboReference
    private FaceFturDubboService faceFturDubboService;

    @ApiOperation("RPC调用-根据ID查询人脸特征")
    @GetMapping("/{id}")
    public FaceFturDTO getById(@ApiParam("人脸特征ID") @PathVariable("id") String id) {
        FaceFturDTO bean = faceFturDubboService.getById(id);
        return bean;
    }

    @ApiOperation("RPC调用-查询人脸特征列表")
    @PostMapping("/queryList")
    public List<FaceFturDTO> queryList(@RequestBody FaceFturDTO dto) {
        List<FaceFturDTO> list = faceFturDubboService.queryList(dto);
        return list;
    }

}
