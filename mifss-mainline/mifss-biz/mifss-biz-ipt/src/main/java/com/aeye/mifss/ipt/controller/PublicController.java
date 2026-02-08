package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.context.HsafContextHolder;
import cn.hsa.hsaf.core.framework.util.CurrentUser;
import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.ipt.dto.MenuDTO;
import com.aeye.mifss.ipt.dto.SysParaDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/public")
@Api(description="public", tags = "场景监管公共接口")
public class PublicController {

    @RequestMapping(value = "/querySysParam", method = {RequestMethod.POST})
    @ApiOperation(value = "查询系统参数")
    public WrapperResponse<List<SysParaDTO>> querySysParam() throws Exception {
        return WrapperResponse.success(new ArrayList<>());
    }

    @RequestMapping(value = "/queryStopUseMenu", method = {RequestMethod.POST})
    @ApiOperation(value = "查询停用的菜单")
    public WrapperResponse<List<MenuDTO>> queryStopUseMenu() throws Exception{
        return WrapperResponse.success(new ArrayList<>());
    }

    @GetMapping("/loginUserInfo")
    @ApiOperation(value = "获取当前用户信息")
    @ResponseBody
    public WrapperResponse loginUserInfo(){
        CurrentUser user = HsafContextHolder.getContext().getCurrentUser();
        return WrapperResponse.success(null);
    }

}
