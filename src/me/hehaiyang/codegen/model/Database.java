package me.hehaiyang.codegen.model;

import java.io.Serializable;

import static me.hehaiyang.codegen.config.ui.DatabasesUI.*;

/**
 * Desc: 数据库信息
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/31
 */
public class Database implements Serializable {

    private static final long serialVersionUID = -6611073974504244920L;

    /**
     * 数据库类型
     * @see MYSQL
     * @see ORACLE
     * @see SQLSERVER
     */
    private String type;
    // 名称
    private String name;
    // 主机名
    private String host;
    // 端口
    private String port;
    // 数据库名称
    private String database;
    // 数据库驱动
    private String driver;
    // 连接url
    private String url;
    // 用户名
    private String username;
    // 密码
    private String password;

    public Database() {
    }

    public Database(String type, String name, String host, String port, String database, String username, String password, String driver, String url) {
        this.type = type;
        this.name = name;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.driver = driver;
        this.url = url;
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
}
