package net.nocpiun.chatty.chat;

import net.nocpiun.chatty.server.packet_data.MessagePacketData;
import net.nocpiun.chatty.sql.ChattySQL;
import net.nocpiun.chatty.utils.Manager;

import java.sql.ResultSet;
import java.util.*;

public class RoomManager extends Manager {
    public RoomManager(ChattySQL sql) {
        super(sql);
    }

    public void createRoom(Room room) throws Exception {
        sql.statement.execute("use chatty_rooms;");
        sql.statement.execute("create table `"+ room.id +"` ( `key` int auto_increment primary key, sender varchar(50) not null, content text, time long not null );");
    }

    public void deleteRoom(String roomId) throws Exception {
        sql.statement.execute("use chatty_rooms;");
        sql.statement.execute("drop table `"+ roomId +"`;");
    }

    public List<Message> getRoomDetail(String roomId) throws Exception {
        sql.statement.execute("use chatty_rooms;");
        ResultSet rs = sql.statement.executeQuery("select * from `"+ roomId +"`;");
        List<Message> result = new ArrayList<>();

        while(rs.next()) {
            result.add(new Message(
                rs.getInt("key"),
                roomId,
                rs.getString("sender"),
                rs.getString("content"),
                rs.getLong("time")
            ));
        }

        return result;
    }

    public Message storeMessage(MessagePacketData messageData) throws Exception {
        sql.statement.execute("use chatty_rooms;");

        final String roomId = messageData.roomId;

        sql.statement.execute("insert into `"+ roomId +"` ( sender, content, time ) values ( \""+ messageData.sender +"\", \""+ messageData.content +"\", "+ new Date().getTime() +" );");
        ResultSet rs = sql.statement.executeQuery("select * from `"+ roomId +"` order by `key` desc limit 1;");
        if(!rs.next()) {
            throw new Exception("无法发送消息：未知错误");
        }
        return new Message(rs.getInt("key"), roomId, rs.getString("sender"), rs.getString("content"), rs.getLong("time"));
    }
}
