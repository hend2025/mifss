package com.aeye.mifss.common.mybatis.service;

import cn.hsa.hsaf.core.framework.web.HsafRestPath;
import cn.hsa.ims.common.utils.AeyePageInfo;
import cn.hsa.ims.common.utils.AeyePageResult;
import cn.hutool.core.bean.BeanUtil;
import com.aeye.mifss.common.dto.RpcMergeDTO;
import com.aeye.mifss.common.mybatis.wrapper.RpcQueryWrapper;
import com.aeye.mifss.common.mybatis.wrapper.RpcUpdateWrapper;
import com.aeye.mifss.common.mybatis.wrapper.RpcWrapperConverter;
import com.aeye.mifss.common.utils.PageUtils;
import com.aeye.mifss.common.utils.Query;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseServiceImpl<DTO, Entity, BO extends IService<Entity>>
        implements LocalService<Entity>, RpcService<DTO> {

    @Autowired
    protected BO bo;

    /**
     * 获取 DTO Class
     */
    protected Class<DTO> getDtoClass() {
        return (Class<DTO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 获取 Entity Class
     */
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


    // ================= LocalService Implementation =================

    @Override
    public Entity getById(Serializable id) {
        return bo.getById(id);
    }

    @Override
    public Entity getOne(Wrapper<Entity> queryWrapper) {
        return bo.getOne(queryWrapper);
    }

    @Override
    public List<Entity> list(Wrapper<Entity> queryWrapper) {
        return bo.list(queryWrapper);
    }

    @Override
    public AeyePageResult<Entity> page(AeyePageInfo pageParam, Wrapper queryWrapper) throws Exception{
        IPage<Entity> page = bo.page(
                new Query<Entity>().getPage(pageParam)
                ,queryWrapper);
        return PageUtils.pageConvert(page);
    }

    @Override
    public boolean save(Entity entity) {
        return bo.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<Entity> entityList) {
        return bo.saveBatch(entityList);
    }

    @Override
    public boolean updateById(Entity entity) {
        return bo.updateById(entity);
    }

    @Override
    public boolean update(Entity entity, Wrapper<Entity> updateWrapper) {
        return bo.update(entity, updateWrapper);
    }

    @Override
    public boolean updateBatchById(Collection<Entity> entityList) {
        return bo.updateBatchById(entityList);
    }

    @Override
    public boolean saveOrUpdate(Entity entity) {
        return bo.saveOrUpdate(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return bo.removeById(id);
    }

    @Override
    public boolean remove(Wrapper<Entity> queryWrapper) {
        return bo.remove(queryWrapper);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return bo.removeByIds(idList);
    }

    @Override
    public long count() {
        return bo.count();
    }

    @Override
    public long count(Wrapper<Entity> queryWrapper) {
        return bo.count(queryWrapper);
    }

    @Override
    public Map<String, Object> getMap(Wrapper<Entity> queryWrapper) {
        return bo.getMap(queryWrapper);
    }


    // ================= RpcService Implementation =================

    @Override
    @HsafRestPath(value = "/getByIdRpc", method = RequestMethod.POST)
    public DTO getByIdRpc(@RequestBody Serializable id) {
        return toDto(bo.getById(id));
    }

    @Override
    @HsafRestPath(value = "/getOneRpc", method = RequestMethod.POST)
    public DTO getOneRpc(@RequestBody RpcQueryWrapper<DTO> queryWrapper) {
        return toDto(bo.getOne(RpcWrapperConverter.toQueryWrapper(queryWrapper)));
    }

    @Override
    @HsafRestPath(value = "/listRpc", method = RequestMethod.POST)
    public List<DTO> listRpc(@RequestBody RpcQueryWrapper<DTO> queryWrapper) {
        return toDtoList(bo.list(RpcWrapperConverter.toQueryWrapper(queryWrapper)));
    }

    @Override
    @HsafRestPath(value = "/pageRpc", method = RequestMethod.POST)
    public AeyePageResult<DTO> pageRpc(@RequestBody RpcMergeDTO<DTO> mergeDTO) throws Exception {
        //转本地条件构造器
        Wrapper<Entity> wrapper = RpcWrapperConverter.toQueryWrapper(mergeDTO.getQueryWrapper());
        AeyePageResult pageResult = this.page(mergeDTO.getPageParam(), wrapper);
        List<DTO> dtoList = getTargetDtoList(pageResult.getData());
        pageResult.setData(dtoList);
        return pageResult;
    }

    @Override
    @HsafRestPath(value = "/saveRpc", method = RequestMethod.POST)
    public boolean saveRpc(@RequestBody DTO dto) {
        return save(toEntity(dto));
    }

    @Override
    @HsafRestPath(value = "/saveBatchRpc", method = RequestMethod.POST)
    public boolean saveBatchRpc(@RequestBody Collection<DTO> dtoList) {
        return saveBatch(toEntityList(dtoList));
    }

    @Override
    @HsafRestPath(value = "/saveOrUpdateRpc", method = RequestMethod.POST)
    public boolean saveOrUpdateRpc(@RequestBody DTO dto) {
        return saveOrUpdate(toEntity(dto));
    }

    @Override
    @HsafRestPath(value = "/updateBatchByIdRpc", method = RequestMethod.POST)
    public boolean updateBatchByIdRpc(@RequestBody Collection<DTO> dtoList) {
        return updateBatchById(toEntityList(dtoList));
    }

    @Override
    @HsafRestPath(value = "/updateByIdRpc", method = RequestMethod.POST)
    public boolean updateByIdRpc(@RequestBody DTO dto) {
        return updateById(toEntity(dto));
    }

    @Override
    @HsafRestPath(value = "/updateRpc", method = RequestMethod.POST)
    public boolean updateRpc(@RequestBody DTO dto, RpcUpdateWrapper<DTO> updateWrapper) {
        return update(toEntity(dto), RpcWrapperConverter.toUpdateWrapper(updateWrapper));
    }

    @Override
    @HsafRestPath(value = "/removeByIdRpc", method = RequestMethod.POST)
    public boolean removeByIdRpc(@RequestBody Serializable id) {
        return removeById(id);
    }

    @Override
    @HsafRestPath(value = "/removeRpc", method = RequestMethod.POST)
    public boolean removeRpc(@RequestBody RpcQueryWrapper<DTO> queryWrapper) {
        return remove(RpcWrapperConverter.toQueryWrapper(queryWrapper));
    }

    @Override
    @HsafRestPath(value = "/removeByIdsRpc", method = RequestMethod.POST)
    public boolean removeByIdsRpc(@RequestBody Collection<? extends Serializable> idList) {
        return removeByIds(idList);
    }

    @Override
    @HsafRestPath(value = "/countRpc", method = RequestMethod.POST)
    public long countRpc(@RequestBody RpcQueryWrapper<DTO> queryWrapper) {
        return count(RpcWrapperConverter.toQueryWrapper(queryWrapper));
    }

    @Override
    @HsafRestPath(value = "/getMapRpc", method = RequestMethod.POST)
    public Map<String, Object> getMapRpc(@RequestBody RpcQueryWrapper<DTO> queryWrapper) {
        return getMap(RpcWrapperConverter.toQueryWrapper(queryWrapper));
    }

    protected List<DTO> getTargetDtoList(List<Entity> dtos) throws Exception{
        String result = JSONObject.toJSONString(dtos);
        return (List<DTO>) JSONObject.parseArray(result, getDtoClass());
    }

}
