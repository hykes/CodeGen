# CodeGen V0.6

This plugin helps you to generate specific code by template.

## Install

Preferences -> Plugins -> Browse repositories -> [search] CodeGen

## Usage

shift + command + g

Tools -> CodeGen

## Options

Preferences -> Tools -> CodeGen
    
1. use Text / use database
2. in-house variables / predefined variables
3. code templates
4. data sources

### In-house Variables

```
Predefined variables will take the following values:

{{$.Year}} current year

{{$.Month}} current month

{{$.Date}} current system date

{{$.Now}} current system time

{{$.Day_Of_Month}} current day of the month

{{$.Hour}} current hour

{{$.Minute}} current minute

{{$.Second}} current second

<del>
${PACKAGE_NAME} name of the package in which the new interface is created
${NAME} name of the new interface specified by you in the Create New Class dialog
${USER} current username system login name
${PROJECT_NAME} the name of the current project
${MODULE_NAME} the name of the current module
${MONTH_NAME_SHORT} first 3 letters of the current month name. Example: Jan, Feb, etc.
${MONTH_NAME_FULL} full name of the current month. Example: January, February, etc.
<del>

```

### Text Cases

- markdown

```
| user_id |  BIGINT(20) | 申请用户ID |
| user_name |  VARCHAR(20) | 申请用户名称 |
| company_id |  BIGINT(20) | 供应商ID |
| company_name | VARCHAR(64) | 供应商名称 |
| checked_at | DATETIME | 审核日期 |
| checker_id | BIGINT(20) | 审核用户名称 |
| checker_name | VARCHAR(64) | 审核用户名称 |
| data_json | VARCHAR(1024) | 审核数据 |
```

- sqlScript

```
CREATE TABLE `t_addresses` (
  `id` bigint(20) NOT NULL,
  `pid` bigint(20) DEFAULT NULL COMMENT '父级ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `level` int(11) DEFAULT NULL COMMENT '级别',
  `pinyin` varchar(100) DEFAULT NULL COMMENT '拼音',
  `english_name` varchar(100) DEFAULT NULL COMMENT '英文名',
  `unicode_code` varchar(200) DEFAULT NULL COMMENT 'ASCII码',
  `order_no` varchar(32) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

## Idea sdk docs

http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started.html

## TODO

1. serialVersionUID
2. wiki

## License
Copyright © 2017 [MIT License](https://spdx.org/licenses/MIT.html)
