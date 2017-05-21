package me.hehaiyang.codegen.setting;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/20
 */
public interface UIConfigurable {

    boolean isModified();

    void apply();

    void reset();

}
