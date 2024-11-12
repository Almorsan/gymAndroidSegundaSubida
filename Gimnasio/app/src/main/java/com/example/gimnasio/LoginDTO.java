package com.example.gimnasio;


public class LoginDTO {
    private String nick;
    private String password;

    // Constructor
    public LoginDTO(String nick, String password) {
        this.nick = nick;
        this.password = password;
    }

    // Getters y Setters
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
