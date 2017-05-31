package me.hehaiyang.codegen.setting.ui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.treeStructure.Tree;
import lombok.Data;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.setting.SettingManager;
import me.hehaiyang.codegen.setting.UIConfigurable;
import me.hehaiyang.codegen.setting.ui.template.TemplateEditor;
import me.hehaiyang.codegen.setting.ui.template.TemplateTreeCellRenderer;
import me.hehaiyang.codegen.setting.ui.variable.AddDialog;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/10
 */
@Data
public class TemplatesUI extends JBPanel implements UIConfigurable {

    private Tree templateTree;
    private ToolbarDecorator toolbarDecorator;
    private TemplateEditor templateEditor;

    private final SettingManager settingManager = SettingManager.getInstance();

    public TemplatesUI() {
        init();
        setTemplates(settingManager.getTemplatesSetting());
    }

    @Override
    public boolean isModified(){

        TemplatesSetting setting = settingManager.getTemplatesSetting();

        DefaultMutableTreeNode gen=(DefaultMutableTreeNode)templateTree.getModel().getRoot();
        Integer count = 0;
        for (int i=0;i<gen.getChildCount();i++) {
            count += 1;
            count += gen.getChildAt(i).getChildCount();
        }
        if(!count.equals(setting.getCount())){
            return true;
        }

//        TemplateEditor template = this.getTemplateEditor();
//        if(!setting.getCodeTemplateTree().isEmpty() && template != null){
//            CodeTemplate codeTemplate = setting.getCodeTemplate("SpringMvc", template.getCodeTemplate().getId());
//            if (codeTemplate != null && codeTemplate.hashCode() != template.hashCode()) {
//                return true;
//            }
//        }else if(!Strings.isNullOrEmpty(template.getCodeTemplate().getId())){
//            return true;
//        }

        return false;
    }

    @Override
    public void apply() {
        Map<String, List<CodeTemplate>> codeTemplateTree = Maps.newHashMap();

        DefaultTreeModel treeModel = (DefaultTreeModel) templateTree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        Enumeration enumeration = rootNode.children();
        while(enumeration.hasMoreElements()){
            List<CodeTemplate> codeTemplates = Lists.newArrayList();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();

            Enumeration childEnum = node.children();
            while(childEnum.hasMoreElements()){
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childEnum.nextElement();
                codeTemplates.add((CodeTemplate) childNode.getUserObject());
            }
            codeTemplateTree.put((String)node.getUserObject(), codeTemplates);
        }
        settingManager.getTemplatesSetting().setCodeTemplateTree(codeTemplateTree);
    }

    @Override
    public void reset() {

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
            if (node == null) return;
            Object object = node.getUserObject();
            if(object instanceof CodeTemplate) {
                CodeTemplate template = (CodeTemplate) object;
                templateEditor.refresh(template);
                System.out.println("你点击了：" + template.getDisplay());
            } else if(object instanceof String) {
                String text = (String)object;
                System.out.println("你点击了：" + text);
            }
        });

        toolbarDecorator = ToolbarDecorator.createDecorator(templateTree)
            .setAddAction( it -> addAction())
            .setRemoveAction( it -> removeAction())
            .setEditAction(it -> editorAction())
            .setEditActionUpdater( it -> {
                final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                return selectedNode != null && selectedNode.getParent() != null && !(selectedNode.getUserObject() instanceof CodeTemplate);
            })
            .setAddActionUpdater( it -> {
                final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                return selectedNode != null && !(selectedNode.getUserObject() instanceof CodeTemplate);
            })
            .setRemoveActionUpdater( it -> {
                final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
                return selectedNode != null && selectedNode.getParent() != null;
            });
        JPanel templatesPanel = toolbarDecorator.createPanel();
        templatesPanel.setPreferredSize(new Dimension(200,100));
        add(templatesPanel, BorderLayout.WEST);

        templateEditor = new TemplateEditor();
        add(templateEditor, BorderLayout.CENTER);
    }

    private void addAction(){
        //获取选中节点
        final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) templateTree.getLastSelectedPathComponent();
        //如果节点为空，直接返回
        if (selectedNode == null) {
            return;
        }
        Object object = selectedNode.getUserObject();
        if(object instanceof CodeTemplate) return;
        if(object instanceof String) {

            String text = (String)object;
            if("TemplateList".equals(text)){
                //创建一个新节点

                JDialog dialog = new AddDialog();
                dialog.setTitle("Create New Group");
                dialog.setLayout(new BorderLayout());

                JPanel form = new JPanel(new GridLayout(2,2));
                form.add(new Label("Group Name"));
                JTextField keyJTextField = new JTextField();
                form.add(keyJTextField);

                dialog.add(form, BorderLayout.CENTER);

                JButton add = new JButton("ADD");
                add.addActionListener( it ->{
                    String key = keyJTextField.getText();

                    addNode(selectedNode, new DefaultMutableTreeNode(key));

                    dialog.setVisible(false);
                });
                dialog.add(add, BorderLayout.SOUTH);

                dialog.setSize(300, 100);
                dialog.setAlwaysOnTop(true);
                dialog.setLocationRelativeTo(this);
                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                dialog.setResizable(false);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);

            }else{

                //创建一个新节点

                JDialog dialog = new AddDialog();
                dialog.setTitle("Create New Template");
                dialog.setLayout(new BorderLayout());

                JPanel form = new JPanel(new GridLayout(2,2));
                form.add(new Label("display"));
                JTextField displayJTextField = new JTextField();
                form.add(displayJTextField);
                form.add(new Label("extension"));
                JTextField extensionJTextField = new JTextField();
                form.add(extensionJTextField);

                dialog.add(form, BorderLayout.CENTER);

                JButton add = new JButton("ADD");
                add.addActionListener( it ->{
                    String display = displayJTextField.getText();
                    String extension = extensionJTextField.getText();

                    addNode(selectedNode, new DefaultMutableTreeNode(new CodeTemplate(UUID.randomUUID().toString(), display, extension, "Unnamed", "")));

                    dialog.setVisible(false);
                });
                dialog.add(add, BorderLayout.SOUTH);

                dialog.setSize(300, 100);
                dialog.setAlwaysOnTop(true);
                dialog.setLocationRelativeTo(this);
                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                dialog.setResizable(false);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            }
        }
    }

    private void addNode(DefaultMutableTreeNode pNode, MutableTreeNode newNode){
        //直接通过model来添加新节点，则无需通过调用JTree的updateUI方法
        //model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());
        //直接通过节点添加新节点，则需要调用tree的updateUI方法
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
        TreePath selectedPath = templateTree.getSelectionPath();
        if (selectedPath != null) {
            //编辑选中节点
            templateTree.startEditingAtPath(selectedPath);
        }
    }

    private void setTemplates(TemplatesSetting templatesSetting){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("TemplateList");

        Map<String, List<CodeTemplate>> codeTemplateTree = templatesSetting.getCodeTemplateTree();
        for (String pStr : codeTemplateTree.keySet()) {
            DefaultMutableTreeNode group = new DefaultMutableTreeNode(pStr);
            for (CodeTemplate template : codeTemplateTree.get(pStr)) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(template);
                group.add(node);
            }
            root.add(group);
        }
        templateTree.setModel(new DefaultTreeModel(root, false));
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
