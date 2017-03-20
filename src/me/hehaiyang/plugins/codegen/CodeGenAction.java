package me.hehaiyang.plugins.codegen;

import me.hehaiyang.plugins.codegen.windows.CodeGenWindow;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.vfs.VirtualFile;


public class CodeGenAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        CodeGenWindow windowdialog=new CodeGenWindow(anActionEvent);

        windowdialog.setSize(800, 400);
        windowdialog.setAlwaysOnTop(false);
        windowdialog.setLocationRelativeTo(null);
        windowdialog.setVisible(true);

    }

    @Override
    public void update(AnActionEvent event) {
        //在Action显示之前,根据选中文件扩展名判定是否显示此Action
        String extension = getFileExtension(event.getDataContext());
//        this.getTemplatePresentation().setEnabled(extension != null && "jar".equals(extension));
        this.getTemplatePresentation().setEnabled(true);
    }

    public static String getFileExtension(DataContext dataContext) {
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(dataContext);
        return file == null ? null : file.getExtension();
    }


}
