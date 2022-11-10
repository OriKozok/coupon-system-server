package com.example.CouponSystemServer;

import com.example.CouponSystemServer.utils.CouponExpirationDailyJob;
import com.example.CouponSystemServer.utils.LoginManager;
import com.example.CouponSystemServer.utils.SessionsJob;
import org.springframework.stereotype.Service;

@Service
public class Test {

    private LoginManager loginManager;
    private CouponExpirationDailyJob job;
    private SessionsJob job2;

    public Test(LoginManager loginManager, CouponExpirationDailyJob job, SessionsJob job2) {
        this.loginManager = loginManager;
        this.job = job;
        this.job2 = job2;
    }

    public void runApplication() {
        Thread thread = new Thread(job);
        Thread thread2 = new Thread(job2);
        try {
            thread.start();
            thread2.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            job.stop();
        }
    }
}