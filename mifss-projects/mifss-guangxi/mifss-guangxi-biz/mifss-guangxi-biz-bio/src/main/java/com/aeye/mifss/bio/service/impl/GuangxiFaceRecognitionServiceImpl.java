package com.aeye.mifss.bio.service.impl;

import com.aeye.mifss.bio.dto.FaceImageReq;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service("guangxiFaceRecognitionService")
@DubboService
public class GuangxiFaceRecognitionServiceImpl extends FaceRecognitionServiceImpl {

    @Override
    public String detectFace(FaceImageReq request) {
        System.out.println("Executing Guangxi Custom Pre-processing...");
        return "Guangxi Custom Algorithm: Face Detected (Enhanced Model for Guangxi)";
    }

}
