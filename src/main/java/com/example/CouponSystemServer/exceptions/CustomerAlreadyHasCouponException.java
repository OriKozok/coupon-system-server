package com.example.CouponSystemServer.exceptions;

public class CustomerAlreadyHasCouponException extends Exception{
    public CustomerAlreadyHasCouponException()
    {
        super("Error! The customer already has that coupon!");
    }
}
