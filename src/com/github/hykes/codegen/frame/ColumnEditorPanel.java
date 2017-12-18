package com.github.hykes.codegen.frame;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.configurable.ui.variable.AddDialog;
import com.github.hykes.codegen.model.Field;
import com.github.hykes.codegen.model.Table;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Objects;

/**
 * 字段编辑面板
 *
 * @author: hehaiyang@terminus.io
 * @date: 2017/12/17
 */
public class ColumnEditorPanel extends JPanel {

    private final SettingManager settingManager = SettingManager.getInstance();

    private Table table;

    private JBTable fieldTable = new JBTable();

    private JTextField modelText;

    private JTextField tableText;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public JBTable getFieldTable() {
        return fieldTable;
    }

    public void setFieldTable(JBTable fieldTable) {
        this.fieldTable = fieldTable;
    }

    public JTextField getModelText() {
        return modelText;
    }

    public void setModelText(JTextField modelText) {
        this.modelText = modelText;
    }

    public JTextField getTableText() {
        return tableText;
    }

    public void setTableText(JTextField tableText) {
        this.tableText = tableText;
    }

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public ColumnEditorPanel(JBTable jbTable, Table table) {
        setBorder(IdeBorderFactory.createTitledBorder(table.getTableName(), false));
        setLayout(new BorderLayout());

        this.fieldTable = jbTable;
        this.table = table;

        final JPanel topPanel = new JPanel(new GridLayout(1, 4));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        topPanel.add(new JLabel("model"));
        modelText = new JTextField(StringUtils.nullOr(table.getModelName(), ""));
        topPanel.add(modelText);
        topPanel.add(new JLabel("table"));
        tableText = new JTextField(StringUtils.nullOr(table.getTableName(), ""));
        topPanel.add(tableText);

        final JPanel mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setPreferredSize(JBUI.size(300, 400));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        //不可整列移动
        fieldTable.getTableHeader().setReorderingAllowed(false);
        //不可拉动表格
        fieldTable.getTableHeader().setResizingAllowed(false);
        JPanel panel = ToolbarDecorator.createDecorator(fieldTable)
                .setAddAction( it -> addAction())
                .setRemoveAction( it -> removeAction())
                .setEditAction( it -> editAction()).createPanel();
        final JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.setBorder(IdeBorderFactory.createTitledBorder("Field Table", false));
        localPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(localPanel);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setFields(table.getFields());
        fieldTable.getEmptyText().setText("No Fields");
    }

    /**
     * 添加
     */
    private void addAction(){
        JDialog dialog = new AddDialog();
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(7,2));
        form.add(new Label("Field"));
        JTextField field = new JTextField();
        form.add(field);
        form.add(new Label("FieldType"));
        JTextField fieldType = new JTextField();
        form.add(fieldType);

        form.add(new Label("Column"));
        JTextField column = new JTextField();
        form.add(column);

        form.add(new Label("ColumnType"));
        JTextField columnType = new JTextField();
        form.add(columnType);

        form.add(new Label("SqlType"));
        JTextField sqlType = new JTextField();
        form.add(sqlType);

        form.add(new Label("ColumnSize"));
        JTextField columnSize = new JTextField();
        form.add(columnSize);

        form.add(new Label("Comment"));
        JTextField comment = new JTextField();
        form.add(comment);

        dialog.add(form, BorderLayout.CENTER);

        JButton add = new JButton("ADD");
        add.addActionListener( it ->{
            String fieldText = field.getText().trim();
            String fieldTypeText = fieldType.getText().trim();
            String columnText = column.getText();
            String columnTypeText = columnType.getText().trim();
            String sqlTypeText = sqlType.getText().trim();
            String columnSizeText = columnSize.getText().trim();
            String commentText = comment.getText().trim();

            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            String []rowValues = {fieldText, fieldTypeText, columnText, columnTypeText, sqlTypeText, columnSizeText, commentText};
            tableModel.addRow(rowValues);
            dialog.setVisible(false);
        });
        dialog.add(add, BorderLayout.SOUTH);

