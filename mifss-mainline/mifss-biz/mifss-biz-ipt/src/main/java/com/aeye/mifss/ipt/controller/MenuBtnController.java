package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.ipt.dto.MenuBtnDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/web/scen/menubtn")
@Api(description="MenuBtn", tags = "菜单按钮")
public class MenuBtnController {

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ApiOperation(value = "查询列表")
    public WrapperResponse<List<MenuBtnDTO>> list(MenuBtnDTO params) throws Exception {
        return (WrapperResponse) WrapperResponse.success(new ArrayList<>());
    }


}
