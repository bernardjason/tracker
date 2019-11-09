package org.bjason.microservices.users;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@RedisHash("User")
public class User implements  Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    protected Integer userId;

    @Indexed
    protected String userName;

    @Indexed
    protected String userPassword;


    public User(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
        String s = null;
        s = aInputStream.readUTF();
        System.out.println("XXXXXXXXXXXXXXXXXXX " + s);
        ObjectMapper mapper = new ObjectMapper();
        User u = mapper.readValue(s, User.class);
        this.userId = u.userId;
        this.userName = u.userName;
        this.userPassword = u.userPassword;
    }


    private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAA");
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(this);
        System.out.println("YYYYYYYYYYYYYYYYYY " + s);
        aOutputStream.writeUTF(s);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
