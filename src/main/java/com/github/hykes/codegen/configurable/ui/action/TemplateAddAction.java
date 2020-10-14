package com.github.hykes.codegen.configurable.ui.action;

import com.github.hykes.codegen.configurable.ui.TemplatesUI;
import com.github.hykes.codegen.configurable.ui.dialog.TemplateEditDialog;
import com.github.hykes.codegen.configurable.ui.dialog.TemplateGroupEditDialog;
import com.github.hykes.codegen.configurable.ui.dialog.TemplateRootEditDialog;
import com.github.hykes.codegen.model.CodeGroup;
import com.github.hykes.codegen.model.CodeRoot;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Desc: 模板页面的添加事件
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/1/5
 */
public class TemplateAddAction extends BaseTemplateAction implements AnActionButtonRunnable {

    public TemplateAddAction(TemplatesUI templatesUI) {
        super(templatesUI);
    }

    @Override
    public void run(AnActionButton button) {
        // 获取选中节点
        final DefaultMutableTreeNode selectedNode = getSelectedNode();
        List<AnAction> actions = getMultipleActions(selectedNode);
        if (actions == null || actions.isEmpty()) {
            return;
        }
        // 显示新增按钮
        final DefaultActionGroup group = new DefaultActionGroup(actions);
        JBPopupFactory.getInstance()
                .createActionGroupPopup(null, group, DataManager.getInstance().getDataContext(button.getContextComponent()),
                        JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, true).show(button.getPreferredPopupPoint());
    }


    /**
     * 获取点击加号按钮的各种添加操作, 包括root/group/template
     */
    private List<AnAction> getMultipleActions(DefaultMutableTreeNode selectedNode) {
        // 初始化所有的AnAction
        List<AnAction> actions = new ArrayList<>();
        CodeRootAddAction rootAction = new CodeRootAddAction();
        CodeGroupAddAction groupAction = new CodeGroupAddAction();
        CodeTemplateAddAction templateAction = new CodeTemplateAddAction();

        // 0. 如果未选中节点
        if (selectedNode == null) {
            actions.add(rootAction);
            return actions;
        }

        Object object = selectedNode.getUserObject();
        // 1. 如果是选中的root, 则可以新增root和group
        if (object instanceof CodeRoot) {
            actions.add(rootAction);
            actions.add(groupAction);
        }
        // 2. 如果选中的是group, 则可以新增root, group以及template
        if (object instanceof CodeGroup) {
            actions.add(rootAction);
            actions.add(groupAction);
            actions.add(templateAction);
        }
        // 3. 如果选中的是template, 则可以新增root, group以及template
        if (object instanceof CodeTemplate) {
            actions.add(rootAction);
            actions.add(groupAction);
            actions.add(templateAction);
        }
        return actions;
    }

    /**
     * 新增CodeRoot的事件
     */
    class CodeRootAddAction extends AnAction {

        public CodeRootAddAction() {
            super("Code Root", null, AllIcons.Nodes.JavaModule);
        }

        @Override
        public void actionPerformed(AnActionEvent anActionEvent) {
            // 1. 显示root的新增dialog
            TemplateRootEditDialog dialog = new TemplateRootEditDialog();
            dialog.setTitle("Create New Root");
            dialog.getButtonOK().addActionListener(e -> {
                // 获取名称
                String name = dialog.getNameField().getText();
                if (StringUtils.isBlank(name)) {
                    showErrorBorder(dialog.getNameField(), true);
                    return;
                }
                // 新增root
                addCodeRoot(CodeRoot.fromName(name.trim()));
                dialog.setVisible(false);
            });
            // 2. 显示dialog
            showDialog(dialog, 300, 150);
        }
    }

    /**
     * 新增CodeGroup的事件
     */
    class CodeGroupAddAction extends AnAction {

        public CodeGroupAddAction() {
            super("Code Group", null, AllIcons.Nodes.Folder);
        }

        @Override
        public void actionPerformed(AnActionEvent anActionEvent) {
            // 1. 显示CodeGroup新增dialog
            TemplateGroupEditDialog dialog = new TemplateGroupEditDialog();
            dialog.setTitle("Create New Group");
            dialog.getButtonOK().addActionListener(it -> {
                // 获取名称和level, 校验
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
                // 新增group
                addCodeGroup(new CodeGroup(name.trim(), Integer.valueOf(level.trim())));
                dialog.setVisible(false);
            });
            // 2. 显示dialog
            showDialog(dialog, 300, 150);
        }
    }

    /**
     * 新增CodeTemplate的事件
     */
    public class CodeTemplateAddAction extends AnAction {

        public CodeTemplateAddAction() {
            super("Code Template", null, AllIcons.FileTypes.Text);
        }

        @Override
        public void actionPerformed(AnActionEvent anActionEvent) {
            // 1. 显示CodeTemplate新增dialog
            TemplateEditDialog dialog = new TemplateEditDialog();
            dialog.setTitle("Create New Template");
            dialog.getButtonOK().addActionListener(it -> {
                // 获取内容, 校验
                String display = dialog.getDisplayTextField().getText();
                String extension = dialog.getExtensionTextField().getText();
                if (StringUtils.isBlank(display)) {
                    showErrorBorder(dialog.getDisplayTextField(), true);
                    return;
                } else {
                    showErrorBorder(dialog.getDisplayTextField(), false);
                }
                if (StringUtils.isBlank(extension)) {
                    showErrorBorder(dialog.getExtensionTextField(), true);
                    return;
                } else {
                    showErrorBorder(dialog.getExtensionTextField(), false);
                }
                // 新增template
                addCodeTemplate(new CodeTemplate(UUID.randomUUID().toString(), display.trim(), extension.trim(), display.trim(), "", null, false));
                dialog.setVisible(false);
            });
            // 2. 显示dialog
            showDialog(dialog, 300, 150);
        }
    }
}
