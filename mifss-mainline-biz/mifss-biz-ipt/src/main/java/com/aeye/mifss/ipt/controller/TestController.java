package com.aeye.mifss.ipt.controller;

import com.aeye.mifss.bio.dto.FaceFturDTO;
import com.aeye.mifss.ipt.rpc.FaceFturRpcClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "RPC测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private FaceFturRpcClient faceFturFeignClient;

    @ApiOperation("RPC调用-根据ID查询人脸特征")
    @GetMapping("/{id}")
    public FaceFturDTO getById(@ApiParam("人脸特征ID") @PathVariable("id") String id) {
        FaceFturDTO bean = faceFturFeignClient.getById(id);
        return bean;
    }

    @ApiOperation("RPC调用-查询人脸特征列表")
    @PostMapping("/queryList")
    public List<FaceFturDTO> queryList(@RequestBody FaceFturDTO dto) {
        List<FaceFturDTO> list = faceFturFeignClient.queryList(dto);
        return list;
    }

}
