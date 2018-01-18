package com.github.hykes.codegen.gui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.constants.Defaults;
import com.github.hykes.codegen.model.*;
import com.github.hykes.codegen.provider.FileProviderFactory;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.containers.JBIterable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段自定义编辑器
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/5/12
 */
public class ColumnEditorFrame extends JFrame {

    private final SettingManager SETTING_MANAGER = SettingManager.getInstance();

    private final List<TablePanel> panels = new ArrayList<>();
    private Map<String, String> groupPathMap = new HashMap<>();

    public void newColumnEditorByDb(IdeaContext ideaContext, List<DbTable> dbTables) {
        List<Table> tables = new ArrayList<>();
        for (DbTable dbTable: dbTables) {
            Table table = new Table();
            table.setTableName(dbTable.getName());

            List<Field> fields = new ArrayList<>();

            JBIterable<? extends DasColumn> columnsIter = DasUtil.getColumns(dbTable);
            List<? extends DasColumn> dasColumns = columnsIter.toList();
            for (DasColumn dasColumn : dasColumns) {
                Field field = new Field();
                field.setColumn(dasColumn.getName());
                field.setColumnType(dasColumn.getDataType().typeName);
                field.setColumnSize(String.valueOf(dasColumn.getDataType().size));
                field.setComment(dasColumn.getComment());
                fields.add(field);
            }
            table.setFields(fields);
            tables.add(table);
        }
        init(ideaContext, tables);

        // esc
        this.getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void newColumnEditorBySql(IdeaContext ideaContext, List<Table> tables) {
        init(ideaContext, tables);
        // esc
        this.getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void init(IdeaContext ideaContext, List<Table> tables){
        setLayout(new BorderLayout());

        JBTabbedPane tabbedPane = new JBTabbedPane();


        for (Table table: tables) {
            TablePanel tablePanel = new TablePanel(table);
            tabbedPane.add(tablePanel.getRootComponent(), table.getTableName());
            panels.add(tablePanel);
        }

        List<CodeRoot> codeRoots =  SETTING_MANAGER.getTemplates().getRoots();
        SelectGroupPanel selectGroupPanel = new SelectGroupPanel(codeRoots, ideaContext.getProject());

        JPanel groupPanel = selectGroupPanel.getGroupsPanel();
        JButton genBtn = selectGroupPanel.getGenerator();
        groupPathMap = selectGroupPanel.getGroupPathMap();
        genBtn.addActionListener( it -> {
            List<String> selectGroups = new ArrayList<>();
            this.getAllJCheckBoxValue(groupPanel, selectGroups);

            if(!selectGroups.isEmpty()) {
                List<CodeContext> contexts = new ArrayList<>();

                for (TablePanel panel: panels) {
                    String modelName = panel.getModelTextField().getText().trim();
                    String tableName = panel.getTableTextField().getText().trim();
                    String comment = panel.getCommentTextField().getText().trim();
                    // 组装数据
                    CodeContext context = new CodeContext(modelName, tableName, comment, panel.getFields());
                    contexts.add(context);
                }
                generator(ideaContext, selectGroups, contexts);
                dispose();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
        selectGroupPanel.getRootPanel().setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        add(selectGroupPanel.getRootPanel(), BorderLayout.SOUTH);
    }

    private List<String> getAllJCheckBoxValue(Container ct, List<String> selectGroups){
        if(selectGroups == null){
            selectGroups = new ArrayList<>();
        }
        int count=ct.getComponentCount();
        for(int i=0;i<count;i++){
            Component c=ct.getComponent(i);
            if(c instanceof JCheckBox && ((JCheckBox)c).isSelected()){
                selectGroups.add(c.getName());
            } else if (c instanceof Container){
                this.getAllJCheckBoxValue((Container)c, selectGroups);
            }
        }
        return selectGroups;
    }

    public void generator(IdeaContext ideaContext, List<String> groups, List<CodeContext> contexts){
        Map<String, Object> params = new HashMap<>();
        params.putAll(Defaults.getDefaultVariables());
        params.putAll(SETTING_MANAGER.getVariables().getParams());
        params.put("Project", ideaContext.getProject().getName());

        List<CodeGroup> groupList = new ArrayList<>();
        List<CodeRoot> codeRoots = SETTING_MANAGER.getTemplates().getRoots();
        for (CodeRoot root: codeRoots) {
            groupList.addAll(root.getGroups());
        }
        groupList = groupList.stream().filter(it -> groups.contains(it.getId())).sorted(new ComparatorUtil()).collect(Collectors.toList());

        for (CodeGroup group : groupList) {
            String outputPath = groupPathMap.get(group.getId());
            if (StringUtils.isNotEmpty(outputPath)) {
                for (CodeContext context : contexts) {
                    for (CodeTemplate codeTemplate : group.getTemplates()) {
                        FileProviderFactory fileFactory = new FileProviderFactory(ideaContext.getProject(), outputPath);
                        fileFactory.getInstance(codeTemplate.getExtension()).create(codeTemplate, context, params);
                    }
                }
            }
        }
    }

    /**
     * 模版组优先级排序
     */
    public class ComparatorUtil implements Comparator<CodeGroup> {

        @Override
        public int compare(CodeGroup o1, CodeGroup o2) {
            double level1 = o1.getLevel();
            double level2 = o2.getLevel();
            if (level1 > level2) {
                return 1;
            } else if (level1 < level2) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
