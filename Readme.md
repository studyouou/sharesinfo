#基本环境
本内容需要基本java环境   
jdk1.8   
spring-boot 2.1.13   
mybatis 3.5.1   
springmvc    
maven 3.6.1   
mysql 5.7    
docker   
    
开发工具为   
点击下载[java](https://www.oracle.com/technetwork/java/javase/downloads/index.html)   
点击下载[maven](http://maven.apache.org/)
点击学习[docker安装教程](https://www.runoob.com/docker/docker-tutorial.html)
点击学习[docker下MySQL安装](https://www.runoob.com/docker/docker-install-mysql.html)
    
#修改    
##mysql 
项目是基于docker的mysql，也可以用本机mysql，docker在安装完mysql，需要修改一下操作，否则可能中文乱码   
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
   
##配置文件   
需要修改application.yml文件如下
`    url: jdbc:mysql://192.168.43.201:3306/shares?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=GMT`
修改为本机mysql地址以及端口，以及修改类com.hand.sharesinfo.datadownload下的mysql地址以及端口

#数据下载
下载功能未集成到项目中，直接运行的java程序，在com.hand.sharesinfo.datadownload.DiaoYongApp中可直接下载进入数据库

#代理配置
如果要开启代理ip下载数据，需要一下操作(由于免费爬取的代理，极不稳定，建议不用，有自己代理可设置)  
+解开com.hand.sharesinfo.datadownload.DiaoYongApp中35行、318行、并且注释掉332-336行，代码如下
```
35 :  ipAdds = new ProxyIP().getIpAddrs(5);

318： setIpProxy();

332-336 : try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
```

项目效果   
![首页](https://github.com/studyouou/markdown/blob/master/1562518516(1).png) 
![30天涨幅超过5%页面](https://github.com/studyouou/markdown/blob/master/1562518575(1).png) 
![历史数据页](https://github.com/studyouou/markdown/blob/master/1562518627(1).png) 

