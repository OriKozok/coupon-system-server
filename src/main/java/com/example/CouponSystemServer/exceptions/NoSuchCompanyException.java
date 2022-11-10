package com.example.CouponSystemServer.exceptions;

public class NoSuchCompanyException extends Exception
{
    public NoSuchCompanyException()
    {
        super("Error! No such company!");
    }
}
