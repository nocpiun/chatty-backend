package net.nocpiun.chatty.utils;

import net.nocpiun.chatty.sql.ChattySQL;

public class Manager {
    protected ChattySQL sql;
    public Manager(ChattySQL sql) {
        this.sql = sql;
    }
}
