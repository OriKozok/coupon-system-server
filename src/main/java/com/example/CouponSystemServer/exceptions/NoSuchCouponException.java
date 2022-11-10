package com.example.CouponSystemServer.exceptions;

public class NoSuchCouponException extends Exception
{

    public NoSuchCouponException() {
        super("Error! No such coupon!");
    }
}
