package com.github.hykes.codegen.gui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeRoot;
import com.github.hykes.codegen.model.Field;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.model.Table;
import com.github.hykes.codegen.utils.GuiUtil;
import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.containers.JBIterable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 字段自定义编辑器
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/5/12
 */
public class ColumnEditorFrame extends JFrame implements ActionOperator {

    private final SettingManager SETTING_MANAGER = SettingManager.getInstance();

    private final List<TablePanel> panels = new ArrayList<>();
    private ActionListener generateAction;

    public void newColumnEditorByDb(IdeaContext ideaContext, List<DbTable> dbTables) {
        List<Table> tables = new ArrayList<>();
        for (DbTable dbTable: dbTables) {
            Table table = new Table();
            table.setTableName(dbTable.getName());

            List<Field> fields = new ArrayList<>();

            JBIterable<? extends DasColumn> columnsIter = DasUtil.getColumns(dbTable);
            List<? extends DasColumn> dasColumns = columnsIter.toList();
            for (DasColumn dasColumn : dasColumns) {
                Field field = new Field();
                field.setColumn(dasColumn.getName());
                field.setColumnType(dasColumn.getDataType().typeName);
                field.setColumnSize(String.valueOf(dasColumn.getDataType().size));
                field.setComment(dasColumn.getComment());
                fields.add(field);
            }
            table.setFields(fields);
            tables.add(table);
        }
        init(ideaContext, tables);

        // esc
        this.getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void newColumnEditorBySql(IdeaContext ideaContext, List<Table> tables) {
        init(ideaContext, tables);
        // esc
        this.getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void init(IdeaContext ideaContext, List<Table> tables){
        setLayout(new BorderLayout());
        JBTabbedPane tabbedPane = new JBTabbedPane();

        for (Table table: tables) {
            TablePanel tablePanel = new TablePanel(table);
            tabbedPane.add(tablePanel.getRootComponent(), table.getTableName());
            panels.add(tablePanel);
        }

        List<CodeRoot> codeRoots =  SETTING_MANAGER.getTemplates().getRoots();
        SelectGroupPanel selectGroupPanel = new SelectGroupPanel(codeRoots, ideaContext.getProject());
        generateAction = it -> {
            if(selectGroupPanel.hasSelected()) {
                List<CodeContext> contexts = new ArrayList<>();

                for (TablePanel panel: panels) {
                    String modelName = panel.getModelTextField().getText().trim();
                    String tableName = panel.getTableTextField().getText().trim();
                    String comment = panel.getCommentTextField().getText().trim();
                    // 组装数据
                    CodeContext context = new CodeContext(modelName, tableName, comment, panel.getFields());
                    contexts.add(context);
                }
                GuiUtil.generateFile(ideaContext, contexts, selectGroupPanel.getGroupPathMap());
                dispose();
            }
        };

        add(tabbedPane, BorderLayout.CENTER);
        selectGroupPanel.getRootPanel().setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        add(selectGroupPanel.getRootPanel(), BorderLayout.SOUTH);
    }

    @Override
    public void ok() {
        this.generateAction.actionPerformed(null);
    }

    @Override
    public void cancel() { }

    @Override
    public boolean valid() { return true; }

}
