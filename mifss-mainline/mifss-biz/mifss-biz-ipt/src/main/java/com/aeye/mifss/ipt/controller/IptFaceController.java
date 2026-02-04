package com.aeye.mifss.ipt.controller;

import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.FaceRecognitionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IPT Module Face Controller
 * Demonstrates RPC call to FaceRecognitionService
 */
@RestController
@RequestMapping("/face")
public class IptFaceController {

    /**
     * RPC Reference to Bio Service
     * check=false: Don't check for service availability at startup
     */
    @DubboReference(check = false)
    private FaceRecognitionService faceRecognitionService;

    @PostMapping("/detect")
    public String detectFace(@RequestBody FaceImageReq request) {
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return "Error: Empty Image Data";
        }
        // RPC Call
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
