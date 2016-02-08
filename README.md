# syncounter

##Building
This project uses maven.

```
mvn clean install
```
## How to run
As a client-server application, we need to run the server first.


### Running the server

```
java -jar server/target/server-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Running the client

```
java -jar client/target/client-1.0-SNAPSHOT-jar-with-dependencies.jar [ ip [ rand ]]
```

Arguments:
- ip: It is the server ip address. Default: 127.0.0.1
- rand: It does 1000 server requests. It is used for test porpouse only.