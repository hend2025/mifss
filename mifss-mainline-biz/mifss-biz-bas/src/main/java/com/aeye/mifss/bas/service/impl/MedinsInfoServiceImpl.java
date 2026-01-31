package com.aeye.mifss.bas.service.impl;

import com.aeye.mifss.bas.bo.MedinsInfoBo;
import com.aeye.mifss.bas.dto.MedinsInfoDTO;
import com.aeye.mifss.bas.entity.MedinsInfoDO;
import com.aeye.mifss.bas.service.MedinsInfoService;
import com.aeye.mifss.common.mybatis.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MedinsInfoServiceImpl extends BaseServiceImpl<MedinsInfoDTO, MedinsInfoDO, MedinsInfoBo>
        implements MedinsInfoService {

}
