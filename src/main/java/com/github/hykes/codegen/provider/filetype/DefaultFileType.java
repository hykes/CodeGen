package com.github.hykes.codegen.provider.filetype;

import com.github.hykes.codegen.provider.language.DefaultLanguage;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * 默认的模板文件类型
 */
public class DefaultFileType extends LanguageFileType {

    private String type;
    private DefaultFileType(String type) {
        super(DefaultLanguage.of(type));
        this.type = type;
    }

    public static LanguageFileType of(String type) {
        if (StringUtils.isBlank(type)) {
            return PlainTextFileType.INSTANCE;
        }
        return new DefaultFileType(type);
    }

    @Override
    @NotNull
    public String getName() {
        return type;
    }

    @Override
    @NotNull
    public String getDescription() {
        return type;
    }

    @Override
    @NotNull
    public String getDefaultExtension() {
        return type.toLowerCase();
    }

    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Any_type;
    }
}
