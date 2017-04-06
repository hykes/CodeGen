<?xml version='1.0' encoding='utf-8' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">

<helpset version="1.0">
  <maps>
    <mapref location="Map.jhm"/>
    <homeID>overview</homeID>
  </maps>
  <view>
    <name>TOC</name>
    <label>TOC</label>
    <type>javax.help.TOCView</type>
    <data>toc.xml</data>
  </view>
  <view>
    <name>Index</name>
    <label>Index</label>
    <type>javax.help.IndexView</type>
    <data>index.xml</data>
  </view>
    <presentation default="true">
        <name>Main_Window</name>
        <size width="640" height="480" />
        <location x="0" y="0" />
        <title>CodeGen 帮助文档</title>
        <image>icon</image>
        <toolbar>
            <helpaction>javax.help.BackAction</helpaction>
            <helpaction>javax.help.ForwardAction</helpaction>
        </toolbar>
     </presentation>
</helpset>