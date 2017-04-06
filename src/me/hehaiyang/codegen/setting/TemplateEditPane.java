package me.hehaiyang.codegen.setting;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.uiDesigner.core.GridConstraints;

import javax.swing.*;
import java.awt.*;


public class TemplateEditPane {

    private JPanel templateEdit;
    private JTextField templateNameText;
    private JTextField templateTypeText;
    private JTextField fileNameText;
    private JLabel templateNameLabel;
    private JLabel templateTypeLabel;
    private JLabel fileNameLabel;
    private JPanel editorPane;
    private Editor editor;

//    public TemplateEditPane(CodeMakerSettings settings, String template,
//                            CodeMakerConfiguration parentPane) {
//        CodeTemplate codeTemplate = settings.getCodeTemplate(template);
//        if (codeTemplate == null) {
//            codeTemplate = CodeTemplate.EMPTY_TEMPLATE;
//        }
//
//        templateNameText.setText(codeTemplate.getName());
//        fileNameText.setText(String.valueOf(codeTemplate.getClassNumber()));
//        classNameText.setText(codeTemplate.getClassNameVm());
//        addVmEditor(codeTemplate.getCodeTemplate());
//        deleteTemplateButton.addActionListener(e -> {
//            int result = Messages.showYesNoDialog("Delete this template?", "Delete", null);
//            if (result == Messages.OK) {
//                settings.removeCodeTemplate(template);
//                parentPane.refresh(settings);
//            }
//        });
//    }

    private void addVmEditor(String template) {
        EditorFactory factory = EditorFactory.getInstance();
        Document velocityTemplate = factory.createDocument(template);
        editor = factory.createEditor(velocityTemplate, null, FileTypeManager.getInstance()
            .getFileTypeByExtension("vm"), false);
        GridConstraints constraints = new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 300), null, 0, true);
        editorPane.add(editor.getComponent(), constraints);
    }


}
