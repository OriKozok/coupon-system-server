package com.example.CouponSystemServer.exceptions;

public class CompanyAlreadyExistsException extends Exception
{

    public CompanyAlreadyExistsException()
    {
        super("Error! Company already exists!");
    }
}
