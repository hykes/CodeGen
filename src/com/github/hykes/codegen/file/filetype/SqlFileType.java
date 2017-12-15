package com.github.hykes.codegen.file.filetype;

import com.intellij.icons.AllIcons;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/21
 */
public class SqlFileType extends LanguageFileType {

    @NonNls
    public static final String DEFAULT_EXTENSION = "sql";
    @NonNls
    public static final String DOT_DEFAULT_EXTENSION = ".sql";
    public static final SqlFileType INSTANCE = new SqlFileType();

    private SqlFileType() {
        super(JavaLanguage.INSTANCE);
    }

    @NotNull
    public String getName() {
        return "SQL";
    }

    @NotNull
    public String getDescription() {
        return "Sql source files";
    }

    @NotNull
    public String getDefaultExtension() {
        return "sql";
    }

    public Icon getIcon() {
        return AllIcons.FileTypes.Any_type;
    }

}
