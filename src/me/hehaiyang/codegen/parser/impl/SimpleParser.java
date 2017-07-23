package me.hehaiyang.codegen.parser.impl;

import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.model.Table;
import me.hehaiyang.codegen.parser.Parser;
import me.hehaiyang.codegen.parser.ParserAware;
import me.hehaiyang.codegen.utils.BuilderUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc: 简单的parser实现, 以支持旧版本的输入.
 * 旧版本的输入如:
 * <p>
 * ```
 * | user_id |  BIGINT(20) | 申请用户ID |
 * | user_name |  VARCHAR(20) | 申请用户名称 |
 * | company_id |  BIGINT(20) | 供应商ID |
 * | company_name | VARCHAR(64) | 供应商名称 |
 * | checked_at | DATETIME | 审核日期 |
 * | checker_id | BIGINT(20) | 审核用户名称 |
 * | checker_name | VARCHAR(64) | 审核用户名称 |
 * | data_json | VARCHAR(1024) | 审核数据 |
 * ```
 * 直接将旧的代码迁移过来
 * <p>
 *
 * 不提供DB连接解析 !!!
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/6/20
 */
@Deprecated
public class SimpleParser extends ParserAware {

    @Override
    public Table parseSQL(String sql) {
        List<Field> fiellist = new ArrayList<Field>();
        Table table = new Table(fiellist);

        String a = sql;
        ByteArrayInputStream is = new ByteArrayInputStream(a.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        int totolline = 0;
        String line;

        // 读取文件，并对读取到的文件进行操作
        try {
            while ((line = br.readLine()) != null) {
                totolline++;
                String[] data_temp = line.split("\\|");
                if (data_temp.length >= 3) {
                    String field = trim(data_temp[1]);
                    String type = trim(data_temp[2]);
                    String comment = trim(data_temp[3]);
                    Field entity = new Field();

                    entity.setColumn(field);
                    entity.setColumnType(type);
                    if (type.toUpperCase().contains("INT")) {
                        entity.setFieldType("Integer");
                        entity.setKotlinType("Int");
                    }
                    if (type.toUpperCase().contains("TINYINT")) {
                        entity.setFieldType("Integer");
                        entity.setKotlinType("Int");
                    }
                    if (type.toUpperCase().contains("BIGINT")) {
                        entity.setFieldType("Long");
                        entity.setKotlinType("Long");
                    }
                    if (type.toUpperCase().contains("VARCHAR")) {
                        entity.setFieldType("String");
                        entity.setKotlinType("String");
                    }
                    if (type.toUpperCase().contains("DATETIME")) {
                        entity.setFieldType("Date");
                        entity.setKotlinType("Date");
                    }
                    entity.setField(BuilderUtil.underlineToCamel(field));
                    entity.setComment(comment);
                    fiellist.add(entity);
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (totolline != fiellist.size())
            fiellist = new ArrayList<>();

        table.setFields(fiellist);
        return table;
    }

    private String trim(String str) {
        if (!"".equals(str)) {
            String str2 = str.replaceAll("\\s*", "");
            return str2;
        } else
            return "";
    }
}
