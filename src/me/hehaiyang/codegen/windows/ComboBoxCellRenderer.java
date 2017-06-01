package me.hehaiyang.codegen.windows;

import me.hehaiyang.codegen.model.Database;

import javax.swing.*;
import java.awt.*;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/6/1
 */
public class ComboBoxCellRenderer extends JLabel implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Database database = (Database) value;
        setText(database.getUrl());
        return this;
    }
}
