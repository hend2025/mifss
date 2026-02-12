package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.RpcFaceRecognitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "测试RPC调用")
@RestController
@RequestMapping("/face")
public class FaceController {

    @Autowired(required = false)
    private RpcFaceRecognitionService rpcFaceRecognitionService;

    @PostMapping("/detect")
    @ApiOperation("人脸检测")
    public WrapperResponse<String> detectFace(@RequestBody FaceImageReq request) {
        System.out.println("调用了父类的方法 - FaceController.detectFace");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("认证照不能为空！");
        }
        return rpcFaceRecognitionService.faceDetect(request);
    }

    @ApiOperation("认证")
    @PostMapping(value = "/authenticate")
    public WrapperResponse<Boolean> authenticateFace(@RequestBody FaceImageReq request) {
        System.out.println("调用了父类的方法 - FaceController.authenticateFace");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("认证照不能为空！",false);
        }
        return rpcFaceRecognitionService.faceAuthenticate(request);
    }

}
