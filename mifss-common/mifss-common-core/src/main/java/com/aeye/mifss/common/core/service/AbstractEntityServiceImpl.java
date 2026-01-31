package com.aeye.mifss.common.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 基础服务抽象实现类
 * 封装 IService 常用方法，减少代码冗余
 *
 * @param <Entity> Entity 类型
 * @param <BO>     Bo 类型（需继承 IService）
 */
public abstract class AbstractEntityServiceImpl<Entity, BO extends IService<Entity>> implements BaseService<Entity> {

    /**
     * 获取 Bo 实例，由子类实现
     */
    protected abstract BO getBo();

    // ==================== 新增操作 ====================

    /**
     * 插入一条记录
     */
    public boolean save(Entity entity) {
        return getBo().save(entity);
    }

    /**
     * 批量插入
     */
    public boolean saveBatch(Collection<Entity> entityList) {
        return getBo().saveBatch(entityList);
    }

    /**
     * 批量插入（指定批次大小）
     */
    public boolean saveBatch(Collection<Entity> entityList, int batchSize) {
        return getBo().saveBatch(entityList, batchSize);
    }

    /**
     * 插入或更新一条记录
     */
    public boolean saveOrUpdate(Entity entity) {
        return getBo().saveOrUpdate(entity);
    }

    /**
     * 批量插入或更新
     */
    public boolean saveOrUpdateBatch(Collection<Entity> entityList) {
        return getBo().saveOrUpdateBatch(entityList);
    }

    // ==================== 删除操作 ====================

    /**
     * 根据 ID 删除
     */
    public boolean removeById(Serializable id) {
        return getBo().removeById(id);
    }

    /**
     * 根据 实体 删除 (BaseService 接口定义为 removeByDto，此处映射为 Entity)
     */
    public boolean removeByDto(Entity entity) {
        return getBo().removeById(entity);
    }

    /**
     * 根据实体 ID 删除 (保留原有方法，尽管接口可能没定义)
     */
    public boolean removeById(Entity entity) {
        return getBo().removeById(entity);
    }

    /**
     * 根据条件删除
     */
    public boolean remove(Wrapper<Entity> queryWrapper) {
        return getBo().remove(queryWrapper);
    }

    /**
     * 根据 ID 批量删除
     */
    public boolean removeByIds(Collection<?> idList) {
        return getBo().removeByIds(idList);
    }

    /**
     * 根据 columnMap 条件删除
     */
    public boolean removeByMap(Map<String, Object> columnMap) {
        return getBo().removeByMap(columnMap);
    }

    // ==================== 更新操作 ====================

    /**
     * 根据 ID 更新
     */
    public boolean updateById(Entity entity) {
        return getBo().updateById(entity);
    }

    /**
     * 根据条件更新
     */
    public boolean update(Wrapper<Entity> updateWrapper) {
        return getBo().update(updateWrapper);
    }

    /**
     * 根据条件更新（带实体）
     */
    public boolean update(Entity entity, Wrapper<Entity> updateWrapper) {
        return getBo().update(entity, updateWrapper);
    }

    /**
     * 根据 ID 批量更新
     */
    public boolean updateBatchById(Collection<Entity> entityList) {
        return getBo().updateBatchById(entityList);
    }

    /**
     * 根据 ID 批量更新（指定批次大小）
     */
    public boolean updateBatchById(Collection<Entity> entityList, int batchSize) {
        return getBo().updateBatchById(entityList, batchSize);
    }

    // ==================== 查询操作 ====================

    /**
     * 根据 ID 查询
     */
    public Entity getById(Serializable id) {
        return getBo().getById(id);
    }

    /**
     * 根据条件查询一条记录
     */
    public Entity getOne(Wrapper<Entity> queryWrapper) {
        return getBo().getOne(queryWrapper);
    }

    /**
     * 根据条件查询一条记录（可选是否抛出异常）
     */
    public Entity getOne(Wrapper<Entity> queryWrapper, boolean throwEx) {
        return getBo().getOne(queryWrapper, throwEx);
    }

    /**
     * 查询所有记录
     */
    public List<Entity> list() {
        return getBo().list();
    }

    /**
     * 根据条件查询列表
     */
    public List<Entity> list(Wrapper<Entity> queryWrapper) {
        return getBo().list(queryWrapper);
    }

    /**
     * 根据 ID 批量查询
     */
    public List<Entity> listByIds(Collection<? extends Serializable> idList) {
        return getBo().listByIds(idList);
    }

    /**
     * 根据 columnMap 条件查询
     */
    public List<Entity> listByMap(Map<String, Object> columnMap) {
        return getBo().listByMap(columnMap);
    }

    // ==================== 统计操作 ====================

    /**
     * 查询总记录数
     */
    public long count() {
        return getBo().count();
    }

    /**
     * 根据条件查询记录数
     */
    public long count(Wrapper<Entity> queryWrapper) {
        return getBo().count(queryWrapper);
    }

    // ==================== 分页操作 ====================

    /**
     * 无条件分页查询 (适配 BaseService 接口)
     */
    public IPage<Entity> page(long current, long size) {
        return getBo().page(new Page<>(current, size));
    }

    /**
     * 无条件分页查询
     */
    public <P extends IPage<Entity>> P page(P page) {
        return getBo().page(page);
    }

    /**
     * 条件分页查询
     */
    public <P extends IPage<Entity>> P page(P page, Wrapper<Entity> queryWrapper) {
        return getBo().page(page, queryWrapper);
    }

    // ==================== Lambda 链式操作 ====================

    /**
     * 获取 LambdaQueryChainWrapper
     * 支持 Lambda 表达式的链式查询
     */
    public LambdaQueryChainWrapper<Entity> lambdaQuery() {
        return getBo().lambdaQuery();
    }

    /**
     * 获取带初始实体的 LambdaQueryChainWrapper
     */
    public LambdaQueryChainWrapper<Entity> lambdaQuery(Entity entity) {
        return getBo().lambdaQuery(entity);
    }

    /**
     * 获取 LambdaUpdateChainWrapper
     * 支持 Lambda 表达式的链式更新
     */
    public LambdaUpdateChainWrapper<Entity> lambdaUpdate() {
        return getBo().lambdaUpdate();
    }

    /**
     * 获取 QueryChainWrapper
     */
    public QueryChainWrapper<Entity> query() {
        return getBo().query();
    }

    /**
     * 获取 UpdateChainWrapper
     */
    public UpdateChainWrapper<Entity> update() {
        return getBo().update();
    }

}
