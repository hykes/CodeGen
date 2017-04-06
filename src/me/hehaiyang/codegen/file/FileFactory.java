package me.hehaiyang.codegen.file;

import com.intellij.openapi.actionSystem.AnActionEvent;
import me.hehaiyang.codegen.file.impl.JavaProviderImpl;
import me.hehaiyang.codegen.file.impl.SqlProviderImpl;
import me.hehaiyang.codegen.file.impl.XmlProviderImpl;

public class FileFactory {

    public AnActionEvent anActionEvent;

    public FileFactory(AnActionEvent anActionEvent) {
       this.anActionEvent = anActionEvent;
    }

    public FileProvider getInstance(String type) {

        if(type.equals("java")) {
            return new JavaProviderImpl(anActionEvent);
        } else if(type.equals("sql")) {
            return new SqlProviderImpl(anActionEvent);
        } else if(type.equals("xml")) {
            return new XmlProviderImpl(anActionEvent);
        }else{
            return null;
        }
    }
}