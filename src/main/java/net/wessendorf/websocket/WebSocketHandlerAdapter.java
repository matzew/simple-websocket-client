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

import java.nio.ByteBuffer;

/**
 * Convenience implementation for the WebSocketHandler
 */
public class WebSocketHandlerAdapter implements WebSocketHandler {

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose(int closeCode, String reason) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onMessage(ByteBuffer message) {

    }
}
