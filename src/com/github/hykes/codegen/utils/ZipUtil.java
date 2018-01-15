package com.github.hykes.codegen.utils;

import com.github.hykes.codegen.model.CodeGroup;
import com.github.hykes.codegen.model.CodeRoot;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.model.ExportTemplate;
import com.intellij.openapi.diagnostic.Logger;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/30
 */
public class ZipUtil {

    private static final Logger LOGGER = Logger.getInstance(ZipUtil.class);

    private static final Pattern PATTERN = Pattern.compile("(?<=#\\*)(.+?)(?=\\*#)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static void export(List<ExportTemplate> files, String targetPath) {
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetPath));
            for(ExportTemplate file: files) {
                out.putNextEntry(new ZipEntry(file.getName()));
                out.write(file.getBytes());
                out.closeEntry();
            }
            out.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void readZipFile(String file, List<CodeTemplate> templates, List<CodeGroup> groups, List<CodeRoot> roots) throws Exception {
        ZipFile zf = new ZipFile(file);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;

        List<String> rootNames = new ArrayList<>();
        List<String> groupNames = new ArrayList<>();
        while ((ze = zin.getNextEntry()) != null) {
            if (!ze.isDirectory() && ze.getName().endsWith(".vm")) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(zf.getInputStream(ze)));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                Matcher matcher = PATTERN.matcher(sb.toString());
                Map<String, String> infoMap = null;
                while(matcher.find()) {
                    infoMap = getInfoMap(matcher.group());
                }
                if (infoMap != null ) {
                    CodeTemplate codeTemplate = new CodeTemplate();
                    codeTemplate.setId(UUID.randomUUID().toString());
                    codeTemplate.setGroup(infoMap.containsKey("group") ? infoMap.get("group") : null);
                    codeTemplate.setDisplay(infoMap.containsKey("display") ? infoMap.get("display") : null);
                    codeTemplate.setExtension(infoMap.containsKey("extension") ? infoMap.get("extension") : null);
                    codeTemplate.setFilename(infoMap.containsKey("filename") ? infoMap.get("filename") : null);
                    codeTemplate.setSubPath(infoMap.containsKey("subPath") ? infoMap.get("subPath") : null);
                    codeTemplate.setResources(infoMap.containsKey("isResources") ? Boolean.valueOf(infoMap.get("isResources")) : null);
                    String result = matcher.replaceAll("").replace("#**#", "");
                    codeTemplate.setTemplate(result);
                    templates.add(codeTemplate);

                    if (infoMap.containsKey("group")) {
                        if (!groupNames.contains(infoMap.get("group"))) {
                            groupNames.add(infoMap.get("group"));
                            CodeGroup codeGroup = new CodeGroup();
                            codeGroup.setId(UUID.randomUUID().toString());
                            codeGroup.setLevel(Integer.valueOf(infoMap.get("level")));
                            codeGroup.setName(infoMap.get("group"));
                            codeGroup.setTemplates(new ArrayList<>());
                            codeGroup.setRoot(infoMap.containsKey("root") ? infoMap.get("root") : null);
                            groups.add(codeGroup);
                        }
                    }

                    if (infoMap.containsKey("root")) {
                        if (!rootNames.contains(infoMap.get("root"))) {
                            rootNames.add(infoMap.get("root"));
                            CodeRoot codeRoot = new CodeRoot();
                            codeRoot.setId(UUID.randomUUID().toString());
                            codeRoot.setName(infoMap.get("root"));
                            codeRoot.setGroups(new ArrayList<>());
                            roots.add(codeRoot);
                        }
                    }
                }
            }
        }
        zin.closeEntry();
    }

    private static Map<String, String> getInfoMap(String str){
        Map<String, String> result = new HashMap<>();
        String[] attr = str.trim().split(";");
        for(String s: attr){
            int pos = s.indexOf(":");
            if (pos == -1) {
                break;
            }
            String key = s.substring(0, pos).trim();
            String value = s.substring(pos+1).trim();
            result.put(key, value);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String targetPath = "Demo.zip";
        List<ExportTemplate> files = new ArrayList<>();
        files.add(new ExportTemplate("item.log", "sss".getBytes()));
        files.add(new ExportTemplate("bids.log", "sss".getBytes()));
        export(files, targetPath);
        System.out.println("生成Demo.zip成功");
    }
}
