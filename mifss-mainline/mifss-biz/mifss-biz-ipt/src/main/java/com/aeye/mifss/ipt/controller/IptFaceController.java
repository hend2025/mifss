package com.aeye.mifss.ipt.controller;

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
    public String detectFace(@RequestBody FaceImageReq request) {
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return "Error: Empty Image Data";
        }
        return faceRecognitionService.detectFace(request);
    }

    @PostMapping("/authenticate")
    public boolean authenticate(@RequestBody FaceImageReq request) {
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return false;
        }
        return faceRecognitionService.authenticate(request);
    }

}
