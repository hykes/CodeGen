package me.hehaiyang.codegen.model;

import com.intellij.openapi.project.Project;
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
public class IdeaContext implements Serializable {

    private static final long serialVersionUID = -3766582517674940760L;

    private Project project;
}
