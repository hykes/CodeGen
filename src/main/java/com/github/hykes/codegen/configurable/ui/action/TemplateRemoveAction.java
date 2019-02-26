package com.github.hykes.codegen.configurable.ui.action;

import com.github.hykes.codegen.configurable.ui.TemplatesUI;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Desc: 模板页面的删除事件
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/1/13
 */
public class TemplateRemoveAction extends BaseTemplateAction implements AnActionButtonRunnable {

    public TemplateRemoveAction(TemplatesUI templatesUI) {
        super(templatesUI);
    }

    @Override
    public void run(AnActionButton button) {
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        if (selectedNode != null && selectedNode.getParent() != null) {
            // 删除指定节点
            DefaultTreeModel model = (DefaultTreeModel) this.templateTree.getModel();
            model.removeNodeFromParent(selectedNode);
        }
    }
}
