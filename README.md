#### DemoProxyPlugin

##### 功能

使用代理的方式实现 android 插件化。

##### 工程结构说明

- app
  宿主 app 模块

- plugindemo
  插件 app 模块

- pluginkit
  插件核心模块，实现核心功能

##### 运行方式

1. 执行 plugindemo 的 generatePluginApk task，该任务会生成 plugindemo.apk 并复制到 app 模块下的
   assets 目录中。
2. 运行 app

##### 实现原理

参考：

https://juejin.cn/post/6973888932572315678#heading-0

https://zhuanlan.zhihu.com/p/33017826

##### 参考项目

https://github.com/vimerzhao/PluginDemo

