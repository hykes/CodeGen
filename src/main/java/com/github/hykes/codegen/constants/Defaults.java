package com.github.hykes.codegen.constants;

import com.github.hykes.codegen.model.CodeGroup;
import com.github.hykes.codegen.model.CodeRoot;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.io.FileUtil;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.swing.*;
import java.util.*;

/**
 * 内置参数
 * @author hehaiyangwork@gmail.com
 * @date 2017/4/16
 */
public class Defaults {
    private static final Logger LOGGER = Logger.getInstance(Defaults.class);

    /**
     * 项目图片
     */
    public static final Icon CODEGEN = IconLoader.findIcon("/icons/codegen.png", Defaults.class.getClassLoader());

    public static Map<String, String> getDefaultVariables() {
        Map<String, String> context = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        context.put("YEAR", String.valueOf(calendar.get(Calendar.YEAR)));
        context.put("MONTH", String.valueOf(calendar.get(Calendar.MONTH) + 1));
        context.put("DAY", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        context.put("DATE", DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd"));
        context.put("TIME", DateFormatUtils.format(calendar.getTime(), "HH:mm:ss"));
        context.put("NOW", DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"));
        context.put("USER", System.getProperty("user.name"));
        return context;
    }

    /**
     * 获取默认的模板
     */
    public static List<CodeRoot> getDefaultTemplates(){
        // 设置默认分组和模板
        List<CodeGroup> groups = new ArrayList<>();
        try {
            List<CodeTemplate> templates1 = new ArrayList<>();
            templates1.add(new CodeTemplate(UUID.randomUUID().toString(), "Model", "java", "${model}", FileUtil.loadTextAndClose(Defaults.class.getResourceAsStream("/templates/ModelTemplate.vm")), "model", false));
            templates1.add(new CodeTemplate(UUID.randomUUID().toString(), "ReadService", "java", "${model}ReadService", FileUtil.loadTextAndClose(Defaults.class.getResourceAsStream("/templates/ReadServiceTemplate.vm")), "service", false));
            templates1.add(new CodeTemplate(UUID.randomUUID().toString(), "WriteService", "java", "${model}WriteService", FileUtil.loadTextAndClose(Defaults.class.getResourceAsStream("/templates/WriteServiceTemplate.vm")), "service", false));
            CodeGroup modelGroup = new CodeGroup(UUID.randomUUID().toString(), "model-service", 1, templates1);
            groups.add(modelGroup);

            List<CodeTemplate> templates2 = new ArrayList<>();
            templates2.add(new CodeTemplate(UUID.randomUUID().toString(), "Dao", "java", "${model}Dao", FileUtil.loadTextAndClose(Defaults.class.getResourceAsStream("/templates/DaoTemplate.vm")), "dao", false));
            templates2.add(new CodeTemplate(UUID.randomUUID().toString(), "ReadService", "java", "${model}ReadServiceImpl", FileUtil.loadTextAndClose(Defaults.class.getResourceAsStream("/templates/ReadServiceImplTemplate.vm")), "service", false));
            templates2.add(new CodeTemplate(UUID.randomUUID().toString(), "WriteService", "java", "${model}WriteServiceImpl", FileUtil.loadTextAndClose(Defaults.class.getResourceAsStream("/templates/WriteServiceImplTemplate.vm")), "service", false));
            templates2.add(new CodeTemplate(UUID.randomUUID().toString(), "Mapper", "xml", "${model}Mapper", FileUtil.loadTextAndClose(Defaults.class.getResourceAsStream("/templates/MapperTemplate.vm")), "mapper", true));
            CodeGroup implGroup = new CodeGroup(UUID.randomUUID().toString(), "service-impl-dao", 2,  templates2);
            groups.add(implGroup);

            List<CodeTemplate> templates3 = new ArrayList<>();
            templates3.add(new CodeTemplate(UUID.randomUUID().toString(), "Controller", "java", "${model}s", FileUtil.loadTextAndClose(Defaults.class.getResourceAsStream("/templates/ControllerTemplate.vm")), "front", false));
            CodeGroup apiGroup = new CodeGroup(UUID.randomUUID().toString(), "api", 2,  templates3);
            groups.add(apiGroup);
        } catch (Exception e){
            LOGGER.error(StringUtils.getStackTraceAsString(e));
        }
        // 设置默认根, 名称为root
        List<CodeRoot> roots = new ArrayList<>();
        roots.add(new CodeRoot(UUID.randomUUID().toString(), "example", groups));
        return roots;
    }

}
