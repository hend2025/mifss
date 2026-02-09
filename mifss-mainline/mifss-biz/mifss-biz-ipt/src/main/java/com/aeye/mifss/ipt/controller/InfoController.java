package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.ipt.dto.InfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/web/msg/info")
@Api(description="Info", tags = "消息中心表")
public class InfoController {

    @RequestMapping(value = "/list",method = {RequestMethod.POST})
    @ApiOperation(value = "查询列表")
    public WrapperResponse<List<InfoDTO>> list(@RequestBody InfoDTO params) throws Exception{
        return (WrapperResponse)WrapperResponse.success(null);
    }

}
