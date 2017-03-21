# Intellij ideal 插件开发教程

## 官方教程
http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started.html


## 使用案例
根据下面的格式生成module、mapper、sql

```
| user_id |  BIGINT(20) | 申请用户ID | 否 |  |
| user_name |  VARCHAR(20) | 申请用户名称 | 否 |  |
| company_id |  BIGINT(20) | 供应商ID | 否 |  |
| company_name | VARCHAR(64) | 供应商名称 | 否 |  |
| checked_at | DATETIME | 审核日期 | 否 |  |
| checker_id | BIGINT(20) | 审核用户名称 | 否 |  |
| checker_name | VARCHAR(64) | 审核用户名称 | 否 |  |
| data_json | VARCHAR(1024) | 审核数据 | 否 |  |
```

## TODO

1. 自定义常量
2. 文件名称设置
3. 任意字段顺序