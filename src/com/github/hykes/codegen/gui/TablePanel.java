package com.github.hykes.codegen.gui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.configurable.ui.dialog.ColumnEditDialog;
import com.github.hykes.codegen.model.Field;
import com.github.hykes.codegen.model.Table;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/18
 */
public class TablePanel {
    private JPanel rootPanel;
    private JPanel mainPanel;
    private JTextField modelTextField;
    private JTextField tableTextField;
    private JPanel topPanel;
    private JPanel modelPanel;
    private JPanel tablePanel;
    private JLabel modelLab;
    private JLabel tableLab;
    private JPanel columnsPanel;

    private JBTable fieldTable = new JBTable();

    private final SettingManager settingManager = SettingManager.getInstance();

    public JTextField getModelTextField() {
        return modelTextField;
    }

    public JTextField getTableTextField() {
        return tableTextField;
    }

    public TablePanel(Table table) {
        $$$setupUI$$$();
        rootPanel.setBorder(IdeBorderFactory.createTitledBorder(table.getTableName(), false));

        //不可整列移动
        fieldTable.getTableHeader().setReorderingAllowed(false);
        //不可拉动表格
        fieldTable.getTableHeader().setResizingAllowed(false);
        fieldTable.getEmptyText().setText("No Columns");
        JPanel panel = ToolbarDecorator.createDecorator(fieldTable)
                .setAddAction(it -> addAction())
                .setRemoveAction(it -> removeAction())
                .setEditAction(it -> editAction())
                .createPanel();
        panel.setPreferredSize(JBUI.size(300, 200));
        columnsPanel.setBorder(IdeBorderFactory.createTitledBorder("Columns", false));
        columnsPanel.add(panel, BorderLayout.CENTER);

        mainPanel.add(columnsPanel);

        modelTextField.setText(table.getModelName());
        tableTextField.setText(table.getTableName());
        initFields(table.getFields());
    }

    /**
     * 添加
     */
    private void addAction() {
        ColumnEditDialog dialog = new ColumnEditDialog();
        dialog.setTitle("Add a Column");

        dialog.getButtonOK().addActionListener(it -> {
            String fieldText = dialog.getFieldTextField().getText().trim();
            String fieldTypeText = dialog.getFieldTypeTextField().getText().trim();
            String columnText = dialog.getColumnTextField().getText();
            String columnTypeText = dialog.getColumnTypeTextField().getText().trim();
            String columnSizeText = dialog.getColumnSizeTextField().getText().trim();
            String commentText = dialog.getCommentTextField().getText().trim();

            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            String[] rowValues = {fieldText, fieldTypeText, columnText, columnTypeText, "0", columnSizeText, commentText};
            tableModel.addRow(rowValues);
            dialog.setVisible(false);
        });

        dialog.setSize(500, 260);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(this.$$$getRootComponent$$$());
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * 删除
     */
    private void removeAction() {
        int selectedRow = fieldTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            tableModel.removeRow(selectedRow);
        }
        if (fieldTable.getRowCount() > 0) {
            fieldTable.setRowSelectionInterval(0, 0);
        }
    }

    /**
     * 编辑
     */
    private void editAction() {
        int selectedRow = fieldTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            String oldFieldText = (String) tableModel.getValueAt(selectedRow, 0);
            String oldFieldTypeText = (String) tableModel.getValueAt(selectedRow, 1);
            String oldColumnText = (String) tableModel.getValueAt(selectedRow, 2);
            String oldColumnTypeText = (String) tableModel.getValueAt(selectedRow, 3);
            String oldSqlTypeText = (String) tableModel.getValueAt(selectedRow, 4);
            String oldColumnSizeText = (String) tableModel.getValueAt(selectedRow, 5);
            String oldCommentText = (String) tableModel.getValueAt(selectedRow, 6);

            ColumnEditDialog dialog = new ColumnEditDialog();
            dialog.setTitle("Edit a Column");

            dialog.getFieldTextField().setText(oldFieldText);
            dialog.getFieldTypeTextField().setText(oldFieldTypeText);
            dialog.getColumnTextField().setText(oldColumnText);
            dialog.getColumnTypeTextField().setText(oldColumnTypeText);
            dialog.getColumnSizeTextField().setText(oldColumnSizeText);
            dialog.getCommentTextField().setText(oldCommentText);

            dialog.getButtonOK().addActionListener(it -> {
                String fieldText = dialog.getFieldTextField().getText().trim();
                String fieldTypeText = dialog.getFieldTypeTextField().getText().trim();
                String columnText = dialog.getColumnTextField().getText();
                String columnTypeText = dialog.getColumnTypeTextField().getText().trim();
                String columnSizeText = dialog.getColumnSizeTextField().getText().trim();
                String commentText = dialog.getCommentTextField().getText().trim();

                tableModel.setValueAt(fieldText, selectedRow, 0);
                tableModel.setValueAt(fieldTypeText, selectedRow, 1);
                tableModel.setValueAt(columnText, selectedRow, 2);
                tableModel.setValueAt(columnTypeText, selectedRow, 3);
                tableModel.setValueAt(columnSizeText, selectedRow, 4);
                tableModel.setValueAt(commentText, selectedRow, 5);
                dialog.setVisible(false);
            });

            dialog.setSize(500, 260);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(this.$$$getRootComponent$$$());
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

        }
    }

    private void initFields(List<Field> fields) {
        // 列名
        String[] columnNames = {"Field_Name", "Field_Type", "Column_Name", "Column_Type", "Sql_Type", "Column_Size", "Comment"};
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
        DefaultTableModel tableModel = new DefaultTableModel(tableVales, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fieldTable.setModel(tableModel);
    }

    public List<Field> getFields() {
        List<Field> fields = new ArrayList<>();
        List<String> ignoreList;
        if (settingManager.getVariables().getParams().containsKey("ignoreFields")) {
            ignoreList = StringUtils.splitToList(settingManager.getVariables().getParams().get("ignoreFields"), ",", true);
        } else {
            ignoreList = new ArrayList<>();
        }

        DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
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
            if (Objects.nonNull(tableModel.getValueAt(i, 5))) {
                field.setColumnSize(tableModel.getValueAt(i, 5).toString());
            }
            if (Objects.nonNull(tableModel.getValueAt(i, 6))) {
                field.setComment(tableModel.getValueAt(i, 6).toString());
            }
            // 过滤
            if (!ignoreList.contains(field.getColumn().toUpperCase().trim())) {
                fields.add(field);
            }
        }
        return fields;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("TestForm");
        Table table = new Table();
        table.setTableName("ss");
        frame.setContentPane(new TablePanel(table).rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        mainPanel = new JPanel(new GridLayout(1, 1));
    }

    public JComponent getRootComponent() {
        return rootPanel;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setLayout(new BorderLayout(0, 0));
        rootPanel.add(mainPanel, BorderLayout.CENTER);
        columnsPanel = new JPanel();
        columnsPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(columnsPanel, BorderLayout.CENTER);
        topPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(topPanel, BorderLayout.NORTH);
        modelPanel = new JPanel();
        modelPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        topPanel.add(modelPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        modelLab = new JLabel();
        modelLab.setText("Model");
        modelPanel.add(modelLab, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modelTextField = new JTextField();
        modelPanel.add(modelTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        topPanel.add(tablePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tableLab = new JLabel();
        tableLab.setText("Table ");
        tablePanel.add(tableLab, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tableTextField = new JTextField();
        tablePanel.add(tableTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
