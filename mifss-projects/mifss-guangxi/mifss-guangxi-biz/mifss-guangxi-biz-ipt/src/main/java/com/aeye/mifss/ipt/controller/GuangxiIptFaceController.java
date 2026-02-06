package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.RpcFaceRecognitionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/face")
public class GuangxiIptFaceController extends IptFaceController {

    @DubboReference(check = false, group = "${mifss.dubbo.group:guangxi}")
    private RpcFaceRecognitionService faceRecognitionService;

    @PostMapping("/faceAuthenticate")
    public WrapperResponse<Boolean> faceAuthenticate(@RequestBody FaceImageReq request) {
        System.out.println(" Guangxi   faceAuthenticate ");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.success(false);
        }
        boolean ret = faceRecognitionService.faceAuthenticate(request);
        return WrapperResponse.success(ret);
    }

    @PostMapping("/faceDetect22")
    public WrapperResponse<String> faceDetect22(@RequestBody FaceImageReq request) {
        System.out.println(" Guangxi   faceDetect22 ");
        String ret = faceRecognitionService.faceDetect(request);
        return WrapperResponse.success(ret);
    }

}
