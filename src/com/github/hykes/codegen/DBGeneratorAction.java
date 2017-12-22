package com.github.hykes.codegen;

import com.github.hykes.codegen.gui.ColumnEditorFrame;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.database.model.basic.BasicTable;
import com.intellij.database.view.DatabaseView;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * 直连数据库生成代码
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/16
 */
public class DBGeneratorAction extends AnAction implements DumbAware {

    public DBGeneratorAction() {
        super(AllIcons.Icon_small);
    }

    @Override
    public void update(AnActionEvent e) {
        DatabaseView view = DatabaseView.DATABASE_VIEW_KEY.getData(e.getDataContext());
        if (view == null) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }

        List<Object> elements = view.getSelectedElements().toList();

        boolean hasTable = false;
        for (Object table : elements) {
            if (table instanceof BasicTable) {
                hasTable = true;
                break;
            }
        }
        e.getPresentation().setEnabledAndVisible(hasTable);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = PsiUtil.getProject(e);
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification("CodeGen plugin is not available during indexing !");
            return;
        }

        DatabaseView view = DatabaseView.DATABASE_VIEW_KEY.getData(e.getDataContext());
        List<Object> elements = view.getSelectedElements().toList();

        List<BasicTable> tables = new ArrayList<>();
        for (Object table : elements) {
            if (table instanceof BasicTable) {
                tables.add((BasicTable) table);
            }
        }

        ColumnEditorFrame frame = new ColumnEditorFrame();
        frame.newColumnEditorByDb(new IdeaContext(project), tables);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(false);
        frame.setVisible(true);
        frame.setResizable(false);
    }

}