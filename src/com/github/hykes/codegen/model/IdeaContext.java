package com.github.hykes.codegen.model;

import com.intellij.openapi.project.Project;

import java.io.Serializable;

/**
 *
 * @author : hehaiyangwork@qq.com
 * @date : 17/3/21
 */
public class IdeaContext implements Serializable {

    private static final long serialVersionUID = -3766582517674940760L;

    public IdeaContext() {
    }

    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
