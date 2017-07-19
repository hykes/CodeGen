package me.hehaiyang.codegen.windows;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import me.hehaiyang.codegen.config.SettingManager;
import me.hehaiyang.codegen.config.ui.variable.AddDialog;
import me.hehaiyang.codegen.constants.DefaultParams;
import me.hehaiyang.codegen.file.FileProviderFactory;
import me.hehaiyang.codegen.model.*;
import me.hehaiyang.codegen.utils.BuilderUtil;
import me.hehaiyang.codegen.utils.PsiUtil;
import me.hehaiyang.codegen.utils.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/12
 */
public class ColumnEditorFrame extends JFrame {

    private final SettingManager settingManager = SettingManager.getInstance();

    private final JBTable fieldTable = new JBTable();

    private JFrame thisFrame;

    public ColumnEditorFrame(IdeaContext ideaContext, Table table) {
        thisFrame = this;

        init(ideaContext, table);
        setFields(table.getFields());
    }

    private void init(IdeaContext ideaContext, Table table){
        setLayout(new BorderLayout());

        final JPanel topPanel = new JPanel(new GridLayout(1, 4));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        topPanel.add(new JLabel("model"));
        JTextField modelText = new JTextField(StringUtils.nullOr(table.getModelName(), ""));
        topPanel.add(modelText);
        topPanel.add(new JLabel("table"));
        JTextField tableText = new JTextField(StringUtils.nullOr(table.getTableName(), ""));
        topPanel.add(tableText);

        add(topPanel, BorderLayout.NORTH);

        final JPanel mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setPreferredSize(JBUI.size(300, 400));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        fieldTable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
        fieldTable.getTableHeader().setResizingAllowed(false);   //不可拉动表格
        JPanel panel = ToolbarDecorator.createDecorator(fieldTable)
            .setAddAction( it -> addAction())
            .setRemoveAction( it -> removeAction())
            .setEditAction( it -> editAction()).createPanel();
        final JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.setBorder(IdeBorderFactory.createTitledBorder("Field Table", false));
        localPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(localPanel);

        add(mainPanel, BorderLayout.CENTER);

        final JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.X_AXIS));

        List<CodeGroup> groups = settingManager.getTemplatesSetting().getGroups();
        groups.forEach( it -> {
            JCheckBox groupBox = new JCheckBox(it.getName());
            groupBox.setName(it.getId());
            groupPanel.add(groupBox);
        });
        groupPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton genButton = new JButton("Generate");
        genButton.addActionListener( it ->{
            List<String> list = Lists.newArrayList();
            getAllJCheckBoxValue(groupPanel, list);

            if(!list.isEmpty()) {
                String modelName = modelText.getText().trim();
                String tableName = tableText.getText().trim();
                // 组装数据
                CodeContext context = new CodeContext(modelName, tableName, getFields());
                gen(ideaContext, list, context);
                dispose();
            }
        });
        groupPanel.add(genButton);

        add(groupPanel, BorderLayout.SOUTH);

        fieldTable.getEmptyText().setText("No Fields");
        // esc
        thisFrame.getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public static List<String> getAllJCheckBoxValue(Container ct, List<String> list){
        if(list==null){
            list= Lists.newArrayList();
        }
        int count=ct.getComponentCount();
        for(int i=0;i<count;i++){
            Component c=ct.getComponent(i);
            if(c instanceof JCheckBox && ((JCheckBox)c).isSelected()){
                list.add(c.getName());
            }
            else if(c instanceof Container){
                getAllJCheckBoxValue((Container)c,list);
            }
        }
        return list;
    }

    private List<Field> getFields(){
        List<Field> fields = Lists.newArrayList();
        List<String> ignoreList = StringUtils.splitToList(settingManager.getConfigSetting().getIgnoreFields(), ",", true);
        DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
        for(int i = 0;i< tableModel.getRowCount(); i++){
            Field field = new Field();
            // field
            field.setField(tableModel.getValueAt(i, 0).toString());
            // field.setFieldType(tableModel.getValueAt(i, 1).toString()); // setColumnType设置
            // column and type
            field.setColumn(tableModel.getValueAt(i, 2).toString());
            if (Objects.nonNull(tableModel.getValueAt(i, 4))) {
                field.setSqlType(Integer.parseInt(tableModel.getValueAt(i, 4).toString()));
            }
            field.setColumnType(tableModel.getValueAt(i, 3).toString());
            if(Objects.nonNull(tableModel.getValueAt(i, 5))){
                field.setColumnSize(tableModel.getValueAt(i, 5).toString());
            }
            if(Objects.nonNull(tableModel.getValueAt(i, 6))){
                field.setComment(tableModel.getValueAt(i, 6).toString());
            }
            // 过滤
            if (!ignoreList.contains(field.getColumn().toUpperCase().trim())) {
                fields.add(field);
            }
        }
        return fields;
    }

    private void setFields(List<Field> fields){
        // 列名
        String[] columnNames = {"Field_Name", "Field_Type", "Column_Name","Column_Type", "Sql_Type", "Column_Size", "Comment"};
        // 默认数据
        Object[][] tableVales = new String[fields.size()][7];
        for (int row = 0; row < fields.size(); row++) {
            tableVales[row][0] = fields.get(row).getField();
            tableVales[row][1] = fields.get(row).getFieldType();
            tableVales[row][2] = fields.get(row).getColumn();
            tableVales[row][3] = fields.get(row).getColumnType();
            tableVales[row][4] = fields.get(row).getSqlType() + "";
            tableVales[row][5] = fields.get(row).getColumnSize();
            tableVales[row][6] = fields.get(row).getComment();
        }
        DefaultTableModel tableModel = new DefaultTableModel(tableVales,columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fieldTable.setModel(tableModel);
    }

    private void addAction(){
        JDialog dialog = new AddDialog();
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6,2));
        form.add(new Label("Field"));
        JTextField field = new JTextField();
        form.add(field);
        form.add(new Label("FieldType"));
        JTextField fieldType = new JTextField();
        form.add(fieldType);

        form.add(new Label("Column"));
        JTextField column = new JTextField();
        form.add(column);

        form.add(new Label("ColumnType"));
        JTextField columnType = new JTextField();
        form.add(columnType);

        form.add(new Label("SqlType"));
        JTextField sqlType = new JTextField();
        form.add(sqlType);

        form.add(new Label("ColumnSize"));
        JTextField columnSize = new JTextField();
        form.add(columnSize);

        form.add(new Label("Comment"));
        JTextField comment = new JTextField();
        form.add(comment);

        dialog.add(form, BorderLayout.CENTER);

        JButton add = new JButton("ADD");
        add.addActionListener( it ->{
            String fieldText = field.getText().trim();
            String fieldTypeText = fieldType.getText().trim();
            String columnText = column.getText();
            String columnTypeText = columnType.getText().trim();
            String sqlTypeText = sqlType.getText().trim();
            String columnSizeText = columnSize.getText().trim();
            String commentText = comment.getText().trim();

            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            String []rowValues = {fieldText, fieldTypeText, columnText, columnTypeText, sqlTypeText, columnSizeText, commentText};
            tableModel.addRow(rowValues);
            dialog.setVisible(false);
        });
        dialog.add(add, BorderLayout.SOUTH);

        dialog.setSize(300, 260);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(this);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void removeAction(){
        int selectedRow = fieldTable.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            tableModel.removeRow(selectedRow);
        }
        if(fieldTable.getRowCount() > 0){
            fieldTable.setRowSelectionInterval(0,0);
        }
    }

    private void editAction(){
        int selectedRow = fieldTable.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel tableModel = (DefaultTableModel) fieldTable.getModel();
            String oldFieldText = (String) tableModel.getValueAt(selectedRow, 0);
            String oldFieldTypeText = (String) tableModel.getValueAt(selectedRow, 1);
            String oldColumnText = (String) tableModel.getValueAt(selectedRow, 2);
            String oldColumnTypeText = (String) tableModel.getValueAt(selectedRow, 3);
            String oldSqlTypeText = (String) tableModel.getValueAt(selectedRow, 4);
            String oldColumnSizeText = (String) tableModel.getValueAt(selectedRow, 5);
            String oldCommentText = (String) tableModel.getValueAt(selectedRow, 6);

            JDialog dialog = new AddDialog();
            dialog.setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(6,2));

            form.add(new Label("Field"));
            JTextField field = new JTextField(oldFieldText);
            form.add(field);
            form.add(new Label("FieldType"));
            JTextField fieldType = new JTextField(oldFieldTypeText);
            form.add(fieldType);

            form.add(new Label("Column"));
            JTextField column = new JTextField(oldColumnText);
            form.add(column);

            form.add(new Label("ColumnType"));
            JTextField columnType = new JTextField(oldColumnTypeText);
            form.add(columnType);

            form.add(new Label("SqlType"));
            JTextField sqlType = new JTextField(oldSqlTypeText);
            form.add(sqlType);

            form.add(new Label("ColumnSize"));
            JTextField columnSize = new JTextField(oldColumnSizeText);
            form.add(columnSize);

            form.add(new Label("Comment"));
            JTextField comment = new JTextField(oldCommentText);
            form.add(comment);
            
            dialog.add(form, BorderLayout.CENTER);

            JButton add = new JButton("Confirm");
            add.addActionListener( it ->{
                String fieldText = field.getText().trim();
                String fieldTypeText = fieldType.getText().trim();
                String columnText = column.getText();
                String columnTypeText = columnType.getText().trim();
                String columnSizeText = columnSize.getText().trim();
                String commentText = comment.getText().trim();

                tableModel.setValueAt(fieldText, selectedRow, 0);
                tableModel.setValueAt(fieldTypeText, selectedRow, 1);
                tableModel.setValueAt(columnText, selectedRow, 2);
                tableModel.setValueAt(columnTypeText, selectedRow, 3);
                tableModel.setValueAt(columnSizeText, selectedRow, 4);
                tableModel.setValueAt(commentText, selectedRow, 5);
                dialog.setVisible(false);
            });
            dialog.add(add, BorderLayout.SOUTH);

            dialog.setSize(300, 260);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(this);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

        }
    }

    public void gen(IdeaContext ideaContext, List<String> groups, CodeContext context){

        Map<String, String> params = Maps.newHashMap();
        params.putAll(DefaultParams.getInHouseVariables());
        params.putAll(settingManager.getVariablesSetting().getParams());
        params.put("serialVersionUID", BuilderUtil.computeDefaultSUID(context.getModel(), context.getFields()) + "");
        params.put("Project", ideaContext.getProject().getName());
        List<CodeGroup> groupList = settingManager.getTemplatesSetting().getGroups();

        groupList = groupList.stream().filter(it -> groups.contains(it.getId())).collect(Collectors.toList());

        // 模版组优先级排序
        Collections.sort(groupList, new ComparatorUtil());

        int packageNum = 0;
        for(CodeGroup g: groupList){
            packageNum ++;

            PsiDirectory psiDirectory = PsiUtil.createDirectory(ideaContext.getProject(), "Select Package For " + g.getName(), "");

            if(Objects.nonNull(psiDirectory)){

                VirtualFile virtualFile = psiDirectory.getVirtualFile();
                String path = virtualFile.getPresentableUrl();

                String modulePath = StringUtils.substringAfter(path, ideaContext.getProject().getName()+"/");
                modulePath = StringUtils.substringBefore(modulePath, "/src/main/java");
                params.put("Module"+ packageNum, modulePath);

                String packagePath = StringUtils.substringAfter(path,"src/main/java");

                if(StringUtils.isNotEmpty(packagePath)){
                    if(packagePath.substring(0,1).equals("/")){
                        packagePath = packagePath.substring(1);
                    }
                    packagePath = packagePath.replace("/", ".");
                    params.put("Package"+ packageNum, packagePath);
                }else{
                    params.put("Package"+ packageNum, "");
                }
                context.set$(params);

                if(psiDirectory != null) {
                    FileProviderFactory fileFactory = FileProviderFactory.create(ideaContext.getProject(), psiDirectory);
                    WriteCommandAction.runWriteCommandAction(ideaContext.getProject(), () -> {
                        try {
                            for (CodeTemplate codeTemplate : g.getTemplates()) {
                                fileFactory.getInstance(codeTemplate.getExtension()).create(codeTemplate, context);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        }

    }

    public class ComparatorUtil implements Comparator<CodeGroup> {
        //倒序排列即从大到小，若需要从小到大修改返回值1 和 -1
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
