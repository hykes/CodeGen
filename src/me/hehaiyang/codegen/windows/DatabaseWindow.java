package me.hehaiyang.codegen.windows;

import javax.swing.*;

import java.util.List;
import java.sql.Types;
/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/23
 */
public class DatabaseWindow extends JFrame{

    public DatabaseWindow(){
        super();
        DBOperation op = new DBOperation();
        op.getConnection();
        List<String> list = op.getAllTables(op.getConnection());
        for(String str: list){
            System.out.println(str);
            op.getTableColumn(str, op.getConnection());
            break;
        }
    }

}
