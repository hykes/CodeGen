package com.github.hykes.codegen.configurable.ui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.configurable.UIConfigurable;
import com.github.hykes.codegen.configurable.model.Variables;
import com.github.hykes.codegen.configurable.ui.dialog.VariableEditDialog;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hehaiyang@terminus.io
 * @date 2017/12/20
 */
public class VariableUI implements UIConfigurable {

    private final SettingManager settingManager = SettingManager.getInstance();

    private JPanel rootPanel;
    private JPanel varPanel;
    private JPanel descPanel;
    private JSplitPane jSplitPane;
    private JBTable varTable;
    private JTextArea descArea;

    public VariableUI() {
        $$$setupUI$$$();
        setVariables(settingManager.getVariables());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        jSplitPane = new JSplitPane();
        jSplitPane.setOrientation(0);
        jSplitPane.setContinuousLayout(true);
        varPanel = new JPanel(new BorderLayout());
        varPanel.setBorder(IdeBorderFactory.createTitledBorder("Predefined Variables", false));
        varPanel.setPreferredSize(JBUI.size(300, 200));
        varTable = new JBTable();
        varTable.getEmptyText().setText("No Variables");
        //不可整列移动
        varTable.getTableHeader().setReorderingAllowed(false);
        //不可拉动表格
        varTable.getTableHeader().setResizingAllowed(false);

        JPanel panel = ToolbarDecorator.createDecorator(varTable)
                .setAddAction(it -> addAction())
                .setRemoveAction(it -> removeAction())
                .setEditAction(it -> editAction()).createPanel();
        varPanel.add(panel, BorderLayout.CENTER);

        descPanel = new JPanel(new BorderLayout());
        descPanel.setPreferredSize(JBUI.size(300, 200));
        descPanel.setBorder(IdeBorderFactory.createTitledBorder("Description", false));

        String inHouseVariables;
        try {
            InputStream template = getClass().getResourceAsStream("/variables.md");
            inHouseVariables = StringUtils.stream2String(template);
        } catch (IOException ioe) {
            inHouseVariables = "IOException";
        }
        descArea = new JTextArea();
        descArea.setText(inHouseVariables);
        descArea.setEnabled(false);
        descPanel.add(new JBScrollPane(descArea), BorderLayout.CENTER);
    }


    private void setVariables(Variables variables) {
        // 列名
        String[] columnNames = {"Key", "Value"};
        // 默认数据
        Map<String, String> map = variables.getParams();
        Object[][] tableVales = new String[map.size()][2];
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();
        for (int row = 0; row < tableVales.length; row++) {
            tableVales[row][0] = keys[row];
            tableVales[row][1] = values[row];
        }
        DefaultTableModel tableModel = new DefaultTableModel(tableVales, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        varTable.setModel(tableModel);
    }

    private void addAction() {
        VariableEditDialog dialog = new VariableEditDialog();
        dialog.setTitle("Create New Variable");
        dialog.getButtonOK().addActionListener(it -> {
            String key = dialog.getKeyTextField().getText().trim();
            String value = dialog.getValueTextField().getText().trim();
            DefaultTableModel tableModel = (DefaultTableModel) varTable.getModel();
            String[] rowValues = {key, value};
            tableModel.addRow(rowValues);
            dialog.setVisible(false);
        });

        dialog.setSize(300, 150);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(rootPanel);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void removeAction() {
        int selectedRow = varTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) varTable.getModel();
            tableModel.removeRow(selectedRow);
        }
        if (varTable.getRowCount() > 0) {
            varTable.setRowSelectionInterval(0, 0);
        }
    }

    private void editAction() {
        int selectedRow = varTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) varTable.getModel();
            String oldKey = (String) tableModel.getValueAt(selectedRow, 0);
            String oldValue = (String) tableModel.getValueAt(selectedRow, 1);

            VariableEditDialog dialog = new VariableEditDialog();
            dialog.setTitle("Edit Variable");
            dialog.getKeyTextField().setText(oldKey);
            dialog.getValueTextField().setText(oldValue);

            dialog.getButtonOK().addActionListener(it -> {
                String key = dialog.getKeyTextField().getText().trim();
                String value = dialog.getValueTextField().getText().trim();

                tableModel.setValueAt(key, selectedRow, 0);
                tableModel.setValueAt(value, selectedRow, 1);
                dialog.setVisible(false);
            });

            dialog.setSize(300, 150);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(rootPanel);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
    }

    /**
     * 是否已修改
     *
     * @return
     */
    @Override
    public boolean isModified() {
        Variables variables = settingManager.getVariables();
        DefaultTableModel tableModel = (DefaultTableModel) varTable.getModel();
        if (variables.getParams().size() != tableModel.getRowCount()) {
            return true;
        }
        Map<String, String> params = variables.getParams();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String key = tableModel.getValueAt(i, 0).toString();
            String value = tableModel.getValueAt(i, 1).toString();
            if (!params.containsKey(key)) {
                return true;
            } else if (params.containsKey(key) && !params.get(key).equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 应用
     */
    @Override
    public void apply() {
        Map<String, String> params = new HashMap<>();
        DefaultTableModel tableModel = (DefaultTableModel) varTable.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            params.put(tableModel.getValueAt(i, 0).toString().trim(), tableModel.getValueAt(i, 1).toString().trim());
        }
        settingManager.getVariables().setParams(params);
    }

    /**
     * 重置
     */
    @Override
    public void reset() {
        setVariables(settingManager.getVariables());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new VariableUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(jSplitPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        jSplitPane.setLeftComponent(varPanel);
        jSplitPane.setRightComponent(descPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
