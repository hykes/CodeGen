package com.github.hykes.codegen.provider.language;

import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * 默认的文件类型
 */
public class DefaultLanguage extends Language {

    private String id;
    private DefaultLanguage(String id) {
        super(id);
        this.id = id;
    }

    public static Language of(String id) {
        if (StringUtils.isBlank(id)) {
            id = PlainTextLanguage.INSTANCE.getID();
        }
        // 防止重复注册
        Language language = Language.findLanguageByID(id);
        if (language == null) {
            language = new DefaultLanguage(id);
        }
        return language;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return id;
    }

    @Override
    public boolean isCaseSensitive() {
        return true;
    }
}
