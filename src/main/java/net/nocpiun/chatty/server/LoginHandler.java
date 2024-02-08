package net.nocpiun.chatty.server;

import net.nocpiun.chatty.server.packet_data.LoginPacketData;
import net.nocpiun.chatty.user.User;
import net.nocpiun.chatty.utils.Security;
import org.webbitserver.WebSocketConnection;

import java.util.UUID;

public class LoginHandler extends WebSocketHandler {
    public LoginHandler(Server server) {
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
            case LOGIN:
                LoginPacketData data = (LoginPacketData) packet.getData(LoginPacketData.class);

                try {
                    User user = app.userManager.loginUser(data.userName, Security.md5Encrypt(data.password));
                    conn.send(Packet.create(packet.type, user.token));
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
        }
    }
}
