package com.aeye.mifss.bio.service.impl;

import com.aeye.mifss.bio.dto.FaceImageReq;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("faceRecognitionServiceImpl")
@Primary
public class GuangxiFaceRecognitionServiceImpl extends FaceRecognitionServiceImpl {

    @Override
    public String faceDetect(FaceImageReq request) {
        System.out.println("Guangxi - detectFace");
        if (request == null || request.getImageData() == null) {
            return "Error: Empty Request";
        }
        return "Mainline Algorithm: Face Detected (Standard Model)";
    }

}
