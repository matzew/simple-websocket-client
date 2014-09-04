/**
 * Copyright Matthias Weßendorf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wessendorf.websocket;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractSimpleClientTest {

    @Test
    public void simpleBinaryEcho() throws InterruptedException, URISyntaxException {
        final CountDownLatch closeLatch = new CountDownLatch(1);
        final URI securedEndpointURL = new URI("ws://localhost:9999/echo");
        final SimpleWebSocketClient spc = new SimpleWebSocketClient(securedEndpointURL);


        final AtomicReference<ByteBuffer> receivedPayload = new AtomicReference<ByteBuffer>(ByteBuffer.allocate(128));
        final AtomicReference<ReadyState> receivedOpenReadyState = new AtomicReference<ReadyState>();
        final AtomicReference<ReadyState> receivedCloseReadyState = new AtomicReference<ReadyState>();
        final AtomicReference<Integer> receivedCloseCode = new AtomicReference<Integer>();

        spc.setWebSocketHandler(new WebSocketHandlerAdapter() {
            @Override
            public void onOpen() {
                // store readystate on open:
                receivedOpenReadyState.set(spc.getReadyState());

                // create some bogus binary object...
                ByteBuffer bb = ByteBuffer.allocate(128);
                bb.putChar('B');
                bb.putChar('V');
                bb.putChar('B');

                bb.flip();

                spc.sendBinary(bb); // ship it!
            }

            @Override
            public void onClose(int closeCode, String reason) {
                // store received data one close:
                receivedCloseReadyState.set(spc.getReadyState());
                receivedCloseCode.set(closeCode);

                // cause the shutdown
                closeLatch.countDown();
            }

            @Override
            public void onMessage(ByteBuffer message) {

                receivedPayload.get().putChar(message.getChar());
                receivedPayload.get().putChar(message.getChar());
                receivedPayload.get().putChar(message.getChar());

                // hrm
                receivedPayload.get().flip();

                // close it now:
                spc.close();
            }
        });


        spc.connect();

        // wait until the close was called
        closeLatch.await(2000, TimeUnit.MILLISECONDS);


        assertThat(receivedOpenReadyState.get()).isEqualTo(ReadyState.OPEN);

        assertThat(receivedPayload.get().getChar()).isEqualTo('B');
        assertThat(receivedPayload.get().getChar()).isEqualTo('V');
        assertThat(receivedPayload.get().getChar()).isEqualTo('B');

        assertThat(receivedCloseReadyState.get()).isEqualTo(ReadyState.CLOSED);
        assertThat(receivedCloseCode.get()).isEqualTo(1000);
    }

   @Test
    public void simpleTextEcho() throws InterruptedException, URISyntaxException {
        final CountDownLatch closeLatch = new CountDownLatch(1);
        final URI securedEndpointURL = new URI("ws://localhost:9999/echo");
        final SimpleWebSocketClient spc = new SimpleWebSocketClient(securedEndpointURL);


        final AtomicReference<String> receivedPayload = new AtomicReference<String>("");
        final AtomicReference<ReadyState> receivedOpenReadyState = new AtomicReference<ReadyState>();
        final AtomicReference<ReadyState> receivedCloseReadyState = new AtomicReference<ReadyState>();
        final AtomicReference<Integer> receivedCloseCode = new AtomicReference<Integer>();

        spc.setWebSocketHandler(new WebSocketHandlerAdapter() {
            @Override
            public void onOpen() {
                // store readystate on open:
                receivedOpenReadyState.set(spc.getReadyState());

                spc.sendText("Hello"); // ship it!
            }

            @Override
            public void onClose(int closeCode, String reason) {
                // store received data one close:
                receivedCloseReadyState.set(spc.getReadyState());
                receivedCloseCode.set(closeCode);
                // cause the shutdown
                closeLatch.countDown();
            }

            @Override
            public void onMessage(String message) {
                receivedPayload.set(message);
                // close it now:
                spc.close();
            }
        });

        spc.connect();

        // wait until the close was called
        closeLatch.await(2000, TimeUnit.MILLISECONDS);


       assertThat(receivedOpenReadyState.get()).isEqualTo(ReadyState.OPEN);

       assertThat(receivedPayload.get()).isEqualTo("Hello");

       assertThat(receivedCloseReadyState.get()).isEqualTo(ReadyState.CLOSED);
       assertThat(receivedCloseCode.get()).isEqualTo(1000);
    }

    @Test
    public void getURI() throws URISyntaxException {
        final URI securedEndpointURL = new URI("ws://localhost:9999/echo");
        final SimpleWebSocketClient spc = new SimpleWebSocketClient(securedEndpointURL);

        assertThat(spc.getWebsocketURI()).isEqualTo(securedEndpointURL);
    }

    @Test(expected = RuntimeException.class)
    public void noConnection() throws URISyntaxException {
        final URI securedEndpointURL = new URI("ws://localhost:9090/foo");
        final SimpleWebSocketClient spc = new SimpleWebSocketClient(securedEndpointURL);

        spc.connect();
    }
}
