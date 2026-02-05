package com.aeye.mifss.bio.service.impl;

import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.FaceRecognitionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@DubboService(interfaceClass = FaceRecognitionService.class, group = "guangxi")
@Primary
public class GuangxiFaceRecognitionServiceImpl extends FaceRecognitionServiceImpl {

    @Override
    public String detectFace(FaceImageReq request) {
        System.out.println("Guangxi - detectFace");
        if (request == null || request.getImageData() == null) {
            return "Error: Empty Request";
        }
        return "Mainline Algorithm: Face Detected (Standard Model)";
    }

}
