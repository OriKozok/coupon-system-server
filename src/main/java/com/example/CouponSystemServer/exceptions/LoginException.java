package com.example.CouponSystemServer.exceptions;

public class LoginException extends Exception{
    public LoginException() {
        super("Cannot log in");
    }
}
