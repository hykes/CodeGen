package me.hehaiyang.codegen.utils;

import me.hehaiyang.codegen.model.Field;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.MessageDigest;
import java.util.*;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 16/4/21
 */
public class BuilderUtil {

    public static final char UNDERLINE = '_';

    /**
     * 创建文件目录
     * @param directory
     */
    public static void mkdirs(String directory){
        File file = new File(directory);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 创建文件
     * @param filePath
     * @param context
     * @throws IOException
     */
    public static void createFile(String filePath, String context) throws IOException{
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(context);
            bw.close();
        }
    }

    /**
     * Bean --> Map
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map(Object obj) {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    /**
     * 下划线字符串修改为驼峰命名
     */
    public static String underlineToCamel(String param) {
        return underlineToCamel(param, false);
    }

    /**
     * 下划线字符串修改为驼峰命名
     * @param firstUpper 首字符是否大写
     */
    public static String underlineToCamel(String param, boolean firstUpper) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                if (i == 0 && firstUpper) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰名称修改为下划线分隔的字符串
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 计算默认的 serialVersionUID
     *
     * @see java.io.ObjectStreamClass#lookup(Class)
     * @see java.io.ObjectStreamClass#computeDefaultSUID(Class)
     */
    public static long computeDefaultSUID(String className, List<Field> fields) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);

            // simple class name
            dout.writeUTF(className);
            int classMods = Modifier.PUBLIC & (Modifier.PUBLIC | Modifier.FINAL | Modifier.INTERFACE | Modifier.ABSTRACT);
            dout.writeInt(classMods);

            // interface name
            dout.writeUTF("java.io.Serializable");

            // fields
            // fields.sort(Comparator.comparing(Field::getField));
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                int mods = Modifier.PRIVATE &
                        (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED |
                                Modifier.STATIC | Modifier.FINAL | Modifier.VOLATILE |
                                Modifier.TRANSIENT);
                if (((mods & Modifier.PRIVATE) == 0) ||
                        ((mods & (Modifier.STATIC | Modifier.TRANSIENT)) == 0)) {
                    dout.writeUTF(field.getField());
                    dout.writeInt(mods);
                    dout.writeUTF(field.getFieldType());
                }
            }

            // method ignore
            //
            dout.flush();

            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hashBytes = md.digest(bout.toByteArray());
            long hash = 0;
            for (int i = Math.min(hashBytes.length, 8) - 1; i >= 0; i--) {
                hash = (hash << 8) | (hashBytes[i] & 0xFF);
            }
            return hash;
        } catch (Exception e) {
            // ignore
        }
        return 1;
    }
}
