package com.example.CouponSystemServer.exceptions;

public class NoSuchCustomerException extends Exception
{

    public NoSuchCustomerException() {
        super("Error! No such customer!");
    }
}

