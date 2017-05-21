package me.hehaiyang.codegen.model;

import com.intellij.openapi.util.text.StringUtil;
import lombok.*;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;

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

    private static final long serialVersionUID = -5333282359902221032L;

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
     * 文件名称
     */
    private String filename;

    /**
     * 扩展名
     */
    private String extension;

    /**
     * 模版内容
     */
    private String template;

    public boolean isValid() {
        return StringUtil.isNotEmpty(display) && StringUtil.isNotEmpty(extension)
                && StringUtil.isNotEmpty(filename);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CodeTemplate that = (CodeTemplate) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (display != null ? !display.equals(that.display) : that.display != null) return false;
        if (filename != null ? !filename.equals(that.filename) : that.filename != null) return false;
        if (extension != null ? !extension.equals(that.extension) : that.extension != null) return false;
        return template != null ? template.equals(that.template) : that.template == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (display != null ? display.hashCode() : 0);
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        result = 31 * result + (template != null ? template.hashCode() : 0);
        return result;
    }
}
