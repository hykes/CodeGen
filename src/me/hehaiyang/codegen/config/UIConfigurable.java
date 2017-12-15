package me.hehaiyang.codegen.config;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/5/20
 */
public interface UIConfigurable {

    boolean isModified();

    void apply();

    void reset();

}
