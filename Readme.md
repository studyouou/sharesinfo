# 基本环境
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
    
# 修改    
## mysql
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
   
## 配置文件   
需要修改application.yml文件如下
`    url: jdbc:mysql://192.168.43.201:3306/shares?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=GMT`
修改为本机mysql地址以及端口，以及修改类com.hand.sharesinfo.datadownload下的mysql地址以及端口

# 数据下载
下载功能未集成到项目中，直接运行的java程序，在com.hand.sharesinfo.datadownload.DiaoYongApp中可直接下载进入数据库

# 代理配置
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
# 添加docker启动教程
首先将代码打包为jar包，然后上传到linux的/study目录下（study目录自己创建，以下所有目录均可以自定义，但要对应好）
1. 在linux上执行操作
```
mkdir /study
vi Dockerfile
```
2. 在Dockerfile文件中添加以下代码
```
FROM centos
WORKDIR /code
# 安装中文支持
RUN yum -y  groupinstall "fonts"
RUN yum -y install nmap-ncat.x86_64
ADD jdk-8u211-linux-i586.tar.gz /usr/local
# 添加环境变量
ENV LANG=zh_CN.UTF-8
ENV JAVA_HOME=/usr/local/jdk1.8.0_211
ENV CLASSPATH=$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:/code
ENV PATH $PATH:$JAVA_HOME/bin:$CLASSPATH
# java装好后装了这个才能正常使用，原因不知道，待查
RUN yum -y install glibc.i686
# 重新启动source
ENV JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8
COPY ./shareinfo.jar .
COPY ./wait-for-it.sh .
RUN chmod 777 ./wait-for-it.sh
EXPOSE 8080
ENTRYPOINT ["./wait-for-it.sh", "192.168.43.201:3307", "--", "java", "-jar","./shareinfo.jar"]
```
3. 下载[wait-for-it.sh](https://github.com/vishnubob/wait-for-it)到本目录
4. 本目录下创建一个mysql/mysql目录，自己创建一个mysql镜像，在初始化时创建shares库
```
mkdir /study/mysql/mysql
cd /study/mysql/mysql
vi create.sql
    create database shares
vi Dockerfile
    FROM mysql:5.7
    COPY ./create.sql ./docker-entrypoint-initdb.d/
```
5. 创建docker-compose.yml文件，添加以下内容
```
version: "3"
services:
  share:
    build: .
    ports:
      - "8080:8080"
  mysql2:
    build: ./mysql/mysql
    ports:
      - "3306:3306"
    expose:
      - 3306
    environment:
      - MYSQL_ROOT_PASSWORD=mysql123
    volumes:
      - /data/shares:/var/lib/mysql
```
6. 最后执行命令,即可启动项目
```
docker-compose up
```
7. 另外项目所需linux命令请自行下载
下载[docker](https://www.runoob.com/docker/docker-tutorial.html)
安装[docker-compose](https://docs.docker.com/compose/install/)
图片如下
![启动界面](https://github.com/studyouou/markdown/blob/master/1562749281(1).png)
8. 可直接去我的dockerhub上下载镜像，[我的dockerhub的shares地址](https://cloud.docker.com/repository/docker/304489914/share_info)，[我的dockerhub的mysql地址](https://cloud.docker.com/repository/docker/304489914/shares_mysql)但是需要先启动mysql镜像，再启动shares镜像(注意mysql密码和ip及端口需要与自己代码对应)
9. 第一次执行时，由于数据量过大，可下载部分数据后关掉docker然后重启，将不再进行下载
# 项目效果   
![首页](https://github.com/studyouou/markdown/blob/master/1562518516(1).png) 
![30天涨幅超过5%页面](https://github.com/studyouou/markdown/blob/master/1562518575(1).png) 
![历史数据页](https://github.com/studyouou/markdown/blob/master/1562518627(1).png) 

# 注意
点击下载股票信息功能是下载服务器文件到本地，文件地址用的本机的，所以该功能会有问题，后续修改。

# 定时任务描述    
定时任务是每隔5秒去更新一个股票的信息，当你点击进入详细的一个股票的历史记录时，后台就会优先更新这个股票现有价格，但是如果5秒内点击过快，则会被覆盖上次信息，上次点击的则不会优先跟新了。
