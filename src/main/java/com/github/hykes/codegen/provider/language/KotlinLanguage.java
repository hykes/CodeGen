package com.github.hykes.codegen.provider.language;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

/**
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/5/26
 */
public class KotlinLanguage extends Language {
    public static final KotlinLanguage INSTANCE = new KotlinLanguage();

    private KotlinLanguage() {
        super("Kotlin");
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Kotlin";
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }
}
