package com.example.CouponSystemServer.utils;

import com.example.CouponSystemServer.beans.OurSession;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SessionsJob implements Runnable{

    private HashMap<Integer, OurSession> sessions;
    long limit = 1000*60*30; //You need to remove the session after 30 minutes


    public SessionsJob(HashMap<Integer, OurSession> sessions) {
        this.sessions = sessions;
    }

    /***
     * This method runs with the server.
     * It checks the sessions map for expired sessions and deletes them.
     */
    @Override
    public void run() {
        try{
        while(true){
            List<Integer> keysExpired = new ArrayList<>();
            for(Map.Entry<Integer, OurSession> session : sessions.entrySet()){
                if(session.getValue().getLastActive() - System.currentTimeMillis() > limit){
                    keysExpired.add(session.getKey());
                }
            }
            for(Integer expiredKey: keysExpired){
                sessions.remove(expiredKey);
            }
                Thread.sleep(1000*60*2);
        }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
