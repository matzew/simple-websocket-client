/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Little handy util for various things around valid WebSocket URIs
 */
public final class WebSocketUtil {

    public static final String WS_SCHEME = "ws";
    public static final String WSS_SCHEME = "wss";

    private WebSocketUtil() {
        // noop
    }


    /**
     * Checks if the given URI is a contains a valid WebSocket scheme
     */
    public static boolean containsWebSocketScheme(URI uri) {
        Objects.requireNonNull(uri, "no URI object given");

        final String scheme = uri.getScheme();
        if (scheme != null && (scheme.equals(WS_SCHEME) || scheme.equals(WSS_SCHEME))) {
            return true;
        }

        return false;
    }

    /**
     * If needed applies the default ports (80 / 443) to the given URI.
     *
     * @param uri the WebSocket URI
     * @return URI containing WebSocket ports
     * @throws IllegalArgumentException if there is a non valid WS URI
     * @throws URISyntaxException given URI can not be parsed
     */
    public static URI applyDefaultPorts(URI uri) throws URISyntaxException {

        // contains WSS or WS...
        if (containsWebSocketScheme(uri)) {
            if (uri.getPort() == -1) {

                if (WS_SCHEME.equals(uri.getScheme())) {
                    return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), 80, uri.getPath(), uri.getQuery(), uri.getFragment());
                } else {
                    // must be WSS...
                    return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), 443, uri.getPath(), uri.getQuery(), uri.getFragment());
                }
            }
            // contains a custom port:
            return uri;
        }
        throw new IllegalArgumentException("Can not apply WebSocket ports to invalid URI scheme");
    }
}
