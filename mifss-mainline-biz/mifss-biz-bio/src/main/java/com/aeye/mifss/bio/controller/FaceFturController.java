package com.aeye.mifss.bio.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aeye.mifss.bio.dto.FaceFturDTO;
import com.aeye.mifss.bio.entity.FaceFturDO;
import com.aeye.mifss.bio.service.FaceFturService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "人脸特征管理")
@RestController
@RequestMapping("/faceFtur")
public class FaceFturController {

    @Autowired
    private FaceFturService faceFturService;

    @ApiOperation("根据ID查询人脸特征")
    @GetMapping("/{id}")
    public FaceFturDTO getById(@ApiParam("人脸特征ID") @PathVariable("id") String id) {

        System.out.println("11111111111");

        FaceFturDO bean = new FaceFturDO();

        bean = faceFturService.getById(id);

        bean = faceFturService.getOne(new LambdaQueryWrapper<FaceFturDO>().last("limit 1"));

        FaceFturDTO  dto = BeanUtil.copyProperties(bean, FaceFturDTO.class);

        return dto;
    }

    @ApiOperation("查询人脸特征列表")
    @PostMapping("/queryList")
    public List<FaceFturDO> queryList(@RequestBody FaceFturDTO dto) {
        List<FaceFturDO> list = faceFturService.list(new LambdaQueryWrapper<FaceFturDO>().last("limit 10"));
        return list;
    }

}
