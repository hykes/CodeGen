package me.hehaiyang.codegen.parser.impl;

import com.google.common.collect.Lists;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.parser.Parser;
import me.hehaiyang.codegen.utils.StringUtils;
import me.hehaiyang.codegen.windows.DBOperation;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.List;

/**
 * Desc: 默认的解析器, 使用 JSqlParser 进行解析.
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/6/20
 */
public class DefaultParser implements Parser {

    @Override
    public List<Field> parseSQL(String sql) {
        List<Field> fields = Lists.newArrayList();
        if (StringUtils.isBlank(sql)) {
            return fields;
        }
        // 解析sql语句
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            if (!(statement instanceof CreateTable)) {
                throw new RuntimeException("Only support create table statement !!!");
            }
            CreateTable createTable = (CreateTable) statement;
            createTable.getColumnDefinitions().forEach(it -> {
                Field field = new Field();
                // 字段名称
                String columnName = removeQuotes(it.getColumnName());
                field.setColumn(columnName); // 同时设置了 FieldName

                // 字段类型
                ColDataType colDataType = it.getColDataType();
                field.setColumnType(colDataType.getDataType());
                field.setColumnSize(firstOrNull(colDataType.getArgumentsStringList()));

                // comment注释
                field.setComment(getColumnComment(it.getColumnSpecStrings()));

                // 字段的java类型
                field.setFieldType(DBOperation.toJavaType(field.getColumnType()));

                fields.add(field);
            });
        } catch (Exception e) {
            // 需要异常统一下
            e.printStackTrace();
        }
        return fields;
    }

    /**
     * 去除一些特殊的引号字符
     */
    private String removeQuotes(String input) {
        if (StringUtils.isBlank(input)) return "";

        StringBuilder sb = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (ch == '`' || ch == '\'' || ch == '\"') {
                continue;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 获取第一个元素或者null
     */
    private <T> T firstOrNull(List<T> lists) {
        if (lists == null || lists.isEmpty()) {
            return null;
        }
        return lists.get(0);
    }

    /**
     * 获取字段的注释
     */
    private String getColumnComment(List<String> specs) {
        if (specs == null || specs.isEmpty()) {
            return null;
        }
        for (int size = specs.size(), i = 0; i < size; i++) {
            String spec = specs.get(i);
            if (spec.toUpperCase().equals("COMMENT")) {
                // 下一个为comment内容, 查看是否越界
                if (i + 1 >= size) {
                    return null;
                }
                return removeQuotes(specs.get(i + 1));
            }
        }
        return null;
    }

}
