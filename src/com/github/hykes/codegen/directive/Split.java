package com.github.hykes.codegen.directive;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;

/**
 * 驼峰转拼接字符
 * ${Split 'ABcD' '_'} => A_bc_d
 * @author: hehaiyang@terminus.io
 * @date: 2017/12/19
 */
public class Split extends Directive {
    @Override
    public String getName() {
        return "Split";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        String value = (String) node.jjtGetChild(0).value(context);
        String character = (String) node.jjtGetChild(1).value(context);
        int len = value.length();
        StringBuilder sb = new StringBuilder(len);
        sb.append(value.charAt(0));
        for (int i = 1; i < len; i++) {
            char c = value.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(character).append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        writer.write(sb.toString());
        return true;
    }
}
