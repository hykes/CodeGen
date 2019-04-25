package com.github.hykes.codegen.gui;

/**
 * Desc: 实际执行动作的对象
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2019-04-05
 */
public interface ActionOperator {

    /**
     * 执行OK的动作
     */
    void ok();

    /**
     * 执行取消的动作
     */
    void cancel();

    /**
     * 执行验证的动作
     */
    boolean valid();

    ActionOperator BLANK = new ActionOperator() {
        @Override public void ok() { }
        @Override public void cancel() { }
        @Override public boolean valid() { return true; }
    };
}


