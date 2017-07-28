package me.hehaiyang.codegen.windows;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/7/25
 */
public class BaseWindow extends JFrame {

    final JRadioButton sqlRadio = new JRadioButton("Use SQL");
    final JRadioButton dbRadio = new JRadioButton("Use DB");

    public BaseWindow(){
        // esc
        this.getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public JPanel generationTypePanel(String type){
        ButtonGroup group = new ButtonGroup();
        group.add(dbRadio);
        group.add(sqlRadio);

        JPanel boxes = new JPanel();
        boxes.setLayout(new BoxLayout(boxes, BoxLayout.X_AXIS));
        boxes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        boxes.add(dbRadio);
        boxes.add(sqlRadio);
        if("SQL".equals(type)){
            sqlRadio.setSelected(true);
            dbRadio.setFocusable(false);
        }
        if("DB".equals(type)){
            dbRadio.setSelected(true);
            sqlRadio.setFocusable(false);
        }
        return boxes;
    }
}
