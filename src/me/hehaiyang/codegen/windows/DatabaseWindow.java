package me.hehaiyang.codegen.windows;

import com.intellij.openapi.ui.ComboBox;
import me.hehaiyang.codegen.model.Database;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.model.IdeaContext;
import me.hehaiyang.codegen.config.SettingManager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/23
 */
public class DatabaseWindow extends JFrame{

    public DatabaseWindow(IdeaContext ideaContext){
        setLayout(new BorderLayout());
        JFrame thisFrame = this;

        SettingManager settingManager = SettingManager.getInstance();

        JPanel configPanel = new JPanel();

        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        ComboBox databaseBox=new ComboBox();
        JButton connectBtn = new JButton("connect");

        List<Database> databases = settingManager.getDatabasesSetting().getDatabases();
        if(databases != null && !databases.isEmpty()){
            databaseBox.setRenderer(new ComboBoxCellRenderer());
            databases.forEach( it -> databaseBox.addItem(it));
        }else{
            databaseBox.addItem("无数据源");
            databaseBox.setEnabled(false);
            connectBtn.setEnabled(false);
        }

        configPanel.add(databaseBox);
        configPanel.add(connectBtn);

        ComboBox comboBox=new ComboBox();
        comboBox.addItem("请选择");
        comboBox.setEnabled(false);

        connectBtn.addActionListener( e ->{

            Database database = (Database) databaseBox.getSelectedItem();
            DBOperation op = new DBOperation();
            Connection conn = op.getConnection(database.getDriver(), database.getUrl(), database.getUsername(), database.getPassword());

            try {
                if (!conn.isClosed()) {
                    List<String> list = op.getAllTables(conn);
                    comboBox.removeAllItems();
                    for (String str : list) {
                        comboBox.addItem(str);
                    }
                    comboBox.setEnabled(true);
                }
            }catch (SQLException sqle){
                comboBox.addItem("获取表单失败");
            }

        });

        configPanel.add(comboBox);

        JButton sureBtn = new JButton("sure");
        sureBtn.addActionListener( e ->{
            Database database = (Database) databaseBox.getSelectedItem();
            String table = comboBox.getSelectedItem().toString();
            DBOperation op = new DBOperation();
            Connection conn = op.getConnection(database.getDriver(), database.getUrl(), database.getUsername(), database.getPassword());
            List<Field> fields = op.getTableColumn(table, conn);
            ColumnEditorFrame frame = new ColumnEditorFrame(ideaContext, fields);
            frame.setSize(800, 400);
            frame.setAlwaysOnTop(false);
            frame.setLocationRelativeTo(thisFrame);
            frame.setVisible(true);
            frame.setResizable(false);
            dispose();
        });

        configPanel.add(sureBtn);

        add(configPanel, BorderLayout.CENTER);

    }

}
