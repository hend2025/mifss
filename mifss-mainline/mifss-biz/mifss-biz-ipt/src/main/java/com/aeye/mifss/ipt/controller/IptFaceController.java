package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.RpcFaceRecognitionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/face")
public class IptFaceController {

    @DubboReference(check = false, group = "${mifss.dubbo.group:}")
    private RpcFaceRecognitionService faceRecognitionService;

    @PostMapping("/detect")
    public WrapperResponse<String> faceDetect(@RequestBody FaceImageReq request) {
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("Error: Empty Image Data");
        }
        String ret = faceRecognitionService.faceDetect(request);
        return WrapperResponse.success(ret);
    }

    @PostMapping("/faceAuthenticate")
    public WrapperResponse<Boolean> faceAuthenticate(@RequestBody FaceImageReq request) {
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.success(false);
        }
        boolean ret = faceRecognitionService.faceAuthenticate(request);
        return WrapperResponse.success(ret);
    }

}
