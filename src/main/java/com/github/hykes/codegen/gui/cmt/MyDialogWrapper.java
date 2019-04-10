package com.github.hykes.codegen.gui.cmt;

import com.github.hykes.codegen.gui.ActionOperator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Desc: 封装的dialog wrapper, 需要将窗口固定在IDEA内部, 且自带cancel和ok的按钮
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/1/19
 */
public class MyDialogWrapper extends DialogWrapper {

    /**
     * 窗口中的内容
     */
    private JComponent component;

    /**
     * 执行动作的对象
     */
    private ActionOperator operator = ActionOperator.BLANK;

    public MyDialogWrapper(Project project, JComponent component) {
        super(project, true);
        this.component = component;
        super.init();
    }

    public MyDialogWrapper(Project project, JFrame frame) {
        this(project, frame.getRootPane());
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return this.component;
    }

    @Override
    protected void doOKAction() {
        if (this.getOKAction().isEnabled() && operator.valid()) {
            this.close(0);
            this.operator.ok();
        }
    }

    @Override
    public void doCancelAction() {
        if (this.getCancelAction().isEnabled()) {
            this.close(1);
            this.operator.cancel();
        }
    }

    /**
     * 设置ok按钮事件
     */
    public void setActionOperator(ActionOperator operator) {
        if (operator != null) {
            this.operator = operator;
        }
    }
}
