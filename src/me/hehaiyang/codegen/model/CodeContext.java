package me.hehaiyang.codegen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 17/3/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeContext implements Serializable {

    private static final long serialVersionUID = 6235968905610310027L;

    private String model;

    private String modelName;

    private String table;

    private String tableName;

    private List<Field> fields;

    private Map<String, String> $;

    public Map<String, String> get$() {
        return $;
    }

    public void set$(Map<String, String> $) {
        this.$ = $;
    }
}
