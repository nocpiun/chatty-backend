package net.nocpiun.chatty.main;

import net.nocpiun.chatty.configuration.Configuration;
import net.nocpiun.chatty.sql.ChattySQL;
import net.nocpiun.chatty.user.UserManager;
import net.nocpiun.chatty.server.Server;

public class App {
    public final ChattySQL sql;
    public final UserManager userManager;
    private final Server server;

    public App() throws Exception {
        final Configuration config = Configuration.get();
        sql = new ChattySQL(config.getSQLServer(), config.getSQLUserName(), config.getSQLPassword());
        userManager = new UserManager(sql);
        server = new Server(this);

        // Start the server
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
        sql.close();
    }
}
