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

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 * A simplified WebSocket client, based on the JSR 356 client API.
 *
 * The client is fairly simple to use, and hides the JSR API
 *
 */
public class SimpleWebSocketClient {

    private final URI websocketURI;
    private final WebSocketContainer container = ContainerProvider.getWebSocketContainer();

    private ReadyState readyState = ReadyState.CLOSED;
    private Session webSocketSession;
    private WebSocketHandler webSocketHandler;

    /**
     * The @{ReadyState} for the underlying connection
     */
    public ReadyState getReadyState() {
        return readyState;
    }

    /**
     * Applying the handler class to react on the different WebSocket events.
     * @param webSocketHandler
     */
    public void setWebSocketHandler(final WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }


    /**
     * Creates new WebSocket client for given address
     */
    public SimpleWebSocketClient(final URI websocketURI) throws URISyntaxException {
        this.websocketURI =  WebSocketUtil.applyDefaultPorts(websocketURI);
    }


    /**
     * Creates new WebSocket client for given address
     */
    public SimpleWebSocketClient(final String websocketURI) throws URISyntaxException {
        this(new URI(websocketURI));
    }


    /**
     * Establishes the connection to the given WebSocket Server Address.
     */
    public void connect() {

        readyState = ReadyState.CONNECTING;

        try {
            if (webSocketHandler == null) {
                webSocketHandler = new WebSocketHandlerAdapter();
            }

            container.connectToServer(new SimpleWebSocketClientEndpoint(), ClientEndpointConfig.Builder.create().build(), websocketURI);
        } catch (Exception e) {

            readyState = ReadyState.CLOSED;
            // throws DeploymentException, IOException
            throw new RuntimeException("could not establish connection");

        }
    }

    /**
     * Shutting down the current connection.
     */
    public void close() {
        readyState = ReadyState.CLOSING;

        try {
            webSocketSession.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sends a text base payload
     * @param payload the payload string
     */
    public void sendText(final String payload) {
        getRemote().sendText(payload);
    }

    /**
     * Sends binary payload
     * @param payload the binary payload
     */
    public void sendBinary(final ByteBuffer payload) {
        getRemote().sendBinary(payload);

    }

    /**
     * Returns the <code>URI</code> of the connection
     */
    public URI getWebsocketURI() {
        return websocketURI;
    }

    /**
     * Helper to get the async remote endpoint.
     */
    private RemoteEndpoint.Async getRemote() {
        return webSocketSession.getAsyncRemote();
    }

    /**
     * Internal helper that implements all the mess from the JSR.
     * Currently provides code for 'onOpen', 'onClose', 'onError' and 'onMessage'
     */
    private class SimpleWebSocketClientEndpoint extends Endpoint {

        @Override
        public void onOpen(final Session session, final EndpointConfig config) {
            readyState = ReadyState.OPEN;
            webSocketSession = session;

            // callback:
            webSocketHandler.onOpen();

            // text handler:
            webSocketSession.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    webSocketHandler.onMessage(message);

                }
            });

            // binary handler
            webSocketSession.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {
                @Override
                public void onMessage(ByteBuffer message) {
                    webSocketHandler.onMessage(message);
                }
            });
        }

        public void onClose(final Session session, final CloseReason closeReason) {
            readyState = ReadyState.CLOSED;
            webSocketHandler.onClose(closeReason.getCloseCode().getCode(), closeReason.getReasonPhrase());
        }

        public void onError(final Session session, final Throwable throwable) {
            readyState = ReadyState.CLOSED;
            throwable.printStackTrace();
            webSocketHandler.onError(throwable);
        }
    }
}
