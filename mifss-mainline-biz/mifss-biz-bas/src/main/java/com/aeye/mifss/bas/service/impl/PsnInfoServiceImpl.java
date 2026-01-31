package com.aeye.mifss.bas.service.impl;

import com.aeye.mifss.bas.bo.PsnInfoBo;
import com.aeye.mifss.bas.dto.PsnInfoDTO;
import com.aeye.mifss.bas.entity.PsnInfoDO;
import com.aeye.mifss.bas.service.PsnInfoService;
import com.aeye.mifss.common.mybatis.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PsnInfoServiceImpl extends BaseServiceImpl<PsnInfoDTO, PsnInfoDO, PsnInfoBo>
        implements PsnInfoService {

}
