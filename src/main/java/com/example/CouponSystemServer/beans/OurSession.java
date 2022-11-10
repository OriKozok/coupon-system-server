package com.example.CouponSystemServer.beans;

import com.example.CouponSystemServer.facades.ClientFacade;

public class OurSession {
    private ClientFacade facade;
    private long lastActive;

    public OurSession(ClientFacade facade, long lastActive) {
        this.facade = facade;
        this.lastActive = lastActive;
    }

    public ClientFacade getFacade() {
        return facade;
    }

    public long getLastActive() {
        return lastActive;
    }

    public void setLastActive(long lastActive) {
        this.lastActive = lastActive;
    }
}

