package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.context.HsafContextHolder;
import cn.hsa.hsaf.core.framework.util.CurrentUser;
import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.ims.common.utils.AeyePageResult;
import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.RpcFaceRecognitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.aeye.mifss.ipt.dto.MainHilistDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "测试接口")
@RestController
@RequestMapping("/mainhilist")
public class MainhilistController {

    @Autowired(required = false)
    private RpcFaceRecognitionService rpcFaceRecognitionService;

    @ApiOperation(value = "查询列表")
    @RequestMapping(value = "/list", method = { RequestMethod.POST })
    public WrapperResponse<List<MainHilistDTO>> list() throws Exception {

        CurrentUser user = HsafContextHolder.getContext().getCurrentUser();
        System.out.println("user = " + user);

        FaceImageReq request = new FaceImageReq();
        request.setImageData("123");
        boolean res = rpcFaceRecognitionService.faceAuthenticate(request);
        System.out.println("res = " + res);

        List<MainHilistDTO> list = new ArrayList();
        MainHilistDTO baean1 = new MainHilistDTO();
        baean1.setHilistCode("1000000000001");
        baean1.setHilistName("螺旋CT");
        list.add(baean1);

        MainHilistDTO baean2 = new MainHilistDTO();
        baean2.setHilistCode("1000000000002");
        baean2.setHilistName("心电图");
        list.add(baean2);

        AeyePageResult page = new AeyePageResult();
        page.setData(list);
        page.setPageNum(1);
        page.setPageSize(10);
        page.setPages(1);
        page.setRecordCounts(2);
        return (WrapperResponse) WrapperResponse.success(page);
    }

}
