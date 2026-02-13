package com.aeye.mifss.common.mybatis.service;

import cn.hsa.hsaf.core.cache.HsafCacheManager;
import cn.hsa.hsaf.core.framework.web.HsafRestPath;
import cn.hsa.ims.common.cache.AeyeCacheManager;
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aeye.mifss.common.annotation.CacheEntity;
import cn.hutool.core.util.StrUtil;

public class BaseServiceImpl<DTO, Entity, BO extends IService<Entity>>
        implements LocalService<Entity>, RpcService<DTO> {

    @Autowired(required = false)
    protected BO bo;

    @Autowired(required = false)
    HsafCacheManager cacheManager;

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

    @Override
    public Entity getById(Serializable id) {
        CacheEntity cacheEntity = getEntityClass().getAnnotation(CacheEntity.class);
        if (cacheEntity != null) {
            String key = id.toString();
            String cachePre = getCachePrefix(cacheEntity);
            Entity obj = AeyeCacheManager.get(cachePre, key, getEntityClass());
            if (obj != null) {
                return obj;
            }
            Entity entity = bo.getById(id);
            if (entity != null) {
                long expire = cacheEntity.expire() > 0 ? cacheEntity.expire() : 1440 * 7;
                AeyeCacheManager.put(cachePre, key, entity, expire);
            }
            return entity;
        }
        return bo.getById(id);
    }

    private String getCachePrefix(CacheEntity cacheEntity) {
        String prefix = cacheEntity.keyPrefix();
        if (StrUtil.isEmpty(cacheEntity.keyPrefix())) {
            prefix = getEntityClass().getSimpleName();
        }
        return prefix;
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
    public AeyePageResult<Entity> page(AeyePageInfo pageParam, Wrapper queryWrapper) throws Exception {
        IPage<Entity> page = bo.page(
                new Query<Entity>().getPage(pageParam), queryWrapper);
        return PageUtils.pageConvert(page);
    }

    @Override
    public boolean save(Entity entity) {
        boolean result = bo.save(entity);
        if (result) {
            updateCache(entity);
        }
        return result;
    }

    @Override
    public boolean saveBatch(Collection<Entity> entityList) {
        return bo.saveBatch(entityList);
    }

    @Override
    public boolean updateById(Entity entity) {
        boolean result = bo.updateById(entity);
        if (result) {
            updateCache(entity);
        }
        return result;
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
        boolean result = bo.saveOrUpdate(entity);
        if (result) {
            updateCache(entity);
        }
        return result;
    }

    @Override
    public boolean removeById(Serializable id) {
        boolean result = bo.removeById(id);
        if (result) {
            removeCache(id);
        }
        return result;
    }

    @Override
    public boolean remove(Wrapper<Entity> queryWrapper) {
        return bo.remove(queryWrapper);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        boolean result = bo.removeByIds(idList);
        if (result && idList != null) {
            for (Serializable id : idList) {
                removeCache(id);
            }
        }
        return result;
    }

    private void updateCache(Entity entity) {
        CacheEntity cacheEntity = getEntityClass().getAnnotation(CacheEntity.class);
        if (cacheEntity != null) {
            try {
                Object id = getIdVal(entity);
                if (id != null) {
                    String key = id.toString();
                    String cachePre = getCachePrefix(cacheEntity);
                    long expire = cacheEntity.expire() > 0 ? cacheEntity.expire() : 1440 * 7;
                    AeyeCacheManager.put(cachePre, key, entity, expire);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void removeCache(Serializable id) {
        CacheEntity cacheEntity = getEntityClass().getAnnotation(CacheEntity.class);
        if (cacheEntity != null) {
            String key = id.toString();
            String cachePre = getCachePrefix(cacheEntity);
            AeyeCacheManager.remove(cachePre, key);
        }
    }

    private Object getIdVal(Entity entity) {
        try {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
            String keyProperty = tableInfo.getKeyProperty();
            Assert.notEmpty(keyProperty, "错误: 不能够执行. 因为不能找到主键配置在实体类!", new Object[0]);
            Object idVal = ReflectionKit.getFieldValue(entity, keyProperty);
            return idVal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        // 转本地条件构造器
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

    protected List<DTO> getTargetDtoList(List<Entity> dtos) throws Exception {
        String result = JSONObject.toJSONString(dtos);
        return (List<DTO>) JSONObject.parseArray(result, getDtoClass());
    }

    @Override
    public void reloadCache() {
        CacheEntity cacheEntity = getEntityClass().getAnnotation(CacheEntity.class);
        if (cacheEntity == null) {
            return;
        }

        // 分页读取全部数据
        int pageNum = 1;
        int pageSize = 1000;
        List<Entity> allList = new ArrayList<>();
        while (true) {
            Page<Entity> page = new Page<>(pageNum, pageSize);
            IPage<Entity> resultPage = bo.page(page, new QueryWrapper<>());
            List<Entity> records = resultPage.getRecords();
            if (records == null || records.isEmpty()) {
                break;
            }
            allList.addAll(records);
            if (records.size() != pageSize) {
                break;
            }
            pageNum++;
        }

        if (allList == null || allList.isEmpty()) {
            return;
        }

        // 存入缓存
        String cachePre = getCachePrefix(cacheEntity);
        long expire = cacheEntity.expire() > 0 ? cacheEntity.expire() : 1440 * 7;
        for (Entity entity : allList) {
            Object id = getIdVal(entity);
            if (id != null) {
                AeyeCacheManager.put(cachePre, id.toString(), entity, expire);
            }
        }

        // Put list
        String simpleName = getEntityClass().getSimpleName();
        String listKey = StrUtil.lowerFirst(simpleName) + "List";
        if (simpleName.endsWith("DO")) {
            listKey = StrUtil.lowerFirst(simpleName.substring(0, simpleName.length() - 2)) + "List";
        }

        if (cacheEntity.cacheList()) {
            AeyeCacheManager.putList(listKey, allList);
        }

        // Group caching
        String groupField = cacheEntity.groupField();
        if (StrUtil.isNotEmpty(groupField)) {
            Map<String, List<Entity>> groupMap = new java.util.HashMap<>();
            for (Entity entity : allList) {
                try {
                    Object groupVal = ReflectionKit.getFieldValue(entity, groupField);
                    if (groupVal != null) {
                        String groupKey = groupVal.toString();
                        groupMap.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(entity);
                    }
                } catch (Exception e) {

                }
            }
            for (Map.Entry<String, List<Entity>> entry : groupMap.entrySet()) {
                String cacheKey = listKey + ":" + entry.getKey();
                AeyeCacheManager.putList(cacheKey, entry.getValue());
            }
        }

    }

}
