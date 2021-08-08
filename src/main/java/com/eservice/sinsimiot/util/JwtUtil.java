package com.eservice.sinsimiot.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.ServletException;
import java.util.Date;

/**
 * @author Hu Tong
 * @date 2019-11-28
 */
public class JwtUtil {

    private static String VERIFY_KEY = "HanKun";


    private static long TOKEN_EXPIRE = 24 * 60 * 60 * 1000L;

    public static Date EXPIRED_TIME;

    /**
     * Generate token by userName, date,expire date, key
     *
     * @param userName login user name
     * @return
     */
    public static String getToken(String userName, String id) {
        Date time = new Date(System.currentTimeMillis() + TOKEN_EXPIRE);
        EXPIRED_TIME = time;
        return Jwts.builder()
                .setSubject(userName)
                .claim("userName", userName)
                .claim("roleId", id)
                .claim("date", System.currentTimeMillis())
                .setIssuedAt(new Date())
                .setExpiration(time)
                .signWith(SignatureAlgorithm.HS256, VERIFY_KEY)
                .compact();
    }



    /**
     * Check token
     */
    public static void checkToken(String token) throws ServletException {
        try {
            Jwts.parser().setSigningKey(VERIFY_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e1) {
            throw new ServletException("Token expired");
        } catch (Exception e) {
            throw new ServletException("Other token exception");
        }
    }

    public static String getAccount(String token) {
        final Claims claims = Jwts.parser().setSigningKey(VERIFY_KEY).parseClaimsJws(token).getBody();
        String userName = (String) claims.get("userName");
        return userName;
    }


}
