package com.example.CouponSystemServer.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(2)
public class TokenFilter extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = "";
        try {
            token = request.getHeader("Authorization").replace("Bearer ", "");
            String type = JWT.decode(token).getClaim("type").asString();
            request.setAttribute("type", type);
            if(!type.equals("ADMINISTRATOR")){
                int id = JWT.decode(token).getClaim("id").asInt();
                request.setAttribute("id", id);
            }
            if(type.equals("CUSTOMER")){
                String firstName = JWT.decode(token).getClaim("firstName").asString();
                String lastName = JWT.decode(token).getClaim("lastName").asString();
            }
            if(type.equals("COMPANY")){
                String name = JWT.decode(token).getClaim("name").asString();
            }
            String email = JWT.decode(token).getClaim("email").asString();
            filterChain.doFilter(request, response); // move to next filter or to the Controller if no more filters

        }catch (JWTDecodeException | NullPointerException e){
            response.setStatus(401);
            response.getWriter().println("Invalid credentials!");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/auth/all") || path.startsWith("/auth/login");
    }
}
