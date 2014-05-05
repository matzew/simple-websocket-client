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

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractSimpleClientTest {

    @Test
    public void simpleBinaryEcho() throws InterruptedException, URISyntaxException {
        final CountDownLatch closeLatch = new CountDownLatch(1);
        final URI securedEndpointURL = new URI("ws://localhost:9999/echo");
        final SimpleWebSocketClient spc = new SimpleWebSocketClient(securedEndpointURL);

        spc.setWebSocketHandler(new WebSocketHandlerAdapter() {
            @Override
            public void onOpen() {

                // create some bogus binary object...
                ByteBuffer bb = ByteBuffer.allocate(128);
                bb.putChar('B');
                bb.putChar('V');
                bb.putChar('B');

                // hrm.........
                bb.flip();

                spc.sendBinary(bb); // ship it!
            }

            @Override
            public void onClose(int closeCode, String reason) {
                assertThat(closeCode).isEqualTo(1000);

                // cause the shutdown
                closeLatch.countDown();
            }

            @Override
            public void onMessage(ByteBuffer message) {

                assertThat(message.getChar()).isEqualTo('B');
                assertThat(message.getChar()).isEqualTo('V');
                assertThat(message.getChar()).isEqualTo('B');

                // close it now:
                spc.close();
            }
        });


        spc.connect();

        // wait until the close was called
        closeLatch.await(2000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void simpleTextEcho() throws InterruptedException, URISyntaxException {
        final CountDownLatch closeLatch = new CountDownLatch(1);
        final URI securedEndpointURL = new URI("ws://localhost:9999/echo");
        final SimpleWebSocketClient spc = new SimpleWebSocketClient(securedEndpointURL);

        spc.setWebSocketHandler(new WebSocketHandlerAdapter() {
            @Override
            public void onOpen() {

                spc.sendText("Hello"); // ship it!
            }

            @Override
            public void onClose(int closeCode, String reason) {
                assertThat(closeCode).isEqualTo(1000);
                // cause the shutdown
                closeLatch.countDown();
            }

            @Override
            public void onMessage(String message) {

                assertThat(message).isEqualTo("Hello");
                // close it now:
                spc.close();
            }
        });


        spc.connect();

        // wait until the close was called
        closeLatch.await(2000, TimeUnit.MILLISECONDS);
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
