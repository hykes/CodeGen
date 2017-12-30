package com.github.hykes.codegen.utils;

import com.github.hykes.codegen.model.CodeGroup;
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

    public static void export(List<ExportTemplate> files, String targetPath) {
        try {
            byte[] buffer = new byte[4096];
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetPath));

            for(ExportTemplate file: files) {
                ByteArrayInputStream fis = file.getInputStream();
                out.putNextEntry(new ZipEntry(file.getName()));
                int len;
                while((len = fis.read(buffer))>0) {
                    out.write(buffer,0,len);
                }
                out.closeEntry();
                fis.close();
            }
            out.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void readZipFile(String file,  List<CodeTemplate> templates, List<CodeGroup> groups) throws Exception {
        ZipFile zf = new ZipFile(file);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;

        while ((ze = zin.getNextEntry()) != null) {
            if (!ze.isDirectory() && ze.getName().endsWith(".vm")) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(zf.getInputStream(ze)));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                Pattern pattern = Pattern.compile("(?<=#\\*)(.+?)(?=\\*#)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher matcher = pattern.matcher(sb.toString());
                Map<String, String> infoMap = null;
                while(matcher.find()) {
                    infoMap = getInfoMap(matcher.group());
                }
                if (infoMap == null ) {
                    infoMap = new HashMap<>();
                }
                CodeTemplate codeTemplate = new CodeTemplate();
                codeTemplate.setId(UUID.randomUUID().toString());
                codeTemplate.setGroup(infoMap.containsKey("group") ? infoMap.get("group") : null);
                codeTemplate.setDisplay(infoMap.containsKey("display") ? infoMap.get("display") : null);
                codeTemplate.setExtension(infoMap.containsKey("extension") ? infoMap.get("extension") : null);
                codeTemplate.setFilename(infoMap.containsKey("filename") ? infoMap.get("filename") : null);
                codeTemplate.setSubPath(infoMap.containsKey("subPath") ? infoMap.get("subPath") : null);
                codeTemplate.setResources(infoMap.containsKey("isResource") ? Boolean.valueOf(infoMap.get("isResource")) : null);
                String result = matcher.replaceAll("").replace("#**#", "");
                codeTemplate.setTemplate(result);
                templates.add(codeTemplate);

                if (infoMap.containsKey("group")) {
                    CodeGroup codeGroup = new CodeGroup();
                    codeGroup.setId(UUID.randomUUID().toString());
                    codeGroup.setLevel(1);
                    codeGroup.setName(infoMap.get("group"));
                    codeGroup.setTemplates(new ArrayList<>());
                    groups.add(codeGroup);
                }
            }
        }
        zin.closeEntry();
    }

    private static Map<String, String> getInfoMap(String str){
        Map<String, String> result = new HashMap<>();
        String attr[] = str.trim().split(";");
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
        files.add(new ExportTemplate("item.log", new ByteArrayInputStream("sss".getBytes())));
        files.add(new ExportTemplate("bids.log", new ByteArrayInputStream("ssssss".getBytes())));
        export(files, targetPath);
        System.out.println("生成Demo.zip成功");
    }
}