        dialog.setSize(300, 260);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(this);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * 删除
     */
    private void removeAction(){
        int selectedRow = fieldTable.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            tableModel.removeRow(selectedRow);
        }
        if(fieldTable.getRowCount() > 0){
            fieldTable.setRowSelectionInterval(0,0);
        }
    }

    /**
     * 编辑
     */
    private void editAction(){
        int selectedRow = fieldTable.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            String oldFieldText = (String) tableModel.getValueAt(selectedRow, 0);
            String oldFieldTypeText = (String) tableModel.getValueAt(selectedRow, 1);
            String oldColumnText = (String) tableModel.getValueAt(selectedRow, 2);
            String oldColumnTypeText = (String) tableModel.getValueAt(selectedRow, 3);
            String oldSqlTypeText = (String) tableModel.getValueAt(selectedRow, 4);
            String oldColumnSizeText = (String) tableModel.getValueAt(selectedRow, 5);
            String oldCommentText = (String) tableModel.getValueAt(selectedRow, 6);

            JDialog dialog = new AddDialog();
            dialog.setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(7,2));

            form.add(new Label("Field"));
            JTextField field = new JTextField(oldFieldText);
            form.add(field);
            form.add(new Label("FieldType"));
            JTextField fieldType = new JTextField(oldFieldTypeText);
            form.add(fieldType);

            form.add(new Label("Column"));
            JTextField column = new JTextField(oldColumnText);
            form.add(column);

            form.add(new Label("ColumnType"));
            JTextField columnType = new JTextField(oldColumnTypeText);
            form.add(columnType);

            form.add(new Label("SqlType"));
            JTextField sqlType = new JTextField(oldSqlTypeText);
            form.add(sqlType);

            form.add(new Label("ColumnSize"));
            JTextField columnSize = new JTextField(oldColumnSizeText);
            form.add(columnSize);

            form.add(new Label("Comment"));
            JTextField comment = new JTextField(oldCommentText);
            form.add(comment);

            dialog.add(form, BorderLayout.CENTER);

            JButton add = new JButton("Confirm");
            add.addActionListener( it ->{
                String fieldText = field.getText().trim();
                String fieldTypeText = fieldType.getText().trim();
                String columnText = column.getText();
                String columnTypeText = columnType.getText().trim();
                String columnSizeText = columnSize.getText().trim();
                String commentText = comment.getText().trim();

                tableModel.setValueAt(fieldText, selectedRow, 0);
                tableModel.setValueAt(fieldTypeText, selectedRow, 1);
                tableModel.setValueAt(columnText, selectedRow, 2);
                tableModel.setValueAt(columnTypeText, selectedRow, 3);
                tableModel.setValueAt(columnSizeText, selectedRow, 4);
                tableModel.setValueAt(commentText, selectedRow, 5);
                dialog.setVisible(false);
            });
            dialog.add(add, BorderLayout.SOUTH);

            dialog.setSize(300, 260);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(this);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

        }
    }

    private void setFields(List<Field> fields){
        // 列名
        String[] columnNames = {"Field_Name", "Field_Type", "Column_Name","Column_Type", "Sql_Type", "Column_Size", "Comment"};
        // 默认数据
        Object[][] tableVales = new String[fields.size()][7];
        for (int row = 0; row < fields.size(); row++) {
            tableVales[row][0] = fields.get(row).getField();
            tableVales[row][1] = fields.get(row).getFieldType();
            tableVales[row][2] = fields.get(row).getColumn();
            tableVales[row][3] = fields.get(row).getColumnType();
            tableVales[row][4] = fields.get(row).getSqlType() + "";
            tableVales[row][5] = fields.get(row).getColumnSize();
            tableVales[row][6] = fields.get(row).getComment();
        }
        DefaultTableModel tableModel = new DefaultTableModel(tableVales,columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fieldTable.setModel(tableModel);
    }

    public List<Field> getFields(){
        List<Field> fields = new ArrayList<>();
        List<String> ignoreList = StringUtils.splitToList(settingManager.getConfigs().getIgnoreFields(), ",", true);
        DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
        for(int i = 0;i< tableModel.getRowCount(); i++){
            Field field = new Field();
            // field
            field.setField(tableModel.getValueAt(i, 0).toString());
            // field.setFieldType(tableModel.getValueAt(i, 1).toString()); // setColumnType设置
            // column and type
            field.setColumn(tableModel.getValueAt(i, 2).toString());
            Object sqlType = tableModel.getValueAt(i, 4);
            if (Objects.nonNull(sqlType)) {
                field.setSqlType(sqlType.toString());
            }
            field.setColumnType(tableModel.getValueAt(i, 3).toString());
            if(Objects.nonNull(tableModel.getValueAt(i, 5))){
                field.setColumnSize(tableModel.getValueAt(i, 5).toString());
            }
            if(Objects.nonNull(tableModel.getValueAt(i, 6))){
                field.setComment(tableModel.getValueAt(i, 6).toString());
            }
            // 过滤
            if (!ignoreList.contains(field.getColumn().toUpperCase().trim())) {
                fields.add(field);
            }
        }
        return fields;
    }
}
