package com.aeye.mifss.common.mybatis.service;

import com.aeye.mifss.common.mybatis.wrapper.RpcQueryWrapper;
import com.aeye.mifss.common.mybatis.wrapper.RpcUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RpcService<DTO> {

    /**
     * 根据 ID 查询
     */
    DTO getByIdRpc(Serializable id);

    /**
     * 根据条件查询一条记录
     */
    DTO getOneRpc(RpcQueryWrapper<DTO> queryWrapper);

    /**
     * 根据条件查询列表
     */
    List<DTO> listRpc(RpcQueryWrapper<DTO> queryWrapper);

    /**
     * 分页查询
     */
    IPage<DTO> pageRpc(IPage<DTO> page, RpcQueryWrapper<DTO> queryWrapper);

    /**
     * 插入一条记录
     */
    boolean saveRpc(DTO dto);

    /**
     * 插入（批量）
     */
    boolean saveBatchRpc(Collection<DTO> dtoList);

    /**
     * 根据 ID 修改
     */
    boolean updateByIdRpc(DTO dto);

    /**
     * 根据 UpdateWrapper 条件，更新记录
     */
    boolean updateRpc(DTO dto, RpcUpdateWrapper<DTO> updateWrapper);

    /**
     * 批量更新
     */
    boolean updateBatchByIdRpc(Collection<DTO> dtoList);

    /**
     * 保存或更新
     */
    boolean saveOrUpdateRpc(DTO dto);


    /**
     * 根据 ID 删除
     */
    boolean removeByIdRpc(Serializable id);

    /**
     * 根据 entity 条件，删除记录
     */
    boolean removeRpc(RpcQueryWrapper<DTO> queryWrapper);

    /**
     * 删除（根据ID 批量删除）
     */
    boolean removeByIdsRpc(Collection<? extends Serializable> idList);

    /**
     * 查询总数
     */
    long countRpc();

    /**
     * 根据条件查询总数
     */
    long countRpc(RpcQueryWrapper<DTO> queryWrapper);

    /**
     * 查询 Map
     */
    Map<String, Object> getMapRpc(RpcQueryWrapper<DTO> queryWrapper);

}
