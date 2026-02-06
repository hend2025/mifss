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
public class GuangxiIptFaceController extends IptFaceController {

    @DubboReference(check = false, group = "${mifss.dubbo.group:guangxi}")
    private RpcFaceRecognitionService faceRecognitionService;

    @PostMapping("/authenticate")
    public boolean authenticate(@RequestBody FaceImageReq request) {
        System.out.println(" Guangxi   authenticate ");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return false;
        }
        return faceRecognitionService.authenticate(request);
    }

    @PostMapping("/detectFace22")
    public String detectFace22(@RequestBody FaceImageReq request) {
        System.out.println(" Guangxi   detectFace22 ");
        return faceRecognitionService.detectFace(request);
    }

}
