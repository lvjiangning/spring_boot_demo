package com.rihao.property.modules.system.utils;

import com.rihao.property.modules.system.entity.SysResource;
import com.rihao.property.modules.system.vo.TreeResourceVo;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author gaoy
 * 2020/2/29/029
 */
public class TreeResourceUtil {

    private static Comparator<SysResource> comparator = (s, t) -> {
        if (s.getParentId().equals(t.getParentId())) {
            return s.getOrders() - t.getOrders();
        }
        return s.getParentIds().compareTo(t.getParentIds());
    };

    public static List<TreeResourceVo> convertToTreeResource(List<SysResource> resources, boolean removeNoLeaf) {
        if (resources.size() == 0) {
            return Collections.emptyList();
        }
        TreeResourceVo root = new TreeResourceVo().setId(0L)
                .setName("root");
        //对原resource 排序
        resources.sort(comparator);
        recursive(root, resources);
        List<TreeResourceVo> resultTree = root.getChildren();
        if (removeNoLeaf) {
            removeNoLeaf(resultTree);
        }
        return resultTree;
    }

    private static void recursive(TreeResourceVo parent, List<SysResource> resources) {
        Iterator<SysResource> iterator = resources.iterator();
        while (iterator.hasNext()) {
            SysResource resource = iterator.next();
            if (resource.getParentId().equals(parent.getId())) {
                parent.getChildren().add(convert(resource));
                iterator.remove();
            }
        }
        for (TreeResourceVo node : parent.getChildren()) {
            recursive(node, resources);
        }
    }

    private static TreeResourceVo convert(SysResource resource) {
        return new TreeResourceVo().setId(resource.getId())
                .setIcon(resource.getIcon())
                .setName(resource.getName())
                .setText(resource.getText())
                .setType(resource.getType().name())
                .setUrl(resource.getUrl())
                .setParentIds(resource.getParentIds())
                .setCode(resource.getCode());
    }

    private static void removeNoLeaf(List<TreeResourceVo> tree) {
        if (tree.size() == 0) {
            return;
        }
        Iterator<TreeResourceVo> iterator = tree.iterator();
        while (iterator.hasNext()) {
            TreeResourceVo node = iterator.next();
            removeNoLeaf(node.getChildren());
            if (!node.isHasChildren() && ("".equals(node.getUrl()) || null == node.getUrl())) {
                iterator.remove();
            }
        }
    }
}
