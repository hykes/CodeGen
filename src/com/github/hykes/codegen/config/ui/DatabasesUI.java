package com.github.hykes.codegen.config.ui;

import com.github.hykes.codegen.config.SettingManager;
import com.github.hykes.codegen.config.UIConfigurable;
import com.github.hykes.codegen.config.setting.DatabasesSetting;
import com.github.hykes.codegen.config.ui.variable.AddDialog;
import com.github.hykes.codegen.model.Database;
import com.github.hykes.codegen.parser.Parser;
import com.github.hykes.codegen.parser.impl.MssqlParser;
import com.github.hykes.codegen.parser.impl.MysqlParser;
import com.github.hykes.codegen.parser.impl.OracleParser;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import org.jdesktop.swingx.HorizontalLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Desc: 数据库配置面板
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/5/12
 */
public class DatabasesUI extends JPanel implements UIConfigurable {

    private final JBTable databasesTable = new JBTable();

    private final SettingManager settingManager = SettingManager.getInstance();

    private JPanel thisPanel;

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
            if (databasesSetting.getDatabases().size() != tableModel.getRowCount()) {
                return true;
            }
            Map<String, Database> databaseMap = databasesSetting.getDatabaseMap();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Database newDataBase = Database.fromTableModel(tableModel, i);

                if (!databaseMap.containsKey(newDataBase.getName())) {
                    return true;
                } else {
                    Database dbParam = databaseMap.get(newDataBase.getName());
                    if (!dbParam.getDriver().equals(newDataBase.getDriver())
                            || !dbParam.getUrl().equals(newDataBase.getUrl())
                            || !dbParam.getType().equals(newDataBase.getType())
                            || !dbParam.getHost().equals(newDataBase.getHost())
                            || !dbParam.getPort().equals(newDataBase.getPort())
                            || !dbParam.getDatabase().equals(newDataBase.getDatabase())
                            || !dbParam.getUsername().equals(newDataBase.getUsername())
                            || !dbParam.getPassword().equals(newDataBase.getPassword())
                            || !dbParam.getServiceType().equals(newDataBase.getServiceType())) {
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
        settingManager.getDatabasesSetting().setDatabases(new ArrayList<>(getNoPersistentDatabaseMap().values()));
    }

    /**
     * 获取当前窗口的数据源, 还未持久化的
     * @return
     */
    private Map<String, Database> getNoPersistentDatabaseMap() {
        Map<String, Database> databases = new HashMap<>();
        DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Database database = Database.fromTableModel(tableModel, i);
            // 去重
            databases.put(database.getName(), database);
        }
        return databases;
    }

    @Override
    public void reset() {
        setDatabases(settingManager.getDatabasesSetting());
    }

