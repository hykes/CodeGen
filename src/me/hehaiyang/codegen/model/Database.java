package me.hehaiyang.codegen.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Desc: 数据库信息
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/31
 */
@Data
public class Database implements Serializable {

    private static final long serialVersionUID = -6611073974504244920L;


    private String name;

    private String driver = "com.mysql.jdbc.Driver";

    private String url = "jdbc:mysql://127.0.0.1:3306/hsh";

    private String username;

    private String password;

}
