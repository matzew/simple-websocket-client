# Simple WebSocket Client [![Build Status](https://travis-ci.org/matzew/simple-websocket-client.png)](https://travis-ci.org/matzew/simple-websocket-client)


The JSR 356 describes a standard API for WebSocket Server and clients, but the client API is very complex. This project aims to offer a simplified client.

## Get started

Add the following to your ```pom.xml``` file:

```
<dependency>
  <groupId>net.wessendorf.websocket</groupId>
  <artifactId>simple-client</artifactId>
  <version>0.8.0</version>
</dependency>
```

The library is a simplified version of a WebSocket client, based on the JSR 356 client APIs, basically hiding some of the standard APIs for an easy and simple usage:

```java
final SimpleWebSocketClient client =
      new SimpleWebSocketClient("ws://localhost:9999/echo");

client.setWebSocketHandler(new WebSocketHandlerAdapter() {
  @Override
  public void onOpen() {
    client.sendText("Hello"); // ship it!!
  }

  @Override
  public void onMessage(String message) {

    assertThat(message).isEqualTo("Hello");

    // close it now:
    client.close();
  }
});

// connect
client.connect();

//send a text:
client.sendText("Hello again!");
```

Have fun!
