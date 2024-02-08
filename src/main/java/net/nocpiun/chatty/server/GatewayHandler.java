package net.nocpiun.chatty.server;

import org.webbitserver.WebSocketConnection;

import java.util.UUID;

public class GatewayHandler extends WebSocketHandler {
    public GatewayHandler(Server server) {
        super(server);
    }

    public void onOpen(WebSocketConnection conn) {
        final String id = UUID.randomUUID().toString();

        conn.send(Packet.create(PacketType.HANDSHAKE, id));
        System.out.println("Client connected, ID: "+ id);
        super.onOpen(conn, id);
    }

    public void onClose(WebSocketConnection conn) {
        super.onClose(conn);
    }

    public void onMessage(WebSocketConnection conn, String message) {
        Packet packet = Packet.from(message);
        super.onMessage(conn, packet);
    }
}
