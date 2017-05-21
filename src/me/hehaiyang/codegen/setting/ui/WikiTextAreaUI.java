package me.hehaiyang.codegen.setting.ui;

import me.hehaiyang.codegen.utils.ParseUtils;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Desc: Wiki
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/6
 */
public class WikiTextAreaUI extends JTextArea {

    public WikiTextAreaUI() {
        super();
        String wiki;
        try {
            InputStream template = getClass().getResourceAsStream("/wiki/README.md");
            wiki = ParseUtils.stream2String(template);
        }catch (IOException ioe){
            wiki = "IOException";
        }
        setText(wiki);
        setEditable(false);
    }

}
