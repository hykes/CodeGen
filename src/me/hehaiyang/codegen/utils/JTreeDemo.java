package me.hehaiyang.codegen.utils;

import com.intellij.ui.treeStructure.Tree;
import me.hehaiyang.codegen.model.CodeTemplate;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/9
 */
public class JTreeDemo {


    JFrame f;
    Tree jTree1;

    public JTreeDemo()
    {
        f = new JFrame(" JTreeDemo1 ");

        //向此组件添加任意的键/值

        // 构造函数:JTree(TreeModel newModel)
        // 用DefaultMutableTreeNodel类定义一个结点再用这个结点做参数定义一个用DefaultTreeMode
        // 创建一个树的模型,再用JTree的构造函数创建一个树
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Template List");
        DefaultMutableTreeNode first = new DefaultMutableTreeNode("first");
        CodeTemplate template = new CodeTemplate();
        template.setDisplay("ss");
        template.setFilename("ssdf");
        DefaultMutableTreeNode dao = new DefaultMutableTreeNode(template);
        DefaultMutableTreeNode second = new DefaultMutableTreeNode("second");
        DefaultMutableTreeNode three = new DefaultMutableTreeNode("three");
        root.add(first);
        first.add(dao);
        root.add(second);
        root.add(three);

        jTree1 = new Tree(root);

        jTree1.putClientProperty("JTree.lineStyle", "Horizontal");
        jTree1.setRootVisible(false);
        jTree1.setCellRenderer(new TreeCellRenderer());
        jTree1.addTreeSelectionListener( it -> {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
                if (node == null){
                    return;
                }
                Object object = node.getUserObject();
                if(object instanceof CodeTemplate)
                {
                    CodeTemplate user = (CodeTemplate) object;
                    System.out.println("你点击了：" + user.toString());
                }
                else if(object instanceof String)
                {
                    String text = (String)object;
                    System.out.println("你点击了：" + text);
                }

        });


        f.getContentPane().add(jTree1, BorderLayout.CENTER);

        f.setSize(300, 240);
        //f.pack();
        f.setLocation(300, 200);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new JTreeDemo();
    }
}
