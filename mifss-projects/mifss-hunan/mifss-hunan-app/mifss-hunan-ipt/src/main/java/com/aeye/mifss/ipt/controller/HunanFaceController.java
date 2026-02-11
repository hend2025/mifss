package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.RpcFaceRecognitionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "RPC测试人脸接口")
@RestController
@RequestMapping("/face")
public class HunanFaceController extends FaceController{

    @Autowired(required = false)
    private RpcFaceRecognitionService rpcFaceRecognitionService;

    @PostMapping("/faceDetectNew")
    public WrapperResponse<String> faceDetectNew(@RequestBody FaceImageReq request) {
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("Error: Empty Image Data");
        }
        String ret = rpcFaceRecognitionService.faceDetect(request);
        return WrapperResponse.success(ret);
    }

    @PostMapping("/faceAuthenticate")
    public WrapperResponse<Boolean> faceAuthenticate(@RequestBody FaceImageReq request) {
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.success(false);
        }
        boolean ret = rpcFaceRecognitionService.faceAuthenticate(request);
        return WrapperResponse.success(ret);
    }

}
