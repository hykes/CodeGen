package me.hehaiyang.codegen.windows;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/3/19
 */
public class MapperTemplate {

    public static String mapper ="<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
            "\n" +
            "<mapper namespace=\"{{modelName}}\">\n" +
            "    <resultMap id=\"{{modelName}}Map\" type=\"{{modelName}}\">\n" +
            "        <id property=\"id\" column=\"id\"/>{{#each fields}}\n" +
            "        <result property=\"{{field}}\" column=\"{{column}}\"/>{{/each}}\n" +
            "        <result property=\"createdAt\" column=\"created_at\"/>\n" +
            "        <result property=\"updatedAt\" column=\"updated_at\"/>\n" +
            "    </resultMap>\n" +
            "\n" +
            "    <sql id=\"tb\">\n" +
            "        {{tableName}}\n" +
            "    </sql>\n" +
            "\n" +
            "    <sql id=\"cols_all\">\n" +
            "        id,\n" +
            "        <include refid=\"cols_exclude_id\"/>\n" +
            "    </sql>\n" +
            "\n" +
            "    <sql id=\"cols_exclude_id\">\n" +
            "        {{#each fields}}`{{column}}`, {{/each}}\n" +
            "        created_at, updated_at\n" +
            "    </sql>\n" +
            "\n" +
            "    <sql id=\"vals\">\n" +
            "        {{#each fields}}{{Rich field}}, {{/each}}\n" +
            "        now(), now()\n" +
            "    </sql>\n" +
            "\n" +
            "    <sql id=\"criteria\">\n" +
            "        <where>\n" +
            "            {{#each fields}}<if test=\"{{field}} != null\">AND `{{column}}` = {{Rich field}}</if>\n" +
            "            {{/each}}<if test=\"createdAt != null\">AND <![CDATA[created_at >= #{createdAt}]]> </if>\n" +
            "            <if test=\"updatedAt != null\">AND <![CDATA[updated_at < #{updatedAt}]]> </if>\n" +
            "        </where>\n" +
            "    </sql>\n" +
            "\n" +
            "    <insert id=\"create\" parameterType=\"{{modelName}}\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n" +
            "        INSERT INTO\n" +
            "        <include refid=\"tb\"/>\n" +
            "        (<include refid=\"cols_exclude_id\"/>)\n" +
            "        VALUES\n" +
            "        (<include refid=\"vals\"/>)\n" +
            "    </insert>\n" +
            "\n" +
            "    <select id=\"load\" parameterType=\"long\" resultMap=\"{{modelName}}Map\">\n" +
            "        SELECT\n" +
            "        <include refid=\"cols_all\"/>\n" +
            "        FROM\n" +
            "        <include refid=\"tb\"/>\n" +
            "        WHERE id = #{id} LIMIT 1\n" +
            "    </select>\n" +
            "\n" +
            "    <select id=\"loads\" parameterType=\"list\" resultMap=\"{{modelName}}Map\">\n" +
            "        SELECT\n" +
            "        <include refid=\"cols_all\"/>\n" +
            "        FROM\n" +
            "        <include refid=\"tb\"/>\n" +
            "        WHERE id IN\n" +
            "        <foreach item=\"id\" collection=\"list\" open=\"(\" separator=\",\" close=\")\">\n" +
            "            #{id}\n" +
            "        </foreach>\n" +
            "    </select>\n" +
            "\n" +
            "    <update id=\"update\" parameterType=\"{{modelName}}\">\n" +
            "        UPDATE\n" +
            "        <include refid=\"tb\"/>\n" +
            "        <set>\n" +
            "            {{#each fields}}<if test=\"{{field}} != null\">`{{column}}` = {{Rich field}},</if>\n" +
            "            {{/each}}updated_at = now()\n" +
            "        </set>\n" +
            "        WHERE id = #{id}\n" +
            "    </update>\n" +
            "\n" +
            "    <delete id=\"delete\" parameterType=\"long\">\n" +
            "        DELETE FROM <include refid=\"tb\"/>\n" +
            "        WHERE id = #{id}\n" +
            "    </delete>\n" +
            "\n" +
            "    <!--  查询记录数  -->\n" +
            "    <select id=\"count\" parameterType=\"map\" resultType=\"long\">\n" +
            "        SELECT COUNT (1)\n" +
            "        FROM <include refid=\"tb\"/>\n" +
            "        <include refid=\"criteria\"/>\n" +
            "    </select>\n" +
            "\n" +
            "    <!--  分页  -->\n" +
            "    <select id=\"paging\" parameterType=\"map\" resultMap=\"{{modelName}}Map\">\n" +
            "        SELECT <include refid=\"cols_all\"/>\n" +
            "        FROM <include refid=\"tb\"/>\n" +
            "        <include refid=\"criteria\"/>\n" +
            "        LIMIT #{offset}, #{limit}\n" +
            "    </select>\n" +
            "\n" +
            "</mapper>\n";
}
