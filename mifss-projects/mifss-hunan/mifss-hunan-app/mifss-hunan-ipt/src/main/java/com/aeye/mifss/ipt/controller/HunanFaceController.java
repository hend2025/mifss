package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.RpcFaceRecognitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试RPC调用")
@RestController
@RequestMapping("/face")
public class HunanFaceController extends FaceController{

    @Autowired(required = false)
    private RpcFaceRecognitionService rpcFaceRecognitionService;

    @PostMapping("/detectNew")
    @ApiOperation("人脸检测New")
    public WrapperResponse<String> detectNew(@RequestBody FaceImageReq request) {
        System.out.println("调用了【子类】的方法 - HunanFaceController.detectNew");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("认证照不能为空！");
        }
        return rpcFaceRecognitionService.faceDetect(request);
    }

    @PostMapping("/detect")
    @ApiOperation("人脸检测")
    public WrapperResponse<String> detectFace(@RequestBody FaceImageReq request) {
        System.out.println("调用了【子类】的方法 - HunanFaceController.detectFace");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("认证照不能为空！");
        }
        return rpcFaceRecognitionService.faceDetect(request);
    }

    @ApiOperation("认证")
    @PostMapping(value = "/authenticate")
    public WrapperResponse<Boolean> authenticateFace(@RequestBody FaceImageReq request) {
        System.out.println("调用了【子类】的方法 - HunanFaceController.authenticateFace");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("认证照不能为空！",false);
        }
        return rpcFaceRecognitionService.faceAuthenticate(request);
    }

}
