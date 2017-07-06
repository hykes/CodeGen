In-house Variables:

{{$.Year}} current year

{{$.Month}} current month

{{$.Date}} current system date

{{$.Now}} current system time

{{$.Day_Of_Month}} current day of the month

{{$.Hour}} current hour

{{$.Minute}} current minute

{{$.Second}} current second

{{$.serialVersionUID}} current model serialVersionUID

In-house Helpers:

* 首尾拼接
{{Join 'ABC' '#' '%'}}  => #ABC%

* 首字母小写
{{LowerCase 'ABC'}} => aBC

* 首字母大写
{{LowerCase 'abc'}} => Abc

* 驼峰分割
{{Split 'ABcD' '_'}} => A_Bc_D

* 组合用法
{{Split (Join (LowerCase 'AbcDefGhi') '$' '%') '_'}} => $abc_def_ghi%
