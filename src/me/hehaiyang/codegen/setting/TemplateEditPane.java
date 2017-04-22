package me.hehaiyang.codegen.setting;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileTypes.FileTypeManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.hehaiyang.codegen.model.CodeTemplate;

import javax.swing.*;
import java.awt.*;

/**
 * Desc: 模版配置
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TemplateEditPane extends JPanel {
    private JTextField templateName;
    private JTextField templateType;
    private JTextField fileName;
    private JLabel templateNameLabel;
    private JLabel templateTypeLabel;
    private JLabel fileNameLabel;
    private JPanel configPanel;

    private Editor editor;

    public TemplateEditPane(FormatSetting settings, String template) {
        super();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        CodeTemplate codeTemplate = settings.getCodeTemplate(template);
        if (codeTemplate == null) {
            codeTemplate = new CodeTemplate();
        }

        GridBagLayout layout = new GridBagLayout();
        JPanel jPanel = new JPanel(layout);

        templateName = new JTextField(codeTemplate.getName());
        templateName.setColumns(15);

        templateType = new JTextField(codeTemplate.getType());
        templateType.setColumns(10);

        fileName = new JTextField(codeTemplate.getFileName());
        fileName.setColumns(15);

        jPanel.add(new JLabel(" Name:"));
        jPanel.add(templateName);
        jPanel.add(new JLabel(" Extension:"));
        jPanel.add(templateType);
        jPanel.add(new JLabel(" Filename:"));
        jPanel.add(fileName);

        GridBagConstraints s= new GridBagConstraints();//定义一个GridBagConstraints，
        //是用来控制添加进的组件的显示位置
        s.fill = GridBagConstraints.BOTH;
        //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
        //NONE：不调整组件大小。
        //HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
        //VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
        //BOTH：使组件完全填满其显示区域。
        s.gridwidth=1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        s.weighty=0;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        layout.setConstraints(templateName, s);
        layout.setConstraints(templateType, s);
        layout.setConstraints(fileName, s);
        this.add(jPanel, BorderLayout.NORTH);

        editor = getEditor(codeTemplate.getTemplate(), codeTemplate.getType());
        this.add(editor.getComponent(), BorderLayout.CENTER);
    }

    /**
     * 创建编辑器
     * @param template
     * @param extension
     * @return
     */
    private Editor getEditor(String template, String extension) {
        EditorFactory factory = EditorFactory.getInstance();
        Document velocityTemplate = factory.createDocument(template);
        Editor editor = factory.createEditor(velocityTemplate, null, FileTypeManager.getInstance()
                .getFileTypeByExtension(extension), false);
        EditorSettings editorSettings = editor.getSettings();
        editorSettings.setLineNumbersShown(true);
        return editor;
    }

}
