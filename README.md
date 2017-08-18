# SpringBoot member Maven 多模块

项目基于[SpringBootDemo 项目](http://git.duofee.com/zhangmz/SpringBootDemo), 升级至多模块项目。

### 构建方式
使用Intellij IDEA方式构建项目模块

Eclipse/Myeclipse 请逐个Import maven 项目方式加入

### 代码风格：

IDEA 请使用**doc/java-code-style-IDEA.xml**

eclipse 请使用**doc/java-code-style-eclipse.xml**

导入以上Java代码风格，提交代码请先格式化后再提交。

### 提交代码方式：

先Fork ，编写代码，再发起PullRequest合并

注：不熟悉SpringBoot项目构建，请先去使用SpringBoot单方式模块构建项目

### 运行
运行项目有两种方式：

1. 使用memberApplication.java
2. 使用tomcat

默认打包成war

> 注:打包上（测试 or 正式）环境前，请先确定当前环境。

多模块细分各模块职责。

### 会员 系统
模块分析
- member  基础 提供jar包依赖管理并管理各子模块
- member-common 提供基础设施
- member-dao  数据持久层 依赖 entity 和 common
- member-entity 实体层 
- member-generator 项目代码生成，一键生成DAO Xml Service
- member-service 业务层 依赖dao
- member-web  程序入口，并依赖service 统一集成单元测试，所有测试代码写入
- member-web-api 接口层 依赖service

多模块 多web 端构建项目基建。

使用Lombok 插件简化 getter/setter 详细请查看：
[使用 Lombok 自动生成 Getter and Setter](http://www.qtdebug.com/java-lombok/)

多环境切换日志文件配置
[Spring Boot干货系列：（七）默认日志logback配置解析 | 掘金技术征文](https://juejin.im/post/58f86981b123db0062363203)