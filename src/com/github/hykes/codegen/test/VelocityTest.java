package com.github.hykes.codegen.test;

import com.github.hykes.codegen.gui.SqlEditorPanel;
import com.google.common.collect.Maps;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.java.generate.velocity.VelocityFactory;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/19
 */
public class VelocityTest {

    private static final Logger LOGGER = Logger.getInstance(SqlEditorPanel.class);

    public static void main(String[] args) {
        VelocityEngine velocityEngine = VelocityFactory.getVelocityEngine();
        velocityEngine.loadDirective("com.github.hykes.codegen.directive.LowerCase");
        velocityEngine.loadDirective("com.github.hykes.codegen.directive.Split");
        String template = "#Split(\"#LowerCase(${NAME})\" '.')";
//        String template = "#LowerCase(${NAME})";
        Map<String, Object> map = Maps.newHashMap();
        map.put("NAME", "HykesIsStrong");

        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(new VelocityContext(map), writer, "", template);

        System.out.println(writer.toString());
    }

    private static void reformatJavaDoc(PsiElement theElement) {
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(theElement.getProject());
        try {
//            int javadocTextOffset = findJavaDocTextOffset(theElement);
//            int javaCodeTextOffset = findJavaCodeTextOffset(theElement);
//            codeStyleManager.reformatText(theElement.getContainingFile(), javadocTextOffset, javaCodeTextOffset + 1);
        } catch (Exception e) {
            LOGGER.error("reformat code failed", e);
        }
    }
}