    private void init() {
        setLayout(new BorderLayout());

        // 不可整列移动
        databasesTable.getTableHeader().setReorderingAllowed(false);
        // 不可拉动表格
        databasesTable.getTableHeader().setResizingAllowed(false);

        final JPanel mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setPreferredSize(JBUI.size(300, 400));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

        JPanel panel = ToolbarDecorator.createDecorator(databasesTable)
                .addExtraAction(new AnActionButton("Connect", AllIcons.Actions.Refresh) {
                    @Override
                    public void actionPerformed(AnActionEvent event) {
                        int selectedRow = databasesTable.getSelectedRow();
                        if (selectedRow != -1) {
                            Database database = Database.fromTableModel((DefaultTableModel) databasesTable.getModel(), selectedRow);

                            Parser parser = Parser.fromType(database.getType());
                            Connection connection = parser.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
                            try {
                                if (connection != null && !connection.isClosed()) {
                                    Messages.showInfoMessage(thisPanel, "连接成功！", "Info");
                                    connection.close();
                                } else {
                                    Messages.showInfoMessage(thisPanel, "连接失败！", "Error");
                                }
                            } catch (SQLException se) {
                                Messages.showInfoMessage(thisPanel, "连接失败！", "Error");
                            } finally {
                                try {
                                    if (connection != null) {
                                        connection.close();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public boolean isEnabled() {
                        return super.isEnabled() && databasesTable.getSelectedRow() != -1;
                    }
                })
                .setAddAction(it -> addAction())
                .setRemoveAction(it -> removeAction())
                .setEditAction(it -> editAction()).createPanel();
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

    private void setDatabases(DatabasesSetting databasesSetting) {
        // 处理成TableModel
        List<Database> databases = databasesSetting.getDatabases();
        databasesTable.setModel(Database.toTableModel(databases));

        // 处理每个列
        databasesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = databasesTable.getColumnModel();
        TableColumn typeColumn = columnModel.getColumn(0);
        typeColumn.setPreferredWidth(100);
        TableColumn nameColumn = columnModel.getColumn(1);
        nameColumn.setPreferredWidth(134);
        TableColumn hostColumn = columnModel.getColumn(2);
        TableColumn portColumn = columnModel.getColumn(3);
        TableColumn dbColumn = columnModel.getColumn(4);
        TableColumn userColumn = columnModel.getColumn(5);
        userColumn.setPreferredWidth(134);
        TableColumn passColumn = columnModel.getColumn(6);
        TableColumn driverColumn = columnModel.getColumn(7);
        TableColumn urlColumn = columnModel.getColumn(8);
        urlColumn.setPreferredWidth(400);
        TableColumn serviceTypeColumn = columnModel.getColumn(9);
        // 隐藏部分列
        databasesTable.removeColumn(hostColumn);
        databasesTable.removeColumn(portColumn);
        databasesTable.removeColumn(dbColumn);
        databasesTable.removeColumn(passColumn);
        databasesTable.removeColumn(driverColumn);
        databasesTable.removeColumn(serviceTypeColumn);
    }

    private void addAction() {
        JDialog dialog = new AddDialog();
        dialog.setLayout(new HorizontalLayout());

        // 1. 创建左部数据库类型选择
        JBList databaseList = setDatabaseList(dialog, Parser.MYSQL);

        // 2. 创建参数表单, 默认显示mysql
        JPanel mysqlForm = getMysqlForm(dialog, null, -1, new Database());
        JPanel oracleForm = getOracleForm(dialog, null, -1, new Database());
        JPanel sqlServerForm = getSQLServerForm(dialog, null, -1, new Database());
        dialog.add(mysqlForm);

        // 添加选择事件
        databaseList.addListSelectionListener(e -> {
            JBList source = (JBList) e.getSource();
            Object value = source.getSelectedValue();
            if (value == null) {
                return;
            }
            if (Objects.equals(value, Parser.MYSQL)) {
                dialog.getContentPane().remove(1);
                dialog.add(mysqlForm);
                dialog.revalidate();
                mysqlForm.repaint();
            } else if (Objects.equals(value, Parser.ORACLE)) {
                dialog.getContentPane().remove(1);
                dialog.add(oracleForm);
                dialog.revalidate();
                oracleForm.repaint();
            } else if (Objects.equals(value, Parser.SQLSERVER)) {
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

    private void removeAction() {
        int selectedRow = databasesTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
            tableModel.removeRow(selectedRow);
        }
        if (databasesTable.getRowCount() > 0) {
            databasesTable.setRowSelectionInterval(0, 0);
        }
    }

    private void editAction() {
        int selectedRow = databasesTable.getSelectedRow();
        if (selectedRow != -1) {
            // 就的database数据
            DefaultTableModel tableModel = (DefaultTableModel) databasesTable.getModel();
            Database oldDatabaseParam = Database.fromTableModel(tableModel, selectedRow);

            JDialog dialog = new AddDialog();
            dialog.setLayout(new HorizontalLayout());

            // 1. 创建左部数据库类型选择
            JBList databaseList = setDatabaseList(dialog, oldDatabaseParam.getType());

            // 2. 创建参数表单, 根据类型选择数据库类型
            JPanel mysqlForm = getMysqlForm(dialog, tableModel, selectedRow, oldDatabaseParam);
            JPanel oracleForm = getOracleForm(dialog, tableModel, selectedRow, oldDatabaseParam);
            JPanel sqlServerForm = getSQLServerForm(dialog, tableModel, selectedRow, oldDatabaseParam);
            if (Objects.equals(oldDatabaseParam.getType(), Parser.MYSQL)) {
                dialog.add(mysqlForm);
            } else if (Objects.equals(oldDatabaseParam.getType(), Parser.ORACLE)) {
                dialog.add(oracleForm);
            } else if (Objects.equals(oldDatabaseParam.getType(), Parser.SQLSERVER)) {
                dialog.add(sqlServerForm);
            }

            // 添加选择事件
            databaseList.addListSelectionListener(e -> {
                JBList source = (JBList) e.getSource();
                Object value = source.getSelectedValue();
                if (value == null) {
                    return;
                }
                if (Objects.equals(value, Parser.MYSQL)) {
                    dialog.getContentPane().remove(1);
                    dialog.add(mysqlForm);
                    dialog.revalidate();
                    mysqlForm.repaint();
                } else if (Objects.equals(value, Parser.ORACLE)) {
                    dialog.getContentPane().remove(1);
                    dialog.add(oracleForm);
                    dialog.revalidate();
                    oracleForm.repaint();
                } else if (Objects.equals(value, Parser.SQLSERVER)) {
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
    private JBList<String> setDatabaseList(Window dialog, String type) {
        JPanel datasource = new JPanel();
        datasource.setLayout(null);
        Label dsLable = new Label("Data Source");
        dsLable.setBounds(15, 10, 100, 14);
        dsLable.setFont(new Font("Default", 1, 14));
        dsLable.setForeground(new JBColor(new Color(93, 108, 123), new Color(93, 108, 123)));
        datasource.add(dsLable);

        // data source list
        JBList<String> list = new JBList<>(Parser.MYSQL, Parser.ORACLE, Parser.SQLSERVER);
        list.setSelectedIndex(0);
        list.setCellRenderer(new DatabaseCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBackground(dialog.getParent().getBackground());
        list.setBounds(16, 30, 100, 400);
        if (StringUtils.isNotBlank(type)) {
            list.setSelectedValue(type, true);
        }
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
        // 是否是新增
        boolean add = tableModel == null;

        // name
        setLable(form, "Name:", 15, 10, 70, 25);
        JTextField nameText = setTextField(form, StringUtils.nullOr(dbParam.getName(), ""), 90, 10, 320, 25, add);
        // host
        setLable(form, "Host:", 15, 70, 70, 25);
        JTextField hostText = setTextField(form, StringUtils.nullOr(dbParam.getHost(), "localhost"), 90, 70, 320, 25);
        // port
        setLable(form, "Port:", 420, 70, 30, 25);
        JTextField portText = setTextField(form, StringUtils.nullOr(dbParam.getPort(), "3306"), 460, 70, 70, 25);
        // database
        setLable(form, "Database:", 15, 100, 70, 25);
        JTextField databaseText = setTextField(form, StringUtils.nullOr(dbParam.getDatabase(), ""), 90, 100, 320, 25);
        // user
        setLable(form, "User:", 15, 130, 70, 25);
        JTextField userText = setTextField(form, StringUtils.nullOr(dbParam.getUsername(), "root"), 90, 130, 320, 25);
        // password
        setLable(form, "Password:", 15, 160, 70, 25);
        JPasswordField passwordText = setPasswordField(form, StringUtils.nullOr(dbParam.getPassword(), ""), 90, 160, 320, 25);
        // url
        setLable(form, "URL:", 15, 190, 70, 25);
        JTextField urlText = setTextField(form, StringUtils.nullOr(computeMysqlUrl(dbParam.getHost(), dbParam.getPort(), dbParam.getDatabase()),
                "jdbc:mysql://localhost:3306/"), 90, 190, 400, 25, false);
        // message
        String mess = "Select Mysql...";
        JLabel message = setMessage(form, mess);
        // test button
        setTestConnection(form, event -> {
            String tUser = userText.getText().trim();
            String tPass = new String(passwordText.getPassword()).trim();
            // String tDriver = MysqlParser.DRIVER;
            String tUrl = urlText.getText().trim();

            Parser parser = Parser.fromType(Parser.MYSQL);
            Connection connection = parser.getConnection(tUrl, tUser, tPass);
            try {
                if (connection != null && !connection.isClosed()) {
                    message.setText("Successful !!!");
                    message.setForeground(JBColor.GREEN);
                    connection.close();
                } else {
                    message.setText("Connect failed !!!");
                    message.setForeground(JBColor.RED);
                }
            } catch (Exception te) {
                message.setText("Connect failed !!!");
                message.setForeground(JBColor.RED);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
            String type = Parser.MYSQL;
            String driver = MysqlParser.DRIVER;
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
            if (add) {
                Map<String, Database> databaseMap = getNoPersistentDatabaseMap();
                if (databaseMap.containsKey(name)) {
                    message.setText("Data source name already exists...");
                    message.setForeground(JBColor.RED);
                    return;
                }
                DefaultTableModel tModel = (DefaultTableModel) databasesTable.getModel();
                String[] rowValues = {type, name, host, port, database, username, password, driver, url, ""};
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
    private JPanel getOracleForm(Window dialog, DefaultTableModel tableModel, int selectedRow, Database dbParam) {
        JPanel form = new JPanel(null);
        form.setPreferredSize(new Dimension(540, 520));

        // 是否是新增
        boolean add = tableModel == null;

        // name
        setLable(form, "Name:", 15, 10, 70, 25);
        JTextField nameText = setTextField(form, StringUtils.nullOr(dbParam.getName(), ""), 90, 10, 320, 25, add);
        // host
        setLable(form, "Host:", 15, 70, 70, 25);
        JTextField hostText = setTextField(form, StringUtils.nullOr(dbParam.getHost(), "localhost"), 90, 70, 320, 25);
        // port
        setLable(form, "Port:", 420, 70, 30, 25);
        JTextField portText = setTextField(form, StringUtils.nullOr(dbParam.getPort(), "1521"), 460, 70, 70, 25);
        // database
        setLable(form, "SID/Service:", 15, 100, 78, 25);
        JTextField databaseText = setTextField(form, StringUtils.nullOr(dbParam.getDatabase(), ""), 93, 100, 320, 25);
        // user
        setLable(form, "User:", 15, 130, 70, 25);
        JTextField userText = setTextField(form, StringUtils.nullOr(dbParam.getUsername(), "scott"), 90, 130, 320, 25);
        // password
        setLable(form, "Password:", 15, 160, 70, 25);
        JPasswordField passwordText = setPasswordField(form, StringUtils.nullOr(dbParam.getPassword(), ""), 90, 160, 320, 25);
        // connect type
        JComboBox<String> comboBox = new ComboBox<>(new String[]{OracleParser.SID, OracleParser.SERVICE});
        if (StringUtils.isNotBlank(dbParam.getServiceType())) {
            comboBox.setSelectedItem(dbParam.getServiceType());
        }
        comboBox.setBounds(415, 190, 100, 25);
        form.add(comboBox);
        // url
        setLable(form, "URL:", 15, 190, 70, 25);
        JTextField urlText = setTextField(form, StringUtils.nullOr(computeOracleUrl(dbParam.getHost(), dbParam.getPort(), dbParam.getDatabase(), StringUtils.nullOr(dbParam.getServiceType(), OracleParser.SID)),
                "jdbc:oracle:thin:@localhost:1521"), 90, 190, 320, 25, false);

        // message
        String mess = "Select Oracle...";
        JLabel message = setMessage(form, mess);
        // test button
        setTestConnection(form, event -> {
            String tUser = userText.getText().trim();
            String tPass = new String(passwordText.getPassword()).trim();
            // String tDriver = OracleParser.DRIVER;
            String tUrl = urlText.getText().trim();

            Parser parser = Parser.fromType(Parser.ORACLE);
            Connection connection = parser.getConnection(tUrl, tUser, tPass);
            try {
                if (connection != null && !connection.isClosed()) {
                    message.setText("Successful !!!");
                    message.setForeground(JBColor.GREEN);
                    connection.close();
                } else {
                    message.setText("Connect failed !!!");
                    message.setForeground(JBColor.RED);
                }
            } catch (Exception te) {
                message.setText("Connect failed !!!");
                message.setForeground(JBColor.RED);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // 设置事件
        nameText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess));
        hostText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess, e ->
                urlText.setText(computeOracleUrl(hostText.getText().trim(), portText.getText().trim(), databaseText.getText().trim(), comboBox.getSelectedItem().toString()))
        ));
        portText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess, e ->
                urlText.setText(computeOracleUrl(hostText.getText().trim(), portText.getText().trim(), databaseText.getText().trim(), comboBox.getSelectedItem().toString()))
        ));
        databaseText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess, e ->
                urlText.setText(computeOracleUrl(hostText.getText().trim(), portText.getText().trim(), databaseText.getText().trim(), comboBox.getSelectedItem().toString()))
        ));
        comboBox.addItemListener(evt -> {
            String newType = comboBox.getSelectedItem().toString();
            urlText.setText(computeOracleUrl(hostText.getText().trim(), portText.getText().trim(), databaseText.getText().trim(), newType));
        });
        userText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess));

        // 设置操作
        setOperation(form, e -> {
            String serviceType = comboBox.getSelectedItem().toString();
            String type = Parser.ORACLE;
            String driver = OracleParser.DRIVER;
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
                message.setText("Please input data source SID or Service...");
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
            if (add) {
                Map<String, Database> databaseMap = getNoPersistentDatabaseMap();
                if (databaseMap.containsKey(name)) {
                    message.setText("Data source name already exists...");
                    message.setForeground(JBColor.RED);
                    return;
                }
                DefaultTableModel tModel = (DefaultTableModel) databasesTable.getModel();
                String[] rowValues = {type, name, host, port, database, username, password, driver, url, serviceType};
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
                tableModel.setValueAt(serviceType, selectedRow, 9);
            }

            dialog.setVisible(false);
        }, e -> dialog.setVisible(false));

        return form;
    }

    /**
     * 获取SQL Server类型的form
     */
    private JPanel getSQLServerForm(Window dialog, DefaultTableModel tableModel, int selectedRow, Database dbParam) {
        JPanel form = new JPanel(null);
        form.setPreferredSize(new Dimension(540, 520));
        // 是否是新增
        boolean add = tableModel == null;

        // name
        setLable(form, "Name:", 15, 10, 70, 25);
        JTextField nameText = setTextField(form, StringUtils.nullOr(dbParam.getName(), ""), 90, 10, 320, 25, add);
        // host
        setLable(form, "Host:", 15, 70, 70, 25);
        JTextField hostText = setTextField(form, StringUtils.nullOr(dbParam.getHost(), "localhost"), 90, 70, 320, 25);
        // port
        setLable(form, "Port:", 420, 70, 30, 25);
        JTextField portText = setTextField(form, StringUtils.nullOr(dbParam.getPort(), "1433"), 460, 70, 70, 25);
        // database
        setLable(form, "Database:", 15, 100, 70, 25);
        JTextField databaseText = setTextField(form, StringUtils.nullOr(dbParam.getDatabase(), ""), 90, 100, 320, 25);
        // user
        setLable(form, "User:", 15, 130, 70, 25);
        JTextField userText = setTextField(form, StringUtils.nullOr(dbParam.getUsername(), "sa"), 90, 130, 320, 25);
        // password
        setLable(form, "Password:", 15, 160, 70, 25);
        JPasswordField passwordText = setPasswordField(form, StringUtils.nullOr(dbParam.getPassword(), ""), 90, 160, 320, 25);
        // url
        setLable(form, "URL:", 15, 190, 70, 25);
        JTextField urlText = setTextField(form, StringUtils.nullOr(computeSqlServerUrl(dbParam.getHost(), dbParam.getPort(), dbParam.getDatabase()),
                "jdbc:sqlserver://localhost:1433"), 90, 190, 400, 25, false);
        // message
        String mess = "Select SQL Server...";
        JLabel message = setMessage(form, mess);
        // test button
        setTestConnection(form, event -> {
            String tUser = userText.getText().trim();
            String tPass = new String(passwordText.getPassword()).trim();
            // String tDriver = MssqlParser.DRIVER;
            String tUrl = urlText.getText().trim();

            Parser parser = Parser.fromType(Parser.SQLSERVER);
            Connection connection = parser.getConnection(tUrl, tUser, tPass);
            try {
                if (connection != null && !connection.isClosed()) {
                    message.setText("Successful !!!");
                    message.setForeground(JBColor.GREEN);
                    connection.close();
                } else {
                    message.setText("Connect failed !!!");
                    message.setForeground(JBColor.RED);
                }
            } catch (Exception te) {
                message.setText("Connect failed !!!");
                message.setForeground(JBColor.RED);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // 设置事件
        nameText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess));
        hostText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess, e ->
                urlText.setText(computeSqlServerUrl(hostText.getText().trim(), portText.getText().trim(), databaseText.getText().trim()))
        ));
        portText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess, e ->
                urlText.setText(computeSqlServerUrl(hostText.getText().trim(), portText.getText().trim(), databaseText.getText().trim()))
        ));
        databaseText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess, e ->
                urlText.setText(computeSqlServerUrl(hostText.getText().trim(), portText.getText().trim(), databaseText.getText().trim()))
        ));
        userText.getDocument().addDocumentListener(new MessageResetAdapter(message, mess));

        // 设置操作
        setOperation(form, e -> {
            String type = Parser.SQLSERVER;
            String driver = MssqlParser.DRIVER;
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
            if (add) {
                Map<String, Database> databaseMap = getNoPersistentDatabaseMap();
                if (databaseMap.containsKey(name)) {
                    message.setText("Data source name already exists...");
                    message.setForeground(JBColor.RED);
                    return;
                }
                DefaultTableModel tModel = (DefaultTableModel) databasesTable.getModel();
                String[] rowValues = {type, name, host, port, database, username, password, driver, url, ""};
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

    // 设置Test按钮
    private void setTestConnection(Container form, ActionListener onOk) {
        JButton test = new JButton("Test Connection");
        test.setBounds(87, 220, 130, 30);
        test.addActionListener(onOk);
        form.add(test);
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

    // 重新计算oracle的url
    private String computeOracleUrl(String host, String port, String database, String serviceType) {
        if (StringUtils.isBlank(host) && StringUtils.isBlank(port) && StringUtils.isBlank(database)) {
            return null;
        }
        if (Objects.equals(serviceType, OracleParser.SID)) {
            return "jdbc:oracle:thin:@" + host + ":" + port + ":" + database;
        }
        // OracleParser.SERVICE
        else {
            return "jdbc:oracle:thin:@//" + host + ":" + port + "/" + database;
        }
    }

    // 重新计算Sql server的url
    private String computeSqlServerUrl(String host, String port, String database) {
        if (StringUtils.isBlank(host) && StringUtils.isBlank(port) && StringUtils.isBlank(database)) {
            return null;
        }
        String url = "jdbc:sqlserver://" + host + ":" + port;
        if (StringUtils.isBlank(database)) {
            return url;
        }
        return url + ";database=" + database;
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
            if (Objects.equals(value, Parser.MYSQL)) {
                setIcon(AllIcons.Providers.Mysql);
            } else if (Objects.equals(value, Parser.ORACLE)) {
                setIcon(AllIcons.Providers.Oracle);
            } else if (Objects.equals(value, Parser.SQLSERVER)) {
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
