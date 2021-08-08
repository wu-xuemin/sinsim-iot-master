package com.eservice.sinsimiot.config;


import com.eservice.sinsimiot.core.Constant;
import com.eservice.sinsimiot.core.ServiceException;
import com.eservice.sinsimiot.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HT
 * @date 2019-11-28
 * Token Interceptor
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        //自动排除生成token的路径,并且如果是options请求是cors跨域预请求，设置allow对应头信息
        if (request.getRequestURI().contains(Constant.LOGIN_URL)
                || request.getRequestURI().contains(Constant.VERIFY_URL)
                || request.getRequestURI().contains(Constant.ROLE_URL)
                || request.getRequestURI().contains(Constant.LOGOUT_URL)
                || request.getRequestURI().contains(Constant.PUSH_URL)
                || request.getRequestURI().contains(Constant.IMAGE_URL)
                || RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }
        //获取header信息
        String token = request.getHeader("token");

        if (token == null || token.trim().isEmpty()) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), "Can not get token.");
        }
        try {
            JwtUtil.checkToken(token);
            return true;
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }
}
