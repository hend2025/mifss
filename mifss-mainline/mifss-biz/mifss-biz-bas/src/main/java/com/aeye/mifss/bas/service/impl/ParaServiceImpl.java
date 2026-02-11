package com.aeye.mifss.bas.service.impl;

import com.aeye.mifss.bas.bo.ParaBo;
import com.aeye.mifss.bas.dto.ParaDTO;
import com.aeye.mifss.bas.entity.ParaDO;
import com.aeye.mifss.bas.service.ParaService;
import com.aeye.mifss.bas.service.RpcParaService;
import com.aeye.mifss.common.mybatis.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ParaServiceImpl extends BaseServiceImpl<ParaDTO, ParaDO, ParaBo> implements ParaService, RpcParaService {

}

