package com.aeye.mifss.bio.service.impl;

import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.FaceRecognitionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
@DubboService(interfaceClass = FaceRecognitionService.class)
public class FaceRecognitionServiceImpl implements FaceRecognitionService {

    @Override
    public String detectFace(FaceImageReq request) {
        System.out.println("Parent - detectFace");
        if (request == null || request.getImageData() == null) {
            return "Error: Empty Request";
        }
        return "Mainline Algorithm: Face Detected (Standard Model)";
    }

    @Override
    public boolean authenticate(FaceImageReq request) {
        System.out.println("Parent - authenticate");
        return request != null && request.getImageData() != null && !request.getImageData().isEmpty();
    }


}
