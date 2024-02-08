package net.nocpiun.chatty.server;

import com.google.gson.JsonObject;
import net.nocpiun.chatty.server.packet_data.RegisterPacketData;
import net.nocpiun.chatty.user.User;
import net.nocpiun.chatty.utils.Security;
import org.webbitserver.WebSocketConnection;

import java.util.UUID;

public class RegisterHandler extends WebSocketHandler {
    public RegisterHandler(Server server) {
        super(server);
    }

    public void onOpen(WebSocketConnection conn) {
        super.onOpen(conn);
    }

    public void onClose(WebSocketConnection conn) {
        super.onClose(conn);
    }

    public void onMessage(WebSocketConnection conn, String message) {
        Packet packet = Packet.from(message);
        super.onMessage(conn, packet);

        switch(packet.type) {
            case REGISTER:
                final String token = UUID.randomUUID().toString();
                RegisterPacketData data = (RegisterPacketData) packet.getData(RegisterPacketData.class);

                try {
                    app.userManager.registerUser(new User(data.userName, token, Security.md5Encrypt(data.password)));
                    conn.send(Packet.create(packet.type, null));
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
        }
    }
}
