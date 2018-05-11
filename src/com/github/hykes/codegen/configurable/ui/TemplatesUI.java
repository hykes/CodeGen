package com.github.hykes.codegen.configurable.ui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.configurable.UIConfigurable;
import com.github.hykes.codegen.configurable.model.Templates;
import com.github.hykes.codegen.configurable.ui.action.TemplateAddAction;
import com.github.hykes.codegen.configurable.ui.action.TemplateEditAction;
import com.github.hykes.codegen.configurable.ui.action.TemplateRemoveAction;
import com.github.hykes.codegen.configurable.ui.editor.TemplateEditorUI;
import com.github.hykes.codegen.model.CodeGroup;
import com.github.hykes.codegen.model.CodeRoot;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.model.ExportTemplate;
import com.github.hykes.codegen.provider.DefaultProviderImpl;
import com.github.hykes.codegen.utils.PsiUtil;
import com.github.hykes.codegen.utils.StringUtils;
import com.github.hykes.codegen.utils.VelocityUtil;
import com.github.hykes.codegen.utils.ZipUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.treeStructure.Tree;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;

/**
 * 模版配置面板
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/5/10
 */
public class TemplatesUI extends JBPanel implements UIConfigurable {

    private static final Logger LOGGER = Logger.getInstance(DefaultProviderImpl.class);

    private Tree templateTree;
    private ToolbarDecorator toolbarDecorator;
    private TemplateEditorUI templateEditor;
    private JSplitPane jSplitPane;

    private static final String TEMPLATE_FILE_EXTENSION = "ZIP";

    private final SettingManager settingManager = SettingManager.getInstance();

    public TemplatesUI() {
        init();
        setTemplates(settingManager.getTemplates());
    }

