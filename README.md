# CodeGen

This plugin helps you to generate specific template code by create table statement or database .

[![release](https://img.shields.io/badge/IDEA-v0.8-blue.svg)](https://plugins.jetbrains.com/plugin/9574-codegen) [![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/hykes/CodeGen/blob/master/LICENSE)

![CodeGen](https://raw.githubusercontent.com/hykes/CodeGen/master/doc/codegen.gif)

Powered by :

[Velocity](http://velocity.apache.org) [user-guide](http://velocity.apache.org/engine/2.0/user-guide.html)
[IDEA datebase tools](http://velocity.apache.org)


### Installation
- Using IDE built-in plugin system on Windows:
  - <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "CodeGen"</kbd> > <kbd>Install Plugin</kbd>
- Using IDE built-in plugin system on MacOs:
  - <kbd>Preferences</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "CodeGen"</kbd> > <kbd>Install Plugin</kbd>
- Manually:
  - Download the [latest release](https://github.com/hykes/CodeGen/releases) and install it manually using <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>
  
Restart IDE.

### Usage
- Connecting to Your Database:
  - <kbd>View</kbd> > <kbd>Tool Windows</kbd> > <kbd>Database</kbd>
- CodeGen
  - <kbd>Tools</kbd> > <kbd>CodeGen-SQL</kbd> or `shift + command + g`
  - <kbd>Select Table</kbd> > <kbd>Right click popup menu</kbd> > <kbd>CodeGen-DB</kbd>

## Options

Preferences -> Tools -> CodeGen
    
1. Generation type(SQL/DB)
2. In-house variables and (handlebars) helpers
3. Predefined variables
4. Custom template groups
5. Data sources manager

### In-house Variables

```
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
```

### In-house Helpers

```
- 首尾拼接字符
{{Join 'ABC' '#' '%'}}  => #ABC%

- 首字母小写
{{LowerCase 'ABC'}} => aBC

- 首字母大写
{{UpperCase 'abc'}} => Abc

- 驼峰分割
{{Split 'ABcD' '_'}} => A_bc_d

* 组合用法
{{Split (Join (LowerCase 'AbcDefGhi') '$' '%') '_'}} => $abc_def_ghi%
```

## Idea sdk docs

http://www.jetbrains.org/intellij/sdk/docs/welcome.html
https://www.jetbrains.com/help/idea/meet-intellij-idea.html

> How to setup

* Clone the project, and open with IDEA (Community).
* Modify module type in `.idea/CodeGen.iml` from `JAVA_MODULE` to `PLUGIN_MODULE`.
* Change the project's module compile output path to `/XXX/XXX/CodeGen/out` in `Project Structure -> Modules -> CodeGen -> paths`. You can also modify the Plugin Deployment `plugin.xml path`.
* Add dependencies to project, set the scope to provided
  ```
  Idea.App/Contents/plugins/DatabaseTools/lib/database-impl.jar
  Idea.App/Contents/plugins/DatabaseTools/lib/database-openapi.jar
  ```
* Run CodeGen and enjoy it.

> Welcome to contribute

## Guide

* [Guide_cn](https://github.com/hykes/CodeGen/blob/master/doc/Guide_cn.md)

## Contributions

* [hykes](https://github.com/hykes)
* [IceMimosa](https://github.com/IceMimosa)

## License
Copyright © 2017 [MIT License](https://github.com/hykes/CodeGen/blob/master/LICENSE)


