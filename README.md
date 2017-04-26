# spring-boot-starter-dubbox

## 配置示例
```
dubbox:
  application:
    name: spring-boot-dubbox
    organization: dubbox
    owner: kola
  registry:
    protocol: zookeeper
    address: 192.168.1.1:2181,192.168.1.2:2181,192.168.1.3:2181
    register: true
  provider:
      group: kola
  annotation:
    package: tk.mybatis.springboot.service
  protocol:
    name: dubbo
    port: 20788
    timeout: 30000
    threadpool: fixed
    threads: 500
    accepts: 1000
    serialization: kryo
    keepalive: true
  monitor:
    protocol: registry
```
查看[Dubbo配置](http://dubbo.io/User+Guide-zh.htm#UserGuide-zh-Xml%E9%85%8D%E7%BD%AE)