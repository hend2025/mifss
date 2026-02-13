package com.aeye.mifss.bas.biz.bo.impl;

import com.aeye.mifss.bas.biz.bo.DicBo;
import com.aeye.mifss.bas.biz.entity.DicDO;
import com.aeye.mifss.bas.biz.mapper.DicMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DicBoImpl extends ServiceImpl<DicMapper, DicDO> implements DicBo {
}
