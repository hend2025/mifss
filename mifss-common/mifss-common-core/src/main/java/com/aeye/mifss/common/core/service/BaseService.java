package com.aeye.mifss.common.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基础服务接口，定义通用的 CRUD 操作
 * 入参和出参均为 DTO 类型
 *
 * @param <DTO> DTO 类型
 */
public interface BaseService<DTO> {

    // ==================== 新增操作 ====================

    boolean save(DTO dto);

    boolean saveBatch(Collection<DTO> dtoList);

    boolean saveBatch(Collection<DTO> dtoList, int batchSize);

    boolean saveOrUpdate(DTO dto);

    boolean saveOrUpdateBatch(Collection<DTO> dtoList);

    // ==================== 删除操作 ====================

    boolean removeById(Serializable id);

    boolean removeByDto(DTO dto);

    boolean removeByIds(Collection<?> idList);

    // ==================== 更新操作 ====================

    boolean updateById(DTO dto);

    boolean updateBatchById(Collection<DTO> dtoList);

    boolean updateBatchById(Collection<DTO> dtoList, int batchSize);

    // ==================== 查询操作 ====================

    DTO getById(Serializable id);

    List<DTO> list();

    List<DTO> listByIds(Collection<? extends Serializable> idList);

    // ==================== 统计操作 ====================

    long count();

    // ==================== 分页操作 ====================

    IPage<DTO> page(long current, long size);

}
