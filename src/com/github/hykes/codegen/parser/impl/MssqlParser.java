package com.github.hykes.codegen.parser.impl;

import com.github.hykes.codegen.model.Field;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * Desc: sql server 数据库解析, 继承DefaultParser（需要其中的sql解析）
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/7/23
 */
public class MssqlParser extends DefaultParser {

    public static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    @Override
    public Connection getConnection(String url, String username, String password) {
        try {
            Class.forName(DRIVER);
            DriverManager.setLoginTimeout(10);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getTables(Connection connection) {
        return super.getTables(connection);
    }

    @Override
    public List<Field> getColumns(String tableName, Connection connection) {
        // 这种方式获取不到列的comment
        // https://stackoverflow.com/questions/37612183/how-to-get-column-comments-in-jdbc
        return super.getColumns(tableName, connection);
    }
}
