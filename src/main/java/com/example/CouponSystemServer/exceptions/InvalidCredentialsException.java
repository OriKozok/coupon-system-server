package com.example.CouponSystemServer.exceptions;

public class InvalidCredentialsException extends Exception{
    public InvalidCredentialsException(){
        super("Error! Invalid username or password.");
    }
}
