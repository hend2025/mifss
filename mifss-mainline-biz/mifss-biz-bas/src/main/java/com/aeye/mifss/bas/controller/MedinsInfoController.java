package com.aeye.mifss.bas.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aeye.mifss.bas.dto.MedinsInfoDTO;
import com.aeye.mifss.bas.entity.MedinsInfoDO;
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

    @Autowired
    private MedinsInfoService medinsInfoService;

    @ApiOperation("根据ID查询医疗机构")
    @GetMapping("/{id}")
    public MedinsInfoDTO getById(@ApiParam("医疗机构ID") @PathVariable("id") String id) {
        MedinsInfoDO bean = medinsInfoService.getById(id);
        if(bean == null){
            return null;
        }
        return BeanUtil.toBean(bean, MedinsInfoDTO.class);
    }

}
