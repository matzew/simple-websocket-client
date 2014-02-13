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

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class WebSocketUtilTest {


    @Test(expected = NullPointerException.class)
    public void nullURI() {
        WebSocketUtil.containsWebSocketScheme(null);
    }

    @Test
    public void noSchemeInURI() throws URISyntaxException {
        URI wrongURI = new URI("localhost:9999/echo");
        assertThat(WebSocketUtil.containsWebSocketScheme(wrongURI)).isFalse();
    }

    @Test
    public void wsSchemeInURI() throws URISyntaxException {
        URI wrongURI = new URI("ws://localhost:9999/echo");
        assertThat(WebSocketUtil.containsWebSocketScheme(wrongURI)).isTrue();
    }

    @Test
    public void wssSchemeInURI() throws URISyntaxException {
        URI wrongURI = new URI("wss://localhost:9999/echo");
        assertThat(WebSocketUtil.containsWebSocketScheme(wrongURI)).isTrue();
    }

    @Test(expected = NullPointerException.class)
    public void convertNullURI() throws URISyntaxException {
        WebSocketUtil.applyDefaultPorts(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void convertNoSchemeInURI() throws URISyntaxException {
        URI wrongURI = new URI("localhost/echo");
        WebSocketUtil.applyDefaultPorts(wrongURI);
    }

    @Test
    public void convertWS_SchemeInURI() throws URISyntaxException {
        URI wrongURI = new URI("ws://localhost/echo");
        assertThat(WebSocketUtil.applyDefaultPorts(wrongURI).getPort()).isEqualTo(80);
    }

    @Test
    public void convertWSS_SchemeInURI() throws URISyntaxException {
        URI wrongURI = new URI("wss://localhost/echo");
        assertThat(WebSocketUtil.applyDefaultPorts(wrongURI).getPort()).isEqualTo(443);
    }

    @Test
    public void NoConvertWS_SchemeInURI() throws URISyntaxException {
        URI wrongURI = new URI("ws://localhost:9090/echo");
        assertThat(WebSocketUtil.applyDefaultPorts(wrongURI).getPort()).isEqualTo(9090);
    }
    @Test
    public void NoConvertWSS_SchemeInURI() throws URISyntaxException {
        URI wrongURI = new URI("wss://localhost:9090/echo");
        assertThat(WebSocketUtil.applyDefaultPorts(wrongURI).getPort()).isEqualTo(9090);
    }
}
