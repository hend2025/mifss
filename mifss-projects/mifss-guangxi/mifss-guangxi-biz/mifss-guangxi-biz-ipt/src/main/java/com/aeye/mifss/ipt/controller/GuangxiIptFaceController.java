package com.aeye.mifss.ipt.controller;

import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.RpcFaceRecognitionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestBody;

public class GuangxiIptFaceController extends IptFaceController {

    @DubboReference(check = false, group = "${mifss.dubbo.group:}")
    protected RpcFaceRecognitionService faceRecognitionService;

    @Override
    public String detectFace(@RequestBody FaceImageReq request) {
        System.out.println(" Guangxi  IptFaceController ");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return "Error: Empty Image Data";
        }
        return faceRecognitionService.detectFace(request);
    }

}
