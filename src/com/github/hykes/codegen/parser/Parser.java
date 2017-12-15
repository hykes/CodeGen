package com.github.hykes.codegen.parser;

import com.github.hykes.codegen.model.Field;
import com.github.hykes.codegen.parser.impl.MysqlParser;
import com.github.hykes.codegen.parser.impl.OracleParser;
import com.github.hykes.codegen.model.Table;
import com.github.hykes.codegen.parser.impl.DefaultParser;
import com.github.hykes.codegen.parser.impl.MssqlParser;
import com.github.hykes.codegen.utils.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Desc: 统一解析入口, 目前提供对sql语句的解析
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/6/20
 */
public interface Parser {

    // 数据库类型
    String MYSQL = "Mysql";
    String ORACLE = "Oracle";
    String SQLSERVER = "SQL Server";

    /**
     * 将输入的sql语句解析成 Table 对象
     *
     * @param sql sql语句
     * @see Table
     * @see Field
     */
    Table parseSQL(String sql);


    /**
     * 获取数据库连接
     *
     * @param url      连接url
     * @param username 用户名
     * @param password 密码
     */
    Connection getConnection(String url, String username, String password);

    /**
     * 获取对应数据连接的表
     *
     * @return 暂时返回字符串
     */
    default List<String> getTables(Connection connection) {
        List<String> result = new ArrayList<String>();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, types);

            while (resultSet.next()) {
                result.add((String) resultSet.getObject("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取表的所有列信息
     *
     * @param tableName  表名
     * @param connection 连接
     */
    default List<Field> getColumns(String tableName, Connection connection) {
        List<Field> fields = new ArrayList<>();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, null);
            while (resultSet.next()) {
                Field field = new Field();
                String colName = resultSet.getString("COLUMN_NAME");
                field.setColumn(colName);
                String typeName = resultSet.getString("TYPE_NAME");
                Integer sqlType = StringUtils.string2Integer(resultSet.getString("DATA_TYPE"));
                field.setColumnType(typeName, sqlType);
                String columnSize = resultSet.getString("COLUMN_SIZE");
                field.setColumnSize(columnSize);
                String remarks = resultSet.getString("REMARKS");
                field.setComment(remarks);
                fields.add(field);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fields;
    }


    /**
     * 获取对应parser实例
     */
    static Parser fromType(String type) {
        if (Objects.equals(type, MYSQL)) {
            return new MysqlParser();
        } else if (Objects.equals(type, ORACLE)) {
            return new OracleParser();
        } else if (Objects.equals(type, SQLSERVER)) {
            return new MssqlParser();
        }
        return new DefaultParser();
    }
}
