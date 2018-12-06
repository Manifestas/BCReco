package com.example.manifest.bcreco.data;

public class DbConnectionParams {

    private String ip;
    private String port;
    private String login;
    private String password;

    public DbConnectionParams(String ip, String port, String login, String password) {
        this.ip = ip;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
