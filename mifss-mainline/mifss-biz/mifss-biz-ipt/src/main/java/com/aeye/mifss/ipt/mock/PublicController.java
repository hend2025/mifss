package com.aeye.mifss.ipt.mock;

import cn.hsa.hsaf.core.framework.context.HsafContextHolder;
import cn.hsa.hsaf.core.framework.util.CurrentUser;
import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.ipt.mock.dto.MenuDTO;
import com.aeye.mifss.ipt.mock.dto.SysParaDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/public")
public class PublicController {

    @RequestMapping(value = "/querySysParam", method = {RequestMethod.POST})
    public WrapperResponse<List<SysParaDTO>> querySysParam() throws Exception {
        return WrapperResponse.success(new ArrayList<>());
    }

    @RequestMapping(value = "/queryStopUseMenu", method = {RequestMethod.POST})
    public WrapperResponse<List<MenuDTO>> queryStopUseMenu() throws Exception{
        return WrapperResponse.success(new ArrayList<>());
    }

    @GetMapping("/loginUserInfo")
    @ResponseBody
    public WrapperResponse loginUserInfo(){
        CurrentUser user = HsafContextHolder.getContext().getCurrentUser();
        return WrapperResponse.success(null);
    }

}
