package me.hehaiyang.codegen.file;

import com.intellij.ide.IdeView;
import com.intellij.openapi.project.Project;
import me.hehaiyang.codegen.file.impl.JavaProviderImpl;
import me.hehaiyang.codegen.file.impl.SqlProviderImpl;
import me.hehaiyang.codegen.file.impl.XmlProviderImpl;

public class FileFactory {

    private Project project;

    private IdeView ideView;

    public FileFactory(Project project, IdeView ideView) {
        this.project = project;
        this.ideView = ideView;
    }

    public FileProvider getInstance(String type) {

        if("java".equals(type)) {
            return new JavaProviderImpl(project, ideView);
        } else if("sql".equals(type)) {
            return new SqlProviderImpl(project, ideView);
        } else if("xml".equals(type)) {
            return new XmlProviderImpl(project, ideView);
        }else{
            return new JavaProviderImpl(project, ideView);
        }
    }
}