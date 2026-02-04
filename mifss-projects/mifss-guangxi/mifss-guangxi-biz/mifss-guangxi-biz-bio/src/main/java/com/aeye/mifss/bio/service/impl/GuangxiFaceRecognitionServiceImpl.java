package com.aeye.mifss.bio.service.impl;

import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.impl.MainlineFaceRecognitionServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Guangxi Customization implementation of FaceRecognitionService
 * Inherits from Mainline implementation and overrides logic.
 */
@Service("guangxiFaceRecognitionService")
@Primary // Ensures this bean is preferred when injecting FaceRecognitionService
@DubboService
public class GuangxiFaceRecognitionServiceImpl extends MainlineFaceRecognitionServiceImpl {

    @Override
    public String detectFace(FaceImageReq request) {
        // Custom logic before or after mainline logic, or complete replacement
        System.out.println("Executing Guangxi Custom Pre-processing...");

        // Example: Completely overriding the return value
        return "Guangxi Custom Algorithm: Face Detected (Enhanced Model for Guangxi)";
    }

    @Override
    public boolean authenticate(FaceImageReq request) {
        System.out.println("Executing Guangxi Custom Authentication...");
        // Custom Guangxi logic
        return super.authenticate(request);
    }

}
