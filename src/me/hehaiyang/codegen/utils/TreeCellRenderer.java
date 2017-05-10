package me.hehaiyang.codegen.utils;

import me.hehaiyang.codegen.model.CodeTemplate;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Desc: 自定义树描述类,将树的每个节点设置成不同的图标
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/9
 */
public class TreeCellRenderer extends DefaultTreeCellRenderer{

    /**
     * 重写父类DefaultTreeCellRenderer的方法
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean isLeaf, int row,boolean hasFocus) {
        // 选中
        if (selected) {
            setForeground(getTextSelectionColor());
        } else {
            setForeground(getTextNonSelectionColor());
        }
        // TreeNode
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;

        Object obj = treeNode.getUserObject();

        if (obj instanceof CodeTemplate) {
            CodeTemplate node = (CodeTemplate) obj;
            DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
            return tempCellRenderer.getTreeCellRendererComponent(tree, node.getDisplay(), selected, expanded, true, row, hasFocus);
        } else if (obj instanceof String) {
            String text = (String) obj;
            DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
//            tempCellRenderer.setOpenIcon(new ImageIcon("Image/open.jpg"));
//            tempCellRenderer.setClosedIcon(new ImageIcon("Image/close.jpg"));
            return tempCellRenderer.getTreeCellRendererComponent(tree, text, selected, expanded, false, row, hasFocus);
        }
        return super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, hasFocus);
    }
}