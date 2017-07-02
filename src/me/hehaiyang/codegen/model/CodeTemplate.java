package me.hehaiyang.codegen.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class CodeTemplate implements Serializable {

    private static final long serialVersionUID = -219970561109552275L;

    public CodeTemplate(String id, String display, String extension, String filename, String template){
        this.id = id;
        this.display = display;
        this.extension = extension;
        this.filename = filename;
        this.template = template;

    }

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
