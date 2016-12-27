# BAndroidRouter
BAndroidRouter is an multi module enabled router library. 仿贝贝网App组件化及跳转总线库.

作者: 刘长炯 BeanSoft@126.com (微信号 weblogic ).

借鉴贝贝技术团队微信公众号的一篇文章 "贝贝的组件化之路" , 本代码库
基于apt技术，通过注解方式实现了AAR组件化和跳转总线(Router), 支持通过URL打开Activity功能,
支持基于URL的App数据交互协议. 还能通过避免在最终App类中出现相关注解, 一定程度上保护了代码的安全.

文章详情请自行搜索.

本项目部分代码参考了: https://github.com/joyrun/ActivityRouter 特此鸣谢.

## 核心用法浓缩

```java
@Router(value = {"client/module1/test"})
 // 配置映射路径 
public class MainActivity extends Activity {
// ...
}

// 使用映射路径跳转
HRouter.open(context, "app://client/module1/test?a=b&name=张三");

// 开发和配置动作映射
@Action(value = {"action/test"})
public class TestAction extends HAbstractAction<String>  {
        // 同步模式
        public String action() {// 无参数的调用应该只考虑这一个
            return "TestAction同步调用无参数";
        }

        // 同步模式+参数
        public String action(Object param) {
            return "TestAction同步调用有参数:" + param;
        }

        // 异步模式+回调
        public void action(HCallback<String> callback) {
            action(null, callback);
        }

        // 异步模式+回调+参数
        public void action(Object param, HCallback<String> callback) {
            callback.start();
            callback.ok(action(param), null);
            callback.complete();
        }
}

// 使用HRouter来调用动作
        Object value = HRouter.action("haction://action/test?a=b");
        Toast.makeText(this, "Action带参数执行结果:" + value, Toast.LENGTH_SHORT).show();

        HRouter.action("haction://action/test?a=b", new HAbstractCallback<String>() {
            @Override
            public void start() {
                Toast.makeText(MainActivity.this, "Action执行开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void complete() {
                Toast.makeText(MainActivity.this, "Action执行结束", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void ok(String o, Object response) {
                System.out.println("异步调用结束!");
            }
        });
```
# New! 自动载入映射列表
每个子module项目中的gradle配置:
```gradle
apt {
     arguments {
             targetModuleName 'Other' // 模块名, 用于加载和生成映射关系
            assetsDir "$projectDir/src/main/assets"// 可选, 配置时会自动生成assets/modules
    } 
}
```

```java
HRouter.setupFromAssets(this);
```

## 代码结构
router-demo-app 主演示入口App<br>
router-demo-module1 主演示模块, 仅依赖模块router-compiler和 router-library<br>
router-annotation @Router 路由Java注解(Java库)<br>
router-compiler APT插件库(Java库),用于根据注解自动生成单个模块下所有Activity的映射关系类<br>
router-library Android库, 实现了Activity的路由逻辑
## 如何增加和使用路由关系
### 子组件(模块)的开发和设置
模块必须设置module名称:
参考build.gradle

```gradle
apt {
     arguments {
             targetModuleName 'Other' // 模块名, 用于加载和生成映射关系
    } 
}
```

在需要支持跳转的Activity类名上加注解:


```java
@Router(value = {"client/module1/test"}) 
 public class MainActivity extends Activity {
//...
}
```

### 主App的设置
####初始化Router
可以设置跳转支持的Schema, 以及需要加载的模块中的跳转规则类(用APT插件自动生成), 如下面代码中 Other 即为新增模块中的 targetModuleName, 
```
HRouter.setScheme("app");// 设置跳转的schema 
HRouter.setup("Base", "Other");// 载入映射关系
```
#### 发起路由跳转
跳转到模块内和模块外的Activity的方式都是统一的, 如下所示:

```java
String path = "app://client/module1/test?a=b&name=张三";
if(!HRouter.open(this, path)) {
    Toast.makeText(this, "没有跳转成功, 请检查跳转路径 " + path, Toast.LENGTH_SHORT).show();
} else {
    Toast.makeText(this, "成功跳转到 " + HRouter.getActivityName(path).getCanonicalName(), Toast.LENGTH_SHORT).show();
}
```
a=b&name=张三 这样的字符串会转换为Bundle信息.

### 组件化
由于跳转关系不需要关注具体的类, 所以主App可以只是一个壳工程, 依赖一堆子模块aar, 甚至每个子模块都可以有自己的版本库, 即可happy的跳转了.

###跨模块的数据传递
一个Action可以实现HAction接口, 并加入@Action注解后, 就可以通过路径的方式进行跨模块的调用.
TODO 文档完善

### 产生映射和Action的列表文档
build之后, 会自动产生一个doc目录, 下面分模块会生成不同的映射文件列表,
便于研发人员查看, 在Android Stuido中点击Rebuild会更新生成.

## 待完成功能(TODO)
### WebView和外部浏览器的支持
### 多个参数值的支持, MultiValueMap的调研
目前尚在开发之中

## Changelog
2016-12-27 增加自动载入映射列表的功能


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


