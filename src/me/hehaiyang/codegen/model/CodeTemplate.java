package me.hehaiyang.codegen.model;

import com.intellij.openapi.util.text.StringUtil;
import lombok.*;

import java.io.Serializable;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/7
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CodeTemplate implements Serializable {

    private static final long serialVersionUID = -7427380135917659629L;

    private String id;

    private String name;

    private String type;

    private String fileName;

    private String template;

    public boolean isValid() {
        return StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(type)
                && StringUtil.isNotEmpty(fileName);
    }
}
