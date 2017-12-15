package com.github.hykes.codegen.parser.impl;

import com.github.hykes.codegen.model.Field;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * Desc: mysql 数据库解析, 继承DefaultParser（需要其中的sql解析）
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/7/23
 */
public class MysqlParser extends DefaultParser {

    public static final String DRIVER = "com.mysql.jdbc.Driver";

    @Override
    public Connection getConnection(String url, String username, String password) {
        try {
            Class.forName(DRIVER);
            DriverManager.setLoginTimeout(10);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getTables(Connection connection) {
        /**
         * 也可以从sql中获取
         *
         * select
         *  TABLE_SCHEMA,
         *  TABLE_NAME,
         *  TABLE_COMMENT,
         *  TABLE_ROWS,
         *  DATA_LENGTH
         * from information_schema.tables
         * where table_type = 'BASE TABLE'; // 或者视图 'VIEW'
         */
        return super.getTables(connection);
    }

    @Override
    public List<Field> getColumns(String tableName, Connection connection) {
        /**
         * select
         *  TABLE_SCHEMA,
         *  TABLE_NAME,
         *  COLUMN_NAME,
         *  ORDINAL_POSITION,
         *  DATA_TYPE,
         *  CHARACTER_MAXIMUM_LENGTH,
         *  COLUMN_KEY,
         *  COLUMN_COMMENT
         * from information_schema.columns
         * where TABLE_NAME = '$table' // 或者唯一键 COLUMN_KEY in ('PRI' , 'UNI')
         */
        return super.getColumns(tableName, connection);
    }
}
