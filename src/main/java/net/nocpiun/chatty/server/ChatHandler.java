package net.nocpiun.chatty.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.nocpiun.chatty.chat.Message;
import net.nocpiun.chatty.server.packet_data.AddFriendPacketData;
import net.nocpiun.chatty.server.packet_data.DeleteFriendPacketData;
import net.nocpiun.chatty.server.packet_data.MessagePacketData;
import net.nocpiun.chatty.server.packet_data.RoomPacketData;
import net.nocpiun.chatty.user.Profile;
import net.nocpiun.chatty.chat.Room;
import net.nocpiun.chatty.user.User;
import net.nocpiun.chatty.utils.UniqueID;
import org.webbitserver.WebSocketConnection;

import java.util.*;

public class ChatHandler extends WebSocketHandler {
    private Map<String, WebSocketConnection> connections = new HashMap<>();

    public ChatHandler(Server server) {
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
            case HANDSHAKE:
                try {
                    final User user = app.userManager.getUserByToken((String) packet.data);
                    connections.put(user.name, conn);
                    printAllConnectionNames();
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
            case CLOSE:
                try {
                    final User user = app.userManager.getUserByToken((String) packet.data);
                    connections.remove(user.name);
                    printAllConnectionNames();
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
            case LOGOUT:
                try {
                    User user = app.userManager.getUserByToken((String) packet.data);
                    conn.send(Packet.create(packet.type, null));
                    connections.remove(user.name);
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
            case UNREGISTER:
                try {
                    User user = app.userManager.getUserByToken((String) packet.data);
                    app.userManager.unregisterUser(user);
                    conn.send(Packet.create(packet.type, null));
                    connections.remove(user.name);
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
            case PROFILE:
                try {
                    final User user = app.userManager.getUserByToken((String) packet.data);
                    final Profile profile = app.userManager.getUserProfileByName(user.name);
                    conn.send(Packet.create(packet.type, profile));
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
            case ROOM: {
                RoomPacketData data = (RoomPacketData) packet.getData(RoomPacketData.class);
                Gson gson = new Gson();

                try {
                    final User user = app.userManager.getUserByToken(data.token);
                    final Profile profile = app.userManager.getUserProfileByName(user.name);
                    final List<Room> rooms = gson.fromJson(gson.toJson(profile.rooms), new TypeToken<List<Room>>() {}.getType());

                    for(Room room : rooms) {
                        if(room.id.equals(data.roomId)) {
                            conn.send(Packet.create(packet.type, room));
                        }
                    }
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
            }
            case ROOM_DETAIL:
                try {
                    conn.send(Packet.create(packet.type, app.roomManager.getRoomDetail((String) packet.data)));
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
            case ADD_FRIEND: {
                AddFriendPacketData data = (AddFriendPacketData) packet.getData(AddFriendPacketData.class);

                try {
                    final User user = app.userManager.getUserByToken(data.token);
                    final Profile selfProfile = app.userManager.getUserProfileByName(user.name);
                    final Profile targetProfile = app.userManager.getUserProfileByName(data.friendName);

                    final Room room = new Room(UniqueID.generate(), List.of(user.name, data.friendName));
                    selfProfile.rooms.add(room);
                    targetProfile.rooms.add(room);

                    app.userManager.setUserProfile(user.name, selfProfile);
                    app.userManager.setUserProfile(data.friendName, targetProfile);
                    app.roomManager.createRoom(room);

                    conn.send(Packet.create(packet.type, selfProfile));
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
            }
            case DELETE_FRIEND: {
                DeleteFriendPacketData data = (DeleteFriendPacketData) packet.getData(DeleteFriendPacketData.class);
                Gson gson = new Gson();

                try {
                    final User user = app.userManager.getUserByToken(data.token);
                    final Profile selfProfile = app.userManager.getUserProfileByName(user.name);
                    final List<Room> selfRooms = gson.fromJson(gson.toJson(selfProfile.rooms), new TypeToken<List<Room>>() {}.getType());

                    for(int i = 0; i < selfRooms.size(); i++) {
                        if(selfRooms.get(i).id.equals(data.roomId)) {
                            selfRooms.remove(i);
                        }
                    }
                    selfProfile.rooms = selfRooms;
                    app.userManager.setUserProfile(user.name, selfProfile);

                    try {
                        final Profile targetProfile = app.userManager.getUserProfileByName(data.friendName);
                        final List<Room> targetRooms = gson.fromJson(gson.toJson(targetProfile.rooms), new TypeToken<List<Room>>() {}.getType());

                        for(int i = 0; i < targetRooms.size(); i++) {
                            if(targetRooms.get(i).id.equals(data.roomId)) {
                                targetRooms.remove(i);
                            }
                        }
                        targetProfile.rooms = targetRooms;
                        app.userManager.setUserProfile(data.friendName, targetProfile);
                    } catch (Exception e) {
                        // do nothing...
                    }

                    app.roomManager.deleteRoom(data.roomId);
                    conn.send(Packet.create(packet.type, selfProfile));
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
            }
            case MESSAGE: {
                MessagePacketData data = (MessagePacketData) packet.getData(MessagePacketData.class);
                Gson gson = new Gson();

                try {
                    final User user = app.userManager.getUserByToken(data.token);
                    final Profile profile = app.userManager.getUserProfileByName(user.name);
                    final List<Room> rooms = gson.fromJson(gson.toJson(profile.rooms), new TypeToken<List<Room>>() {}.getType());

                    Message processedMessage = app.roomManager.storeMessage(data);

                    List<String> members = new ArrayList<>() /* for initialization */;
                    for(Room room : rooms) {
                        if(room.id.equals(data.roomId)) {
                            members = room.members;
                        }
                    }

                    for(String userName : members) {
                        connections.forEach((_userName, _conn) -> {
                            if(userName.equals(_userName)) {
                                _conn.send(Packet.create(packet.type, processedMessage));
                            }
                        });
                    }
                } catch (Exception e) {
                    error(conn, e.getMessage());
                }
                break;
            }
        }
    }

    private void printAllConnectionNames() {
        String text = "[Total: "+ connections.size() +"]";
        for(String userName : connections.keySet()) {
            text += ","+ userName;
        }
        System.out.println(text);
    }
}
