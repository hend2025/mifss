package com.aeye.mifss.bio.service;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.bio.dto.FaceImageReq;

public interface FaceRecognitionService {

    WrapperResponse<String> faceDetect(FaceImageReq request);

    WrapperResponse<Boolean> faceAuthenticate(FaceImageReq request);

}
