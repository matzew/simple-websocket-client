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

import net.wessendorf.websocket.AbstractSimpleClientTest;
import org.glassfish.tyrus.server.Server;
import org.junit.After;
import org.junit.Before;

import javax.websocket.DeploymentException;

public class TyrusContainer extends AbstractSimpleClientTest {

    private Server server;

    @Before
    public void bootTyrus() {
        server = new Server("localhost", 9999, "/", null, EchoEndpoint.class);

        try {
            server.start();
        } catch (DeploymentException e) {
            e.printStackTrace();

            server.stop();
        }
    }

    @After
    public void shutTyrus() {
        server.stop();
    }
}
