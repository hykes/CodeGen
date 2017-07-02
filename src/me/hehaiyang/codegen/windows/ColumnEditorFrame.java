package me.hehaiyang.codegen.windows;

import com.google.common.collect.Lists;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import me.hehaiyang.codegen.constants.DefaultParams;
import me.hehaiyang.codegen.file.FileFactory;
import me.hehaiyang.codegen.file.FileProvider;
import me.hehaiyang.codegen.model.*;
import me.hehaiyang.codegen.config.SettingManager;
import me.hehaiyang.codegen.config.ui.variable.AddDialog;
import me.hehaiyang.codegen.utils.BuilderUtil;
import me.hehaiyang.codegen.utils.PsiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/12
 */
public class ColumnEditorFrame extends JFrame {

    private final SettingManager settingManager = SettingManager.getInstance();

    private final JBTable variablesTable = new JBTable();

    private JFrame thisFrame;

    public ColumnEditorFrame(IdeaContext ideaContext, List<Field> fields) {
        thisFrame = this;

        init(ideaContext);
        setFields(fields);
    }

    private void init(IdeaContext ideaContext){
        setLayout(new BorderLayout());

        final JPanel topPanel = new JPanel(new GridLayout(2, 4));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        topPanel.add(new JLabel("model"));
        JTextField modelText = new JTextField();
        topPanel.add(modelText);
        topPanel.add(new JLabel("table"));
        JTextField tableText = new JTextField();
        topPanel.add(tableText);
        topPanel.add(new JLabel("model_cn"));
        JTextField modelCnText = new JTextField();
        topPanel.add(modelCnText);
        topPanel.add(new JLabel("table_cn"));
        JTextField tableCnText = new JTextField();
        topPanel.add(tableCnText);

        add(topPanel, BorderLayout.NORTH);

        final JPanel mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setPreferredSize(JBUI.size(300, 400));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        variablesTable.getTableHeader().setReorderingAllowed(false);   //不可整列移动
        variablesTable.getTableHeader().setResizingAllowed(false);   //不可拉动表格
        JPanel panel = ToolbarDecorator.createDecorator(variablesTable)
            .setAddAction( it -> addAction())
            .setRemoveAction( it -> removeAction())
            .setEditAction( it -> editAction()).createPanel();
        final JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.setBorder(IdeBorderFactory.createTitledBorder("User Defined Variables", false));
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
                String model = modelText.getText().trim();
                String modelName = modelCnText.getText().trim();
                String table = tableText.getText().trim();
                String tableName = tableCnText.getText().trim();
                // 组装数据
                CodeGenContext context = new CodeGenContext(model, modelName, table, tableName, getFields());
                gen(ideaContext.getProject(), list, context);
                dispose();
            }
        });
        groupPanel.add(genButton);

        add(groupPanel, BorderLayout.SOUTH);

        variablesTable.getEmptyText().setText("No Variables");
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
        DefaultTableModel tableModel = (DefaultTableModel) variablesTable.getModel();
        for(int i = 0;i< tableModel.getRowCount(); i++){
            Field field = new Field();
            field.setField(tableModel.getValueAt(i, 0).toString());
            field.setFieldType(tableModel.getValueAt(i, 0).toString());
            field.setColumn(tableModel.getValueAt(i, 2).toString());
            field.setColumnType(tableModel.getValueAt(i, 3).toString());
            if(Objects.nonNull(tableModel.getValueAt(i, 4))){
                field.setColumnSize(tableModel.getValueAt(i, 4).toString());
            }
            if(Objects.nonNull(tableModel.getValueAt(i, 5))){
                field.setComment(tableModel.getValueAt(i, 5).toString());
            }
            fields.add(field);
        }
        return fields;
    }

    private void setFields(List<Field> fields){
        // 列名
        String[] columnNames = {"Field_Name", "Field_Type", "Column_Name","Column_Type", "Column_Size", "Comment"};
        // 默认数据
        Object[][] tableVales = new String[fields.size()][6];
        for (int row = 0; row < fields.size(); row++) {
            tableVales[row][0] = fields.get(row).getField();
            tableVales[row][1] = fields.get(row).getFieldType();
            tableVales[row][2] = fields.get(row).getColumn();
            tableVales[row][3] = fields.get(row).getColumnType();
            tableVales[row][4] = fields.get(row).getColumnSize();
            tableVales[row][5] = fields.get(row).getComment();
        }
        DefaultTableModel tableModel = new DefaultTableModel(tableVales,columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        variablesTable.setModel(tableModel);
    }

    private void addAction(){
        JDialog dialog = new AddDialog();
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(2,2));
        form.add(new Label("Key"));
        JTextField keyJTextField = new JTextField();
        form.add(keyJTextField);
        form.add(new Label("Value"));
        JTextField valueJTextField = new JTextField();
        form.add(valueJTextField);

        dialog.add(form, BorderLayout.CENTER);

        JButton add = new JButton("ADD");
        add.addActionListener( it ->{
            String key = keyJTextField.getText();
            String value = valueJTextField.getText();

            DefaultTableModel tableModel = (DefaultTableModel) variablesTable.getModel();
            String []rowValues = {key, value};
            tableModel.addRow(rowValues);
            dialog.setVisible(false);
        });
        dialog.add(add, BorderLayout.SOUTH);

        dialog.setSize(300, 100);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(this);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void removeAction(){
        int selectedRow = variablesTable.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel tableModel = (DefaultTableModel) variablesTable.getModel();
            tableModel.removeRow(selectedRow);
        }
        if(variablesTable.getRowCount() > 0){
            variablesTable.setRowSelectionInterval(0,0);
        }
    }

    private void editAction(){
        int selectedRow = variablesTable.getSelectedRow();
        if(selectedRow != -1){
            DefaultTableModel tableModel = (DefaultTableModel) variablesTable.getModel();
            String oldKey = (String) tableModel.getValueAt(selectedRow, 0);
            String oldValue = (String) tableModel.getValueAt(selectedRow, 1);

            JDialog dialog = new AddDialog();
            dialog.setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridLayout(2,2));
            form.add(new Label("Key"));
            JTextField keyJTextField = new JTextField(oldKey);
            form.add(keyJTextField);
            form.add(new Label("Value"));
            JTextField valueJTextField = new JTextField(oldValue);
            form.add(valueJTextField);

            dialog.add(form, BorderLayout.CENTER);

            JButton add = new JButton("Confirm");
            add.addActionListener( it ->{
                String key = keyJTextField.getText();
                String value = valueJTextField.getText();

                tableModel.getValueAt(selectedRow, 1);
                tableModel.setValueAt(key, selectedRow, 0);
                tableModel.setValueAt(value, selectedRow, 1);
                dialog.setVisible(false);
            });
            dialog.add(add, BorderLayout.SOUTH);

            dialog.setSize(300, 100);
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(this);
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

        }
    }

    public void gen(Project project, List<String> groups, CodeGenContext context){

        Map<String, String> params = new HashMap<>();
        params.putAll(DefaultParams.getDefaults());
        params.putAll(settingManager.getVariablesSetting().getParams());
        params.put("serialVersionUID", BuilderUtil.computeDefaultSUID(context.getModel(), context.getFields()) + "");
        context.set$(params);

        Map<String, List<CodeTemplate>> templatesMap = settingManager.getTemplatesSetting().getTemplatesMap();

        for(String id: groups){

            PsiDirectory psiDirectory = PsiUtil.browseForFile(project);
            if(psiDirectory != null) {
                FileFactory fileFactory = new FileFactory(project, psiDirectory);
                WriteCommandAction.runWriteCommandAction(project, () -> {
                    try {
                        for (CodeTemplate codeTemplate : templatesMap.get(id)) {
                            FileProvider fileProvider = fileFactory.getInstance(codeTemplate.getExtension());
                            fileProvider.create(codeTemplate.getTemplate(), context, codeTemplate.getFilename());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }else{
                break;
            }
        }

    }

}
