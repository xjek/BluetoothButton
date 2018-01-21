package com.klaks.evgenij.bluetoothbutton.model;

public class Auth {
    private String token = "";


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public boolean isError() {
        return token.equals("error");
    }

    @Override
    public String toString() {
        return "Auth{" +
                "token='" + token + '\'' +
                '}';
    }
}
