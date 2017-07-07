package me.hehaiyang.codegen.windows;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.IdeBorderFactory;
import me.hehaiyang.codegen.model.Database;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.model.IdeaContext;
import me.hehaiyang.codegen.config.SettingManager;
import me.hehaiyang.codegen.utils.DBOperationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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

        ComboBox databaseBox=new ComboBox();
        JButton connectBtn = new JButton("Connect");
        ComboBox tableBox=new ComboBox();
        JButton sureBtn = new JButton("Sure");

        List<Database> databases = settingManager.getDatabasesSetting().getDatabases();
        if(databases != null && !databases.isEmpty()){
            databaseBox.setRenderer(new ComboBoxCellRenderer());
            databases.forEach( it -> databaseBox.addItem(it));
        }else{
            databaseBox.addItem("无数据源");
            databaseBox.setEnabled(false);
            connectBtn.setEnabled(false);
        }

        tableBox.addItem("请选择");
        tableBox.setEnabled(false);
        sureBtn.setEnabled(false);

        connectBtn.addActionListener( it ->{

            Database database = (Database) databaseBox.getSelectedItem();
            if(database != null){
                try {
                    Connection conn = DBOperationUtil.getConnection(database.getDriver(), database.getUrl(), database.getUsername(), database.getPassword());

                    if (conn != null && !conn.isClosed()) {
                        List<String> list = DBOperationUtil.getTables(conn);
                        tableBox.removeAllItems();
                        for (String str : list) {
                            tableBox.addItem(str);
                        }
                        tableBox.setEnabled(true);
                        sureBtn.setEnabled(true);
                    }
                }catch (SQLException e){
                    Messages.showInfoMessage(thisFrame,"connect fail !", "Error");
                }
            }
        });

        sureBtn.addActionListener( it ->{
            Database database = (Database) databaseBox.getSelectedItem();
            if(database != null){
                try {
                    String table = tableBox.getSelectedItem().toString();
                    Connection conn = DBOperationUtil.getConnection(database.getDriver(), database.getUrl(), database.getUsername(), database.getPassword());
                    if (conn != null && !conn.isClosed()) {
                        List<Field> fields = DBOperationUtil.getColumns(table, conn);
                        ColumnEditorFrame frame = new ColumnEditorFrame(ideaContext, fields);
                        frame.setSize(800, 400);
                        frame.setAlwaysOnTop(false);
                        frame.setLocationRelativeTo(thisFrame);
                        frame.setVisible(true);
                        frame.setResizable(false);
                        dispose();
                    }
                }catch (SQLException e){
                    Messages.showInfoMessage(thisFrame,"get columns fail !", "Error");
                }
            }
        });

        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setSize(300, 150);
        configPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        configPanel.add(databaseBox);
        configPanel.add(connectBtn);
        configPanel.add(tableBox);
        configPanel.add(sureBtn);

        add(configPanel, BorderLayout.CENTER);
        // esc
        thisFrame.getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_FOCUSED);
    }

    public class ComboBoxCellRenderer extends JLabel implements ListCellRenderer {

        ComboBoxCellRenderer(){ setOpaque(true); }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Database database = (Database) value;
            setText(database.getName());
            return this;
        }
    }

}
