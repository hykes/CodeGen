package com.github.hykes.codegen.configurable.ui.action;

import com.github.hykes.codegen.configurable.ui.TemplatesUI;
import com.github.hykes.codegen.configurable.ui.dialog.TemplateGroupEditDialog;
import com.github.hykes.codegen.configurable.ui.dialog.TemplateRootEditDialog;
import com.github.hykes.codegen.model.CodeGroup;
import com.github.hykes.codegen.model.CodeRoot;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Desc: 模板页面的编辑事件
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/1/13
 */
public class TemplateEditAction extends BaseTemplateAction implements AnActionButtonRunnable {

    public TemplateEditAction(TemplatesUI templatesUI) {
        super(templatesUI);
    }

    @Override
    public void run(AnActionButton button) {
        // 获取选中节点
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        if (selectedNode == null) {
            return;
        }
        Object object = selectedNode.getUserObject();
        // 1. 如果是codeRoot
        if (object instanceof CodeRoot) {
            CodeRoot root = (CodeRoot) object;
            // 编辑codeRoot
            TemplateRootEditDialog dialog = new TemplateRootEditDialog();
            dialog.setTitle("Edit Root");
            dialog.getNameField().setText(root.getName());

            dialog.getButtonOK().addActionListener(it -> {
                String name = dialog.getNameField().getText();
                if (StringUtils.isBlank(name)) {
                    showErrorBorder(dialog.getNameField(), true);
                    return;
                }
                root.setName(name.trim());
                selectedNode.setUserObject(root);
                dialog.setVisible(false);
            });
            // 显示dialog
            showDialog(dialog, 300, 150);
        }
        // 2. 如果是codeGroup
        else if (object instanceof CodeGroup) {
            CodeGroup group = (CodeGroup) object;
            // 编辑模版组
            TemplateGroupEditDialog dialog = new TemplateGroupEditDialog();
            dialog.setTitle("Edit Group");
            dialog.getNameTextField().setText(group.getName());
            dialog.getLevelTextField().setText(String.valueOf(group.getLevel() != null ? group.getLevel() : 0));

            dialog.getButtonOK().addActionListener(it -> {
                String name = dialog.getNameTextField().getText();
                String level = dialog.getLevelTextField().getText();
                if (StringUtils.isBlank(name)) {
                    showErrorBorder(dialog.getNameTextField(), true);
                    return;
                } else {
                    showErrorBorder(dialog.getNameTextField(), false);
                }
                if (StringUtils.isBlank(level)) {
                    showErrorBorder(dialog.getLevelTextField(), true);
                    return;
                } else {
                    showErrorBorder(dialog.getLevelTextField(), false);
                }

                group.setName(name.trim());
                group.setLevel(Integer.valueOf(level.trim()));
                selectedNode.setUserObject(group);
                dialog.setVisible(false);
            });
            // 显示dialog
            showDialog(dialog, 300, 150);
        }
    }
}
