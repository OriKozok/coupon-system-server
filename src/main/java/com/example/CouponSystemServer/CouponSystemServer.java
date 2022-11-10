package com.example.CouponSystemServer;

import com.example.CouponSystemServer.beans.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

@SpringBootApplication
public class CouponSystemServer {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =SpringApplication.run(CouponSystemServer.class, args);
		Test test = ctx.getBean(Test.class);
		test.runApplication();

	}

	@Bean
	public HashMap<String, OurSession> sessions(){
		return new HashMap<String, OurSession>();
	}

}
