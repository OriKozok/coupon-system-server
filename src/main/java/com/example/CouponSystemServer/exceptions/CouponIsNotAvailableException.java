package com.example.CouponSystemServer.exceptions;

public class CouponIsNotAvailableException extends Exception{


    public CouponIsNotAvailableException() {
        super("Error! This coupon has ran out!");
    }
}
