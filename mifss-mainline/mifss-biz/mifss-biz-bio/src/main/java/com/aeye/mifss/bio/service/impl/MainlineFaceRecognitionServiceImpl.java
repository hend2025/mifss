package com.aeye.mifss.bio.service.impl;

import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.FaceRecognitionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * Mainline implementation of FaceRecognitionService
 */
@Service("faceRecognitionService")
@DubboService
public class MainlineFaceRecognitionServiceImpl implements FaceRecognitionService {

    @Override
    public String detectFace(FaceImageReq request) {
        // Mainline standard algorithm
        if (request == null || request.getImageData() == null) {
            return "Error: Empty Request";
        }
        return "Mainline Algorithm: Face Detected (Standard Model)";
    }

    @Override
    public boolean authenticate(FaceImageReq request) {
        // Mainline standard authentication logic
        // Mock implementation
        return request != null && request.getImageData() != null && !request.getImageData().isEmpty();
    }
}
