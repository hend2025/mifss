package com.aeye.mifss.bio.service;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.bio.dto.FaceImageReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "${feign_bio_application.name:mifss-bio}",
        path = "${feign_bio_application.context:/mifss/bio}/cloud/face",
        configuration = FeignClientsConfiguration.class
)
public interface RpcFaceRecognitionService {

    @PostMapping("/faceDetect")
    WrapperResponse<String> faceDetect(@RequestBody FaceImageReq request);

    @PostMapping("/faceAuthenticate")
    WrapperResponse<Boolean> faceAuthenticate(@RequestBody FaceImageReq request);

}
