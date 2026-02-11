package com.aeye.mifss.common.mybatis.service;

import cn.hsa.ims.common.utils.AeyePageInfo;
import cn.hsa.ims.common.utils.AeyePageResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    /**
     * 分页查询
     */
    AeyePageResult<Entity> page(AeyePageInfo page, Wrapper<Entity> queryWrapper) throws Exception;

    /**
     * 插入一条记录
     */
    boolean save(Entity entity);

    /**
     * 插入（批量）
     */
    boolean saveBatch(Collection<Entity> entityList);
    /**
     * 保存或更新
     */
    boolean saveOrUpdate(Entity entity);

    /**
     * 根据 ID 修改
     */
    boolean updateById(Entity entity);

    /**
     * 根据 UpdateWrapper 条件，更新记录
     */
    boolean update(Entity entity, Wrapper<Entity> updateWrapper);

    /**
     * 批量更新
     */
    boolean updateBatchById(Collection<Entity> entityList);


    /**
     * 根据 ID 删除
     */
    boolean removeById(Serializable id);

    /**
     * 根据 entity 条件，删除记录
     */
    boolean remove(Wrapper<Entity> queryWrapper);

    /**
     * 删除（根据ID 批量删除）
     */
    boolean removeByIds(Collection<? extends Serializable> idList);

    /**
     * 查询总数
     */
    long count();

    /**
     * 根据条件查询总数
     */
    long count(Wrapper<Entity> queryWrapper);

    /**
     * 查询 Map
     */
    Map<String, Object> getMap(Wrapper<Entity> queryWrapper);

}
