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

    private String driver;

    private String url;

    private String username;

    private String password;

}
