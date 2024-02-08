package net.nocpiun.chatty.server;

import net.nocpiun.chatty.configuration.Configuration;
import net.nocpiun.chatty.main.App;
import net.nocpiun.chatty.sql.ChattySQL;
import net.nocpiun.chatty.user.UserManager;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

public class Server {
    public static int port = 7000; // default port: 7000

    private WebServer serverInstance;
    private App app;

    public Server(App app) throws Exception {
        this.app = app;
        port = Configuration.get().getServerPort();

        serverInstance = WebServers.createWebServer(port);
        serverInstance.add("/gateway", new GatewayHandler(this));
        serverInstance.add("/login", new LoginHandler(this));
        serverInstance.add("/register", new RegisterHandler(this));
    }

    public App getApp() {
        return this.app;
    }

    public void start() {
        serverInstance.start();
        System.out.println("Chatty Server is ready on the port "+ port +"! Type `stop` to stop the server.");
    }

    public void stop() {
        serverInstance.stop();
        System.out.println("Chatty Server stopped.");
    }
}
