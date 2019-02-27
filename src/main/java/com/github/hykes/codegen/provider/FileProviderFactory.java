package com.github.hykes.codegen.provider;

import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * 内置的文件类型
     */
    private static Map<String, FileType> FILE_TYPES = new ConcurrentHashMap<>();
    static {
        FileTypeManager manager = FileTypeManager.getInstance();
        // 预制, 目测可以删除
        FILE_TYPES.put("java", manager.getStdFileType("JAVA"));
        FILE_TYPES.put("js", manager.getStdFileType("JavaScript"));
        FILE_TYPES.put("css", manager.getStdFileType("CSS"));
        FILE_TYPES.put("html", manager.getStdFileType("HTML"));
        FILE_TYPES.put("xhtml", manager.getStdFileType("XHTML"));
        FILE_TYPES.put("json", manager.getStdFileType("JSON"));
        FILE_TYPES.put("jsp", manager.getStdFileType("JSP"));
        FILE_TYPES.put("jspx", manager.getStdFileType("JSPX"));
        FILE_TYPES.put("xml", manager.getStdFileType("XML"));
        FILE_TYPES.put("dtd", manager.getStdFileType("DTD"));
        FILE_TYPES.put("properties", manager.getStdFileType("Properties"));
        FILE_TYPES.put("manifest", manager.getStdFileType("Manifest"));
        FILE_TYPES.put("txt", manager.getStdFileType("PLAIN_TEXT"));
        FILE_TYPES.put("sql", manager.getStdFileType("SQL"));
        FILE_TYPES.put("kt", manager.getStdFileType("Kotlin"));
    }

    /**
     * 根据文件后缀获取文件类型
     */
    public static FileType getFileType(String suffix) {
        if (StringUtils.isBlank(suffix)) {
            return PlainTextFileType.INSTANCE;
        }
        String extension = suffix.toLowerCase();
        FileType fileType = FILE_TYPES.get(extension);
        if (fileType == null) {
            for (FileType ft : FileTypeManager.getInstance().getRegisteredFileTypes()) {
                if (extension.equals(ft.getDefaultExtension())) {
                    fileType = ft;
                    FILE_TYPES.put(extension, ft);
                    break;
                }
            }
        }
        return StringUtils.nullOr(fileType, PlainTextFileType.INSTANCE);
    }

    public AbstractFileProvider getInstance(String type) {
        return new DefaultProviderImpl(project, outputPath, getFileType(type));
    }
}
