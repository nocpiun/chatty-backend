package net.nocpiun.chatty.chat;

import java.util.Date;

public class Message {
    public int key;
    public String roomId;
    public String sender;
    public String content;
    public long time;

    public Message(int key, String roomId, String sender, String content, long time) {
        this.key = key;
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.time = time;
    }
}
