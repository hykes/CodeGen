package com.github.hykes.codegen.provider.filetype;

import com.intellij.icons.AllIcons;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Desc:
 * Mail: hehaiyangwork@gmail.com
 * Date: 2018/01/17
 */
public class MdFileType extends LanguageFileType {

    @NonNls
    public static final String DEFAULT_EXTENSION = "md";
    @NonNls
    public static final String DOT_DEFAULT_EXTENSION = ".md";

    public static final MdFileType INSTANCE = new MdFileType();

    private MdFileType() {
        super(XMLLanguage.INSTANCE);
    }

    @Override
    @NotNull
    public String getName() {
        return "Md";
    }

    @Override
    @NotNull
    public String getDescription() {
        return "Md source files";
    }

    @Override
    @NotNull
    public String getDefaultExtension() {
        return "md";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Any_type;
    }

}
