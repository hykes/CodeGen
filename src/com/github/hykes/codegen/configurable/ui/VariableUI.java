package com.github.hykes.codegen.configurable.ui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.configurable.UIConfigurable;
import com.github.hykes.codegen.configurable.model.Variables;
import com.github.hykes.codegen.configurable.ui.dialog.VariableEditDialog;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private JPanel ignorePane;
    private JTextField ignoreText;
    private JLabel ignoreLabel;
    private JBTable varTable;
    private JTextArea descArea;

    public VariableUI() {
        $$$setupUI$$$();
        GuiUtils.replaceJSplitPaneWithIDEASplitter(rootPanel);
        setVariables(settingManager.getVariables());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        jSplitPane = new JSplitPane();
        jSplitPane.setOrientation(0);
        jSplitPane.setContinuousLayout(true);
        jSplitPane.setBorder(IdeBorderFactory.createEmptyBorder());
        varPanel = new JPanel(new BorderLayout());
        varPanel.setBorder(IdeBorderFactory.createTitledBorder("Predefined Variables", false));
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
        descPanel.setBorder(IdeBorderFactory.createTitledBorder("Default Variables & Directives", false));

        String inHouseVariables;
        try {
            inHouseVariables = FileUtil.loadTextAndClose(VariableUI.class.getResourceAsStream("/variables.md"));
        } catch (Exception e) {
            inHouseVariables = "something error";
        }
        descArea = new JTextArea();
        descArea.setText(inHouseVariables);
        // descArea.setEnabled(false);
        descArea.setEditable(false);
        descPanel.add(new JBScrollPane(descArea), BorderLayout.CENTER);

        // ignore fields
        ignorePane = new JPanel();
        ignorePane.setBorder(IdeBorderFactory.createTitledBorder("The Ignore Fields", false));
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
        // 设置ignore fields
        ignoreText.setText(variables.getIgnoreFields());
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
        if (!Objects.equals(ignoreText.getText().trim(), variables.getIgnoreFields())) {
            return true;
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
        settingManager.getVariables().setIgnoreFields(ignoreText.getText().trim());
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
        rootPanel.setLayout(new GridBagLayout());
        jSplitPane.setDividerLocation(172);
        jSplitPane.setDividerSize(5);
        jSplitPane.setResizeWeight(1.0);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 5.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(jSplitPane, gbc);
        jSplitPane.setLeftComponent(varPanel);
        jSplitPane.setRightComponent(descPanel);
        ignorePane.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        rootPanel.add(ignorePane, gbc);
        ignoreLabel = new JLabel();
        ignoreLabel.setText("Ignore Fields");
        ignorePane.add(ignoreLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ignoreText = new JTextField();
        ignorePane.add(ignoreText, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}
