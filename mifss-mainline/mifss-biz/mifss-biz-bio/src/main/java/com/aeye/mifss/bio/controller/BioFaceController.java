package com.aeye.mifss.bio.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.FaceRecognitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/face")
@Api(tags = "人脸识别")
public class BioFaceController {

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @PostMapping("/detect")
    @ApiOperation("人脸检测")
    public WrapperResponse<String> detectFace(@RequestBody FaceImageReq request) {
        System.out.println("调用了父类的方法 - BioFaceController.detectFace");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("认证照不能为空！");
        }
        return faceRecognitionService.faceDetect(request);
    }

    @ApiOperation("认证")
    @PostMapping(value = "/authenticate")
    public WrapperResponse<Boolean> authenticateFace(@RequestBody FaceImageReq request) {
        System.out.println("调用了父类的方法 - BioFaceController.authenticateFace");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("认证照不能为空！",false);
        }
        return faceRecognitionService.faceAuthenticate(request);
    }


}
