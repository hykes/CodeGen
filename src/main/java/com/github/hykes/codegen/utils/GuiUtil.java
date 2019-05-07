package com.github.hykes.codegen.utils;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.constants.Defaults;
import com.github.hykes.codegen.gui.SelectGroupPanel;
import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeGroup;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.provider.FileProviderFactory;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Desc: GUI utils
 */
public class GuiUtil {

    /**
     * Get all checkbox values
     */
    public static List<String> getAllJCheckBoxValue(Container ct) {
        return getAllJCheckBoxValue(ct, null);
    }

    private static List<String> getAllJCheckBoxValue(Container ct, List<String> selectGroups) {
        if (selectGroups == null) {
            selectGroups = new ArrayList<>();
        }
        int count = ct.getComponentCount();
        for (int i = 0; i < count; i++) {
            Component c = ct.getComponent(i);
            if (c instanceof JCheckBox && ((JCheckBox) c).isSelected()) {
                selectGroups.add(c.getName());
            } else if (c instanceof Container) {
                getAllJCheckBoxValue((Container) c, selectGroups);
            }
        }
        return selectGroups;
    }

    /**
     * 生成模板文件
     *
     * @param groupPathMap 如 {@link SelectGroupPanel#getGroupPathMap()}
     */
    public static void generateFile(IdeaContext ideaContext, List<CodeContext> codeContexts, Map<String, String> groupPathMap) {
        Map<String, Object> params = new HashMap<>();
        params.putAll(Defaults.getDefaultVariables());
        params.putAll(SettingManager.getInstance().getVariables().getParams());
        params.put("Project", ideaContext.getProject().getName());

        final List<CodeGroup> groupList = new ArrayList<>();
        SettingManager.getInstance().getTemplates().getRoots().forEach(it -> groupList.addAll(it.getGroups()));

        final List<CodeGroup> genGroups = groupList.stream().filter(it -> groupPathMap.containsKey(it.getId())).sorted(new ComparatorUtil()).collect(Collectors.toList());
        ProgressManager.getInstance().run(new Task.Backgroundable(ideaContext.getProject(), "CodeGen Progress ..."){
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                // Set the progress bar percentage and text
                progressIndicator.setFraction(0D);
                progressIndicator.setText("start to generator code ...");

                // start your process
                for (CodeGroup group : genGroups) {

                    // process running ..
                    progressIndicator.setFraction(1/genGroups.size());

                    String outputPath = groupPathMap.get(group.getId());
                    if (StringUtils.isNotEmpty(outputPath)) {
                        for (CodeContext context : codeContexts) {
                            List<CodeTemplate> codeTemplates = group.getTemplates();
                            codeTemplates.sort(Comparator.comparing(CodeTemplate::getOrder));
                            for (CodeTemplate codeTemplate : codeTemplates) {
                                progressIndicator.setText(String.format("generator template %s ...", codeTemplate.getDisplay()));

                                FileProviderFactory fileFactory = new FileProviderFactory(ideaContext.getProject(), outputPath);
                                fileFactory.getInstance(codeTemplate.getExtension()).create(codeTemplate, context, params);
                            }
                        }
                    }
                }
                // Finished
                progressIndicator.setFraction(1D);
                progressIndicator.setText("finished");
            }
        });
    }

    /**
     * 模版组优先级排序
     */
    static class ComparatorUtil implements Comparator<CodeGroup> {

        @Override
        public int compare(CodeGroup o1, CodeGroup o2) {
            double level1 = o1.getLevel();
            double level2 = o2.getLevel();
            if (level1 > level2) {
                return 1;
            } else if (level1 < level2) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
