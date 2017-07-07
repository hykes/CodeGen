package me.hehaiyang.codegen.config.ui;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import me.hehaiyang.codegen.config.setting.DatabasesSetting;
import me.hehaiyang.codegen.model.Database;
import me.hehaiyang.codegen.config.SettingManager;
import me.hehaiyang.codegen.config.UIConfigurable;
import me.hehaiyang.codegen.config.ui.variable.AddDialog;
import me.hehaiyang.codegen.utils.DBOperationUtil;
import me.hehaiyang.codegen.utils.StringUtils;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import static me.hehaiyang.codegen.utils.StringUtils.nullOr;

/**
 * Desc: 数据库配置面板
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/12
 */
public class DatabasesUI extends JPanel implements UIConfigurable {

    private final JBTable databasesTable = new JBTable();

    private final SettingManager settingManager = SettingManager.getInstance();

    private JPanel thisPanel;

    // 数据库类型
    public static final String MYSQL = "Mysql";
    public static final String ORACLE = "Oracle";
    public static final String SQLSERVER = "SQL Server";

    public DatabasesUI() {
        thisPanel = this;
        init();
        setDatabases(settingManager.getDatabasesSetting());
    }

    @Override
    public boolean isModified() {
        try {
            DatabasesSetting databasesSetting = settingManager.getDatabasesSetting();
            DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
            if(databasesSetting.getDatabases().size() != tableModel.getRowCount()){
                return true;
            }
            Map<String, Database> databaseMap = databasesSetting.getDatabaseMap();
            for(int i = 0; i< tableModel.getRowCount(); i++){
                String type = tableModel.getValueAt(i, 0).toString().trim();
                String name = tableModel.getValueAt(i, 1).toString().trim();
                String host = tableModel.getValueAt(i, 2).toString().trim();
                String port = tableModel.getValueAt(i, 3).toString().trim();
                String database = tableModel.getValueAt(i, 4).toString().trim();
                String username = tableModel.getValueAt(i, 5).toString().trim();
                String password = tableModel.getValueAt(i, 6).toString().trim();
                String driver = tableModel.getValueAt(i, 7).toString().trim();
                String url = tableModel.getValueAt(i, 8).toString().trim();
                if(!databaseMap.containsKey(name)){
                    return true;
                }else{
                    Database dbParama = databaseMap.get(name);
                    if(!dbParama.getDriver().equals(driver)
                            || !dbParama.getUrl().equals(url)
                            || !dbParama.getType().equals(type)
                            || !dbParama.getHost().equals(host)
                            || !dbParama.getPort().equals(port)
                            || !dbParama.getDatabase().equals(database)
                            || !dbParama.getUsername().equals(username)
                            || !dbParama.getPassword().equals(password)){
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    @Override
    public void apply() {
        List<Database> databases = Lists.newArrayList();
        DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
        for(int i = 0;i< tableModel.getRowCount(); i++){
            Database database = new Database();
            database.setType(tableModel.getValueAt(i, 0).toString().trim());
            database.setName(tableModel.getValueAt(i, 1).toString().trim());
            database.setHost(tableModel.getValueAt(i, 2).toString().trim());
            database.setPort(tableModel.getValueAt(i, 3).toString().trim());
            database.setDatabase(tableModel.getValueAt(i, 4).toString().trim());
            database.setUsername(tableModel.getValueAt(i, 5).toString().trim());
            database.setPassword(tableModel.getValueAt(i, 6).toString().trim());
            database.setUrl(tableModel.getValueAt(i, 7).toString().trim());
            database.setDriver(tableModel.getValueAt(i, 8).toString().trim());
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

        databasesTable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
        databasesTable.getTableHeader().setResizingAllowed(false);   //不可拉动表格

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
                        String driver = tableModel.getValueAt(selectedRow, 7).toString();
                        String url = tableModel.getValueAt(selectedRow, 8).toString();
                        String username = tableModel.getValueAt(selectedRow, 5).toString();
                        String password = tableModel.getValueAt(selectedRow, 6).toString();
                        try {
                            Connection connection = DBOperationUtil.getConnection(driver, url, username, password);
                            if (connection != null && !connection.isClosed()) {
                                Messages.showInfoMessage(thisPanel,"连接成功！", "Info");
                                connection.close();
                            }else{
                                Messages.showInfoMessage(thisPanel, "连接失败！", "Error");
                            }
                        }catch (SQLException se){
                            Messages.showInfoMessage(thisPanel,"连接失败！", "Error");
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
        localPanel.setBorder(IdeBorderFactory.createTitledBorder("Data Sources", false));
        localPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(localPanel);

        add(mainPanel, BorderLayout.CENTER);

        final JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.add(new JLabel("You can save some data sources .",
                MessageType.INFO.getDefaultIcon(), SwingConstants.LEFT));
        add(infoPanel, BorderLayout.SOUTH);

        databasesTable.getEmptyText().setText("No Data Sources");
    }

    private void setDatabases(DatabasesSetting databasesSetting){
        // 列名
        String[] columnNames = {"Type", "Name", "Host", "Port", "Database", "Username", "Password", "Driver", "URL"};
        // 默认数据
        Object[][] tableVales = new String[databasesSetting.getDatabases().size()][9];
        List<Database> databases = databasesSetting.getDatabases();
        for (int row = 0; row < databases.size(); row++) {
            Database database = databases.get(row);
            tableVales[row][0] = database.getType();
            tableVales[row][1] = database.getName();
            tableVales[row][2] = database.getHost();
            tableVales[row][3] = database.getPort();
            tableVales[row][4] = database.getDatabase();
            tableVales[row][5] = database.getUsername();
            tableVales[row][6] = database.getPassword();
            tableVales[row][7] = database.getDriver();
            tableVales[row][8] = database.getUrl();
        }
        DefaultTableModel tableModel = new DefaultTableModel(tableVales, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        databasesTable.setModel(tableModel);
    }

    private void addAction(){
        JDialog dialog = new AddDialog();
        dialog.setLayout(new HorizontalLayout());

        // 1. 创建左部数据库类型选择
        JBList databaseList = setDatabaseList(dialog);

        // 2. 创建参数表单, 默认显示mysql
        JPanel mysqlForm = getMysqlForm(dialog, null, -1, new Database());
        JPanel oracleForm = getOracleForm(dialog);
        JPanel sqlServerForm = getSQLServerForm(dialog);
        dialog.add(mysqlForm);

        // 添加选择事件
        databaseList.addListSelectionListener(e -> {
            JBList source = (JBList) e.getSource();
            Object value = source.getSelectedValue();
            if (value == null) {
                return;
            }
            if (Objects.equals(value, MYSQL)) {
                dialog.getContentPane().remove(1);
                dialog.add(mysqlForm);
                dialog.revalidate();
                mysqlForm.repaint();
            } else if (Objects.equals(value, ORACLE)) {
                dialog.getContentPane().remove(1);
                dialog.add(oracleForm);
                dialog.revalidate();
                oracleForm.repaint();
            } else if (Objects.equals(value, SQLSERVER)) {
                dialog.getContentPane().remove(1);
                dialog.add(sqlServerForm);
                dialog.revalidate();
                sqlServerForm.repaint();
            }
        });

        dialog.setSize(660, 520);
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
            String oldType = (String) tableModel.getValueAt(selectedRow, 0);
            String oldName = (String) tableModel.getValueAt(selectedRow, 1);
            String oldHost = (String) tableModel.getValueAt(selectedRow, 2);
            String oldPort = (String) tableModel.getValueAt(selectedRow, 3);
            String oldDatabase = (String) tableModel.getValueAt(selectedRow, 4);
            String oldUsername = (String) tableModel.getValueAt(selectedRow, 5);
            String oldPassword = (String) tableModel.getValueAt(selectedRow, 6);
            String driver = (String) tableModel.getValueAt(selectedRow, 7);
            String url = (String) tableModel.getValueAt(selectedRow, 8);
            Database oldDatabaseParam = new Database(oldType, oldName, oldHost, oldPort, oldDatabase, oldUsername, oldPassword, driver, url);

            JDialog dialog = new AddDialog();
            dialog.setLayout(new HorizontalLayout());

            // 1. 创建左部数据库类型选择
            JBList databaseList = setDatabaseList(dialog);

            // 2. 创建参数表单, 默认显示mysql
            JPanel mysqlForm = getMysqlForm(dialog, tableModel, selectedRow, oldDatabaseParam);
            JPanel oracleForm = getOracleForm(dialog);
            JPanel sqlServerForm = getSQLServerForm(dialog);
            dialog.add(mysqlForm);

            // 添加选择事件
            databaseList.addListSelectionListener(e -> {
                JBList source = (JBList) e.getSource();
                Object value = source.getSelectedValue();
                if (value == null) {
                    return;
                }
                if (Objects.equals(value, MYSQL)) {
                    dialog.getContentPane().remove(1);
                    dialog.add(mysqlForm);
                    dialog.revalidate();
                    mysqlForm.repaint();
                } else if (Objects.equals(value, ORACLE)) {
                    dialog.getContentPane().remove(1);
                    dialog.add(oracleForm);
                    dialog.revalidate();
                    oracleForm.repaint();
                } else if (Objects.equals(value, SQLSERVER)) {
                    dialog.getContentPane().remove(1);
                    dialog.add(sqlServerForm);
                    dialog.revalidate();
                    sqlServerForm.repaint();
                }
            });

            dialog.setSize(660, 520);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(this);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
    }

    /**
     * 获取数据库类型列表
     */
    private JBList<String> setDatabaseList(Window dialog) {
        JPanel datasource = new JPanel();
        datasource.setLayout(null);
        Label dsLable = new Label("Data Source");
        dsLable.setBounds(15, 10, 100, 14);
        dsLable.setFont(new Font("Default", 1, 14));
        dsLable.setForeground(new JBColor(new Color(93, 108, 123), new Color(93, 108, 123))); // #5D6C7B
        datasource.add(dsLable);

        // data source list
        JBList<String> list = new JBList<>(MYSQL, ORACLE, SQLSERVER);
        list.setSelectedIndex(0);
        list.setCellRenderer(new DatabaseCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBounds(16, 30, 100, 400);
        Border border = BorderFactory.createMatteBorder(0, 0, 0, 1, JBColor.GRAY);
        datasource.setBorder(border);
        datasource.add(list);
        datasource.setPreferredSize(new Dimension(120, 520));

        dialog.add(datasource);

        return list;
    }

    /**
     * mysql 类型的form
     */
    private JPanel getMysqlForm(Window dialog, DefaultTableModel tableModel, int selectedRow, Database dbParam) {
        JPanel form = new JPanel(null);
        form.setPreferredSize(new Dimension(540, 520));

        // name
        setLable(form, "Name:", 15, 10, 70, 25);
        JTextField nameText = setTextField(form, nullOr(dbParam.getName(), ""), 90, 10, 320, 25);
        // host
        setLable(form, "Host:", 15, 70, 70, 25);
        JTextField hostText = setTextField(form, nullOr(dbParam.getHost(), "localhost"), 90, 70, 320, 25);
        // port
        setLable(form, "Port:", 420, 70, 30, 25);
        JTextField portText = setTextField(form, nullOr(dbParam.getPort(), "3306"), 460, 70, 70, 25);
        // database
        setLable(form, "Database:", 15, 100, 70, 25);
        JTextField databaseText = setTextField(form, nullOr(dbParam.getDatabase(), ""), 90, 100, 320, 25);
        // user
        setLable(form, "User:", 15, 130, 70, 25);
        JTextField userText = setTextField(form, nullOr(dbParam.getUsername(), "root"), 90, 130, 320, 25);
        // password
        setLable(form, "Password:", 15, 160, 70, 25);
        JPasswordField passwordText = setPasswordField(form, nullOr(dbParam.getPassword(), ""), 90, 160, 320, 25);
        // url
        setLable(form, "URL:", 15, 190, 70, 25);
        JTextField urlText = setTextField(form, nullOr(computeMysqlUrl(dbParam.getHost(), dbParam.getPort(), dbParam.getDatabase()),
                "jdbc:mysql://localhost:3306/"), 90, 190, 320, 25, false);
        // message
        String mess = "Select Mysql...";
        JLabel message = setMessage(form, mess);

        // 设置事件
        nameText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess));
        hostText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess, e ->
                urlText.setText(computeMysqlUrl(hostText.getText().trim(), portText.getText().trim(), databaseText.getText().trim()))
        ));
        portText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess, e ->
                urlText.setText(computeMysqlUrl(hostText.getText().trim(), portText.getText().trim(), databaseText.getText().trim()))
        ));
        databaseText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess, e ->
                urlText.setText(computeMysqlUrl(hostText.getText().trim(), portText.getText().trim(), databaseText.getText().trim()))
        ));
        userText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess));

