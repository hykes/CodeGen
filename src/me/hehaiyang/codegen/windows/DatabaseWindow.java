package me.hehaiyang.codegen.windows;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.util.ui.JBUI;
import me.hehaiyang.codegen.model.Database;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.setting.SettingManager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/23
 */
public class DatabaseWindow extends JFrame{

    public DatabaseWindow(){
        setLayout(new BorderLayout());
        setSize(JBUI.size(200, 200));
        JFrame thisFrame = this;

        final JPanel mainPanel = new JPanel(new GridLayout(1, 1));
        mainPanel.setPreferredSize(JBUI.size(200, 200));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

        JPanel configPanel = new JPanel();

        ComboBox databaseBox=new ComboBox();
        databaseBox.setRenderer(new ComboBoxCellRenderer());
        SettingManager settingManager = SettingManager.getInstance();
        List<Database> databases = settingManager.getDatabasesSetting().getDatabases();
        for(Database database: databases){
            databaseBox.addItem(database);
        }

        JButton connectBtn = new JButton("connect");


        configPanel.add(databaseBox);
        configPanel.add(connectBtn);
        ComboBox comboBox=new ComboBox();
        comboBox.addItem("请选择");
        comboBox.setEnabled(false);

        connectBtn.addActionListener( e ->{

            Database database = (Database) databaseBox.getSelectedItem();
            DBOperation op = new DBOperation();
            Connection conn = op.getConnection(database.getDriver(), database.getUrl(), database.getUsername(), database.getPassword());

            List<String> list = op.getAllTables(conn);
            comboBox.removeAllItems();
            for(String str: list){
                comboBox.addItem(str);
            }
            comboBox.setEnabled(true);

        });

        configPanel.add(comboBox);

        JButton tableColumnBtn = new JButton("getColumn");
        tableColumnBtn.addActionListener( e ->{
            Database database = (Database) databaseBox.getSelectedItem();
            String table = comboBox.getSelectedItem().toString();
            DBOperation op = new DBOperation();
            Connection conn = op.getConnection(database.getDriver(), database.getUrl(), database.getUsername(), database.getPassword());
            List<Field> fields = op.getTableColumn(table, conn);
            ColumnEditorFrame frame = new ColumnEditorFrame(fields);
            frame.setSize(800, 400);
            frame.setAlwaysOnTop(false);
            frame.setLocationRelativeTo(thisFrame);
            frame.setVisible(true);
            frame.setResizable(false);
            thisFrame.setVisible(false);

        });

        configPanel.add(tableColumnBtn);


        final JPanel localPanel = new JPanel(new BorderLayout());
        localPanel.setBorder(IdeBorderFactory.createTitledBorder("User Defined Variables", false));
        localPanel.add(configPanel, BorderLayout.CENTER);
        mainPanel.add(localPanel);

        add(mainPanel, BorderLayout.CENTER);

        final JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        infoPanel.add(new JLabel("You can defined some variables for template.",
                MessageType.INFO.getDefaultIcon(), SwingConstants.LEFT));
        add(infoPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args){
        DatabaseWindow codeGenWindow = new DatabaseWindow();
        codeGenWindow.setSize(800, 400);
        codeGenWindow.setAlwaysOnTop(false);
        codeGenWindow.setLocationRelativeTo(null);
        codeGenWindow.setVisible(true);
    }

}
