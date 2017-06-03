package me.hehaiyang.codegen.setting.ui;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import me.hehaiyang.codegen.model.Database;
import me.hehaiyang.codegen.setting.SettingManager;
import me.hehaiyang.codegen.setting.UIConfigurable;
import me.hehaiyang.codegen.setting.ui.variable.AddDialog;
import me.hehaiyang.codegen.windows.DBOperation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Desc: 数据库配置面板
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/12
 */
public class DatabasesUI extends JPanel implements UIConfigurable {

    private final JBTable databasesTable = new JBTable();

    private final SettingManager settingManager = SettingManager.getInstance();

    public DatabasesUI() {
        init();
        setDatabases(settingManager.getDatabasesSetting());
    }

    @Override
    public boolean isModified() {
        DatabasesSetting setting = settingManager.getDatabasesSetting();
        DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
        if(setting.getDatabases().size() != tableModel.getRowCount()){
            return true;
        }
        List<Database> databases = setting.getDatabases();
        for(int i = 0; i< tableModel.getRowCount(); i++){
            String url = tableModel.getValueAt(i, 0).toString();
            String username = tableModel.getValueAt(i, 1).toString();
            String password = tableModel.getValueAt(i, 2).toString();
            for(Database database : databases){
                if(database.getName().equals(url)
                        && (!database.getUsername().equals(username) || !database.getPassword().equals(password))){
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void apply() {
        List<Database> databases = Lists.newArrayList();
        DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
        for(int i = 0;i< tableModel.getRowCount(); i++){
            Database database = new Database();
            database.setUrl(tableModel.getValueAt(i, 0).toString());
            database.setUsername(tableModel.getValueAt(i, 1).toString());
            database.setPassword(tableModel.getValueAt(i, 2).toString());
            databases.add(database);
        }
        settingManager.getDatabasesSetting().setDatabases(databases);
    }

    @Override
    public void reset() {
        setDatabases(settingManager.getDatabasesSetting());
    }

    private void init(){
        setLayout(new BorderLayout());

        final JPanel mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setPreferredSize(JBUI.size(300, 400));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

        JPanel panel = ToolbarDecorator.createDecorator(databasesTable)
            .addExtraAction(new AnActionButton("Connect", AllIcons.Actions.Refresh) {
                @Override
                public void actionPerformed(AnActionEvent e) {

                    int selectedRow = databasesTable.getSelectedRow();
                    if(selectedRow != -1) {
                        DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
                        String url = (String) tableModel.getValueAt(selectedRow, 0);
                        String username = (String) tableModel.getValueAt(selectedRow, 1);
                        String password = (String) tableModel.getValueAt(selectedRow, 2);

                        DBOperation dbOperation = new DBOperation();
                        String driver = "com.mysql.jdbc.Driver";
                        try {
                            Connection connection = dbOperation.getConnection(driver, url, username, password);
                            if (connection != null && !connection.isClosed()) {
                                Messages.showInfoMessage("连接成功！", "Info");
                            }else{
                                Messages.showInfoMessage("连接失败！", "Error");
                            }
                        }catch (SQLException se){
                            Messages.showInfoMessage("连接失败！", "Error");
                        }
                    }
                }

                @Override
                public boolean isEnabled() {
                    return super.isEnabled() && databasesTable.getSelectedRow() != -1;
                }
            })
            .setAddAction( it -> addAction())
            .setRemoveAction( it -> removeAction())
            .setEditAction( it -> editAction()).createPanel();
        final JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.setBorder(IdeBorderFactory.createTitledBorder("Database List", false));
        localPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(localPanel);

        add(mainPanel, BorderLayout.CENTER);

        final JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.add(new JLabel("You can save some database .",
                MessageType.INFO.getDefaultIcon(), SwingConstants.LEFT));
        add(infoPanel, BorderLayout.SOUTH);

        databasesTable.getEmptyText().setText("No Databases");
    }

    private void setDatabases(DatabasesSetting databasesSetting){
        // 列名
        String[] columnNames = {"Url","Username", "Password"};
        // 默认数据
        Object[][] tableVales = new String[databasesSetting.getDatabases().size()][3];
        List<Database> databases = databasesSetting.getDatabases();
        for (int row = 0; row < databases.size(); row++) {
            Database database = databases.get(row);
            tableVales[row][0] = database.getUrl();
            tableVales[row][1] = database.getUsername();
            tableVales[row][2] = database.getPassword();
        }
        DefaultTableModel tableModel = new DefaultTableModel(tableVales,columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        databasesTable.setModel(tableModel);
    }

    private void addAction(){
        JDialog dialog = new AddDialog();
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3,2));
        form.add(new Label("url"));
        JTextField urlText = new JTextField();
        form.add(urlText);
        form.add(new Label("username"));
        JTextField usernameText = new JTextField();
        form.add(usernameText);

        form.add(new Label("password"));
        JTextField passwordText = new JTextField();
        form.add(passwordText);

        dialog.add(form, BorderLayout.CENTER);

        JButton add = new JButton("ADD");
        add.addActionListener( it ->{
            String url = urlText.getText();
            String username = usernameText.getText();
            String password = passwordText.getText();

            DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
            String []rowValues = {url, username, password};
            tableModel.addRow(rowValues);
            dialog.setVisible(false);
        });
        dialog.add(add, BorderLayout.SOUTH);

        dialog.setSize(300, 130);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(this);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void removeAction(){
        int selectedRow = databasesTable.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
            tableModel.removeRow(selectedRow);
        }
        if(databasesTable.getRowCount() > 0){
            databasesTable.setRowSelectionInterval(0,0);
        }
    }

    private void editAction(){
        int selectedRow = databasesTable.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
            String oldUrl = (String) tableModel.getValueAt(selectedRow, 0);
            String oldUsername = (String) tableModel.getValueAt(selectedRow, 1);
            String oldPassword = (String) tableModel.getValueAt(selectedRow, 2);

            JDialog dialog = new AddDialog();
            dialog.setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(3,2));
            form.add(new Label("url"));
            JTextField urlText = new JTextField(oldUrl);
            form.add(urlText);
            form.add(new Label("username"));
            JTextField usernameText = new JTextField(oldUsername);
            form.add(usernameText);

            form.add(new Label("password"));
            JTextField passwordText = new JTextField(oldPassword);
            form.add(passwordText);

            dialog.add(form, BorderLayout.CENTER);

            JButton add = new JButton("Confirm");
            add.addActionListener( it ->{
                String url = urlText.getText();
                String username = usernameText.getText();
                String password = passwordText.getText();

                tableModel.setValueAt(url, selectedRow, 0);
                tableModel.setValueAt(username, selectedRow, 1);
                tableModel.setValueAt(password, selectedRow, 2);
                dialog.setVisible(false);
            });
            dialog.add(add, BorderLayout.SOUTH);

            dialog.setSize(300, 130);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(this);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
    }

}
