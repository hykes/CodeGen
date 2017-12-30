package com.github.hykes.codegen.configurable.ui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.configurable.UIConfigurable;
import com.github.hykes.codegen.configurable.model.Templates;
import com.github.hykes.codegen.configurable.ui.dialog.TemplateEditDialog;
import com.github.hykes.codegen.configurable.ui.dialog.TemplateGroupEditDialog;
import com.github.hykes.codegen.configurable.ui.editor.TemplateEditorUI;
import com.github.hykes.codegen.model.CodeGroup;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.model.ExportTemplate;
import com.github.hykes.codegen.provider.DefaultProviderImpl;
import com.github.hykes.codegen.utils.NotifyUtil;
import com.github.hykes.codegen.utils.ZipUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
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

    private final SettingManager settingManager = SettingManager.getInstance();

    public TemplatesUI() {
        init();
        setTemplates(settingManager.getTemplates());
    }

    @Override
    public boolean isModified(){

        Templates templates = settingManager.getTemplates();
        List<CodeGroup> groups = templates.getGroups();
        Map<String, List<CodeTemplate>> templateMap = templates.getTemplatesMap();

        DefaultMutableTreeNode rootNode=(DefaultMutableTreeNode)templateTree.getModel().getRoot();
        if(rootNode.getChildCount() != groups.size()){
            return true;
        }

        Enumeration enumeration = rootNode.children();
        while(enumeration.hasMoreElements()){
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
            CodeGroup group = (CodeGroup) node.getUserObject();
            if(node.getChildCount() != templateMap.get(group.getId()).size()){
                return true;
            }
            if(templateEditor != null){
                Enumeration childEnum = node.children();
                while(childEnum.hasMoreElements()){
                    DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childEnum.nextElement();
                    CodeTemplate template = (CodeTemplate) childNode.getUserObject();
                    CodeTemplate tmp = templateEditor.getCodeTemplate();
                    if(template.getId().equals(tmp.getId()) && !template.equals(tmp)){
                       return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void apply() {
        List<CodeGroup> groups = new ArrayList<>();
        DefaultTreeModel treeModel = (DefaultTreeModel) templateTree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        Enumeration enumeration = rootNode.children();
        while(enumeration.hasMoreElements()){
            List<CodeTemplate> templates = new ArrayList<>();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
            Enumeration childEnum = node.children();
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

        settingManager.getTemplates().setGroups(groups);
        reset();
    }

    @Override
    public void reset() {
        setTemplates(settingManager.getTemplates());
    }

    private void init(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        templateTree = new Tree();
        templateTree.putClientProperty("JTree.lineStyle", "Horizontal");
        templateTree.setRootVisible(true);
        templateTree.setShowsRootHandles(true);
        templateTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        templateTree.setCellRenderer(new TemplateTreeCellRenderer());

        templateTree.addTreeSelectionListener( it -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }
            Object object = node.getUserObject();
            if(object instanceof CodeTemplate) {
                CodeTemplate template = (CodeTemplate) object;
                templateEditor.refresh(template);
                LOGGER.info(String.format("click: %s", template.getDisplay()));
            }else if(object instanceof CodeGroup) {
                CodeGroup group = (CodeGroup) object;
                LOGGER.info(String.format("click: %s",  group.getName()));
            } else if(object instanceof String) {
                String text = (String)object;
                LOGGER.info(String.format("click: %s", text));
            }
        });

        toolbarDecorator = ToolbarDecorator.createDecorator(templateTree)
            .setAddAction( it -> addAction())
            .setRemoveAction( it -> removeAction())
            .setEditAction(it -> editorAction())
            .addExtraAction(new AnActionButton("Import", AllIcons.Actions.Upload) {
                @Override
                public void actionPerformed(AnActionEvent e) {
                    try {
                        JFileChooser jfc = new JFileChooser();
                        jfc.setAcceptAllFileFilterUsed(false);
                        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fileFilter(jfc);
                        int result = jfc.showDialog(new JLabel(), "确定");
                        if (result == JFileChooser.APPROVE_OPTION) { // 确认打开
                            File file = jfc.getSelectedFile();
                            if (file.exists()) {
                                if (!file.isDirectory()) {
                                    List<CodeTemplate> templates = new ArrayList<>();
                                    List<CodeGroup> groups = new ArrayList<>();
                                    ZipUtil.readZipFile(file.getAbsolutePath(), templates, groups);
                                    Map<String, CodeGroup> groupMap = new HashMap<>();
                                    for (CodeGroup group: groups) {
                                        groupMap.put(group.getName(), group);
                                    }
                                    for (CodeTemplate template: templates) {
                                        if (groupMap.containsKey(template.getGroup())) {
                                            groupMap.get(template.getGroup()).getTemplates().add(template);
                                        }
                                    }
                                    List<CodeGroup> groupSetting = new ArrayList<>();
                                    groupSetting.addAll(groupMap.values());
                                    groupSetting.addAll(settingManager.getTemplates().getGroups());
                                    settingManager.getTemplates().setGroups(groupSetting);
                                }
                            } else {
                                NotifyUtil.notice("请选择模版压缩文件", MessageType.WARNING);
                            }
                        } else if (result == JFileChooser.ERROR_OPTION) {
                            NotifyUtil.notice("选择模版压缩文件出错", MessageType.WARNING);
                        }
                    } catch (Exception var){
                        LOGGER.error(var.getMessage());
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
                        JFileChooser jfc = new JFileChooser();
                        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int result = jfc.showDialog(new JLabel(), "确定");
                        if (result == JFileChooser.APPROVE_OPTION) { // 确认打开
                            File file = jfc.getSelectedFile();
                            if (file.exists()) {
                                if (file.isDirectory()) {
                                    List<ExportTemplate> files = new ArrayList<>();
                                    for (CodeGroup group : settingManager.getTemplates().getGroups()) {
                                        for (CodeTemplate template : group.getTemplates()) {
                                            ExportTemplate exportTemplate = new ExportTemplate();
                                            exportTemplate.setName(group.getName()+template.getDisplay()+".vm");
                                            exportTemplate.setInputStream(new ByteArrayInputStream(template.getTemplate().getBytes(Charset.defaultCharset())));
                                            files.add(exportTemplate);
                                        }
                                    }
                                    ZipUtil.export(files, file.getAbsolutePath() + "/CodeGen-Templates.zip");
                                    NotifyUtil.notice("Export CodeGen templates", file.getAbsolutePath() + "/CodeGen-Templates.zip", MessageType.INFO);
                                }
                            } else {
                                NotifyUtil.notice("请选择模版文件夹", MessageType.WARNING);
                            }
                        } else if (result == JFileChooser.ERROR_OPTION) {
                            NotifyUtil.notice("选择文件夹出错", MessageType.WARNING);
                        }
                    } catch (Exception var){
                        LOGGER.error(var.getMessage());
                    }
                }

                @Override
                public boolean isEnabled() {
                    return super.isEnabled();
                }
            })
            .setEditActionUpdater( it -> {
                // 只能允许CodeGroup在树中编辑
                final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                return selectedNode != null && (selectedNode.getUserObject() instanceof CodeGroup);
            })
            .setAddActionUpdater( it -> {
                // 只允许Root、CodeGroup添加子节点
                final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                return selectedNode != null && !(selectedNode.getUserObject() instanceof CodeTemplate);
            })
            .setRemoveActionUpdater( it -> {
                // 只允许CodeGroup、CodeTemplate删除
                final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                return selectedNode != null && selectedNode.getParent() != null;
            });
        JPanel templatesPanel = toolbarDecorator.createPanel();
        templatesPanel.setPreferredSize(new Dimension(160,100));
        add(templatesPanel, BorderLayout.WEST);

        templateEditor = new TemplateEditorUI();
        add(templateEditor.$$$getRootComponent$$$(), BorderLayout.CENTER);
    }

    /**
     * 多类型时使用
     */
    private void fileFilter(JFileChooser jfc) {
        String[][] fileNames = { { ".zip", "模版压缩文件(*.zip)" } };
        // 循环添加需要显示的文件
        for (String[] fileName : fileNames) {
            jfc.addChoosableFileFilter(new FileFilter() {
                public boolean accept(File file) {
                    if (file.getName().endsWith(fileName[0])
                            || file.isDirectory()) {
                        return true;
                    }
                    return false;
                }

                public String getDescription() {
                    return fileName[1];
                }

            });
        }
    }


    private void addAction(){
        //获取选中节点
        final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
        //如果节点为空，直接返回
        if (selectedNode == null) {
            return;
        }
        Object object = selectedNode.getUserObject();
        if(object instanceof CodeTemplate) {
            return;
        }
        if(object instanceof String) {
            // 新增模版组
            TemplateGroupEditDialog dialog = new TemplateGroupEditDialog();
            dialog.setTitle("Create New Group");
            dialog.getButtonOK().addActionListener( it ->{
                String name = dialog.getNameTextField().getText().trim();
                String level = dialog.getLevelTextField().getText().trim();

                CodeGroup group = new CodeGroup(UUID.randomUUID().toString(), name, Integer.valueOf(level), new ArrayList<>());
                addNode(selectedNode, new DefaultMutableTreeNode(group));
                dialog.setVisible(false);
            });

            dialog.setSize(300, 150);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(this);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
        if(object instanceof CodeGroup){
            // 新增模版
            TemplateEditDialog dialog = new TemplateEditDialog();
            dialog.setTitle("Create New Template");
            dialog.getButtonOK().addActionListener( it ->{
                String display = dialog.getDisplayTextField().getText();
                String extension = dialog.getExtensionTextField().getText();

                addNode(selectedNode, new DefaultMutableTreeNode(new CodeTemplate(UUID.randomUUID().toString(), display, extension, display, "", null, false)));
                dialog.setVisible(false);
            });

            dialog.setSize(300, 150);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(this);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
    }

    /**
     * 直接通过model来添加新节点，则无需通过调用JTree的updateUI方法
     * model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());
     * 直接通过节点添加新节点，则需要调用tree的updateUI方法
     */
    private void addNode(DefaultMutableTreeNode pNode, MutableTreeNode newNode){
        pNode.add(newNode);
        //--------下面代码实现显示新节点（自动展开父节点）-------
        DefaultTreeModel model = (DefaultTreeModel) templateTree.getModel();
        TreeNode[] nodes = model.getPathToRoot(newNode);
        TreePath path = new TreePath(nodes);
        templateTree.scrollPathToVisible(path);
        templateTree.updateUI();
    }

    private void removeAction(){
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
        if (selectedNode != null && selectedNode.getParent() != null) {
            //删除指定节点
            DefaultTreeModel model = (DefaultTreeModel) templateTree.getModel();
            model.removeNodeFromParent(selectedNode);
        }
    }

    private void editorAction(){

        //获取选中节点
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
        //如果节点为空，直接返回
        if (selectedNode == null) {
            return;
        }
        Object object = selectedNode.getUserObject();
        if(object instanceof CodeGroup){

            CodeGroup group = (CodeGroup) object;

            // 编辑模版组
            TemplateGroupEditDialog dialog = new TemplateGroupEditDialog();
            dialog.setTitle("Edit Group");

            dialog.getNameTextField().setText(group.getName());
            dialog.getLevelTextField().setText(String.valueOf(group.getLevel() != null ? group.getLevel() : 0));
            dialog.getButtonOK().addActionListener( it ->{
                String name = dialog.getNameTextField().getText().trim();
                String level = dialog.getLevelTextField().getText().trim();

                group.setName(name);
                group.setLevel(Integer.valueOf(level));
                selectedNode.setUserObject(group);
                dialog.setVisible(false);
            });

            dialog.setSize(300, 150);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(this);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

        }
    }

    private void setTemplates(Templates templates){

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

        List<CodeGroup> groups = templates.getGroups();
        groups.forEach( it -> {
            DefaultMutableTreeNode group = new DefaultMutableTreeNode(it);
            it.getTemplates().forEach( template -> {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(template);
                group.add(node);
            });
            root.add(group);
        });
        templateTree.setModel(new DefaultTreeModel(root, false));
    }

    public class TemplateTreeCellRenderer extends DefaultTreeCellRenderer{

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
            } else if (obj instanceof CodeGroup) {
                CodeGroup group = (CodeGroup) obj;
                DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
                return tempCellRenderer.getTreeCellRendererComponent(tree, group.getName(), selected, expanded, false, row, hasFocus);
            }else{
                String text = (String) obj;
                DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
                return tempCellRenderer.getTreeCellRendererComponent(tree, text, selected, expanded, false, row, hasFocus);
            }
        }
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
