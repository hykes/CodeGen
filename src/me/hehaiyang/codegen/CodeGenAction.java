package me.hehaiyang.codegen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import me.hehaiyang.codegen.windows.CodeGenWindow;

public class CodeGenAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        CodeGenWindow codeGenWindow=new CodeGenWindow(anActionEvent);

        codeGenWindow.setSize(800, 400);
        codeGenWindow.setAlwaysOnTop(false);
        codeGenWindow.setLocationRelativeTo(null);
        codeGenWindow.setVisible(true);
    }

    @Override
    public void update(AnActionEvent event) {
        //在Action显示之前,根据选中文件扩展名判定是否显示此Action
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        String extension = file == null ? null : file.getExtension();
        this.getTemplatePresentation().setEnabled(extension != null && "java".equals(extension));
    }

}
