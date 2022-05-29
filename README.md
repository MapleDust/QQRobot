# QQRobot

一个基于simbot框架写的一个QQ机器人，代码以学习目的为主

- 需要环境
  - java>=8
  - mariaDB>=10.6

- 使用说明

  - 下载 [releases](https://github.com/MapleDust/QQRobot/releases)中的jar文件和sql脚本

  - 确保`需要环境`都成功安装，首次运行需要在数据库里运行sql脚本

  - 在任意处新建文件夹名字随便取，将从`Releases`上下载的jar包直接放入刚刚新建的文件夹

  - 首次运行需要编辑`simbot-bots`文件夹下的`bot1.bot`文件（建议使用小号）和根目录文件夹下的`config.properties`

  - 聊天记录将保存到数据库里，后期可以在`robot`数据库中的`message`表里查询到聊天记录（后面可能在新的项目中有web后台管理）
  
    - `bot1.bot`配置文件示例
  
      ```properties
      code=机器人的QQ账号（必填）
      password=机器人的QQ密码（必填）
      ```

    - `config.properties`配置文件实例
  
      ```properties
      #以下根据实际情况填写
      #服务器信息的目标服务器ip
      robot.mc.server.ping.ip=
      #服务器信息的目标服务器端口
      robot.mc.server.ping.port=25565
      #随机图片的冷却时间
      robot.random.image.cd=120
      #随机图片的api服务器
      robot.random.image.api=https://acg.toubiec.cn/random.php
      
      #以下是必填项
      #数据库链接
      spring.datasource.url=数据库的链接地址+/robot(必填)
      #数据库的用户名
      spring.datasource.username=数据库的用户名（必填）
      #数据库的密码
      spring.datasource.password=数据库的密码（必填）
      ```
 
 - 以上操作完成后在根目录下新建bat脚本(linux新建sh脚本)

    ```bash
    @echo off
    java -jar qq-robot-2.1.0.jar
    pause
    ```
    
- 指令
  - 现在时间——返回现在年月日时分秒
  - mcuuid [mc正版用户名] ——返回玩家的uuid
  - server ping—— 返回目标服务的`服务端版本` `当前在线玩家`(需要编辑机器人配置文件夹)
  - 随机图片——返回api服务器的图片（建议使用二次元随机图片的api效果更佳）

- 升级注意事项
  - 在每次升级中请将原来的`config.properties`重命名或者移动到其他文件夹，然后根据根据实际情况填写配置（懒）




大概是这些功能了，欢迎大家在`issues`提交建议哦~

