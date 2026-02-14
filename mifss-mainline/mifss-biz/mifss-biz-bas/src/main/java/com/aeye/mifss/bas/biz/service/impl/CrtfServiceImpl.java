package com.aeye.mifss.bas.biz.service.impl;

import cn.hsa.hsaf.core.framework.web.HsafRestPath;
import com.aeye.mifss.bas.biz.bo.CrtfBo;
import com.aeye.mifss.bas.biz.entity.CrtfDO;
import com.aeye.mifss.bas.biz.service.CrtfService;
import com.aeye.mifss.bas.dto.CrtfDTO;
import com.aeye.mifss.bas.service.RpcCrtfService;
import com.aeye.mifss.common.mybatis.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 认证记录Service实现
 */
@Service
@HsafRestPath("/cloud/crtf")
@ResponseBody
public class CrtfServiceImpl extends BaseServiceImpl<CrtfDTO, CrtfDO, CrtfBo> implements CrtfService, RpcCrtfService {
}
