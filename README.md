# Intellij ideal 插件开发教程

## 官方教程
http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started.html


## 使用案例
根据下面的格式生成module、mapper、sql

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

## TODO

1. 数据字典表格排序
2. 新增模版
3. 任意生成代码模版组合 模版组优先级概念
4. serialVersionUID
5. jdbc方式
6. wiki

```
Predefined variables will take the following values:
${PACKAGE_NAME}
 
name of the package in which the new interface is created
${NAME}
 
name of the new interface specified by you in the Create New Class dialog
${USER}
 
current user system login name
${DATE}
 
current system date
${TIME}
 
current system time
${YEAR}
 
current year
${MONTH}
 
current month
${MONTH_NAME_SHORT}
 
first 3 letters of the current month name. Example: Jan, Feb, etc.
${MONTH_NAME_FULL}
 
full name of the current month. Example: January, February, etc.
${DAY}
 
current day of the month
${HOUR}
 
current hour
${MINUTE}
 
current minute
${PROJECT_NAME}
 
the name of the current project
```