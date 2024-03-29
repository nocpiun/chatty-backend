package net.nocpiun.chatty.server;

import net.nocpiun.chatty.main.App;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.*;

public class WebSocketHandler extends BaseWebSocketHandler {
    protected Server server;
    protected App app;

    public WebSocketHandler(Server server) {
        this.server = server;
        app = this.server.getApp();
    }

    public void onOpen(WebSocketConnection conn) {
        //
    }

    public void onClose(WebSocketConnection conn) {
        //
    }

    public void onMessage(WebSocketConnection conn, Packet packet) {
        //
    }

    public void error(WebSocketConnection conn, String reason) {
        conn.send(Packet.create(PacketType.ERROR, reason));
    }
}
