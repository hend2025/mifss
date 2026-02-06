package com.aeye.mifss.bio.service.impl;

import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.FaceRecognitionService;
import com.aeye.mifss.bio.service.RpcFaceRecognitionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
@DubboService(interfaceClass = RpcFaceRecognitionService.class)
public class FaceRecognitionServiceImpl implements FaceRecognitionService,RpcFaceRecognitionService {

    @Override
    public String faceDetect(FaceImageReq request) {
        System.out.println("Parent - detectFace");
        if (request == null || request.getImageData() == null) {
            return "Error: Empty Request";
        }
        return "Mainline Algorithm: Face Detected (Standard Model)";
    }

    @Override
    public boolean faceAuthenticate(FaceImageReq request) {
        System.out.println("Parent - authenticate");
        return request != null && request.getImageData() != null && !request.getImageData().isEmpty();
    }


}
