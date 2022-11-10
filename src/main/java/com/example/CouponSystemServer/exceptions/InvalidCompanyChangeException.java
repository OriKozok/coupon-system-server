package com.example.CouponSystemServer.exceptions;

public class InvalidCompanyChangeException extends Exception
{

    public InvalidCompanyChangeException() {
        super("Error! You can't change the company's name or id");
    }
}
