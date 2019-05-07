package com.github.hykes.codegen.gui.cmt;

import com.github.hykes.codegen.gui.ActionOperator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * ok和cancel左侧的自定义按钮事件
     */
    private List<Action> leftSideActions = new ArrayList<>();

    public MyDialogWrapper(Project project, JComponent component, Boolean autoInit) {
        super(project, true);
        this.component = component;
        if (autoInit) {
            this.init();
        }
    }

    public MyDialogWrapper(Project project, JComponent component) {
       this(project, component, true);
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
     * 设置ok和cancel等按钮的事件者
     */
    public void setActionOperator(ActionOperator operator) {
        if (operator != null) {
            this.operator = operator;
        }
    }

    /**
     * 设置左侧操作按钮, 初始化{@link MyDialogWrapper} 需要将 autoInit 置为 false
     * 然后手动去 init
     */
    public void addLeftSideAction(String name, ActionListener action) {
        this.leftSideActions.add(new DialogWrapperAction(name) {
            private static final long serialVersionUID = 332050247789587721L;
            @Override
            protected void doAction(ActionEvent actionEvent) {
                if (action != null) {
                    action.actionPerformed(actionEvent);
                }
            }
        });
    }

    @NotNull
    @Override
    protected Action[] createLeftSideActions() {
        if (this.leftSideActions.isEmpty()) {
            return super.createLeftSideActions();
        }
        return this.leftSideActions.toArray(new Action[0]);
    }

    @Override
    public void init() {
        super.init();
    }
}
