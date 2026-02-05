package com.aeye.mifss.bio.service;

import com.aeye.mifss.bio.dto.FaceImageReq;

public interface RpcFaceRecognitionService {

    String detectFace(FaceImageReq request);

    boolean authenticate(FaceImageReq request);
}
