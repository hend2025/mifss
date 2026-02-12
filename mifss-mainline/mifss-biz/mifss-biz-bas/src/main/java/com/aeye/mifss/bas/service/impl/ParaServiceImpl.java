package com.aeye.mifss.bas.service.impl;

import cn.hsa.hsaf.core.framework.web.HsafRestPath;
import com.aeye.mifss.bas.bo.ParaBo;
import com.aeye.mifss.bas.dto.ParaDTO;
import com.aeye.mifss.bas.entity.ParaDO;
import com.aeye.mifss.bas.service.ParaService;
import com.aeye.mifss.bas.service.RpcParaService;
import com.aeye.mifss.common.mybatis.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@HsafRestPath("/cloud/para")
@ResponseBody
public class ParaServiceImpl extends BaseServiceImpl<ParaDTO, ParaDO, ParaBo> implements ParaService, RpcParaService {

}

