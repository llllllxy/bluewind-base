# bluewind-base

### 介绍
bluewind-base，一个基于Spring5+MyBatis+Thymeleaf整合的javaWeb开发脚手架，开箱即用

### 技术选型
- Spring 5.2.0
- thymeleaf 模板引擎
- redis 
- druid 1.2.5 德鲁伊连接池
- mybatis 3.4.6 持久层框架
- pagehelper 5.0.1 mybatis分页插件
- swagger-ui 2.9.2

### 实现功能
- 整合MyBatis实现数据库查询和分页插件pagehelper
- 整合Redis
- 整合swagger-ui
- 整合了thymeleaf 模板引擎，实现网页的渲染展示
- 整合了多数据源，可用注解灵活切换主从数据源
- 封装Json统一返回消息

### 运行环境
- jdk8
- Mysql5.6+
- redis3.0+

### 启动教程

- 导入sql文件夹下的数据库脚本到MySQL
- 修改配置文件database.properties中MySQL数据库的连接信息
- 使用jetty启动，如下图所示
- ![输入图片说明](https://images.gitee.com/uploads/images/2021/0929/132743_676cddca_5304908.png "QQ截图20210929132710.png")

### 运行截图
- ![输入图片说明](https://images.gitee.com/uploads/images/2021/0929/133238_c8c7991a_5304908.png "1.png")
- ![输入图片说明](https://images.gitee.com/uploads/images/2021/0929/133251_172919c7_5304908.png "2.png")