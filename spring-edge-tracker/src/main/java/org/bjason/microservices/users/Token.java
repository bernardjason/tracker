package org.bjason.microservices.users;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Token implements Serializable {
    String token;
    Integer userId;
    LocalDateTime expire;
    public Token(Integer userId,String token, long expireSeconds){
        this.token = token;
        this.userId = userId;
        LocalDateTime now = LocalDateTime.now();
        expire = now.plusSeconds(expireSeconds);
    }

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(expire);
    }

    public String getToken() {
        return token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", userId=" + userId +
                ", expire=" + expire +
                '}';
    }
}
