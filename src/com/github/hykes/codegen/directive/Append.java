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
 * 拼接元素
 * ${Append '#' 'ABC' '%'}  => #ABC%
 * @author: hehaiyang@terminus.io
 * @date: 2017/12/19
 */
public class Append extends Directive {
    @Override
    public String getName() {
        return "Append";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        String prefix = (String) node.jjtGetChild(0).value(context);
        String str = (String) node.jjtGetChild(1).value(context);
        String suffix = (String) node.jjtGetChild(2).value(context);
        writer.write(String.valueOf(prefix + str + suffix));
        return true;
    }
}
