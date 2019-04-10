package com.github.hykes.codegen.gui.cmt;

import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.BalloonImpl;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Desc: Balloon 包装类组件, 用于消息提示, 如错误消息气泡
 * <p>
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2019-04-06
 */
public class JBalloon implements Serializable {
    private static final long serialVersionUID = 8743819120006383017L;

    private Balloon balloon;
    private AtomicBoolean isShow = new AtomicBoolean(false);

    public JBalloon(Balloon balloon) {
        this.balloon = balloon;
    }

    /**
     * 显示气泡
     */
    public void show(RelativePoint point) {
        if (balloon.isDisposed() || isShow.getAndSet(true)) {
            return;
        }
        balloon.show(point, BalloonImpl.Position.below);
        // timer to hide
        Timer timer = new Timer(2000, e -> { balloon.hide(); isShow.set(false); });
        timer.setRepeats(false);
        timer.start();
    }


    /**
     * 构建一个简单的气泡
     */
    public static JBalloon buildSimple(String msg) {
        JBLabel label = new JBLabel(msg);
        return new JBalloon(
                JBPopupFactory.getInstance()
                        .createDialogBalloonBuilder(label, null)
                        // 去掉关闭的按钮
                        .setCloseButtonEnabled(false)
                        // 设置指针三角形的大小
                        .setPointerSize(new Dimension(1, 1))
                        .createBalloon()
        );
    }
}
