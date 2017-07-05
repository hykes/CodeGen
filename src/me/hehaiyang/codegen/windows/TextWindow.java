package me.hehaiyang.codegen.windows;

import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.ScrollPaneFactory;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.model.IdeaContext;
import me.hehaiyang.codegen.parser.Parser;
import me.hehaiyang.codegen.parser.impl.DefaultParser;
import me.hehaiyang.codegen.parser.impl.SimpleParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class TextWindow extends JFrame {

    final JRadioButton markDownRadio = new JRadioButton("MarkDown");
    final JRadioButton sqlScriptRadio = new JRadioButton("SqlScript", true);

    private JTextArea codeJTextPane;

    private JPanel actionPanel;
    private JLabel tipsLabel;
    private JButton cancel;
    private JButton sure;

    private JFrame thisFrame;

    public TextWindow(IdeaContext ideaContext) {
        setTitle("CodeGen");
        setLayout(new BorderLayout());
        thisFrame = this;
        this.init(ideaContext);
    }

    private void init(IdeaContext ideaContext) {

        sqlScriptRadio.setMnemonic('s');
        sqlScriptRadio.setToolTipText("generate code by sqlScript");

        markDownRadio.setMnemonic('m');
        markDownRadio.setToolTipText("generate code by markDown");

        ButtonGroup group = new ButtonGroup();
        group.add(sqlScriptRadio);
        group.add(markDownRadio);


        JPanel boxes = new JPanel();
        boxes.setLayout(new BoxLayout(boxes, BoxLayout.X_AXIS));
        boxes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        boxes.add(sqlScriptRadio);
        boxes.add(markDownRadio);
        add(boxes, BorderLayout.NORTH);

        codeJTextPane = new JTextArea();
        codeJTextPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(ScrollPaneFactory.createScrollPane(codeJTextPane), BorderLayout.CENTER);

        actionPanel = new JPanel();
        actionPanel.add(cancel = new JButton("Cancel"));
        actionPanel.add(sure = new JButton("Sure"));

        final JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tipsLabel = new JLabel("Input text .",
                MessageType.INFO.getDefaultIcon(), SwingConstants.LEFT);
        infoPanel.add(tipsLabel, BorderLayout.WEST);

        infoPanel.add(actionPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        sure.addActionListener(e -> {
            try {
                String text = codeJTextPane.getText().trim();

                List<Field> fields;
                if(sqlScriptRadio.isSelected()){
                    Parser parser = new DefaultParser();
                    fields = parser.parseSQL(text);
                }else {
                    Parser parser = new SimpleParser();
                    fields = parser.parseSQL(text);
                }

                if (fields == null || fields.isEmpty()) {
                    setTips(true, "Error ! please check text format.");
                    return;
                }
                ColumnEditorFrame frame = new ColumnEditorFrame(ideaContext, fields);
                frame.setSize(800, 400);
                frame.setAlwaysOnTop(false);
                frame.setLocationRelativeTo(thisFrame);
                frame.setVisible(true);
                frame.setResizable(false);

                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        cancel.addActionListener(e -> dispose());
        // esc
        thisFrame.getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void setTips(boolean operator, String tips) {
        tipsLabel.setText(tips);
        tipsLabel.setVisible(operator);
    }

}



