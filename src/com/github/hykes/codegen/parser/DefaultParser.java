package com.github.hykes.codegen.parser;

import com.github.hykes.codegen.model.Field;
import com.github.hykes.codegen.model.Table;
import com.github.hykes.codegen.utils.StringUtils;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc: 默认的解析器, 使用 JSqlParser 进行解析.
 *
 *  不提供DB连接解析 !!!
 *
 * @author: chk19940609@gmail.com
 * @date: 2017/6/20
 */
public class DefaultParser extends AbstractParser {

    @Override
    public List<Table> parseSQLs(String sqls) {
        if (StringUtils.isBlank(sqls)) {
            return Lists.newArrayList();
        }

        List<Table> result = Lists.newArrayList();
        // 解析sql语句
        try {
            List<Statement> statements = CCJSqlParserUtil.parseStatements(sqls).getStatements();
            if (statements == null || statements.isEmpty()) {
                throw new RuntimeException("Nothing in parse !!!");
            }
            List<CreateTable> createTables = Lists.newArrayList();
            for (Statement statement: statements) {
                if (statement instanceof CreateTable) {
                    createTables.add((CreateTable) statement);
                }
            }
            if (createTables.isEmpty()) {
                throw new RuntimeException("Only support create table statement !!!");
            }

            for(CreateTable createTable: createTables) {
                List<Field> fields = new ArrayList<>();
                Table table = new Table(fields);
                table.setTableName(removeQuotes(createTable.getTable().getName()));
                createTable.getColumnDefinitions().forEach(it -> {
                    Field field = new Field();
                    // 字段名称
                    String columnName = removeQuotes(it.getColumnName());
                    // 同时设置了 FieldName
                    field.setColumn(columnName);

                    // 字段类型
                    ColDataType colDataType = it.getColDataType();
                    // 同时设置了字段类型
                    field.setColumnType(colDataType.getDataType());
                    field.setColumnSize(firstOrNull(colDataType.getArgumentsStringList()));

                    // comment注释
                    field.setComment(getColumnComment(it.getColumnSpecStrings()));

                    fields.add(field);
                });

                if (table.getFields() != null && !table.getFields().isEmpty()) {
                    result.add(table);
                }
            }
        } catch (Exception e) {
            // 需要异常统一下
            e.printStackTrace();
        }
        return result;
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
            if ("COMMENT".equals(spec.toUpperCase())) {
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
