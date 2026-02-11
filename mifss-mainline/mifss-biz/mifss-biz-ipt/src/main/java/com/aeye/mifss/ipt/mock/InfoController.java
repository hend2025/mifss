package com.aeye.mifss.ipt.mock;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.ipt.mock.dto.InfoDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/web/msg/info")
public class InfoController {

    @RequestMapping(value = "/list",method = {RequestMethod.POST})
    public WrapperResponse<List<InfoDTO>> list(@RequestBody InfoDTO params) throws Exception{
        return (WrapperResponse)WrapperResponse.success(null);
    }

}
