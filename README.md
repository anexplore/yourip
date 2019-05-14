# yourip
返回你与此服务通信的IP地址


### 启动
java -jar yourip-1.0-jar-with-dependencies.jar

将服务绑定在0.0.0.0:80 

### 请求
curl -kv 'http://bindAddr:bindPort/'

### 响应
172.16.0.3

### 更改配置
use -D to set bindAddr\bindPort\https\workSize\coreSize\sslKeyPath\sslCrtPath

### 使用场景
!!如果是获取外网IP可以使用已有成熟的服务比如proxyjudge.us等

私有网络环境机器多网卡、多网段、ip映射/绑定等


