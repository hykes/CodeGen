package me.hehaiyang.codegen.parser.impl;

import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.utils.ParseUtils;

import java.util.List;

/**
 * Desc: 简单的parser实现, 以支持旧版本的输入.
 *  旧版本的输入如:
 *
 *  ```
 *   | user_id |  BIGINT(20) | 申请用户ID |
 *   | user_name |  VARCHAR(20) | 申请用户名称 |
 *   | company_id |  BIGINT(20) | 供应商ID |
 *   | company_name | VARCHAR(64) | 供应商名称 |
 *   | checked_at | DATETIME | 审核日期 |
 *   | checker_id | BIGINT(20) | 审核用户名称 |
 *   | checker_name | VARCHAR(64) | 审核用户名称 |
 *   | data_json | VARCHAR(1024) | 审核数据 |
 *  ```
 *  直接将旧的代码迁移过来
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/6/20
 */
@Deprecated
public class SimpleParser extends DefaultParser {

    @Override
    public List<Field> parseSQL(String sql) {
        // TODO: 后期考虑将util中的内容移植过来, 由于是旧方法, 可以考虑废弃
        return ParseUtils.parseString(sql);
    }
}
