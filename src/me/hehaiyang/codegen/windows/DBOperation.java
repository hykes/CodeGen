package me.hehaiyang.codegen.windows;

import com.google.common.collect.Lists;
import me.hehaiyang.codegen.model.Field;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBOperation {

    public Connection getConnection(String driver, String url, String user, String password){
        try {
            // 加载驱动程序
            Class.forName(driver);

            // 连续数据库
           return DriverManager.getConnection(url, user, password);

        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<String> getAllTables(Connection connection) {
        List<String> result = new ArrayList<String>();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet resultSet = databaseMetaData.getTables(null,null,null, types);

            while (resultSet.next()) {
                result.add((String)resultSet.getObject("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param tableName
     * @param connection
     */
    public List<Field> getTableColumn(String tableName, Connection connection) {

        List<Field> fields = Lists.newArrayList();
        try {
            DatabaseMetaData databaseMetaData  = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, null);
            while(resultSet.next()) {
                Field field = new Field();
                String colName=resultSet.getString("COLUMN_NAME");
                field.setColumn(colName);
                String typeName=resultSet.getString("TYPE_NAME");
                field.setColumnType(typeName);
                String columnSize=resultSet.getString("COLUMN_SIZE");
                field.setColumnSize(columnSize);
                String remarks=resultSet.getString("REMARKS");
                field.setComment(remarks);
                fields.add(field);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fields;
    }
}
