package com.aeye.mifss.bio.service.impl;

import cn.hsa.hsaf.core.framework.web.HsafRestPath;
import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import com.aeye.mifss.bio.dto.FaceImageReq;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Primary
@ResponseBody
@HsafRestPath("/cloud/face")
@Service("faceRecognitionServiceImpl")
public class HunanFaceRecognitionServiceImpl extends FaceRecognitionServiceImpl {

    @Override
    @HsafRestPath(value = "/faceDetect", method = RequestMethod.POST)
    public WrapperResponse<String> faceDetect(FaceImageReq request) {
        System.out.println("调用了【子类】的方法 - HunanFaceRecognitionServiceImpl.faceDetect");
        if (request == null || request.getImageData() == null || request.getImageData().isEmpty()) {
            return WrapperResponse.fail("认证照不能为空！");
        }
        return WrapperResponse.success("通过人脸检测！");
    }

}
