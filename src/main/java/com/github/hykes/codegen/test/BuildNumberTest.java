package com.github.hykes.codegen.test;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.util.BuildNumber;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/22
 */
public class BuildNumberTest {

    public static void main(String[] args) {
        BuildNumber number = ApplicationInfo.getInstance().getBuild();
        // IU-171.4249.39
        System.out.println(number.asString());
        // IU
        System.out.println(number.getProductCode());
        // 171
        System.out.println(number.getBaselineVersion());
        // 171.4249.39
        System.out.println(number.asStringWithoutProductCode());
        System.out.println(number.asStringWithoutProductCodeAndSnapshot());
        // false
        System.out.println(number.isSnapshot());
    }
}
