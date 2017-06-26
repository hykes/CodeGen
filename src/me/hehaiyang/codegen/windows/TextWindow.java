package me.hehaiyang.codegen.windows;

import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.ScrollPaneFactory;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.utils.ParseUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TextWindow extends JFrame {

    private JCheckBox markdownBox;
    private JCheckBox sqlScriptBox;

    private JTextPane codeJTextPane;

    private JPanel actionPanel;
    private JLabel tipsLabel;
    private JButton cancel;
    private JButton sure;

    public TextWindow() {
        setTitle("CodeGen");
        setLayout(new BorderLayout());

        this.init();
    }

    private void init() {

        markdownBox = new JCheckBox("Use MarkDown");
        markdownBox.setMnemonic('m');
        sqlScriptBox = new JCheckBox("Use SqlScript");
        sqlScriptBox.setMnemonic('s');
        sqlScriptBox.setVisible(true);

        JPanel boxes = new JPanel();
        boxes.setLayout(new BoxLayout(boxes, BoxLayout.X_AXIS));
        boxes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        boxes.add(markdownBox);
        boxes.add(sqlScriptBox);
        add(boxes, BorderLayout.NORTH);

        codeJTextPane = new JTextPane();
        codeJTextPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(ScrollPaneFactory.createScrollPane(codeJTextPane), BorderLayout.CENTER);

        actionPanel = new JPanel();
        actionPanel.add(cancel = new JButton("cancel"));
        actionPanel.add(sure = new JButton("sure"));

        final JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tipsLabel = new JLabel("You can defined some variables for template.",
                MessageType.INFO.getDefaultIcon(), SwingConstants.LEFT);
        infoPanel.add(tipsLabel, BorderLayout.WEST);

        infoPanel.add(actionPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        sure.addActionListener(e -> {
            try {

                String markdown = codeJTextPane.getText().trim();

                List<Field> fields = ParseUtils.parseString(markdown);
                if (fields == null || fields.isEmpty()) {
                    setTips(true, "表结构设计读取失败，请检查！");
                    return;
                }

                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        cancel.addActionListener(e -> {
            dispose();
        });
    }

    private void setTips(boolean operator, String tips) {
        tipsLabel.setText(tips);
        tipsLabel.setVisible(operator);
    }

    public static void main(String[] args) {

        TextWindow startFrame = new TextWindow();
        startFrame.setSize(800, 400);
        startFrame.setResizable(false);
        startFrame.setAlwaysOnTop(true);
        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
    }

}



