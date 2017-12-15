package me.hehaiyang.codegen.windows;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import me.hehaiyang.codegen.config.SettingManager;
import me.hehaiyang.codegen.model.Database;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.model.IdeaContext;
import me.hehaiyang.codegen.model.Table;
import me.hehaiyang.codegen.parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/4/23
 */
public class DBWindow extends BaseWindow {

    private final String type = "DB";

    public DBWindow(IdeaContext ideaContext) {
        super();
        setTitle("CodeGen-Create by DB");
        setLayout(new BorderLayout());
        JFrame thisFrame = this;
        SettingManager settingManager = SettingManager.getInstance();

        add(generationTypePanel(type), BorderLayout.NORTH);

        ComboBox databaseBox = new ComboBox();
        JButton connectBtn = new JButton("Connect");
        ComboBox tableBox = new ComboBox();
        JButton sureBtn = new JButton("Sure");

        List<Database> databases = settingManager.getDatabasesSetting().getDatabases();
        if (databases != null && !databases.isEmpty()) {
            databaseBox.setRenderer(new ComboBoxCellRenderer());
            databases.forEach(it -> databaseBox.addItem(it));
        } else {
            databaseBox.addItem("无数据源");
            databaseBox.setEnabled(false);
            connectBtn.setEnabled(false);
        }

        tableBox.addItem("请选择");
        tableBox.setEnabled(false);
        sureBtn.setEnabled(false);

        connectBtn.addActionListener(it -> {

            Database database = (Database) databaseBox.getSelectedItem();
            if (database != null) {
                Parser parser = Parser.fromType(database.getType());
                Connection conn = parser.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
                try {
                    if (conn != null && !conn.isClosed()) {
                        List<String> list = parser.getTables(conn);
                        tableBox.removeAllItems();
                        for (String str : list) {
                            tableBox.addItem(str);
                        }
                        tableBox.setEnabled(true);
                        sureBtn.setEnabled(true);
                    } else {
                        Messages.showInfoMessage(thisFrame, "connect fail !", "Error");
                    }
                } catch (SQLException e) {
                    Messages.showInfoMessage(thisFrame, "connect fail !", "Error");
                } finally {
                    try {
                        if (conn != null) conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sureBtn.addActionListener(it -> {
            Database database = (Database) databaseBox.getSelectedItem();
            if (database != null) {
                String tableName = tableBox.getSelectedItem().toString();
                Parser parser = Parser.fromType(database.getType());
                Connection conn = parser.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
                try {
                    if (conn != null && !conn.isClosed()) {
                        List<Field> fields = parser.getColumns(tableName, conn);
                        ColumnEditorFrame frame = new ColumnEditorFrame(ideaContext, new Table(tableName, fields));
                        frame.setSize(800, 400);
                        frame.setAlwaysOnTop(false);
                        frame.setLocationRelativeTo(thisFrame);
                        frame.setVisible(true);
                        frame.setResizable(false);
                        dispose();
                    }
                } catch (SQLException e) {
                    Messages.showInfoMessage(thisFrame, "get columns fail !", "Error");
                } finally {
                    try {
                        if (conn != null) conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

        sqlRadio.addActionListener(e -> {
            if(!type.equals("SQL")){
                JFrame startFrame = new SQLWindow(ideaContext);
                startFrame.setSize(500, 350);
                startFrame.setResizable(false);
                startFrame.setAlwaysOnTop(true);
                startFrame.setLocationRelativeTo(null);
                startFrame.setVisible(true);
                dispose();
            }
        });
        dbRadio.addActionListener(e -> {
            if(!type.equals("DB")){
                JFrame startFrame = new DBWindow(ideaContext);
                startFrame.setSize(350, 180);
                startFrame.setResizable(false);
                startFrame.setAlwaysOnTop(true);
                startFrame.setLocationRelativeTo(null);
                startFrame.setVisible(true);
                dispose();
            }
        });
    }

    public class ComboBoxCellRenderer extends JLabel implements ListCellRenderer {

        ComboBoxCellRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Database database = (Database) value;
            setText(database.getName());
            return this;
        }
    }

}
