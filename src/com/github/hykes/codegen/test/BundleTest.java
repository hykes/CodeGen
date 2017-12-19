package com.github.hykes.codegen.test;

import com.github.hykes.codegen.configurable.SettingManager;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author: hehaiyang@terminus.io
 * @date: 2017/12/19
 */
public class BundleTest {

    public static void main(String[] args) {
        SettingManager settingManager = SettingManager.getInstance();

//        Locale locale = new Locale("zh", "CN");
        Locale locale = new Locale("en", "US");
//        Locale locale = Locale.getDefault();

        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

    }
}
