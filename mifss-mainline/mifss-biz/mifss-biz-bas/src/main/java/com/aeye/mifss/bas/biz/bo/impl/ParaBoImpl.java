package com.aeye.mifss.bas.biz.bo.impl;

import com.aeye.mifss.bas.biz.bo.ParaBo;
import com.aeye.mifss.bas.biz.entity.ParaDO;
import com.aeye.mifss.bas.biz.mapper.ParaMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ParaBoImpl extends ServiceImpl<ParaMapper, ParaDO> implements ParaBo {
}
