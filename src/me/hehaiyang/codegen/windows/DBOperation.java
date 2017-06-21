package me.hehaiyang.codegen.windows;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.utils.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    /**
     * sqlType <-> javaType
     *
     * 如果要转javaType的枚举, 可以使用Types
     * @see Types
     */
    private static Map<String, String> sqlTypes = Maps.newHashMap();
    static {
        // mysql https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html
        sqlTypes.put("BIT", "Boolean");
        sqlTypes.put("BOOL", "Boolean");
        sqlTypes.put("BOOLEAN", "Boolean");
        sqlTypes.put("TINYINT", "Integer");
        sqlTypes.put("SMALLINT", "Integer");
        sqlTypes.put("MEDIUMINT", "Integer");
        sqlTypes.put("INT", "Integer");
        sqlTypes.put("INTEGER", "Integer");
        sqlTypes.put("FLOAT", "Double"); // 也用Double吧
        sqlTypes.put("DOUBLE", "Double");
        sqlTypes.put("BIGINT", "Long");
        sqlTypes.put("CHAR", "String");
        sqlTypes.put("VARCHAR", "String");
        sqlTypes.put("TINYTEXT", "String");
        sqlTypes.put("TEXT", "String");
        sqlTypes.put("MEDIUMTEXT", "String");
        sqlTypes.put("LONGTEXT", "String");
        sqlTypes.put("SET", "String");
        sqlTypes.put("DATE", "java.util.Date"); // java.sql.Date ?
        sqlTypes.put("DATETIME", "java.util.Date"); // java.sql.Timestamp ?
        sqlTypes.put("TIMESTAMP", "java.util.Date"); // java.sql.Timestamp ?
        sqlTypes.put("TIME", "java.util.Date"); // java.sql.Time ?
        sqlTypes.put("DECIMAL", "java.math.BigDecimal"); // java.sql.Time ?
        sqlTypes.put("BINARY", "Byte[]");
        sqlTypes.put("VARBINARY", "Byte[]");
        sqlTypes.put("BLOB", "java.sql.Blob");
        sqlTypes.put("TINYBLOB", "java.sql.Blob");
        sqlTypes.put("MEDIUMBLOB", "java.sql.Blob");
        sqlTypes.put("LONGBLOB", "java.sql.Blob");

        // 其他数据库类型
        // sqlTypes.put("CLOB", "java.sql.Clob");
    }

    /**
     * 此处就不转成java的Types了, 直接由columnName去匹配java类型
     *
     * @return 对应字段的java类型
     */
    public static String toJavaType(String typeName) {
        if (StringUtils.isBlank(typeName)) return typeName;
        return sqlTypes.get(typeName.trim().toUpperCase());
    }
}
