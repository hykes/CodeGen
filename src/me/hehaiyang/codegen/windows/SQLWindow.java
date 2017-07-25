package me.hehaiyang.codegen.windows;

import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.ScrollPaneFactory;
import me.hehaiyang.codegen.model.IdeaContext;
import me.hehaiyang.codegen.model.Table;
import me.hehaiyang.codegen.parser.Parser;
import me.hehaiyang.codegen.parser.impl.DefaultParser;

import javax.swing.*;
import java.awt.*;

public class SQLWindow extends BaseWindow{

    private JTextArea codeJTextPane;

    private JPanel actionPanel;
    private JLabel tipsLabel;
    private JButton cancel;
    private JButton sure;

    private JFrame thisFrame;

    public SQLWindow(IdeaContext ideaContext) {
        super();
        setTitle("CodeGen-Create by SQL");
        setLayout(new BorderLayout());
        thisFrame = this;
        this.init(ideaContext);
    }

    private void init(IdeaContext ideaContext) {

        add(generationTypePanel(), BorderLayout.NORTH);

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

                dispose();
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



