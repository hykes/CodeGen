package me.hehaiyang.codegen.windows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBOperation {

    public Connection getConnection(){
        // 驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数据库名scutcs
        String url = "jdbc:mysql://127.0.0.1:3306/hsh";

        // MySQL配置时的用户名
        String user = "root";

        // MySQL配置时的密码
        String password = "anywhere";

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

            while (resultSet.next() ) {
                result.add((String)resultSet.getObject("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     1.TABLE_CAT String => table catalog (may be null)

     2.TABLE_SCHEM String => table schema (may be null)

     3.TABLE_NAME String => table name (表名称)

     4.COLUMN_NAME String => column name(列名)

     5.DATA_TYPE int => SQL type from java.sql.Types(列的数据类型)

     6.TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified

     7.COLUMN_SIZE int => column size.

     8.BUFFER_LENGTH is not used.

     9.DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.

     10.NUM_PREC_RADIX int => Radix (typically either 10 or 2)

     11.NULLABLE int => is NULL allowed.

     12.REMARKS String => comment describing column (may be null)

     13.COLUMN_DEF String => default value for the column, (may be null)

     14.SQL_DATA_TYPE int => unused

     15.SQL_DATETIME_SUB int => unused

     16.CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column

     17.ORDINAL_POSITION int => index of column in table (starting at 1)

     18.IS_NULLABLE String => ISO rules are used to determine the nullability for a column.

     19.SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)

     20.SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)

     21.SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF)

     22.SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types

     23.IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
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
                String dataType=resultSet.getString("DATA_TYPE");
                System.out.println(dataType);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
