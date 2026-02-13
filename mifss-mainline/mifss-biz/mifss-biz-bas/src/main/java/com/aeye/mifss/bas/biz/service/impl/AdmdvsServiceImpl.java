package com.aeye.mifss.bas.biz.service.impl;

import cn.hsa.hsaf.core.framework.web.HsafRestPath;
import com.aeye.mifss.bas.biz.bo.AdmdvsBo;
import com.aeye.mifss.bas.dto.AdmdvsDTO;
import com.aeye.mifss.bas.biz.entity.AdmdvsDO;
import com.aeye.mifss.bas.biz.service.AdmdvsService;
import com.aeye.mifss.bas.service.RpcAdmdvsService;
import com.aeye.mifss.common.mybatis.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@HsafRestPath("/cloud/admdvs")
@ResponseBody
public class AdmdvsServiceImpl extends BaseServiceImpl<AdmdvsDTO, AdmdvsDO, AdmdvsBo> implements AdmdvsService, RpcAdmdvsService {

}
