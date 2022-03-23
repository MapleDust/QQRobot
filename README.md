# QQRobot

一个基于simbot框架写的一个QQ机器人，代码以学习目的为主

- 使用说明

  - 需要在java8及以上运行

  - 在任意处新建文件夹名字随便取，将从`Releases`上下载的jar包直接放入刚刚新建的文件夹

  - 首次运行需要编辑`simbot-bots`文件夹下的`bot1.bot`文件（建议使用小号）

    - 示例

      ```properties
      code=机器人的QQ账号
      password=机器人的QQ密码
      ```

  - 可以根据实际情况来编辑根目录下的机器人配置文件

    - 示例

      ```properties
      #服务器信息的目标服务器ip
      robot.mc.server.ping.ip=
      #服务器信息的目标服务器端口
      robot.mc.server.ping.port=25565
      #随机图片的冷却时间
      robot.random.image.cd=120
      #随机图片的api服务器
      robot.random.image.api=https://acg.toubiec.cn/random.php
      ```

      

- 指令
  - 现在时间——返回现在年月日时分秒
  - mcuuid [mc正版用户名] ——返回玩家的uuid
  - server ping—— 返回目标服务的`服务端版本` `当前在线玩家`(需要编辑机器人配置文件夹)
  - 随机图片——返回api服务器的图片（建议使用二次元随机图片的api效果更佳）

大概是这些功能了，欢迎大家在`issues`提交建议哦~