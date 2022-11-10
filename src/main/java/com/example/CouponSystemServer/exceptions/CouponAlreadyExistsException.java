package com.example.CouponSystemServer.exceptions;

public class CouponAlreadyExistsException extends Exception{
    public CouponAlreadyExistsException()
    {
        super("Error! Coupon already exists!");
    }

}
