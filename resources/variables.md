Predefined Variables:

${YEAR} current year

${MONTH} current month

${DAY} current day of the month

${DATE} current system date yyyy-MM-dd

${TIME} current system time HH:mm:ss

${NOW} current system time yyyy-MM-dd HH:mm:ss

${USER} current system user name

${serialVersionUID} current file serialVersionUID

${Project} the name of the current project

Predefined Directive:

* 首尾拼接
#Append('ABC' '#' '%')  => #ABC%

* 首字母小写
#LowerCase('ABC') => aBC

* 首字母大写
#UpperCase('abc') => Abc

* 驼峰分割
#Split('ABcD' '_') => A_bc_d

* 组合用法
#Append("#LowerCase('AbcDefGhi')" '$' '%') => $abcDefGhi%

* 获取类路径
#GetPackage(${model}) => xx.xxx.model

* 引用类路径
#ImportPackage(${model}) => import xx.xxx.model
