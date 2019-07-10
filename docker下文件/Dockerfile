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

