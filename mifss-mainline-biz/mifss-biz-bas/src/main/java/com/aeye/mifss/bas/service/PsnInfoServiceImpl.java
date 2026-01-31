//package com.aeye.mifss.bas.service;
//
//import com.aeye.mifss.bas.bo.PsnInfoBo;
//import com.aeye.mifss.bas.dto.PsnInfoDTO;
//import com.aeye.mifss.bas.entity.PsnInfoDO;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PsnInfoServiceImpl implements PsnInfoService {
//
//    @Autowired
//    PsnInfoBo psnInfoBo;
//
//    public PsnInfoDTO getById(String id) {
//        PsnInfoDO bean = psnInfoBo.getById(id);
//        if (bean == null) {
//            return null;
//        }
//        PsnInfoDTO dto = new PsnInfoDTO();
//        BeanUtils.copyProperties(bean, dto);
//        return dto;
//    }
//
//}
