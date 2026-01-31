package com.aeye.mifss.common.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;

import java.io.Serializable;
import java.util.List;

public interface LocalService<Entity> {

    /**
     * 根据 ID 查询
     */
    Entity getById(Serializable id);

    /**
     * 根据条件查询一条记录
     */
    Entity getOne(Wrapper<Entity> queryWrapper);

    /**
     * 根据条件查询列表
     */
    List<Entity> list(Wrapper<Entity> queryWrapper);

}
