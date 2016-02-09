# Super Remote Counter (description)


Sharing resources between nodes is a challenge engineers have always faced when writing  distributed systems. 
The difficultly arises when coordinating simultaneous access to the  resource and ensuring that it is done fairly and without corrupting the resource.

Your task is to write a server and a client that allows multiple clients to simultaneously access a counter - 
the resource. Clients should be able to increment and decrement the counter and get its current value, via a protocol 
of your own design. The protocol should be over TCP/IP and not be built upon HTTP, RMI or similar. Care should be 
taken to persist the counter across restarts and/or failures, and a database should not be used. You may use 
whatever libraries or frameworks you deem fit to assist you in the task.

A command line interface is sufficient.

# Development notes

## Technologies

### Spring boot

I've chosen spring boot for the convenience to run Spring as a command line software. 

I used the dependency injection and the ThreadPoolTaskExecutor (as async executor), which is the Executor's spring implementation. 
This thread pool enables the system to add more simultaneous workers (CounterWorkerNode) to access the resource, increasing the queue
management performance.

### Java NIO

Java NIO is used to reduce the number of active threads running on the server side. This technology allows a set of active 
channels sharing the same thread lane. It allows the server to accept more simultaneous clients connected. 


### Testing with Mockito
Mockito offers a set of facilities to Mock classes that make the test process easier. For instance, we are using
the @Mock and @InjectMocks while testing Spring Beans, without running a spring context.

## Design details

### Protocol
The protocol designed is very simple. Basically we have two messages: CounterMessageRequest and CounterMessageResponse those 
encapsulate a client Request and Response, respectively.

Each message is 8 byte length and contains one integer for the command_code/response_code and the value.

#### Request
For request, we have three commands:
1) get: returns the current value from the counter. The value is ignored for this message.
2) inc: increments the counter value. The value represents the delta do be added to the counter. Ex: counter = counter + value.
2) dec: decrements the counter value. The value represents the delta do be subtracted from the counter. Ex: counter = counter - value.

#### Response
For response, we have three scenarios:
1) ok: server processed the request and is returning the current counter value;
2) wrongMessage: server couldn't understand the request. Clients is going to be disconnected. Value is 0;
3) serverBusy: server busy. Too many requests to the server and your message can't be processed at moment. The returned value 
represents the queue size at the moment. 

## Design decicions

### Queue mamagement and thread pool
The main objective to use a queue management is to release the main server thread as soon as possible to accept new connection/message. 

After inserting a new message into the queue, the thread pool is noticed and then it is able to get a free worker node to process the request.

### Delegate and Helper

I decided to use delegate/helper for two reasons:
- To protect the SocketChannel implementation, providing only few methods for the system;
- To make the testing process easier.

I used these pattern for ByteBuffer (ByteBufferDelegate) and SocketChannel (SocketChannelDelegate). Both of those class have 
final methods, making the test process harder.

Another bean used for the same purpose is the SocketHelperBean. Although it is used to facilitate testing , also serves to 
isolate the Java NIO implementation.

### Access to resource

The access to resource is provided by CounterService interface and implemented by CounterServiceBean. I used a simple 
thread LOCK object which synchronizes the access to the resource (counter).

I haven't used the AtomicInteger because it doesn't make sense for this application. We have a functional requisite which
defines that the system have to be able to keep the counter value even on fails and when the server is restarted.

For this reason, I assumed a 'long' transaction, that means: read value, make updates (if required) and write the value 
into the value. The access for all this transaction is thread safe.

I also inserted a little Thread.sleep to make a delay between get and increment/decrement the value. If two threads access
on the same time, it would be a big problem for the counter value.


## Building and Running it
This project uses maven.

```
mvn clean install
```
### How to run
As a client-server application, we need to run the server first.


#### Running the server

```
java -jar server/target/server-exec.jar
```

#### Running the client

```
java -jar client/target/client-exec.jar [--ip=<ip address> | --rand | --help]
```

Options:
1) --ip=<remote ip address>: It is the server ip address. Default: 127.0.0.1
2) --rand: It does 1000 server requests. It is used for test porpouse only. Default disable
3) --help: show the help menu