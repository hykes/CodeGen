package me.hehaiyang.codegen.windows;

import com.google.common.collect.Lists;
import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.ScrollPaneFactory;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.model.IdeaContext;
import me.hehaiyang.codegen.parser.Parser;
import me.hehaiyang.codegen.parser.impl.DefaultParser;
import me.hehaiyang.codegen.parser.impl.SimpleParser;
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

    private JFrame thisFrame;

    public TextWindow(IdeaContext ideaContext) {
        setTitle("CodeGen");
        setLayout(new BorderLayout());
        thisFrame = this;
        this.init(ideaContext);
    }

    private void init(IdeaContext ideaContext) {

        markdownBox = new JCheckBox("Use MarkDown");
        markdownBox.setMnemonic('m');
        markdownBox.setName("markdown");
        sqlScriptBox = new JCheckBox("Use SqlScript");
        sqlScriptBox.setMnemonic('s');
        sqlScriptBox.setName("sqlScript");
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
                List<String> list = Lists.newArrayList();
                getAllJCheckBoxValue(boxes, list);

                String text = codeJTextPane.getText().trim();

                List<Field> fields;
                if("sqlScript".endsWith(list.get(0))){

                    Parser parser = new DefaultParser();
                    fields = parser.parseSQL(text);

                }else {

                    Parser parser = new SimpleParser();
                    fields = parser.parseSQL(text);
                }

                if (fields == null || fields.isEmpty()) {
                    setTips(true, "解析失败，请检查文本格式！");
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
    }

    public static List<String> getAllJCheckBoxValue(Container ct, List<String> list){
        if(list==null){
            list= Lists.newArrayList();
        }
        int count=ct.getComponentCount();
        for(int i=0;i<count;i++){
            Component c=ct.getComponent(i);
            if(c instanceof JCheckBox && ((JCheckBox)c).isSelected()){
                list.add(c.getName());
            }
            else if(c instanceof Container){
                getAllJCheckBoxValue((Container)c,list);
            }
        }
        return list;
    }

    private void setTips(boolean operator, String tips) {
        tipsLabel.setText(tips);
        tipsLabel.setVisible(operator);
    }

}



