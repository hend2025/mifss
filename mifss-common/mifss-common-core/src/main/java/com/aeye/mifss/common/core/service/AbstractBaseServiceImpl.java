package com.aeye.mifss.common.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础服务抽象实现类
 * 封装 IService 常用方法，入参和出参均为 DTO，内部自动转换 Entity
 *
 * @param <DTO>    DTO 类型
 * @param <Entity> Entity 类型
 * @param <BO>     Bo 类型（需继承 IService）
 */
public abstract class AbstractBaseServiceImpl<DTO, Entity, BO extends IService<Entity>> {

    /**
     * 获取 Bo 实例，由子类实现
     */
    protected abstract BO getBo();

    // ==================== 类型转换方法 ====================

    /**
     * 获取 DTO Class
     */
    @SuppressWarnings("unchecked")
    protected Class<DTO> getDtoClass() {
        return (Class<DTO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 获取 Entity Class
     */
    @SuppressWarnings("unchecked")
    protected Class<Entity> getEntityClass() {
        return (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * DTO 转 Entity
     */
    protected Entity toEntity(DTO dto) {
        if (dto == null) {
            return null;
        }
        return BeanUtil.copyProperties(dto, getEntityClass());
    }

    /**
     * Entity 转 DTO
     */
    protected DTO toDto(Entity entity) {
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, getDtoClass());
    }

    /**
     * DTO 集合转 Entity 集合
     */
    protected List<Entity> toEntityList(Collection<DTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return null;
        }
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    /**
     * Entity 集合转 DTO 集合
     */
    protected List<DTO> toDtoList(List<Entity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    // ==================== 新增操作 ====================

    /**
     * 插入一条记录
     */
    public boolean save(DTO dto) {
        return getBo().save(toEntity(dto));
    }

    /**
     * 批量插入
     */
    public boolean saveBatch(Collection<DTO> dtoList) {
        return getBo().saveBatch(toEntityList(dtoList));
    }

    /**
     * 批量插入（指定批次大小）
     */
    public boolean saveBatch(Collection<DTO> dtoList, int batchSize) {
        return getBo().saveBatch(toEntityList(dtoList), batchSize);
    }

    /**
     * 插入或更新一条记录
     */
    public boolean saveOrUpdate(DTO dto) {
        return getBo().saveOrUpdate(toEntity(dto));
    }

    /**
     * 批量插入或更新
     */
    public boolean saveOrUpdateBatch(Collection<DTO> dtoList) {
        return getBo().saveOrUpdateBatch(toEntityList(dtoList));
    }

    // ==================== 删除操作 ====================

    /**
     * 根据 ID 删除
     */
    public boolean removeById(Serializable id) {
        return getBo().removeById(id);
    }

    /**
     * 根据 DTO 删除（通过 DTO 中的 ID）
     */
    public boolean removeByDto(DTO dto) {
        return getBo().removeById(toEntity(dto));
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
    public boolean updateById(DTO dto) {
        return getBo().updateById(toEntity(dto));
    }

    /**
     * 根据条件更新
     */
    public boolean update(Wrapper<Entity> updateWrapper) {
        return getBo().update(updateWrapper);
    }

    /**
     * 根据条件更新（带 DTO）
     */
    public boolean update(DTO dto, Wrapper<Entity> updateWrapper) {
        return getBo().update(toEntity(dto), updateWrapper);
    }

    /**
     * 根据 ID 批量更新
     */
    public boolean updateBatchById(Collection<DTO> dtoList) {
        return getBo().updateBatchById(toEntityList(dtoList));
    }

    /**
     * 根据 ID 批量更新（指定批次大小）
     */
    public boolean updateBatchById(Collection<DTO> dtoList, int batchSize) {
        return getBo().updateBatchById(toEntityList(dtoList), batchSize);
    }

    // ==================== 查询操作 ====================

    /**
     * 根据 ID 查询
     */
    public DTO getById(Serializable id) {
        return toDto(getBo().getById(id));
    }

    /**
     * 根据条件查询一条记录
     */
    public DTO getOne(Wrapper<Entity> queryWrapper) {
        return toDto(getBo().getOne(queryWrapper));
    }

    /**
     * 根据条件查询一条记录（可选是否抛出异常）
     */
    public DTO getOne(Wrapper<Entity> queryWrapper, boolean throwEx) {
        return toDto(getBo().getOne(queryWrapper, throwEx));
    }

    /**
     * 查询所有记录
     */
    public List<DTO> list() {
        return toDtoList(getBo().list());
    }

    /**
     * 根据条件查询列表
     */
    public List<DTO> list(Wrapper<Entity> queryWrapper) {
        return toDtoList(getBo().list(queryWrapper));
    }

    /**
     * 根据 ID 批量查询
     */
    public List<DTO> listByIds(Collection<? extends Serializable> idList) {
        return toDtoList(getBo().listByIds(idList));
    }

    /**
     * 根据 columnMap 条件查询
     */
    public List<DTO> listByMap(Map<String, Object> columnMap) {
        return toDtoList(getBo().listByMap(columnMap));
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
     * 无条件分页查询（返回 DTO 分页）
     */
    public IPage<DTO> page(long current, long size) {
        Page<Entity> entityPage = getBo().page(new Page<>(current, size));
        return convertPage(entityPage);
    }

    /**
     * 条件分页查询（返回 DTO 分页）
     */
    public IPage<DTO> page(long current, long size, Wrapper<Entity> queryWrapper) {
        Page<Entity> entityPage = getBo().page(new Page<>(current, size), queryWrapper);
        return convertPage(entityPage);
    }

    /**
     * 转换分页结果
     */
    protected IPage<DTO> convertPage(IPage<Entity> entityPage) {
        Page<DTO> dtoPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        dtoPage.setRecords(toDtoList(entityPage.getRecords()));
        return dtoPage;
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
    public LambdaQueryChainWrapper<Entity> lambdaQuery(DTO dto) {
        return getBo().lambdaQuery(toEntity(dto));
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
