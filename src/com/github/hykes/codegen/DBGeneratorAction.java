package com.github.hykes.codegen;

import com.github.hykes.codegen.gui.ColumnEditorFrame;
import com.github.hykes.codegen.gui.MyDialogWrapper;
import com.github.hykes.codegen.messages.CodeGenBundle;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.utils.Icons;
import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.database.psi.DbElement;
import com.intellij.database.psi.DbTable;
import com.intellij.database.view.DatabaseView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 直连数据库生成代码
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/16
 */
public class DBGeneratorAction extends AnAction implements DumbAware {

    public DBGeneratorAction() {
        super(Icons.CODEGEN);
    }

    @Override
    public void update(AnActionEvent e) {
        DatabaseView view = DatabaseView.DATABASE_VIEW_KEY.getData(e.getDataContext());
        if (view == null) {
            e.getPresentation().setEnabledAndVisible(false);
            return;
        }

        Iterator<DbElement> iterator = DatabaseView.getSelectedElements(e.getDataContext(), DbElement.class).iterator();

        boolean hasTable = false;
        while (iterator.hasNext()) {
            DbElement table = iterator.next();
            if (table instanceof DbTable) {
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
            dumbService.showDumbModeNotification(CodeGenBundle.message("codegen.plugin.is.not.available.during.indexing"));
            return;
        }

        Iterator<DbElement> iterator = DatabaseView.getSelectedElements(e.getDataContext(), DbElement.class).iterator();

        List<DbTable> tables = new ArrayList<>();
        while (iterator.hasNext()) {
            DbElement table = iterator.next();
            if (table instanceof DbTable) {
                tables.add((DbTable) table);
            }
        }

        ColumnEditorFrame frame = new ColumnEditorFrame();
        frame.newColumnEditorByDb(new IdeaContext(project), tables);
        MyDialogWrapper frameWrapper = new MyDialogWrapper(project, frame);
        frameWrapper.setTitle("CodeGen-DB");
        frameWrapper.setOkAction(frame.getGenerateAction());
        frameWrapper.setSize(800, 550);
        frameWrapper.setResizable(false);
        frameWrapper.show();
    }

}