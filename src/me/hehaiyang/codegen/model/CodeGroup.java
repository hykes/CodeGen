package me.hehaiyang.codegen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import java.util.List;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/6/6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeGroup implements Serializable {

    private static final long serialVersionUID = -8843957173498883576L;

    /**
     * 分组ID
     */
    private String id;

    /**
     * 分组名称
     */
    private String name;

    private List<CodeTemplate> templates;

}
