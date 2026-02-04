package com.aeye.mifss.bio.service;

import com.aeye.mifss.bio.dto.FaceImageReq;

/**
 * Bio Face Recognition Service Interface
 */
public interface FaceRecognitionService {

    /**
     * Detect face from image data
     * 
     * @param request image request
     * @return detection result message
     */
    String detectFace(FaceImageReq request);

    /**
     * Authenticate face
     * 
     * @param request image request
     * @return true if authenticated, false otherwise
     */
    boolean authenticate(FaceImageReq request);
}
