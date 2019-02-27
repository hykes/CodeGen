package com.github.hykes.codegen.provider;

import com.github.hykes.codegen.provider.filetype.DefaultFileType;
import com.github.hykes.codegen.provider.filetype.KotlinFileType;
import com.github.hykes.codegen.provider.filetype.MdFileType;
import com.github.hykes.codegen.provider.filetype.SqlFileType;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件提供者工厂
 * @author hehaiyangwork@gmail.com
 * @date 2017/3/17
 */
public class FileProviderFactory {

    private final Project project;
    private final String outputPath;
    public FileProviderFactory(Project project, String outputPath) {
        this.project = project;
        this.outputPath = outputPath;
    }

    private static Map<String, LanguageFileType> FILE_TYPES = new HashMap<>();
    static {
        FILE_TYPES.put("java", JavaFileType.INSTANCE);
        FILE_TYPES.put("kt", KotlinFileType.INSTANCE);
        FILE_TYPES.put("sql", SqlFileType.INSTANCE);
        FILE_TYPES.put("xml", XmlFileType.INSTANCE);
        FILE_TYPES.put("md", MdFileType.INSTANCE);
    }

    public AbstractFileProvider getInstance(String type) {
        type = StringUtils.trimObject(type).toLowerCase();
        LanguageFileType fileType = ObjectUtils.notNull(FILE_TYPES.get(type), DefaultFileType.of(type));
        return new DefaultProviderImpl(project, outputPath, fileType);
    }
}
