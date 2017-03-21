package me.hehaiyang.codegen.setting;

import javax.swing.*;

/**
 * Desc: 配置面板
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
public class FormatForm {

    /**
     * 主面板
     */
    public JPanel mainPanel;

    /**
     * mapper 模版设置
     */
    public JPanel mapperPanel;

    public JTextArea mapperTextArea;

    /**
     * sql 模版设置
     */
    public JPanel sqlPanel;

    public JTextArea sqlTextArea;

    /**
     * model 模版设置
     */
    public JPanel modelPanel;

    public JTextArea modelTextArea;


    private JTabbedPane tabbedPanel;

}