    @Override
    public boolean isModified(){
        Templates templates = settingManager.getTemplates();
        DefaultMutableTreeNode topNode = (DefaultMutableTreeNode) templateTree.getModel().getRoot();
        // 获取映射map
        Map<String, List<CodeGroup>> groupsMap = templates.getGroupsMap();
        Map<String, List<CodeTemplate>> templateMap = templates.getTemplatesMap();

        // root的判断, 数量判断, name?
        List<CodeRoot> roots = templates.getRoots();
        if(topNode.getChildCount() != roots.size()){
            return true;
        }
        Enumeration rootEnum = topNode.children();
        while (rootEnum.hasMoreElements()) {
            // 组的判断, 数量判断
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) rootEnum.nextElement();
            CodeRoot root = (CodeRoot) rootNode.getUserObject();
            if (rootNode.getChildCount() != groupsMap.get(root.getId()).size()) {
                return true;
            }
            Enumeration enumeration = rootNode.children();
            while(enumeration.hasMoreElements()){
                // 模板判断, 数量判断
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
                CodeGroup group = (CodeGroup) node.getUserObject();
                if(node.getChildCount() != templateMap.get(group.getId()).size()){
                    return true;
                }
                if(templateEditor != null){
                    Enumeration childEnum = node.children();
                    while(childEnum.hasMoreElements()){
                        // 模板内容判断
                        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childEnum.nextElement();
                        CodeTemplate template = (CodeTemplate) childNode.getUserObject();
                        CodeTemplate tmp = templateEditor.getCodeTemplate();
                        if(template.getId().equals(tmp.getId()) && !template.equals(tmp)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void apply() {
        List<CodeRoot> roots = new ArrayList<>();
        DefaultTreeModel treeModel = (DefaultTreeModel) templateTree.getModel();
        DefaultMutableTreeNode topNode = (DefaultMutableTreeNode) treeModel.getRoot();
        Enumeration rootEnum = topNode.children();
        // 获取所有root
        while (rootEnum.hasMoreElements()) {
            List<CodeGroup> groups = new ArrayList<>();
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) rootEnum.nextElement();
            Enumeration enumeration = rootNode.children();
            // 获取所有组
            while(enumeration.hasMoreElements()){
                List<CodeTemplate> templates = new ArrayList<>();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
                Enumeration childEnum = node.children();
                // 获取所有模板
                while(childEnum.hasMoreElements()){
                    DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childEnum.nextElement();
                    CodeTemplate template = (CodeTemplate) childNode.getUserObject();
                    if(templateEditor != null){
                        CodeTemplate tmp = templateEditor.getCodeTemplate();
                        if(template.getId().equals(tmp.getId())){
                            template = tmp;
                        }
                    }
                    templates.add(template);
                }
                CodeGroup group = (CodeGroup) node.getUserObject();
                group.setTemplates(templates);
                groups.add(group);
            }
            CodeRoot root = (CodeRoot) rootNode.getUserObject();
            root.setGroups(groups);
            roots.add(root);
        }
        settingManager.getTemplates().setRoots(roots);
        reset();
    }

    @Override
    public void reset() {
        setTemplates(settingManager.getTemplates());
    }

    private void init(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 新建文件树
        templateTree = new Tree();
        templateTree.putClientProperty("JTree.lineStyle", "Horizontal");
        templateTree.setRootVisible(true);
        templateTree.setShowsRootHandles(true);
        templateTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        templateTree.setCellRenderer(new TemplateTreeCellRenderer());
        // 文件数节点选择事件
        templateTree.addTreeSelectionListener( it -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            Object object = node.getUserObject();
            if(object instanceof CodeTemplate) {
                CodeTemplate template = (CodeTemplate) object;
                templateEditor.getRootPanel().setVisible(true);
                templateEditor.refresh(template);
            } else {
                templateEditor.getRootPanel().setVisible(false);
            }
        });
        // 工具栏
        toolbarDecorator = ToolbarDecorator.createDecorator(templateTree)
            .setAddAction(new TemplateAddAction(this))
            .setRemoveAction(new TemplateRemoveAction(this))
            .setEditAction(new TemplateEditAction(this))
            .addExtraAction(new AnActionButton("Import", AllIcons.Actions.Upload) {
                @Override
                public void actionPerformed(AnActionEvent e) {
                    try {
                        VirtualFile virtualFile = PsiUtil.chooseFile(null, "ZIP Chooser", "Select Import ZIP", true, true, null);
                        if (Objects.isNull(virtualFile)) {
                            return ;
                        }
                        if (!TEMPLATE_FILE_EXTENSION.equals(virtualFile.getExtension().toUpperCase())) {
                            Messages.showInfoMessage("请选择模版压缩文件(.zip)", "ERROR");
                            return ;
                        }
                        List<CodeRoot> roots = new ArrayList<>();
                        List<CodeGroup> groups = new ArrayList<>();
                        List<CodeTemplate> templates = new ArrayList<>();
                        ZipUtil.readZipFile(virtualFile.getCanonicalPath(), templates, groups, roots);

                        Map<String, CodeRoot> rootMap = new HashMap<>();
                        roots.forEach(it -> rootMap.put(it.getName(), it));
                        Map<String, CodeGroup> groupMap = new HashMap<>();
                        groups.forEach(it -> groupMap.put(it.getName(), it));
                        for (CodeGroup group: groups) {
                            if (rootMap.containsKey(group.getRoot())) {
                                rootMap.get(group.getRoot()).getGroups().add(group);
                            }
                        }
                        for (CodeTemplate template: templates) {
                            if (groupMap.containsKey(template.getGroup())) {
                                groupMap.get(template.getGroup()).getTemplates().add(template);
                            }
                        }
                        settingManager.getTemplates().getRoots().addAll(rootMap.values());
                        setTemplates(settingManager.getTemplates());
                        Messages.showInfoMessage("Import templates success", "INFO");
                    } catch (Exception var){
                        LOGGER.error(StringUtils.getStackTraceAsString(var));
                    }
                }

                @Override
                public boolean isEnabled() {
                    return super.isEnabled();
                }
            })
            .addExtraAction(new AnActionButton("Export", AllIcons.Actions.Download) {
                @Override
                public void actionPerformed(AnActionEvent e) {
                    try {
                        VirtualFile virtualFile = PsiUtil.chooseFolder(null, "Directory Chooser", "Select Export Directory", true, true, null);
                        if (Objects.isNull(virtualFile)) {
                            return ;
                        }
                        if (!virtualFile.isDirectory()) {
                            Messages.showInfoMessage("请选择导出文件夹", "ERROR");
                            return ;
                        }

                        String rootId = null;
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                        if (Objects.nonNull(node)) {
                            Object object = node.getUserObject();
                            if(object instanceof CodeRoot) {
                                CodeRoot root = (CodeRoot) object;
                                rootId = root.getId();
                            }
                        }

                        for (CodeRoot root : settingManager.getTemplates().getRoots()) {
                            if (Objects.nonNull(rootId) && !rootId.equals(root.getId())) {
                                continue;
                            }
                            List<ExportTemplate> files = new ArrayList<>();
                            for (CodeGroup group : root.getGroups()) {
                                for (CodeTemplate template : group.getTemplates()) {
                                    ExportTemplate exportTemplate = new ExportTemplate();
                                    exportTemplate.setName(root.getName() + "_" + group.getName() + "_" + template.getDisplay() +".vm");
                                    ByteArrayOutputStream o = new ByteArrayOutputStream();
                                    o.write("#*\n".getBytes());
                                    o.write(("display: "+ template.getDisplay() +";\n").getBytes());
                                    o.write(("extension: "+ template.getExtension() +";\n").getBytes());
                                    o.write(("filename: "+ template.getFilename() +";\n").getBytes());
                                    o.write(("subPath: "+ template.getSubPath() +";\n").getBytes());
                                    o.write(("root: "+ root.getName() +";\n").getBytes());
                                    o.write(("group: "+ group.getName() +";\n").getBytes());
                                    o.write(("level: "+ group.getLevel() +";\n").getBytes());
                                    o.write(("isResources: "+ template.getResources().toString() +";\n").getBytes());
                                    o.write("*#\n".getBytes());
                                    o.write(template.getTemplate().getBytes(Charset.defaultCharset()));
                                    exportTemplate.setBytes(o.toByteArray());
                                    files.add(exportTemplate);
                                }
                            }
                            ZipUtil.export(files, String.format("%s/CodeGen-Template-%s.zip", virtualFile.getCanonicalPath(), root.getName()));
                        }
                        Messages.showInfoMessage("Export CodeGen templates success", "INFO");
                    } catch (Exception var){
                        LOGGER.error(var.getMessage());
                    }
                }

                @Override
                public boolean isEnabled() {
                    return super.isEnabled();
                }
            })
            .addExtraAction(new AnActionButton("Validate", AllIcons.Actions.StartDebugger) {
                @Override
                public void actionPerformed(AnActionEvent e) {
                    if (templateEditor != null) {
                        String template = templateEditor.getCodeTemplate().getTemplate();
                        if (template != null) {
                            try {
                                VelocityContext velocityContext = new VelocityContext();
                                velocityContext.put("model", "Test");
                                VelocityUtil.evaluate(velocityContext, template);
                                Messages.showInfoMessage("validate success", "INFO");
                            } catch (ResourceNotFoundException re){
                                Messages.showInfoMessage("couldn't find the template", "ERROR");
                                LOGGER.error(StringUtils.getStackTraceAsString(re));
                            } catch (ParseErrorException pe){
                                Messages.showInfoMessage("syntax error: problem parsing the template", "ERROR");
                                LOGGER.error(StringUtils.getStackTraceAsString(pe));
                            } catch (MethodInvocationException me){
                                Messages.showInfoMessage("something invoked in the template", "ERROR");
                                LOGGER.error(StringUtils.getStackTraceAsString(me));
                            } catch (Exception ex){
                                Messages.showInfoMessage("validate fail", "ERROR");
                                LOGGER.error(StringUtils.getStackTraceAsString(ex));
                            }
                        }
                    } else {
                        LOGGER.warn("not found template .");
                    }

                }})
            .setEditActionUpdater( it -> {
                // 只能允许CodeRoot, CodeGroup在树中编辑
                final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                return selectedNode != null &&
                        ((selectedNode.getUserObject() instanceof CodeGroup) || selectedNode.getUserObject() instanceof CodeRoot);
            })
            .setAddActionUpdater(it -> {
                final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                // return selectedNode != null;
                return true;
            })
            .setRemoveActionUpdater( it -> {
                // 允许CodeRoot、CodeGroup、CodeTemplate删除
                final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                return selectedNode != null && selectedNode.getParent() != null;
            });
        JPanel templatesPanel = toolbarDecorator.createPanel();
        templatesPanel.setPreferredSize(new Dimension(160,100));
        templateEditor = new TemplateEditorUI();
        jSplitPane = new JSplitPane();
        jSplitPane.setOrientation(1);
        jSplitPane.setContinuousLayout(true);
        jSplitPane.setBorder(BorderFactory.createEmptyBorder());
        jSplitPane.setTopComponent(templatesPanel);
        jSplitPane.setBottomComponent(templateEditor.getRootPanel());
        add(jSplitPane, BorderLayout.CENTER);
        GuiUtils.replaceJSplitPaneWithIDEASplitter(this);
    }

    /**
     * 将模板以tree的方式展开
     */
    private void setTemplates(Templates templates){
        // 获取roots
        List<CodeRoot> roots = templates.getRoots();
        if (roots == null) {
            return;
        }
        // 获取组和模板, 转换成tree
        DefaultMutableTreeNode tree = new DefaultMutableTreeNode();
        roots.forEach(root -> {
            DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode(root);
            root.getGroups().forEach(group -> {
                DefaultMutableTreeNode treeGroup = new DefaultMutableTreeNode(group);
                group.getTemplates().forEach(template -> {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(template);
                    treeGroup.add(node);
                });
                treeRoot.add(treeGroup);
            });
            tree.add(treeRoot);
        });
        templateTree.setModel(new DefaultTreeModel(tree, false));
        templateTree.setRootVisible(false);
    }

    public class TemplateTreeCellRenderer extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = -6564861041507376828L;

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
            if (obj instanceof CodeRoot) {
                CodeRoot node = (CodeRoot) obj;
                DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
                tempCellRenderer.setOpenIcon(AllIcons.Nodes.JavaModuleRoot);
                tempCellRenderer.setClosedIcon(AllIcons.Nodes.JavaModuleRoot);
                tempCellRenderer.setLeafIcon(AllIcons.Nodes.JavaModuleRoot);
                return tempCellRenderer.getTreeCellRendererComponent(tree, node.getName(), selected, expanded, false, row, hasFocus);
            }
            else if (obj instanceof CodeGroup) {
                CodeGroup group = (CodeGroup) obj;
                DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
                tempCellRenderer.setOpenIcon(AllIcons.Nodes.Folder);
                tempCellRenderer.setClosedIcon(AllIcons.Nodes.Folder);
                tempCellRenderer.setLeafIcon(AllIcons.Nodes.Folder);
                return tempCellRenderer.getTreeCellRendererComponent(tree, group.getName(), selected, expanded, false, row, hasFocus);
            }
            else if (obj instanceof CodeTemplate) {
                CodeTemplate node = (CodeTemplate) obj;
                DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
                tempCellRenderer.setOpenIcon(AllIcons.FileTypes.Text);
                tempCellRenderer.setClosedIcon(AllIcons.FileTypes.Text);
                tempCellRenderer.setLeafIcon(AllIcons.FileTypes.Text);
                return tempCellRenderer.getTreeCellRendererComponent(tree, node.getDisplay(), selected, expanded, true, row, hasFocus);
            } else {
                String text = (String) obj;
                DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
                return tempCellRenderer.getTreeCellRendererComponent(tree, text, selected, expanded, false, row, hasFocus);
            }
        }
    }

    public Tree getTemplateTree() {
        return templateTree;
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.getContentPane().add(new TemplatesUI(), BorderLayout.CENTER);
        jFrame.setSize(300, 240);
        jFrame.setLocation(300, 200);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
