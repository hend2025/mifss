package com.aeye.mifss.common.mybatis.service;

import cn.hsa.ims.common.utils.AeyePageResult;
import com.aeye.mifss.common.dto.RpcMergeDTO;
import com.aeye.mifss.common.mybatis.wrapper.RpcQueryWrapper;
import com.aeye.mifss.common.mybatis.wrapper.RpcUpdateWrapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RpcService<DTO> {

    /**
     * 根据 ID 查询
     */
    @PostMapping("/getByIdRpc")
    DTO getByIdRpc(@RequestBody Serializable id);

    /**
     * 根据条件查询一条记录
     */
    @PostMapping("/getOneRpc")
    DTO getOneRpc(@RequestBody RpcQueryWrapper<DTO> queryWrapper);

    /**
     * 根据条件查询列表
     */
    @PostMapping("/listRpc")
    List<DTO> listRpc(@RequestBody RpcQueryWrapper<DTO> queryWrapper);

    /**
     * 分页查询
     */
    @PostMapping("/pageRpc")
    AeyePageResult<DTO> pageRpc(@RequestBody RpcMergeDTO<DTO> mergeDTO) throws Exception;


    /**
     * 插入一条记录
     */
    @PostMapping("/saveRpc")
    boolean saveRpc(@RequestBody DTO dto);

    /**
     * 插入（批量）
     */
    @PostMapping("/saveBatchRpc")
    boolean saveBatchRpc(@RequestBody Collection<DTO> dtoList);

    /**
     * 根据 ID 修改
     */
    @PostMapping("/updateByIdRpc")
    boolean updateByIdRpc(@RequestBody DTO dto);

    /**
     * 根据 UpdateWrapper 条件，更新记录
     */
    @PostMapping("/updateRpc")
    boolean updateRpc(@RequestBody DTO dto, RpcUpdateWrapper<DTO> updateWrapper);

    /**
     * 批量更新
     */
    @PostMapping("/updateBatchByIdRpc")
    boolean updateBatchByIdRpc(@RequestBody Collection<DTO> dtoList);

    /**
     * 保存或更新
     */
    @PostMapping("/saveOrUpdateRpc")
    boolean saveOrUpdateRpc(@RequestBody DTO dto);

    /**
     * 根据 ID 删除
     */
    @PostMapping("/removeByIdRpc")
    boolean removeByIdRpc(@RequestBody Serializable id);

    /**
     * 根据 entity 条件，删除记录
     */
    @PostMapping("/removeRpc")
    boolean removeRpc(@RequestBody RpcQueryWrapper<DTO> queryWrapper);

    /**
     * 删除（根据ID 批量删除）
     */
    @PostMapping("/removeByIdsRpc")
    boolean removeByIdsRpc(@RequestBody Collection<? extends Serializable> idList);

    /**
     * 根据条件查询总数
     */
    @PostMapping("/countRpc")
    long countRpc(@RequestBody RpcQueryWrapper<DTO> queryWrapper);

    /**
     * 查询 Map
     */
    @PostMapping("/getMapRpc")
    Map<String, Object> getMapRpc(@RequestBody RpcQueryWrapper<DTO> queryWrapper);

}
