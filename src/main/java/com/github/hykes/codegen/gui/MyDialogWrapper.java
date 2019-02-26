package com.github.hykes.codegen.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionListener;

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
     * ok按钮事件
     */
    private ActionListener okActionListener;

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
        if (this.getOKAction().isEnabled()) {
            this.close(0);
            if (this.okActionListener != null) {
                // 传入的ActionEvent为空
                this.okActionListener.actionPerformed(null);
            }
        }
    }

    /**
     * 设置ok按钮事件
     */
    public void setOkAction(ActionListener okActionListener) {
        this.okActionListener = okActionListener;
    }
}
