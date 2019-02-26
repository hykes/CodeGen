package com.github.hykes.codegen.utils;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.jetbrains.java.generate.velocity.VelocityFactory;

import java.io.StringWriter;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/24
 */
public class VelocityUtil {

    protected final static VelocityEngine VELOCITY_ENGINE = VelocityFactory.getVelocityEngine();

    static {
        /*
          IDEA 的 URLClassLoader 无法获取当前插件的 path
          @see org.apache.velocity.util.ClassUtils
         */
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(VelocityUtil.class.getClassLoader());
        VELOCITY_ENGINE.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.LowerCase");
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.UpperCase");
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.Append");
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.Split");
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.ImportPackage");
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.GetPackage");
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    public static String evaluate(Context context, String inputStr) {
        StringWriter writer = new StringWriter();
        VELOCITY_ENGINE.evaluate(context, writer, "", inputStr);
        return writer.toString();
    }

}
