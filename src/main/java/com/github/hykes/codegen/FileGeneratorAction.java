package com.github.hykes.codegen;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.constants.CodeGenBundle;
import com.github.hykes.codegen.gui.ActionOperator;
import com.github.hykes.codegen.gui.SelectGroupPanel;
import com.github.hykes.codegen.gui.cmt.MyDialogWrapper;
import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeRoot;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.utils.GuiUtil;
import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Desc: 直接生成模板文件
 * <p>
 * Mail: chk19940609@gmail.com
 * @author IceMimosa
 * Date: 2019-05-07
 */
public class FileGeneratorAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = PsiUtil.getProject(e);
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification(CodeGenBundle.message("codegen.plugin.is.not.available.during.indexing"));
            return;
        }
        IdeaContext ideaContext = new IdeaContext(project);
        // 获取所有模板, 渲染选择控件
        // TODO: 是否需要自定义参数的表格
        List<CodeRoot> codeRoots =  SettingManager.getInstance().getTemplates().getRoots();
        SelectGroupPanel selectGroupPanel = new SelectGroupPanel(codeRoots, project);
        MyDialogWrapper frameWrapper = new MyDialogWrapper(project, selectGroupPanel.getRootPanel());
        frameWrapper.setActionOperator(new FileGeneratorActionOperator(ideaContext, selectGroupPanel));
        frameWrapper.setTitle("CodeGen-Files");
        frameWrapper.setSize(600, 400);
        frameWrapper.setResizable(false);
        frameWrapper.show();
    }

    class FileGeneratorActionOperator implements ActionOperator {

        private SelectGroupPanel selectGroupPanel;
        private IdeaContext ideaContext;
        public FileGeneratorActionOperator(IdeaContext ideaContext, SelectGroupPanel selectGroupPanel) {
            this.ideaContext = ideaContext;
            this.selectGroupPanel = selectGroupPanel;
        }

        @Override
        public void ok() {
            if (selectGroupPanel.hasSelected()) {
                GuiUtil.generateFile(
                        this.ideaContext,
                        Collections.singletonList(new CodeContext("__model__", "", "", Collections.emptyList())),
                        this.selectGroupPanel.getGroupPathMap()
                );
            }
        }
        @Override
        public void cancel() { }
        @Override
        public boolean valid() { return true; }
    }
}
