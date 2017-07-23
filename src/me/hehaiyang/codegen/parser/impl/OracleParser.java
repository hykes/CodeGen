package me.hehaiyang.codegen.parser.impl;

import me.hehaiyang.codegen.model.Field;
import oracle.jdbc.OracleConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc: oracle 数据库解析, 继承DefaultParser（需要其中的sql解析）
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/7/23
 */
public class OracleParser extends DefaultParser {

    public static final String DRIVER = "oracle.jdbc.OracleDriver";

    /**
     * 连接类型
     */
    public static final String SERVICE = "SERVICE";
    public static final String SID = "SID";

    @Override
    public Connection getConnection(String url, String username, String password) {
        try {
            Class.forName(DRIVER);
            OracleConnection connection = (OracleConnection) DriverManager.getConnection(url, username, password);
            // 设置remark为true
            connection.setRemarksReporting(true);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getTables(Connection connection) {
        /**
         * 或从sql中获取
         *
         * -- table
         * SELECT
         *  t.owner TABLE_SCHEMA,
         *  t.table_name TABLE_NAME,
         *  c.comments TABLE_COMMENT,
         *  t.num_rows TABLE_ROWS,
         *  round(t.num_rows * t.avg_row_len) DATA_LENGTH
         * FROM all_tables t
         * LEFT JOIN ALL_TAB_COMMENTS c on c.owner = t.owner and c.table_name = t.table_name
         * WHERE TABLE_TYPE = 'TABLE'
         *
         * -- view
         * select
         *  t.owner TABLE_SCHEMA,
         *  t.view_name TABLE_NAME,
         *  c.comments TABLE_COMMENT
         *  from all_views t
         * LEFT JOIN ALL_TAB_COMMENTS c on c.owner = t.owner and c.table_name = t.view_name
         * WHERE TABLE_TYPE='VIEW'
         */
        List<String> result = new ArrayList<String>();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, types);

            while (resultSet.next()) {
                String schema = resultSet.getString("TABLE_SCHEM");
                if (!"SYS".equals(schema) && // 过滤掉部分系统相关的
                       !"SYSTEM".equals(schema) &&
                       !"XDB".equals(schema) &&
                       !"ORDDATA".equals(schema) &&
                       !"SYSMAN".equals(schema)) {
                    result.add((String) resultSet.getObject("TABLE_NAME"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Field> getColumns(String tableName, Connection connection) {
        /**
         * select
         *  t.owner TABLE_SCHEMA,
         *  t.table_name TABLE_NAME,
         *  t.column_name COLUMN_NAME,
         *  max(t.column_id) ORDINAL_POSITION,
         *  max(t.data_type) DATA_TYPE,
         *  max(t.data_length) DATA_LENGTH,
         *  max(c.comments) COLUMN_COMMENT,
         *  wmsys.wm_concat(all_cons.constraint_type) COLUMN_KEY
         * from ALL_TAB_COLUMNS t
         * left JOIN ALL_COL_COMMENTS c on t.owner=c.owner and t.table_name=c.table_name and t.column_name = c.column_name
         * left JOIN all_cons_columns con on t.owner=con.owner and t.table_name=con.table_name and t.column_name = con.column_name
         * left JOIN all_constraints all_cons on all_cons.constraint_name = con.constraint_name
         * WHERE t.table_name='$table'  // 或者唯一键 all_cons.constraint_type in ('P', 'U')
         * GROUP BY t.owner, t.table_name, t.column_name
         */
        return super.getColumns(tableName, connection);
    }
}
