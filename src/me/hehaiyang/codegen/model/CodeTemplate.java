package me.hehaiyang.codegen.model;

import lombok.*;

import java.io.Serializable;

/**
 * Desc: 模版数据
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/7
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CodeTemplate implements Serializable {

    private static final long serialVersionUID = -329690965910519848L;

    /**
     * 模版ID，取 UUID 值
     */
    private String id;

    /**
     * 模版名称
     */
    private String display;

    /**
     * 扩展名
     */
    private String extension;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 模版内容
     */
    private String template;

    /**
     * 子目录
     */
    private String subPath;

    /**
     * 是否是静态资源
     */
    private Boolean isResources;

}
