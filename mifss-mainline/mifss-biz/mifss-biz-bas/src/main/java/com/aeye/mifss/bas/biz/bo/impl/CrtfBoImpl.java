package com.aeye.mifss.bas.biz.bo.impl;

import com.aeye.mifss.bas.biz.bo.CrtfBo;
import com.aeye.mifss.bas.biz.entity.CrtfDO;
import com.aeye.mifss.bas.biz.mapper.CrtfMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 认证记录Bo实现
 */
@Service
public class CrtfBoImpl extends ServiceImpl<CrtfMapper, CrtfDO> implements CrtfBo {
}
