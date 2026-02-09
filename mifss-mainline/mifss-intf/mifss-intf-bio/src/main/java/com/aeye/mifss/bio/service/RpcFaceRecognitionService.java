package com.aeye.mifss.bio.service;

import com.aeye.mifss.bio.dto.FaceImageReq;

public interface RpcFaceRecognitionService {

    String faceDetect(FaceImageReq request);

    boolean faceAuthenticate(FaceImageReq request);
}
