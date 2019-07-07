本内容需要基本java环境   
jdk1.8   

开发工具为   
点击下载[python](https://www.python.org/)，

进入mysql命令行 执行以下命令设置编码   
```
cd /etc/mysql   
cat <<EOF>> my.cnf
[client]
default-character-set=utf8
 
[mysql]
default-character-set=utf8

[mysqld]
init_connect='SET collation_connection = utf8_unicode_ci'
init_connect='SET NAMES utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
skip-character-set-client-handshake

```
退出重启mysql