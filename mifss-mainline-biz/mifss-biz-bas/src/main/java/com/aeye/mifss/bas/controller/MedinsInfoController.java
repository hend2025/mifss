package com.aeye.mifss.bas.controller;

import cn.hsa.hsaf.core.fsstore.FSEntity;
import cn.hsa.ims.common.utils.AeyeFSManager;
import cn.hutool.core.bean.BeanUtil;
import com.aeye.mifss.bas.dto.MedinsInfoDTO;
import com.aeye.mifss.bas.entity.MedinsInfoDO;
import com.aeye.mifss.bas.service.MedinsInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

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
        if (bean == null) {
            return null;
        }
        return BeanUtil.toBean(bean, MedinsInfoDTO.class);
    }

    @ApiOperation("医疗机构Top10")
    @PostMapping("/list")
    public List<MedinsInfoDO> list(@RequestBody MedinsInfoDTO medinsInfoDTO) {
        List<MedinsInfoDO> list = medinsInfoService.list(
                new LambdaQueryWrapper<MedinsInfoDO>()
                        .like(!StringUtils.isEmpty(medinsInfoDTO.getMedinsCode()), MedinsInfoDO::getMedinsCode,
                                medinsInfoDTO.getMedinsCode())
                        .like(!StringUtils.isEmpty(medinsInfoDTO.getMedinsName()), MedinsInfoDO::getMedinsName,
                                medinsInfoDTO.getMedinsName())
                        .last("limit 5"));


        return BeanUtil.copyToList(list, MedinsInfoDO.class);
    }

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public String upload() {
        try {
            File file = new File("d:\\11.txt");
            try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                FSEntity entity = new FSEntity();
                entity.setName("11");
                entity.setSize(file.length());
                entity.setInputstream(fis);
                AeyeFSManager.putObject("group1", entity);
            }

            FSEntity entity2 = AeyeFSManager.getObject("group1", "11");
            if (entity2 != null && entity2.getInputstream() != null) {
                try (java.io.InputStream is = entity2.getInputstream();
                        FileOutputStream fos = new FileOutputStream("D:\\222.txt")) {
                    org.springframework.util.StreamUtils.copy(is, fos);
                }
            }

        } catch (Exception e) {
            return "error";
        }

        return "success";

    }

}
