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
package net.wessendorf.websocket.tyrus;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

@ServerEndpoint("/echo")
public class EchoEndpoint {

    private static final Logger LOGGER = Logger.getLogger(EchoEndpoint.class.getName());

    @OnMessage
    public void receiveTextMessage(String message, Session session) throws IOException {
        LOGGER.info("Received Text Message");
        session.getBasicRemote().sendText(message);
    }

    @OnMessage
    public void receiveBinaryMessage(ByteBuffer message, Session session) throws IOException {
        LOGGER.info("Received Binary Message");
        session.getBasicRemote().sendBinary(message);
    }
}
