package com.aeye.mifss.bas.service;

import com.aeye.mifss.bas.bo.MedinsInfoBo;
import com.aeye.mifss.bas.dto.MedinsInfoDTO;
import com.aeye.mifss.bas.entity.MedinsInfoDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedinsInfoServiceImpl implements MedinsInfoService {

    @Autowired
    MedinsInfoBo medinsInfoBo;

    public MedinsInfoDTO getById(String id) {
        MedinsInfoDO bean = medinsInfoBo.getById(id);
        if (bean == null) {
            return null;
        }
        MedinsInfoDTO dto = new MedinsInfoDTO();
        BeanUtils.copyProperties(bean, dto);
        return dto;
    }

}
