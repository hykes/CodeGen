package me.hehaiyang.codegen.model;

import java.io.Serializable;

/**
 * Desc: 数据库信息
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/31
 */
public class Database implements Serializable {

    private static final long serialVersionUID = -6611073974504244920L;

    private String name;

    private String driver;

    private String url;

    private String username;

    private String password;

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
}
