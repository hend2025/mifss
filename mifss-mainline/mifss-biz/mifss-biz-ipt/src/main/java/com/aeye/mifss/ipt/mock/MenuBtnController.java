package com.aeye.mifss.ipt.mock;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.ipt.mock.dto.MenuBtnDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/web/scen/menubtn")
public class MenuBtnController {

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public WrapperResponse<List<MenuBtnDTO>> list(MenuBtnDTO params) throws Exception {
        return (WrapperResponse) WrapperResponse.success(new ArrayList<>());
    }


}
