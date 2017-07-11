package me.hehaiyang.codegen.parser;

import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.model.Table;

import java.util.List;

/**
 * Desc: 统一解析入口, 目前提供对sql语句的解析
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/6/20
 */
public interface Parser {


    /**
     * 将输入的sql语句解析成 Table 对象
     *
     * @see Table
     * @see Field
     * @param sql sql语句
     */
    Table parseSQL(String sql);


}
