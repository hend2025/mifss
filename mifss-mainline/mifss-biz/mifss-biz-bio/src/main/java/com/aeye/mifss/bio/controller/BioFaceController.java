package com.aeye.mifss.bio.controller;

import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.FaceRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/face")
public class BioFaceController {

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @PostMapping("/detect")
    public String detectFace(@RequestBody FaceImageReq request) {
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return "Error: Empty Image Data";
        }
        return faceRecognitionService.detectFace(request);
    }

    @PostMapping(value = "/authenticate")
    public boolean authenticate(@RequestBody FaceImageReq request) {
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return false;
        }
        return faceRecognitionService.authenticate(request);
    }
}
