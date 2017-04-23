package me.hehaiyang.codegen.windows;

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
    public void getTableColumn(String tableName, Connection connection) {
        DatabaseMetaData databaseMetaData;
        try {
            databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, null);
            while(resultSet.next()) {
                String colName=resultSet.getString("COLUMN_NAME");
                System.out.println(colName);
                String typeName=resultSet.getString("TYPE_NAME");
                System.out.println(typeName);
                String columnSize=resultSet.getString("COLUMN_SIZE");
                System.out.println(columnSize);
                String remarks=resultSet.getString("REMARKS");
                System.out.println(remarks);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
