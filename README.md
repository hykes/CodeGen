# CodeGen

This plugin helps you to generate specific template code by create table statement or database .

[![release](https://img.shields.io/badge/IDEA-v0.7-blue.svg)](https://plugins.jetbrains.com/plugin/9574-codegen) [![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/hehaiyangwork/CodeGen/blob/master/LICENSE)

![CodeGen](https://raw.githubusercontent.com/hehaiyangwork/CodeGen/master/codegen.gif)

## Install

Preferences -> Plugins -> Browse repositories -> [search] CodeGen

## Usage

`shift + command + g` OR `Tools -> CodeGen`

## Options

Preferences -> Tools -> CodeGen
    
1. Generation type(Text/Database)
2. In-house variables & helpers
3. Predefined variables
4. Template group
5. Data sources

### In-house Variables

```
{{$.Year}} current year

{{$.Month}} current month

{{$.Date}} current system date

{{$.Now}} current system time

{{$.Day_Of_Month}} current day of the month

{{$.Hour}} current hour

{{$.Minute}} current minute

{{$.Second}} current second

{{$.serialVersionUID}} current model serialVersionUID

<todo>
${PROJECT_NAME} the name of the current project
${MODULE_NAME} the name of the current module
${PACKAGE_NAME} name of the package in which the new interface is created
</todo>
```

### In-house Helpers

```
- 首尾拼接字符
{{Join 'ABC' '#' '%'}}  => #ABC%

- 首字母小写
{{LowerCase 'ABC'}} => aBC

- 首字母大写
{{LowerCase 'abc'}} => Abc

- 驼峰分割
{{Split 'ABcD' '_'}} => A_Bc_D

* 组合用法
{{Split (Join (LowerCase 'AbcDefGhi') '$' '%') '_'}} => $abc_def_ghi%
```

### Text Cases

- markdown

| COLUMN_NAME | TYPE | COMMENT  |
|:--------|:--------:|:---------:|
| id    |  BIGINT(20)  | ID     |
| pid   |  BIGINT(20)  | 父级ID  |
| name  |  VARCHAR(64) | 名称    |
| level |  INT(11)     | 级别    |
| pinyin | VARCHAR(100) | 拼音 |
| english_name | VARCHAR(100) | 英文名 |
| unicode_code | VARCHAR(200) | ASCII码 |
| checker_name | VARCHAR(64) | 审核用户名称 |
| order_no | INT(11) | 排序号 |

- sqlScript

```sql
CREATE TABLE `t_addresses` (
  `id` BIGINT(20) NOT NULL,
  `pid` BIGINT(20) DEFAULT NULL COMMENT '父级ID',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '名称',
  `level` INT(11) DEFAULT NULL COMMENT '级别',
  `pinyin` VARCHAR(100) DEFAULT NULL COMMENT '拼音',
  `english_name` VARCHAR(100) DEFAULT NULL COMMENT '英文名',
  `unicode_code` VARCHAR(200) DEFAULT NULL COMMENT 'ASCII码',
  `order_no` INT(11) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

## Idea sdk docs

http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started.html

> How to setup

* Clone the project, and open with IDEA (Community).
* Modify module type in `.idea/CodeGen.iml` from `JAVA_MODULE` to `PLUGIN_MODULE`.
* Change the project's module compile output path to `/XXX/XXX/CodeGen/out` in `Project Structure -> Modules -> CodeGen -> paths`. You can also modify the Plugin Deployment `plugin.xml path`.
* Install `lombok` plugin. In `Preferences -> Build -> Annotation Processors`. 
	* `Enable annotation processing`.
	* Modify `Production sources directory` to `out`
	* Modify `Test sources directory` to `out`
* Run CodeGen and enjoy it.

> Welcome to contribute

## Changes Log

- 2017-07-06
    - add .kt file type & kotlin template
    - add ESC key event action
    - modify database ui
    - fix template reset bug

- v0.7 2017-07-04
    - add in-house variables (serialVersionUID)
    - support template sub directory
    - support resources (sub) directory
    - fix add group bug
    - remove lombok jar
    
- v0.6 2017-06-30
    - Template Group
    - Generation Type (Text/Database)
    - In-house variables
    - Predefined variables
    - Data sources

## TODO

* [x] serialVersionUID
* [x] template sub directory
* [x] resources sub directory
* [ ] ResourcesBundle
* [ ] outer jdbc jar
* [ ] more in-house variables
* [ ] more handlebars helpers

## Contributions

* [hehaiyangwork](https://github.com/hehaiyangwork)
* [IceMimosa](https://github.com/IceMimosa)

## License
Copyright © 2017 [MIT License](https://spdx.org/licenses/MIT.html)


