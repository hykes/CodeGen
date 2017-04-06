package me.hehaiyang.codegen.setting;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.util.ui.JBDimension;
import lombok.Data;
import me.hehaiyang.codegen.components.ConfigPanel;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Desc: 配置面板
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@Data
public class FormatForm {

    /**
     * 主面板
     */
    private JPanel mainPanel;

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


    public JTabbedPane tabbedPanel;

    public JPanel writeServicePanel;
    public JTextArea writeServiceTextArea;
    public JPanel writeServiceImplPanel;
    public JTextArea writeServiceImplTextArea;
    public JPanel readServicePanel;
    public JTextArea readServiceTextArea;
    public JPanel readServiceImplPanel;
    public JTextArea readServiceImplTextArea;
    public JPanel controlPanel;
    public JTextArea controlTextArea;

    private JPanel paramPanel;
    private JTabbedPane tabbedPane1;
    private JPanel extraPanel;
    private JButton help;

    private void createUIComponents() {
        // TODO: place custom component creation code here
//        InputStream in = this.getClass().getResourceAsStream("/template/DaoTemplate.hbs");
//        try {
//            paramPanel = this.addVmEditor(inputStream2String(in), "java");
//            paramPanel.setMaximumSize(new JBDimension(500, 400));
//            paramPanel.setMinimumSize(new JBDimension(400, 300));
//            paramPanel.setSize(500,400);
//            tabbedPanel.addTab("自定义参数", paramPanel);
//        }catch (Exception e){
//
//        }
        paramPanel = new ConfigPanel();

        help = new JButton("help");
        help.setIcon(AllIcons.General.Help);
        help.setBounds(0, 0, 85, 82);
        help.setContentAreaFilled(false);
        help.addActionListener(helpbroker());
    }

    private ActionListener helpbroker(){
        HelpSet helpset;
        ClassLoader loader = this.getClass().getClassLoader();
        URL url = HelpSet.findHelpSet(loader, "javahelp/hello.hs");
        try {
            helpset = new HelpSet(loader, url);
        } catch (HelpSetException e) {
            System.err.println("Error loading HelpSet");
            return null;
        }

        HelpBroker helpbroker = helpset.createHelpBroker();
        helpbroker.setViewDisplayed(true);
        helpbroker.setSize(new JBDimension(800, 600));
        helpbroker.setLocation(getCenterPoint(800,600));
//        helpbroker.setFont(new Font("宋体",Font.BOLD,14));
        return new CSH.DisplayHelpFromSource(helpbroker);
    }

    /**
     * 文档居中处理
     * @param frameW 文档宽度
     * @param frameH 文档高度
     * @return
     */
    public Point getCenterPoint(int frameW,int frameH){

        Toolkit toolkit=Toolkit.getDefaultToolkit();
        Dimension dimension=toolkit.getScreenSize();
        Integer screenW = dimension.width;
        Integer screenH = dimension.height;

        Integer centerX = screenW/2-frameW/2;
        Integer centerY = screenH/2-frameH/2;

        return new Point(centerX,centerY);
    }

    public static String inputStream2String(InputStream is)  throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while((i=is.read())!=-1){
            baos.write(i);
        }
        return baos.toString();
    }

    private JPanel addVmEditor(String template, String extension) {
        JPanel editorPane = new JPanel();
        EditorFactory factory = EditorFactory.getInstance();
        Document velocityTemplate = factory.createDocument(template);
        Editor editor = factory.createEditor(velocityTemplate, null, FileTypeManager.getInstance()
                .getFileTypeByExtension(extension), false);
        GridConstraints constraints = new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, new JBDimension(300, 300), null, 0, true);
        editorPane.add(editor.getComponent(), constraints);
        return editorPane;
    }
}
