package com.aeye.mifss.bas.bo.impl;

import com.aeye.mifss.bas.bo.ParaBo;
import com.aeye.mifss.bas.entity.ParaDO;
import com.aeye.mifss.bas.mapper.ParaMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ParaBoImpl extends ServiceImpl<ParaMapper, ParaDO> implements ParaBo {
}
