# BAndroidRouter
BAndroidRouter is an multi module enabled router library. 仿贝贝网App组件化及跳转总线库.

作者: 刘长炯 BeanSoft@126.com (微信号 weblogic ).

借鉴贝贝技术团队微信公众号的一篇文章 "贝贝的组件化之路" , 本代码库实现了文章中所说的AAR组件化和跳转总线(Router).
文章详情请自行搜索.

本项目部分代码参考了: https://github.com/joyrun/ActivityRouter 特此鸣谢.

## 核心用法浓缩

```java
@Router(value = {"client/module1/test"}) // 配置映射路径 public class MainActivity extends Activity {...}

// 使用映射路径跳转
HRouter.open(context, "app://client/module1/test?a=b&name=张三");

```


## 代码结构
router-demo-app 主演示入口App
router-demo-module1 主演示模块, 仅依赖模块router-compiler和 router-library
router-annotation @Router 路由Java注解(Java库)
router-compiler APT插件库(Java库),用于根据注解自动生成单个模块下所有Activity的映射关系类
router-library Android库, 实现了Activity的路由逻辑
## 如何增加和使用路由关系
### 子组件(模块)的开发和设置
模块必须设置module名称:
参考build.grale

```java
apt {
     arguments {
             targetModuleName 'Other' // 模块名, 用于加载和生成映射关系
    } }
```

在需要支持跳转的Activity类名上加注解:


```java
@Router(value = {"client/module1/test"}) // 配置映射路径 public class MainActivity extends Activity {
```

### 主App的设置
####初始化Router
可以设置跳转支持的Schema, 以及需要加载的模块中的跳转规则类(用APT插件自动生成), 如下面代码中 Other 即为新增模块中的 targetModuleName, 
```
HRouter.setScheme("app");// 设置跳转的schema HRouter.setup("Base", "Other");
```
#### 发起路由跳转
跳转到模块内和模块外的Activity的方式都是统一的, 如下所示:

```java
String path = "app://client/module1/test?a=b&name=张三";
 if(!HRouter.open(this, path)) {     Toast.makeText(this, "没有跳转成功, 请检查跳转路径 " + path, Toast.LENGTH_SHORT).show(); } else {     Toast.makeText(this, "成功跳转到 " + HRouter.getActivityName(path).getCanonicalName(), Toast.LENGTH_SHORT).show(); }
```
a=b&name=张三 这样的字符串会转换为Bundle信息.

### 组件化
由于跳转关系不需要关注具体的类, 所以主App可以只是一个壳工程, 依赖一堆子模块aar, 甚至每个子模块都可以有自己的版本库, 即可happy的跳转了.

## 待完成功能(局限性)
跨模块的数据传递, 目前尚在开发之中, 近期即可支持, 届时即可通过:
数据获取：`Object  o = HAction.open(this, “app://bb/base/product?name=zs”);`
的方式同步或异步获取数据. 敬请关注!



#License
Copyright 2016 BeanSoft@126.com.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


