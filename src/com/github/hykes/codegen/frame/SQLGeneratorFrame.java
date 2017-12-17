package com.github.hykes.codegen.frame;

import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.model.Table;
import com.github.hykes.codegen.parser.DefaultParser;
import com.github.hykes.codegen.parser.Parser;
import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.ScrollPaneFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/06/26
 */
public class SQLGeneratorFrame extends JFrame{

    private JTextArea codeJTextPane;

    private JPanel actionPanel;
    private JLabel tipsLabel;
    private JButton cancel;
    private JButton sure;

    private JFrame thisFrame;

    public SQLGeneratorFrame(IdeaContext ideaContext) {
        setTitle("CodeGen-SQL");
        setLayout(new BorderLayout());
        thisFrame = this;
        this.init(ideaContext);

        this.getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void init(IdeaContext ideaContext) {

        codeJTextPane = new JTextArea();
        codeJTextPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(ScrollPaneFactory.createScrollPane(codeJTextPane), BorderLayout.CENTER);

        actionPanel = new JPanel();
        actionPanel.add(cancel = new JButton("Cancel"));
        actionPanel.add(sure = new JButton("Sure"));

        final JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tipsLabel = new JLabel("Input sql .",
                MessageType.INFO.getDefaultIcon(), SwingConstants.LEFT);
        infoPanel.add(tipsLabel, BorderLayout.WEST);

        infoPanel.add(actionPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        sure.addActionListener(e -> {
            try {
                String text = codeJTextPane.getText().trim();

                Parser parser = new DefaultParser();
                Table table = parser.parseSQL(text);

                if (table == null || table.getFields() == null || table.getFields().isEmpty()) {
                    setTips(true, "Error ! please check text format.");
                    return;
                }
                ColumnEditorFrame frame = new ColumnEditorFrame(ideaContext, table);
                frame.setSize(800, 400);
                frame.setAlwaysOnTop(false);
                frame.setLocationRelativeTo(thisFrame);
                frame.setVisible(true);
                frame.setResizable(false);

                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        cancel.addActionListener(e -> dispose());
   }

    private void setTips(boolean operator, String tips) {
        tipsLabel.setText(tips);
        tipsLabel.setVisible(operator);
    }

}



