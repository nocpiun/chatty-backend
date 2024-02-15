package net.nocpiun.chatty.user;

import com.google.gson.Gson;
import net.nocpiun.chatty.sql.ChattySQL;
import net.nocpiun.chatty.utils.Manager;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class UserManager extends Manager {
    public UserManager(ChattySQL sql) {
        super(sql);
    }

    public User getUserByToken(String token) throws Exception {
        sql.statement.execute("use chatty_users;");
        ResultSet rs = sql.statement.executeQuery("select * from user_info where token=\""+ token +"\";");
        if(!rs.next()) {
            throw new Exception("找不到用户：无效的token");
        }
        String userName = rs.getString("name");
        String userToken = rs.getString("token");
        String password = rs.getString("password");
        rs.close();
        return new User(userName, userToken, password);
    }

    public void registerUser(User user) throws Exception {
        sql.statement.execute("use chatty_users;");
        ResultSet rs = sql.statement.executeQuery("select * from user_info where name=\""+ user.name +"\";");
        if(rs.next()) {
            throw new Exception("无法注册用户：该用户名已被使用");
        }
        rs.close();
        // User Table
        sql.statement.execute("insert into user_info values ( \""+ user.name +"\", \""+ user.token +"\", \""+ user.password +"\" );");
        // Profile Table
        sql.statement.execute("insert into user_profile values ( \""+ user.name +"\", \"[]\" );");
    }

    public void unregisterUser(User user) throws Exception {
        sql.statement.execute("use chatty_users;");
        ResultSet rs = sql.statement.executeQuery("select * from user_info where name=\""+ user.name +"\";");
        if(!rs.next()) {
            throw new Exception("无法注销用户：无法找到指定用户");
        }
        rs.close();
        sql.statement.execute("delete from user_info where name=\""+ user.name +"\" and token=\""+ user.token +"\" and password=\""+ user.password +"\";");
        sql.statement.execute("delete from user_profile where name=\""+ user.name +"\";");
    }

    public User loginUser(String userName, String password) throws Exception {
        sql.statement.execute("use chatty_users;");
        ResultSet rs = sql.statement.executeQuery("select * from user_info where name=\""+ userName +"\";");
        if(!rs.next()) {
            throw new Exception("无法登录：无法找到指定用户");
        }
        final String token = rs.getString("token");
        final String correctPassword = rs.getString("password");
        rs.close();

        if(!password.equals(correctPassword)) {
            throw new Exception("无法登录：密码错误");
        }

        return new User(userName, token, password);
    }

    public Profile getUserProfileByName(String name) throws Exception {
        sql.statement.execute("use chatty_users;");
        ResultSet rs = sql.statement.executeQuery("select * from user_profile where name=\""+ name +"\";");
        if(!rs.next()) {
            throw new Exception("无法获取用户：无法找到指定用户");
        }
        return new Profile(name, new Gson().fromJson(rs.getString("rooms"), List.class));
    }

    public void setUserProfile(String name, Profile profile) throws Exception {
        final String stringifiedRoomIds = new Gson().toJson(profile.rooms);

        sql.statement.execute("use chatty_users;");
        sql.statement.execute("update user_profile set name=\""+ profile.userName +"\", rooms=\'"+ stringifiedRoomIds +"\' where name=\""+ name +"\";");
    }
}
