package com.github.hykes.codegen.configurable.ui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.configurable.UIConfigurable;
import com.github.hykes.codegen.configurable.ui.variable.AddDialog;
import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import com.github.hykes.codegen.configurable.model.Variables;
import com.github.hykes.codegen.utils.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义变量设置面板
 *
 * @author: hehaiyangwork@qq.com
 * @date: 2017/5/12
 */
public class VariablesUI extends JPanel implements UIConfigurable {

    private final JBTable variablesTable = new JBTable();

    private final SettingManager settingManager = SettingManager.getInstance();

    public VariablesUI() {
        init();
        setVariables(settingManager.getVariables());
    }

    @Override
    public boolean isModified() {
        Variables variables = settingManager.getVariables();
        DefaultTableModel tableModel = (DefaultTableModel) variablesTable.getModel();
        if(variables.getParams().size() != tableModel.getRowCount()){
            return true;
        }
        Map<String, String> params = variables.getParams();
        for(int i = 0; i< tableModel.getRowCount(); i++){
            String key = tableModel.getValueAt(i, 0).toString();
            String value = tableModel.getValueAt(i, 1).toString();
            if(!params.containsKey(key)){
                return true;
            }else if(params.containsKey(key) && !params.get(key).equals(value)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void apply() {
        Map<String, String> params = new HashMap<>();
        DefaultTableModel tableModel = (DefaultTableModel) variablesTable.getModel();
        for(int i = 0;i< tableModel.getRowCount(); i++){
            params.put(tableModel.getValueAt(i, 0).toString().trim(), tableModel.getValueAt(i, 1).toString().trim());
        }
        settingManager.getVariables().setParams(params);
    }

    @Override
    public void reset() {
        setVariables(settingManager.getVariables());
    }

    private void init(){
        setLayout(new BorderLayout());

        //不可整列移动
        variablesTable.getTableHeader().setReorderingAllowed(false);
        //不可拉动表格
        variablesTable.getTableHeader().setResizingAllowed(false);

        final JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.setPreferredSize(JBUI.size(300, 400));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

        JPanel panel = ToolbarDecorator.createDecorator(variablesTable)
            .setAddAction( it -> addAction())
            .setRemoveAction( it -> removeAction())
            .setEditAction( it -> editAction()).createPanel();
        final JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.setBorder(IdeBorderFactory.createTitledBorder("Predefined Variables", false));
        localPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(localPanel);

        JTextArea area = new JTextArea();
        final JPanel inHousePanel = new JPanel(new BorderLayout());
        inHousePanel.setBorder(IdeBorderFactory.createTitledBorder("Description", false));

        String inHouseVariables;
        try {
            InputStream template = getClass().getResourceAsStream("/others/In-house-variables.md");
            inHouseVariables = StringUtils.stream2String(template);
        }catch (IOException ioe){
            inHouseVariables = "IOException";
        }
        area.setText(inHouseVariables);
        area.setEnabled(false);
        inHousePanel.add(new JBScrollPane(area),BorderLayout.CENTER);
        mainPanel.add(inHousePanel);

        add(mainPanel, BorderLayout.CENTER);

        final JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.add(new JLabel("You can pre-defined variables for template .",
                MessageType.INFO.getDefaultIcon(), SwingConstants.LEFT));
        add(infoPanel, BorderLayout.SOUTH);

        variablesTable.getEmptyText().setText("No Variables");
    }

    private void setVariables(Variables variables){
        // 列名
        String[] columnNames = {"Key","Value"};
        // 默认数据
        Map<String, String> map = variables.getParams();
        Object[][] tableVales = new String[map.size()][2];
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();
        for (int row = 0; row < tableVales.length; row++) {
            tableVales[row][0] = keys[row];
            tableVales[row][1] = values[row];
        }
        DefaultTableModel tableModel = new DefaultTableModel(tableVales,columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        variablesTable.setModel(tableModel);
    }

    private void addAction(){
        JDialog dialog = new AddDialog();
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2,2));
        form.add(new Label("Key"));
        JTextField keyJTextField = new JTextField();
        form.add(keyJTextField);
        form.add(new Label("Value"));
        JTextField valueJTextField = new JTextField();
        form.add(valueJTextField);

        dialog.add(form, BorderLayout.CENTER);

        JButton add = new JButton("ADD");
        add.addActionListener( it ->{
            String key = keyJTextField.getText().trim();
            String value = valueJTextField.getText().trim();

            DefaultTableModel tableModel = (DefaultTableModel) variablesTable.getModel();
            String []rowValues = {key, value};
            tableModel.addRow(rowValues);
            dialog.setVisible(false);
        });
        dialog.add(add, BorderLayout.SOUTH);

        dialog.setSize(300, 100);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(this);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void removeAction(){
        int selectedRow = variablesTable.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel tableModel = (DefaultTableModel) variablesTable.getModel();
            tableModel.removeRow(selectedRow);
        }
        if(variablesTable.getRowCount() > 0){
            variablesTable.setRowSelectionInterval(0,0);
        }
    }

    private void editAction(){
        int selectedRow = variablesTable.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel tableModel = (DefaultTableModel) variablesTable.getModel();
            String oldKey = (String) tableModel.getValueAt(selectedRow, 0);
            String oldValue = (String) tableModel.getValueAt(selectedRow, 1);

            JDialog dialog = new AddDialog();
            dialog.setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(2,2));
            form.add(new Label("Key"));
            JTextField keyJTextField = new JTextField(oldKey);
            form.add(keyJTextField);
            form.add(new Label("Value"));
            JTextField valueJTextField = new JTextField(oldValue);
            form.add(valueJTextField);

            dialog.add(form, BorderLayout.CENTER);

            JButton add = new JButton("Confirm");
            add.addActionListener( it ->{
                String key = keyJTextField.getText().trim();
                String value = valueJTextField.getText().trim();

                tableModel.setValueAt(key, selectedRow, 0);
                tableModel.setValueAt(value, selectedRow, 1);
                dialog.setVisible(false);
            });
            dialog.add(add, BorderLayout.SOUTH);

            dialog.setSize(300, 100);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(this);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
    }

}
