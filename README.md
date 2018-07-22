# CMA-Android代码说明文档

### 文件组织结构
项目根目录组织如下:
```
.
+--.gradle
+--.idea
+--app
+--build
+--gradle
+--.gitignore
+--build.gradle
+--CMA.iml
...
+--External Libraries
```
其中.gradle和.idea目录下放置的Android Studio自动生成的一些文件；app目录最重要，项目中的代码、资源等内容都是在app目录下，所有的开发工作均在这个目录下；build主要包含一些编译时自动生成的文件；gradle下包含gradle wrapper的配置文件，.gitgnore是用来指定的目录或文件排除在版本控制之外；build.gradle是项目全局的gradle构建脚本，通常这个文件中的内容不需要修改；CMA.iml同样是自动生成的。

app组织如下：
```
+--_app
	+--build
	+--libs
	+--release
	+--_src
       +--androidTest
       +--main
            +--java
            +--res
            +--AndroidManifest.xml
       +--test
    +--.gitignore
    +--app.iml
    +--build.gradle
    +--proguard-rules.pro
    

```
build这个目录和外层的build目录类似，无需改变；libs是存放在项目中使用到的第三方jar包；androidTest是用来编写测试用例；main下面的java目录是放置所以有Java代码的地方；res中存放项目所有图片、布局、字符串等资源，图片在drawable目录下，布局放置layout目录下，字符串放在values目录下；AndroidManifest.xml是整个Android项目的配置文件，并且可以在这给应用程序添加权限声明；build.gradle中会指定很多项目构建相关的配置，其他的东西无需管。

java的组织如下：
```
+--java
|	+--adapter
|	+--model
|	+--ui
|	+--utils
|	+--MainActivity
```
最重要的便是java目录，其中MainActivity是主页面代码，adapter存放了所有的适配器，里面按照功能存放每个子功能的适配器，包括capacity_verification(能力验证)、equipment_management(设备管理)...、training_management(培训管理)等15个功能的文件夹；model也是包含按照功能分的15个文件夹，存放的是每个功能的基类；ui则是存放每个功能的页面设计代码，也是包括15个文件夹；utils是一些通用或可复用的接口。