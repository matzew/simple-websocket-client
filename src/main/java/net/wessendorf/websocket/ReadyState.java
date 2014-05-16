package net.wessendorf.websocket;

/**
 *  The readyState attribute represents the state of the connection.
 *
 *  This JavaDoc was taken from W3C.org website (http://dev.w3.org/html5/websockets/)
 */
public enum ReadyState {

    /**
     * The connection has not yet been established.
     */
    CONNECTING,

    /**
     * The WebSocket connection is established and communication is possible.
     */
    OPEN,

    /**
     * The connection is going through the closing handshake, or the close() method has been invoked.
     */
    CLOSING,

    /**
     * The connection has been closed or could not be opened.
     */
    CLOSED;
}
