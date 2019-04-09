package com.github.hykes.codegen;

import com.github.hykes.codegen.constants.Defaults;
import com.github.hykes.codegen.gui.MyDialogWrapper;
import com.github.hykes.codegen.gui.SqlEditorPanel;
import com.github.hykes.codegen.messages.CodeGenBundle;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * 解析sql生成代码
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/03/21
 */
public class SQLGeneratorAction extends AnAction implements DumbAware {

    public SQLGeneratorAction() {
        super(Defaults.CODEGEN);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project project = PsiUtil.getProject(anActionEvent);
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification(CodeGenBundle.message("codegen.plugin.is.not.available.during.indexing"));
            return;
        }

        JFrame frame = new JFrame();
        SqlEditorPanel sqlPane = new SqlEditorPanel(new IdeaContext(project));
        frame.setContentPane(sqlPane.getRootComponent());
        MyDialogWrapper frameWrapper = new MyDialogWrapper(project, frame);
        frameWrapper.setActionOperator(sqlPane);
        frameWrapper.setTitle("CodeGen-SQL");
        frameWrapper.setSize(600, 400);
        frameWrapper.setResizable(false);

//        frameWrapper.setOkAction(e -> {
//            JBPopup p = JBPopupFactory.getInstance().createMessage("aaaaasdaqwe");
//            p.show(RelativePoint.getSouthOf(sqlPane.getRootComponent()));
//
//            // create a timer to hide the popup later
//            Timer t = new Timer(5000, e1 -> p.cancel());
//            t.setRepeats(false);
//            t.start();
//        });
        frameWrapper.show();
    }

}
