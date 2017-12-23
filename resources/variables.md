Predefined Variables:

${Year} current year

${Month} current month

${Day} current day of the month

${Hour} current hour

${Minute} current minute

${Second} current second

${Date} current system date

${Now} current system time

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
