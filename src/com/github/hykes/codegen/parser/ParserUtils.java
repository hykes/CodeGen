package com.github.hykes.codegen.parser;

import com.github.hykes.codegen.model.FieldType;
import com.github.hykes.codegen.utils.StringUtils;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import static com.github.hykes.codegen.model.FieldType.build;

/**
 * 解析工具
 * @author chk19940609@gmail.com
 * @date 2017/06/21
 */
public class ParserUtils {

    /**
     * sqlType <-> javaType
     *
     * 如果要转javaType的枚举, 可以使用Types
     * @see Types
     */
    private static Map<String, FieldType> sqlTypes = new HashMap<>();
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
        // 也用Double吧
        sqlTypes.put("FLOAT", build("Double"));
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
        // java.sql.Date ?
        sqlTypes.put("DATE", build("java.util.Date"));
        // java.sql.Timestamp ?
        sqlTypes.put("DATETIME", build("java.util.Date"));
        // java.sql.Timestamp ?
        sqlTypes.put("TIMESTAMP", build("java.util.Date"));
        // java.sql.Time ?
        sqlTypes.put("TIME", build("java.util.Date"));
        sqlTypes.put("DECIMAL", build("java.math.BigDecimal"));
        sqlTypes.put("BINARY", build("Byte[]", "ByteArray"));
        sqlTypes.put("VARBINARY", build("Byte[]", "ByteArray"));
        sqlTypes.put("BLOB", build("java.sql.Blob"));
        sqlTypes.put("TINYBLOB", build("java.sql.Blob"));
        sqlTypes.put("MEDIUMBLOB", build("java.sql.Blob"));
        sqlTypes.put("LONGBLOB", build("java.sql.Blob"));

        // oracle https://docs.oracle.com/cd/B19306_01/java.102/b14188/datamap.htm
        // 与上重复的忽略
        sqlTypes.put("CLOB", build("java.sql.Clob"));
        sqlTypes.put("NCLOB", build("java.sql.NClob"));
        sqlTypes.put("CHARACTER", build("String"));
        sqlTypes.put("VARCHAR2", build("String"));
        sqlTypes.put("NCHAR", build("String"));
        sqlTypes.put("NVARCHAR2", build("String"));
        sqlTypes.put("RAW", build("Byte[]", "ByteArray"));
        sqlTypes.put("LONG RAW", build("Byte[]", "ByteArray"));
        sqlTypes.put("BINARY_INTEGER", build("Integer", "Int"));
        sqlTypes.put("NATURAL", build("Integer", "Int"));
        sqlTypes.put("NATURALN", build("Integer", "Int"));
        sqlTypes.put("PLS_INTEGER", build("Integer", "Int"));
        sqlTypes.put("POSITIVE", build("Integer", "Int"));
        sqlTypes.put("POSITIVEN", build("Integer", "Int"));
        sqlTypes.put("SIGNTYPE", build("Integer", "Int"));
        sqlTypes.put("DEC", build("java.math.BigDecimal"));
        sqlTypes.put("NUMBER", build("java.math.BigDecimal"));
        sqlTypes.put("NUMERIC", build("java.math.BigDecimal"));
        sqlTypes.put("DOUBLE PRECISION", build("Double"));
        sqlTypes.put("ROWID", build("java.sql.RowId"));
        sqlTypes.put("UROWID", build("java.sql.RowId"));
        sqlTypes.put("VARRAY", build("java.sql.Array"));

        // sql server https://docs.microsoft.com/en-us/sql/connect/jdbc/using-basic-data-types
        // 与上重复的忽略
        // java.sql.Timestamp ?
        sqlTypes.put("DATETIME2", build("java.util.Date"));
        // java.sql.Timestamp ?
        sqlTypes.put("SMALLDATETIME", build("java.util.Date"));
        sqlTypes.put("IMAGE", build("Byte[]", "ByteArray"));
        sqlTypes.put("MONEY", build("java.math.BigDecimal"));
        sqlTypes.put("SMALLMONEY", build("java.math.BigDecimal"));
        sqlTypes.put("NTEXT", build("String"));
        sqlTypes.put("NVARCHAR", build("String"));
        sqlTypes.put("UNIQUEIDENTIFIER", build("String"));
        sqlTypes.put("UDT", build("Byte[]", "ByteArray"));
        sqlTypes.put("VARBINARY", build("Byte[]", "ByteArray"));
        sqlTypes.put("XML", build("java.sql.SQLXML"));
    }

    /**
     * 此处就不转成java的Types了, 直接由columnName去匹配字段对应的类型
     *
     * @return 对应字段的java类型
     */
    public static FieldType getFieldType(String typeName) {
        if (StringUtils.isBlank(typeName)) {
            sqlTypes.get("UNKNOWN");
        }
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
        if (sqlType == null) {
            return fieldType;
        }

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
            // java.sql.Date ?
            fieldType = sqlTypes.get("DATE");
        } else if (sqlType == Types.TIME) {
            // java.sql.Time ?
            fieldType = sqlTypes.get("TIME");
        } else if (sqlType == Types.TIMESTAMP) {
            // java.sql.Timestamp ?
            fieldType = sqlTypes.get("TIMESTAMP");
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
