package me.hehaiyang.codegen.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.model.FieldType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.hehaiyang.codegen.model.FieldType.build;

public class DBOperationUtil {

    public static Connection getConnection(String driver, String url, String user, String password){
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

    public static List<String> getTables(Connection connection) {
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
    public static List<Field> getColumns(String tableName, Connection connection) {

        List<Field> fields = Lists.newArrayList();
        try {
            DatabaseMetaData databaseMetaData  = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, null);
            while(resultSet.next()) {
                Field field = new Field();
                String colName=resultSet.getString("COLUMN_NAME");
                field.setColumn(colName);
                String typeName=resultSet.getString("TYPE_NAME");
                Integer sqlType=StringUtils.string2Integer(resultSet.getString("DATA_TYPE"));
                field.setColumnType(typeName, sqlType);
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
    private static Map<String, FieldType> sqlTypes = Maps.newHashMap();
    static {
        // mysql https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html
        sqlTypes.put("UNKNOWN", build("UNKNOWN"));
        sqlTypes.put("BIT", build("Boolean"));
        sqlTypes.put("BOOL", build("Boolean"));
        sqlTypes.put("BOOLEAN", build("Boolean"));
        sqlTypes.put("TINYINT", build("Integer", "Int"));
        sqlTypes.put("SMALLINT", build("Integer", "Int"));
        sqlTypes.put("MEDIUMINT", build("Integer", "Int"));
        sqlTypes.put("INT", build("Integer", "Int"));
        sqlTypes.put("INTEGER", build("Integer", "Int"));
        sqlTypes.put("REAL", build("Float"));
        sqlTypes.put("FLOAT", build("Double")); // 也用Double吧
        sqlTypes.put("DOUBLE", build("Double"));
        sqlTypes.put("BIGINT", build("Long"));
        sqlTypes.put("STRING", build("String"));
        sqlTypes.put("CHAR", build("String"));
        sqlTypes.put("VARCHAR", build("String"));
        sqlTypes.put("TINYTEXT", build("String"));
        sqlTypes.put("TEXT", build("String"));
        sqlTypes.put("MEDIUMTEXT", build("String"));
        sqlTypes.put("LONGTEXT", build("String"));
        sqlTypes.put("SET", build("String"));
        sqlTypes.put("DATE", build("java.util.Date")); // java.sql.Date ?
        sqlTypes.put("DATETIME", build("java.util.Date")); // java.sql.Timestamp ?
        sqlTypes.put("TIMESTAMP", build("java.util.Date")); // java.sql.Timestamp ?
        sqlTypes.put("TIME", build("java.util.Date")); // java.sql.Time ?
        sqlTypes.put("DECIMAL", build("java.math.BigDecimal"));
        sqlTypes.put("BINARY", build("Byte[]", "ByteArray"));
        sqlTypes.put("VARBINARY", build("Byte[]", "ByteArray"));
        sqlTypes.put("BLOB", build("java.sql.Blob"));
        sqlTypes.put("TINYBLOB", build("java.sql.Blob"));
        sqlTypes.put("MEDIUMBLOB", build("java.sql.Blob"));
        sqlTypes.put("LONGBLOB", build("java.sql.Blob"));

        // 其他数据库类型
        sqlTypes.put("CLOB", build("java.sql.Clob"));
    }

    /**
     * 此处就不转成java的Types了, 直接由columnName去匹配字段对应的类型
     *
     * @return 对应字段的java类型
     */
    public static FieldType getFieldType(String typeName) {
        if (StringUtils.isBlank(typeName)) return build(typeName);
        FieldType fieldType = sqlTypes.get(typeName.trim().toUpperCase());
        if (fieldType == null) {
            return sqlTypes.get("UNKNOWN");
        }
        return fieldType;
    }

    /**
     * 根据Types获取字段类型
     *
     * @see Types
     * @return 对应字段的java类型
     */
    public static FieldType getFieldType(Integer sqlType) {
        FieldType fieldType = sqlTypes.get("UNKNOWN");

        // https://docs.oracle.com/javase/1.5.0/docs/guide/jdbc/getstart/mapping.html
        if (sqlType == Types.INTEGER) {
            fieldType = sqlTypes.get("INTEGER");
        } else if (sqlType == Types.VARCHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.CHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.LONGVARCHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.NVARCHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.NCHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.LONGNVARCHAR) {
            fieldType = sqlTypes.get("STRING");
        } else if (sqlType == Types.NUMERIC) {
            fieldType = sqlTypes.get("DECIMAL");
        } else if (sqlType == Types.DECIMAL) {
            fieldType = sqlTypes.get("DECIMAL");
        } else if (sqlType == Types.BIT) {
            fieldType = sqlTypes.get("BOOLEAN");
        } else if (sqlType == Types.BOOLEAN) {
            fieldType = sqlTypes.get("BOOLEAN");
        } else if (sqlType == Types.TINYINT) {
            fieldType = sqlTypes.get("INTEGER");
        } else if (sqlType == Types.SMALLINT) {
            fieldType = sqlTypes.get("INTEGER");
        } else if (sqlType == Types.BIGINT) {
            fieldType = sqlTypes.get("BIGINT");
        } else if (sqlType == Types.REAL) {
            fieldType = sqlTypes.get("REAL");
        } else if (sqlType == Types.FLOAT) {
            fieldType = sqlTypes.get("FLOAT");
        } else if (sqlType == Types.DOUBLE) {
            fieldType = sqlTypes.get("DOUBLE");
        } else if (sqlType == Types.DATE) {
            fieldType = sqlTypes.get("DATE"); // java.sql.Date ?
        } else if (sqlType == Types.TIME) {
            fieldType = sqlTypes.get("TIME"); // java.sql.Time ?
        } else if (sqlType == Types.TIMESTAMP) {
            fieldType = sqlTypes.get("TIMESTAMP"); // java.sql.Timestamp ?
        } else if (sqlType == Types.BINARY
                || sqlType == Types.VARBINARY) {
            fieldType = sqlTypes.get("BINARY");
        } else if (sqlType == Types.CLOB) {
            fieldType = sqlTypes.get("CLOB");
        } else if (sqlType == Types.BLOB
                || sqlType == Types.LONGVARBINARY) {
            fieldType = sqlTypes.get("BLOB");
        } else {
            // DISTINCT, ARRAY, STRUCT, REF, JAVA_OBJECT.
            return fieldType;
        }
        return fieldType;
    }
}
