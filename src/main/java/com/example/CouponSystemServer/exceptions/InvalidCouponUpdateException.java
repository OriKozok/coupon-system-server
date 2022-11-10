package com.example.CouponSystemServer.exceptions;

public class InvalidCouponUpdateException extends Exception{

    public InvalidCouponUpdateException() {
        super("Error! Can't update the coupon!");
    }
}
