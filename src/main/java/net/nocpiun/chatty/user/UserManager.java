package net.nocpiun.chatty.user;

import net.nocpiun.chatty.sql.ChattySQL;

import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class UserManager {
    private ChattySQL sql;

    public UserManager(ChattySQL sql) {
        this.sql = sql;
    }

    public List<User> getUsers() throws Exception {
        sql.statement.execute("use chatty_users;");
        ResultSet rs = sql.statement.executeQuery("select * from user_info;");
        List<User> list = new ArrayList<>();

        while(rs.next()) {
            String userName = rs.getString("name");
            String userToken = rs.getString("token");
            String password = rs.getString("password");
            list.add(new User(userName, userToken, password));
        }
        rs.close();
        return list;
    }

    public void registerUser(User user) throws Exception {
        sql.statement.execute("use chatty_users;");
        ResultSet rs = sql.statement.executeQuery("select * from user_info where name=\""+ user.name +"\";");
        if(rs.next()) {
            throw new Exception("无法注册用户：该用户名已被使用");
        }
        rs.close();
        sql.statement.execute("insert into user_info values ( \""+ user.name +"\", \""+ user.token +"\", \""+ user.password +"\" )");
    }

    public void unregisterUser(User user) throws Exception {
        sql.statement.execute("use chatty_users;");
        ResultSet rs = sql.statement.executeQuery("select * from user_info where name=\""+ user.name +"\";");
        if(!rs.next()) {
            throw new Exception("无法注销用户：无法找到指定用户");
        }
        rs.close();
        sql.statement.execute("delete from user_info where name=\""+ user.name +"\" and token=\""+ user.token +"\" and password=\""+ user.password +"\";");
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
}
