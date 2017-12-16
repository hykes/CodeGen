package com.github.hykes.codegen.model;

import javax.swing.table.DefaultTableModel;
import java.io.Serializable;
import java.util.List;

import static com.github.hykes.codegen.utils.StringUtils.trimObject;

/**
 * 数据库信息
 *
 * @author: hehaiyangwork@qq.com
 * @date: 2017/5/31
 */
public class Database implements Serializable {

    private static final long serialVersionUID = -6611073974504244920L;

    /**
     * 数据库类型
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 主机名
     */
    private String host;

    /**
     * 端口
     */
    private String port;

    /**
     * 数据库名称
     */
    private String database;

    /**
     * 数据库驱动
     */
    private String driver;

    /**
     * 连接url
     */
    private String url;

    /**
     * 连接类型(Oracle) @see OracleParser
     */
    private String serviceType;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public Database() {}

    public Database(String type, String name, String host, String port, String database, String username, String password, String driver, String url, String serviceType) {
        this.type = type;
        this.name = name;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.driver = driver;
        this.url = url;
        this.serviceType = serviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }


    /**
     * 从tableModel中创建对象
     */
    public static Database fromTableModel(DefaultTableModel tableModel, Integer row) {
        String type = trimObject(tableModel.getValueAt(row, 0));
        String name = trimObject(tableModel.getValueAt(row, 1));
        String host = trimObject(tableModel.getValueAt(row, 2));
        String port = trimObject(tableModel.getValueAt(row, 3));
        String database = trimObject(tableModel.getValueAt(row, 4));
        String username = trimObject(tableModel.getValueAt(row, 5));
        String password = trimObject(tableModel.getValueAt(row, 6));
        String driver = trimObject(tableModel.getValueAt(row, 7));
        String url = trimObject(tableModel.getValueAt(row, 8));
        String serviceType = trimObject(tableModel.getValueAt(row, 9));
        return new Database(type, name, host, port, database, username, password, driver, url, serviceType);
    }

    public static DefaultTableModel toTableModel(List<Database> databases) {
        // 列名
        String[] columnNames = {"Type", "Name", "Host", "Port", "Database", "Username", "Password", "Driver", "URL", "ServiceType"};
        // 默认数据
        Object[][] tableVales = new String[databases.size()][10];
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
            tableVales[row][9] = database.getServiceType();
        }
        return new DefaultTableModel(tableVales, columnNames) {
            private static final long serialVersionUID = 5495876077995275501L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