        // 设置操作
        setOperation(form, e -> {
            String type = "Mysql";
            String driver = "com.mysql.jdbc.Driver";
            String name = nameText.getText().trim();
            if (StringUtils.isBlank(name)) {
                message.setText("Please input data source name...");
                message.setForeground(JBColor.RED);
                return;
            }
            String host = hostText.getText().trim();
            if (StringUtils.isBlank(host)) {
                message.setText("Please input data source hostname...");
                message.setForeground(JBColor.RED);
                return;
            }
            String port = portText.getText().trim();
            if (StringUtils.isBlank(port)) {
                message.setText("Please input data source port...");
                message.setForeground(JBColor.RED);
                return;
            }
            String database = databaseText.getText().trim();
            if (StringUtils.isBlank(database)) {
                message.setText("Please input data source database...");
                message.setForeground(JBColor.RED);
                return;
            }
            String username = userText.getText().trim();
            if (StringUtils.isBlank(username)) {
                message.setText("Please input data source user...");
                message.setForeground(JBColor.RED);
                return;
            }
            // password can be empty
            String password = new String(passwordText.getPassword());
            String url = urlText.getText().trim();

            // 创建或更行 tableModel
            Map<String, Database> databaseMap = settingManager.getDatabasesSetting().getDatabaseMap();
            if (databaseMap.containsKey(name)) {
                message.setText("Data source name already exists...");
                message.setForeground(JBColor.RED);
                return;
            }
            if (tableModel == null) {
                DefaultTableModel tModel = (DefaultTableModel) databasesTable.getModel();
                String []rowValues = {type, name, host, port, database, username, password, driver, url};
                tModel.addRow(rowValues);
            } else {
                tableModel.setValueAt(type, selectedRow, 0);
                tableModel.setValueAt(name, selectedRow, 1);
                tableModel.setValueAt(host, selectedRow, 2);
                tableModel.setValueAt(port, selectedRow, 3);
                tableModel.setValueAt(database, selectedRow, 4);
                tableModel.setValueAt(username, selectedRow, 5);
                tableModel.setValueAt(password, selectedRow, 6);
                tableModel.setValueAt(driver, selectedRow, 7);
                tableModel.setValueAt(url, selectedRow, 8);
            }

            dialog.setVisible(false);
        }, e -> dialog.setVisible(false));
        return form;
    }


    /**
     * 获取oracle类型的form
     */
    private JPanel getOracleForm(Window dialog) {
        JPanel form = new JPanel(null);
        form.setPreferredSize(new Dimension(540, 520));

        setLable(form, "In progress...", 15, 10, 100, 25);

        // message
        setMessage(form, "Select Oracle...");
        return form;
    }

    /**
     * 获取SQL Server类型的form
     */
    private JPanel getSQLServerForm(Window dialog) {
        JPanel form = new JPanel(null);
        form.setPreferredSize(new Dimension(540, 520));

        setLable(form, "In progress...", 15, 10, 100, 25);

        // message
        setMessage(form, "Select SQL Server...");
        return form;
    }

    // 表单操作
    private void setOperation(Container form, ActionListener onOk, ActionListener onCancel) {
        // ok
        JButton ok = new JButton("OK");
        ok.setBounds(450, 460, 70, 30);
        ok.addActionListener(onOk);
        form.add(ok);
        // cancel
        JButton cancel = new JButton("Cancel");
        cancel.setBounds(380, 460, 70, 30);
        cancel.addActionListener(onCancel);
        form.add(cancel);
    }

    // 获取提示message
    private JLabel setMessage(Container form, String init) {
        JLabel message = new JLabel(init);
        message.setIcon(MessageType.INFO.getDefaultIcon());
        message.setBounds(20, 460, 300, 30);
        form.add(message);
        return message;
    }

    // 设置Label
    private JLabel setLable(Container form, String name, int x, int y, int width, int height) {
        JLabel label = new JLabel(name);
        label.setBounds(x, y, width, height);
        form.add(label);
        return label;
    }

    // 设置输入表单
    private JTextField setTextField(Container form, String name, int x, int y, int width, int height) {
        return setTextField(form, name, x, y, width, height, true);
    }
    private JTextField setTextField(Container form, String name, int x, int y, int width, int height, boolean editable) {
        JTextField textField = new JTextField(name);
        textField.setBounds(x, y, width, height);
        textField.setEditable(editable);
        form.add(textField);
        return textField;
    }

    // 设置密码类型的输入表单
    private JPasswordField setPasswordField(Container form, String name, int x, int y, int width, int height) {
        JPasswordField textField = new JPasswordField(name);
        textField.setBounds(x, y, width, height);
        form.add(textField);
        return textField;
    }

    // 重新计算mysql的url
    private String computeMysqlUrl(String host, String port, String database) {
        if (StringUtils.isBlank(host) && StringUtils.isBlank(port) && StringUtils.isBlank(database)) {
            return null;
        }
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }


    /**
     * 数据库类型的渲染类
     */
    class DatabaseCellRenderer extends JLabel implements ListCellRenderer<String> {
        private static final long serialVersionUID = 5256855406382187341L;

        DatabaseCellRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value);
            setPreferredSize(new Dimension(100, 25));
            setIcon(AllIcons.Providers.Mysql);
            if (Objects.equals(value, MYSQL)) {
                setIcon(AllIcons.Providers.Mysql);
            } else if (Objects.equals(value, ORACLE)) {
                setIcon(AllIcons.Providers.Oracle);
            } else if (Objects.equals(value, SQLSERVER)) {
                setIcon(AllIcons.Providers.SqlServer);
            }
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                //设置选取与取消选取的前景与背景颜色.
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }


    /**
     * 重置消息
     */
    class MessageResetAdapter extends DocumentAdapter {

        private JLabel message;
        private String mess;
        private Consumer<Object> doAction;

        MessageResetAdapter(JLabel message, String mess) {
            this.message = message;
            this.mess = mess;
        }

        MessageResetAdapter(JLabel message, String mess, Consumer<Object> doAction) {
            this.message = message;
            this.mess = mess;
            this.doAction = doAction;
        }

        @Override
        protected void textChanged(DocumentEvent e) {
            this.message.setText(mess);
            this.message.setForeground(this.message.getParent().getForeground());
            if (this.doAction != null) {
                this.doAction.accept(null);
            }
        }
    }
}
