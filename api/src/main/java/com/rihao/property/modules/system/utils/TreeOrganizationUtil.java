package com.rihao.property.modules.system.utils;

import com.rihao.property.modules.system.entity.SysOrganization;
import com.rihao.property.modules.system.vo.TreeOrganizationVo;

import java.util.*;

/**
 * @author gaoy
 * 2020/2/29/029
 */
public class TreeOrganizationUtil {

    private static Comparator<SysOrganization> comparator = (s, t) -> {
        if (s.getParentId().equals(t.getParentId())) {
            if (s.getOrders() == null) {
                return 1;
            }
            if (t.getOrders() == null) {
                return -1;
            }
            return s.getOrders() - t.getOrders();
        }
        return s.getParentIds().compareTo(t.getParentIds());
    };

    public static List<TreeOrganizationVo> convertToTreeOrganization(List<SysOrganization> resources, boolean removeNoLeaf) {
        if (resources.size() == 0) {
            return Collections.emptyList();
        }
        TreeOrganizationVo root = new TreeOrganizationVo().setId(0L)
                .setName("root");
        //对原resource 排序
        resources.sort(comparator);
        recursive(root, resources);
        List<TreeOrganizationVo> resultTree = root.getChildren();
        if (removeNoLeaf) {
            removeNoLeaf(resultTree);
        }
        return resultTree;
    }

    /**
     * 组织结构树转换
     * @param resources 资源列表
     * @param removeNoLeaf 是否清除没有子级的节点
     * @param ids 匹配id
     * @return 树级结构
     */
    public static List<TreeOrganizationVo> convertToTreeOrganization(List<SysOrganization> resources, boolean removeNoLeaf, Set<Long> ids) {
        if (resources.size() == 0) {
            return Collections.emptyList();
        }
        TreeOrganizationVo root = new TreeOrganizationVo().setId(0L)
                .setName("root");
        //对原resource 排序
        resources.sort(comparator);
        recursive(root, resources, ids);
        List<TreeOrganizationVo> resultTree = root.getChildren();
        if (removeNoLeaf) {
            removeNoLeaf(resultTree);
        }
        return resultTree;
    }

    private static void recursive(TreeOrganizationVo parent, List<SysOrganization> resources) {
        Iterator<SysOrganization> iterator = resources.iterator();
        while (iterator.hasNext()) {
            SysOrganization resource = iterator.next();
            if (resource.getParentId().equals(parent.getId())) {
                parent.getChildren().add(convert(resource));
                iterator.remove();
            }
        }
        for (TreeOrganizationVo node : parent.getChildren()) {
            recursive(node, resources);
        }
    }

    private static void recursive(TreeOrganizationVo parent, List<SysOrganization> resources, Set<Long> ids) {
        Iterator<SysOrganization> iterator = resources.iterator();
        while (iterator.hasNext()) {
            SysOrganization resource = iterator.next();
            if (resource.getParentId().equals(parent.getId())) {
                TreeOrganizationVo vo = convert(resource);
                if (!ids.contains(resource.getId())) {
                    vo.setDisabled(true);
                }
                parent.getChildren().add(vo);
                iterator.remove();
            }
        }
        for (TreeOrganizationVo node : parent.getChildren()) {
            recursive(node, resources, ids);
        }
    }

    private static TreeOrganizationVo convert(SysOrganization resource) {
        return new TreeOrganizationVo().setId(resource.getId())
                .setName(resource.getName());
    }

    private static void removeNoLeaf(List<TreeOrganizationVo> tree) {
        if (tree.size() == 0) {
            return;
        }
        Iterator<TreeOrganizationVo> iterator = tree.iterator();
        while (iterator.hasNext()) {
            TreeOrganizationVo node = iterator.next();
            removeNoLeaf(node.getChildren());
            if (!node.isHasChildren()) {
                iterator.remove();
            }
        }
    }
}
