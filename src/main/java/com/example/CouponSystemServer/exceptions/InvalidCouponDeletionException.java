package com.example.CouponSystemServer.exceptions;

public class InvalidCouponDeletionException extends Exception{

    public InvalidCouponDeletionException() {
        super("Error! Can't delete a coupon that is not by your company!");
    }
}
