package com.example.CouponSystemServer.exceptions;

public class InvalidCouponDateException extends Exception{

    public InvalidCouponDateException(){
        super("Error! Current time is not between coupon's start and end date");
    }
}
