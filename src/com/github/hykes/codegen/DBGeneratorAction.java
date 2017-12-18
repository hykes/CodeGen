package com.github.hykes.codegen;

import com.github.hykes.codegen.gui.ColumnEditorFrame;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.database.psi.DbTable;
import com.intellij.database.view.DatabaseView;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 直连数据库生成代码
 *
 * @author: hehaiyangwork@qq.com
 * @date: 2017/12/16
 */
public class DBGeneratorAction extends AnAction {

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
        Object[] elements = view.getTreeBuilder().getSelectedElements().toArray();
        boolean hasTable = false;

        for (Object table : elements) {
            if (table instanceof DbTable) {
                hasTable = true;
            }
        }
        e.getPresentation().setEnabledAndVisible(hasTable);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        DatabaseView view = DatabaseView.DATABASE_VIEW_KEY.getData(e.getDataContext());

        Object[] elements = view.getTreeBuilder().getSelectedElements().toArray();

        List<DbTable> tables = new ArrayList<>();
        for (Object table : elements) {
            if (table instanceof DbTable) {
                tables.add((DbTable) table);
            }
        }

        IdeaContext ideaContext = new IdeaContext();
        ideaContext.setProject(PsiUtil.getProject(e));

        ColumnEditorFrame frame = new ColumnEditorFrame();
        frame.newColumnEditorByDb(ideaContext, tables);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(false);
        frame.setVisible(true);
        frame.setResizable(false);
    }

}