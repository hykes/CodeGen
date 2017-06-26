package me.hehaiyang.codegen.windows;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.utils.ParseUtils;
import me.hehaiyang.codegen.utils.PsiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TextWindow extends JFrame {

    private Project project;

    private JPanel codeGenJPanel;
    private JPanel paramsJPanel;
    private JPanel actionJpanel;
    private JButton cancel;
    private JButton sure;
    private JScrollPane codeJScrollPane;
    private JTextPane codeJTextPane;
    private JPanel tipsJPanel;
    private JLabel tipslabel;


    private JCheckBox markdownBox;
    private JCheckBox sqlScriptBox;

    public TextWindow(AnActionEvent anActionEvent) {
        setContentPane(codeGenJPanel);
        setTitle("CodeGen");

        this.project = PsiUtil.getProject(anActionEvent);

        codeJTextPane.requestFocus(true);

        this.init();

    }

    private void init() {

        markdownBox = new JCheckBox("Use MarkDown");
        markdownBox.setMnemonic('b');
        markdownBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        sqlScriptBox = new JCheckBox("Use SqlScript");
        sqlScriptBox.setMnemonic('s');
        sqlScriptBox.setVisible(true);

        JPanel boxes = new JPanel();
        boxes.setLayout(new BoxLayout(boxes, BoxLayout.Y_AXIS));
        boxes.add(markdownBox);
        boxes.add(sqlScriptBox);
        add(boxes, BorderLayout.NORTH);

        sure.addActionListener(e -> {
            try {

                String markdown = codeJTextPane.getText().trim();

                List<Field> fields = ParseUtils.parseString(markdown);
                if (fields == null || fields.isEmpty()) {
                    setTipsVisbile(true, "表结构设计读取失败，请检查！");
                    codeJTextPane.requestFocus(true);
                    return;
                }


                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        cancel.addActionListener(e -> {
            dispose();
        });
    }

    /**
     * 设置tips
     * @param operator
     * @param tips
     */
    private void setTipsVisbile(boolean operator, String tips) {
        tipslabel.setText(tips);
        tipslabel.setVisible(operator);
    }

}



