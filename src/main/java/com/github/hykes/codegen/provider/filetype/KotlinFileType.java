package com.github.hykes.codegen.provider.filetype;

import com.github.hykes.codegen.provider.language.KotlinLanguage;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class KotlinFileType extends LanguageFileType {

    @NonNls
    public static final String DEFAULT_EXTENSION = "kt";
    @NonNls
    public static final String DOT_DEFAULT_EXTENSION = ".kt";
    public static final KotlinFileType INSTANCE = new KotlinFileType();

    private KotlinFileType() {
        super(KotlinLanguage.INSTANCE);
    }

    @Override
    @NotNull
    public String getName() {
        return "Kotlin";
    }

    @Override
    @NotNull
    public String getDescription() {
        return "Kotlin files";
    }

    @Override
    @NotNull
    public String getDefaultExtension() {
        return "kt";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Any_type;
    }

}
