package com.example.CouponSystemServer.exceptions;

public class CustomerAlreadyExistsException extends Exception{

    public CustomerAlreadyExistsException() {
        super("Error! Customer already exists!");
    }
}
