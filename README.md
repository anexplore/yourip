# yourip
send back your ip address which you are using commuting with the server


### start service
java -jar yourip-1.0-jar-with-dependencies.jar

this is will bind on 0.0.0.0:80 to service for returning your ip which you are using 

### usage
curl -kv 'http://bindAddr:bindPort/'

### change configs
use -D to set bindAddr\bindPort\https\workSize\coreSize\sslKeyPath\sslCrtPath

### response
172.16.0.3
