package me.hehaiyang.codegen.components;

import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/5
 */
public class ConfigTable extends JBTable {

    public ConfigTable(TableModel model) {
        super(model);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getFillsViewportHeight()) {
            paintEmptyRows(g);
        }
    }

    /**
     * Paints the backgrounds of the implied empty rows when the table model is
     * insufficient to fill all the visible area available to us. We don't
     * involve cell renderers, because we have no data.
     */
    protected void paintEmptyRows(Graphics g) {
        final int rowCount = getRowCount();
        final Rectangle clip = g.getClipBounds();
        if (rowCount * rowHeight < clip.height) {
            for (int i = rowCount; i <= clip.height / rowHeight; ++i) {
                g.setColor(colorForRow(i));
                g.fillRect(clip.x, i * rowHeight, clip.width, rowHeight);
            }
        }
    }

    /**
     * Returns the appropriate background color for the given row.
     */
    protected Color colorForRow(int row) {
        return (row % 2 == 0) ? Color.BLACK : Color.GRAY;
    }

    /**
     * Shades alternate rows in different colors.
     */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        if (isCellSelected(row, column) == false) {
            c.setBackground(colorForRow(row));
            c.setForeground(UIManager.getColor("Table.foreground"));
        } else {
            c.setBackground(UIManager.getColor("Table.selectionBackground"));
            c.setForeground(UIManager.getColor("Table.selectionForeground"));
        }
        return c;
    }
}