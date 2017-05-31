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


1. 根据配置弹窗(markdown、database)
2. mk弹窗，输入表设计／db弹窗，选择数据源(CRUD)，选择表
3. 字段表格，可勾选字段，可编辑表格
4. 勾选模版组
5. 自动根据模版组存在的优先级，弹窗选择package，完成。