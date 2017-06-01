package me.hehaiyang.codegen.windows;

import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.setting.SettingManager;
import me.hehaiyang.codegen.setting.ui.TemplatesSetting;
import me.hehaiyang.codegen.setting.ui.variable.AddDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/12
 */
public class ColumnEditorFrame extends JFrame {

    private final SettingManager settingManager = SettingManager.getInstance();

    private final JBTable variablesTable = new JBTable();

    public ColumnEditorFrame(List<Field> fields) {
        init();
        setFields(fields);
    }

    private void init(){
        setLayout(new BorderLayout());

        final JPanel mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setPreferredSize(JBUI.size(300, 400));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

        variablesTable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
        variablesTable.getTableHeader().setResizingAllowed(false);   //不可拉动表格
        JPanel panel = ToolbarDecorator.createDecorator(variablesTable)
            .setAddAction( it -> addAction())
            .setRemoveAction( it -> removeAction())
            .setEditAction( it -> editAction()).createPanel();
        final JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.setBorder(IdeBorderFactory.createTitledBorder("User Defined Variables", false));
        localPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(localPanel);

        add(mainPanel, BorderLayout.CENTER);

        final JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.X_AXIS));

        TemplatesSetting setting = settingManager.getTemplatesSetting();
        for(String group: setting.getCodeTemplateTree().keySet()){
            JCheckBox groupBox = new JCheckBox(group);
            groupPanel.add(groupBox);
        }
        groupPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(groupPanel, BorderLayout.SOUTH);

        variablesTable.getEmptyText().setText("No Variables");
    }

    private void setFields(List<Field> fields){
        // 列名
        String[] columnNames = {"Column_Name","Column_Type", "Column_Size", "Comment"};
        // 默认数据
        Object[][] tableVales = new String[fields.size()][4];
        for (int row = 0; row < fields.size(); row++) {
            tableVales[row][0] = fields.get(row).getColumn();
            tableVales[row][1] = fields.get(row).getColumnType();
            tableVales[row][2] = fields.get(row).getColumnSize();
            tableVales[row][3] = fields.get(row).getComment();
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
            String key = keyJTextField.getText();
            String value = valueJTextField.getText();

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
                String key = keyJTextField.getText();
                String value = valueJTextField.getText();

                tableModel.getValueAt(selectedRow, 1);
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
