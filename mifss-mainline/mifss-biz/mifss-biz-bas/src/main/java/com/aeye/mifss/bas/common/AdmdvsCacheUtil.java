package com.aeye.mifss.bas.common;

import cn.hsa.ims.common.cache.AeyeCacheManager;
import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import cn.hutool.core.bean.BeanUtil;
import com.aeye.mifss.bas.biz.entity.AdmdvsDO;
import com.aeye.mifss.bas.biz.service.AdmdvsService;
import com.aeye.mifss.bas.dto.AdmdvsDTO;

import java.util.ArrayList;
import java.util.List;

public class AdmdvsCacheUtil {

    public static void reloadCache() {
        AdmdvsService admdvsService = AeyeSpringContextUtils.getBean("admdvsServiceImpl");
        admdvsService.reloadCache();
    }

    public static List<AdmdvsDTO> getAdmdvsAll() {
        return AeyeCacheManager.getList("admdvsList", AdmdvsDTO.class);
    }

    public static AdmdvsDTO getDicById(String id) {
        AdmdvsDO bean = AeyeCacheManager.get("scen_admdvs_info_b", id, AdmdvsDO.class);
        if (bean == null) {
            return null;
        }
        return BeanUtil.toBean(bean, AdmdvsDTO.class);
    }

    public static List<AdmdvsDTO> getAdmdvsByPId(String parentId) {
        return AeyeCacheManager.getList("admdvsList:" + parentId, AdmdvsDTO.class);
    }

    public static List<AdmdvsDTO> getAllChildByPId(String parentId) {
        List<AdmdvsDTO> result = new ArrayList<>();
        // 获取直接下级
        List<AdmdvsDTO> children = getAdmdvsByPId(parentId);
        if (children != null && !children.isEmpty()) {
            result.addAll(children);
            // 递归获取所有下级
            for (AdmdvsDTO child : children) {
                result.addAll(getAllChildByPId(child.getAdmdvs()));
            }
        }
        return result;
    }

    public static List<AdmdvsDTO> getAdmdvsAllTree() {
        List<AdmdvsDTO> allList = getAdmdvsAll();
        return buildTree(allList);
    }

    public static List<AdmdvsDTO> getAllChildByPIdTree(String parentId) {
        List<AdmdvsDTO> list = getAllChildByPId(parentId);
        return buildTree(list);
    }

    private static List<AdmdvsDTO> buildTree(List<AdmdvsDTO> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<AdmdvsDTO> trees = new ArrayList<>();
        // ID -> Node
        java.util.Map<String, AdmdvsDTO> nodeMap = new java.util.HashMap<>();
        for (AdmdvsDTO node : list) {
            nodeMap.put(node.getAdmdvs(), node);
        }

        for (AdmdvsDTO node : list) {
            String parentIdVal = node.getPrntAdmdvs();
            if (cn.hutool.core.util.StrUtil.isNotEmpty(parentIdVal) && nodeMap.containsKey(parentIdVal)) {
                AdmdvsDTO parent = nodeMap.get(parentIdVal);
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(node);
            } else {
                trees.add(node);
            }
        }
        return trees;
    }

}
