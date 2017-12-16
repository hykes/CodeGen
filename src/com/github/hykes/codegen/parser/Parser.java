package com.github.hykes.codegen.parser;

import com.github.hykes.codegen.model.Field;
import com.github.hykes.codegen.model.Table;

/**
 * Desc: 统一解析入口, 目前提供对sql语句的解析
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/6/20
 */
public interface Parser {

    /**
     * 将输入的sql语句解析成 Table 对象
     *
     * @param sql sql语句
     * @see Table
     * @see Field
     */
    Table parseSQL(String sql);

    /**
     * 获取对应parser实例
     */
    static Parser fromType(String type) {
        return new DefaultParser();
    }
}
