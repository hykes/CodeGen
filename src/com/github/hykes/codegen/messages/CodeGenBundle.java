package com.github.hykes.codegen.messages;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class CodeGenBundle extends AbstractBundle {

    private static final String BUNDLE = "messages";
    private static final CodeGenBundle INSTANCE = new CodeGenBundle();

    private CodeGenBundle() {
        super(BUNDLE);
    }

    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object... params) {
        return INSTANCE.getMessage(key, params);
    }

}
