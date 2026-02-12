package com.aeye.mifss.bio.service.impl;

import cn.hsa.hsaf.core.framework.web.HsafRestPath;
import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.FaceRecognitionService;
import com.aeye.mifss.bio.service.RpcFaceRecognitionService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@ResponseBody
@HsafRestPath("/cloud/face")
public class FaceRecognitionServiceImpl implements FaceRecognitionService,RpcFaceRecognitionService {

    @Override
    @HsafRestPath(value = "/faceDetect", method = RequestMethod.POST)
    public WrapperResponse<String> faceDetect(FaceImageReq request) {
        System.out.println("调用了父类的方法 - FaceRecognitionServiceImpl.faceDetect");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("认证照不能为空！");
        }
        return WrapperResponse.success("通过人脸检测！",null);
    }

    @Override
    @HsafRestPath(value = "/faceAuthenticate", method = RequestMethod.POST)
    public WrapperResponse<Boolean> faceAuthenticate(FaceImageReq request) {
        System.out.println("调用了父类的方法 - FaceRecognitionServiceImpl.faceAuthenticate");
        Boolean result = request != null && request.getImageData() != null && !request.getImageData().isEmpty();
        return WrapperResponse.success(result?"认证成功":"认证失败",result);
    }

}
