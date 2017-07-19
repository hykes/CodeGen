In-house Variables:

{{$.Year}} current year

{{$.Month}} current month

{{$.Day}} current day of the month

{{$.Hour}} current hour

{{$.Minute}} current minute

{{$.Second}} current second

{{$.Date}} current system date

{{$.Now}} current system time

{{$.serialVersionUID}} current model serialVersionUID

{{$.Project}} the name of the current project

{{$.Module1} the name of the current selected module

{{$.Package1} the name of the current selected package

In-house Helpers:

* 首尾拼接
{{Join 'ABC' '#' '%'}}  => #ABC%

* 首字母小写
{{LowerCase 'ABC'}} => aBC

* 首字母大写
{{UpperCase 'abc'}} => Abc

* 驼峰分割
{{Split 'ABcD' '_'}} => A_bc_d

* 组合用法
{{Split (Join (LowerCase 'AbcDefGhi') '$' '%') '_'}} => $abc_def_ghi%
