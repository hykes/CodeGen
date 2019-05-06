package com.github.hykes.codegen;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.java.generate.velocity.VelocityFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/19
 */
public class VelocityTest {

    @Test
    public void testSplitLowerCase() {
        VelocityEngine velocityEngine = VelocityFactory.getVelocityEngine();
        velocityEngine.loadDirective("com.github.hykes.codegen.directive.LowerCase");
        velocityEngine.loadDirective("com.github.hykes.codegen.directive.Split");
        String template = "#Split(\"#LowerCase(${NAME})\" '.')";
        Map<String, Object> map = new HashMap<>();
        map.put("NAME", "HykesIsStrong");

        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(new VelocityContext(map), writer, "", template);

        Assert.assertEquals(writer.toString(), "hykes.is.strong");
    }

}
