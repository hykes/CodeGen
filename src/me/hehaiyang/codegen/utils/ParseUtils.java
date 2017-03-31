package me.hehaiyang.codegen.utils;

import me.hehaiyang.codegen.model.Field;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ParseUtils {

    public static List<Field> parseString(String paramsstr) {
        List<Field> fiellist=new ArrayList<Field>();

        String a =paramsstr;
        ByteArrayInputStream is=new ByteArrayInputStream(a.getBytes());
        BufferedReader br=new BufferedReader(new InputStreamReader(is));

        int totolline=0;
        String line;

        // 读取文件，并对读取到的文件进行操作
        try {
            while ((line = br.readLine()) != null) {
                totolline++;
                String[] data_temp=line.split("\\|");
                if(data_temp.length>=3) {
                    String field=Trim(data_temp[1]);
                    String type=Trim(data_temp[2]);
                    String comment=Trim(data_temp[3]);
                    Field entity=new Field();

                    entity.setColumn(field);
                    entity.setColumnType(type);
                    entity.setField(BuilderUtil.underlineToCamel(field));

                    if(type.toUpperCase().contains("INT")){
                        entity.setFieldType("Integer");
                    }
                    if(type.toUpperCase().contains("TINYINT")){
                        entity.setFieldType("Integer");
                    }
                    if(type.toUpperCase().contains("BIGINT")){
                        entity.setFieldType("Long");
                    }
                    if(type.toUpperCase().contains("VARCHAR")){
                        entity.setFieldType("String");
                    }
                    if(type.toUpperCase().contains("DATETIME")){
                        entity.setFieldType("Date");
                    }
                    entity.setComment(comment);
                    fiellist.add(entity);
                }
                else {
                    continue;
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {

                br.close();
                is.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if(totolline!=fiellist.size())
            fiellist=new ArrayList<>();
        return fiellist;
    }

    public static String Trim(String str) {
        if(!"".equals(str)) {
            String str2 = str.replaceAll("\\s*", "");
            return str2;
        }
        else
            return  "";

    }

}
