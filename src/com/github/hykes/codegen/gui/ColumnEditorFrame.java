package com.github.hykes.codegen.gui;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.constants.DefaultParams;
import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeGroup;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.model.Field;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.model.Table;
import com.github.hykes.codegen.provider.FileProviderFactory;
import com.github.hykes.codegen.utils.PsiUtil;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.basic.BasicTable;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.util.containers.JBIterable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 字段自定义编辑器
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/5/12
 */
public class ColumnEditorFrame extends JFrame {

    private static final Logger LOGGER = Logger.getInstance(ColumnEditorFrame.class);

    private final SettingManager SETTING_MANAGER = SettingManager.getInstance();

    private final List<TablePanel> panels = new ArrayList<>();

    public ColumnEditorFrame newColumnEditorByDb(IdeaContext ideaContext, List<BasicTable> dbTables) {

        List<Table> tables = new ArrayList<>();
        for (BasicTable dbTable: dbTables) {
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
        return this;
    }

    public ColumnEditorFrame newColumnEditorBySql(IdeaContext ideaContext, List<Table> tables) {
        init(ideaContext, tables);
        // esc
        this.getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        return this;
    }

    private void init(IdeaContext ideaContext, List<Table> tables){
        setLayout(new BorderLayout());

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
        JScrollPane jScrollPane = ScrollPaneFactory.createScrollPane(tablesPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setAlignmentY(TOP_ALIGNMENT);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        for (Table table: tables) {
            TablePanel tablePanel = new TablePanel(table);
            tablesPanel.add(tablePanel.getRootComponent());
            panels.add(tablePanel);
        }

        final JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.X_AXIS));

        List<CodeGroup> groups = SETTING_MANAGER.getTemplates().getGroups();
        groups.forEach( it -> {
            JCheckBox groupBox = new JCheckBox(it.getName());
            groupBox.setName(it.getId());
            groupPanel.add(groupBox);
        });
        groupPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton genBtn = new JButton("Generate");

        genBtn.addActionListener( it ->{
            List<String> list = new ArrayList<>();
            this.getAllJCheckBoxValue(groupPanel, list);

            if(!list.isEmpty()) {
                List<CodeContext> contexts = new ArrayList<>();

                for (TablePanel panel: panels) {
                    String modelName = panel.getModelTextField().getText().trim();
                    String tableName = panel.getTableTextField().getText().trim();
                    // 组装数据
                    CodeContext context = new CodeContext(modelName, tableName, panel.getFields());
                    contexts.add(context);
                }
                generator(ideaContext, list, contexts);
                dispose();
            }
        });
        groupPanel.add(genBtn);

        add(jScrollPane, BorderLayout.CENTER);
        add(groupPanel, BorderLayout.SOUTH);
    }

    private List<String> getAllJCheckBoxValue(Container ct, List<String> list){
        if(list==null){
            list = new ArrayList<>();
        }
        int count=ct.getComponentCount();
        for(int i=0;i<count;i++){
            Component c=ct.getComponent(i);
            if(c instanceof JCheckBox && ((JCheckBox)c).isSelected()){
                list.add(c.getName());
            }
            else if(c instanceof Container){
                this.getAllJCheckBoxValue((Container)c,list);
            }
        }
        return list;
    }

    public void generator(IdeaContext ideaContext, List<String> groups, List<CodeContext> contexts){
        Map<String, Object> params = new HashMap<>();
        params.putAll(DefaultParams.getInHouseVariables());
        params.putAll(SETTING_MANAGER.getVariables().getParams());
        params.put("Project", ideaContext.getProject().getName());

        List<CodeGroup> groupList = SETTING_MANAGER.getTemplates().getGroups();
        groupList = groupList.stream().filter(it -> groups.contains(it.getId())).sorted(new ComparatorUtil()).collect(Collectors.toList());

        for(CodeGroup group: groupList){
            PsiDirectory psiDirectory = PsiUtil.createDirectory(ideaContext.getProject(), "Select Package For " + group.getName(), "");
            if(Objects.nonNull(psiDirectory)){
                try {
                    for (CodeContext context: contexts) {
                        for (CodeTemplate codeTemplate : group.getTemplates()) {
                            FileProviderFactory fileFactory = new FileProviderFactory(ideaContext.getProject(), psiDirectory);
                            fileFactory.getInstance(codeTemplate.getExtension()).create(codeTemplate, context, params);
                        }
                    }
                } catch (Exception e){
                    LOGGER.error(StringUtils.getStackTraceAsString(e));
                }
            }
        }

    }

    /**
     * 模版组优先级排序
     */
    public class ComparatorUtil implements Comparator<CodeGroup> {

        /**
         * 倒序排列即从大到小，若需要从小到大修改返回值1 和 -1
         */
        @Override
        public int compare(CodeGroup o1, CodeGroup o2) {
            double tempResult1 = o1.getLevel();
            double tempResult2 = o2.getLevel();
            if (tempResult1 > tempResult2) {
                return 1;
            } else if (tempResult1 < tempResult2) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
