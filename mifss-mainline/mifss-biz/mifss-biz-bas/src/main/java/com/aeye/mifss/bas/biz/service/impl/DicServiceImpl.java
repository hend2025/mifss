package com.aeye.mifss.bas.biz.service.impl;

import cn.hsa.hsaf.core.framework.web.HsafRestPath;
import com.aeye.mifss.bas.biz.bo.DicBo;
import com.aeye.mifss.bas.dto.DicDTO;
import com.aeye.mifss.bas.biz.entity.DicDO;
import com.aeye.mifss.bas.biz.service.DicService;
import com.aeye.mifss.bas.service.RpcDicService;
import com.aeye.mifss.common.mybatis.service.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@HsafRestPath("/cloud/dic")
@ResponseBody
public class DicServiceImpl extends BaseServiceImpl<DicDTO, DicDO, DicBo> implements DicService, RpcDicService {

}
