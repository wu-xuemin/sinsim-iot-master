package com.eservice.sinsimiot.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.eservice.sinsimiot.core.ServiceException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Class Description: Spring MVC 配置
 *
 * @author Wilson Hu
 * @date 2017-10-25
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        //保留空的字段：String null -> ""，Number null -> 0
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero);
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(converter);
    }


    /**
     * 统一异常处理
     *
     * @param exceptionResolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                e.printStackTrace();
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                try {
                    if (e instanceof HttpClientErrorException) {
                        response.setStatus(((HttpClientErrorException) e).getRawStatusCode());
                        response.getWriter().write(((HttpClientErrorException) e).getResponseBodyAsString());
                    } else if (e instanceof HttpServerErrorException) {
                        response.setStatus(((HttpServerErrorException) e).getRawStatusCode());
                        response.getWriter().write(((HttpServerErrorException) e).getResponseBodyAsString());
                    } else if (e instanceof ServiceException) {
                        response.setStatus(((ServiceException) e).getStatusCode());
                        response.getWriter().write(((ServiceException) e).getMsg());
                    } else if (e instanceof NoHandlerFoundException) {
                        response.setStatus(HttpStatus.NOT_FOUND.value());
                        response.getWriter().write("接口 [" + request.getRequestURI() + "] 不存在");
                    } else {
                        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                        String message;
                        if (handler instanceof HandlerMethod) {
                            HandlerMethod handlerMethod = (HandlerMethod) handler;
                            message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s，请联系管理员",
                                    request.getRequestURI(),
                                    handlerMethod.getBean().getClass().getName(),
                                    handlerMethod.getMethod().getName(),
                                    e.getMessage());
                        } else {
                            message = e.getMessage();
                        }
                        response.getWriter().write(message);
                    }
                } catch (Exception exception) {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
                return new ModelAndView();
            }
        });
    }

    /**
     * 解决跨域问题
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //允许跨域的域名，可以用*表示允许任何域名使用
                .allowedOrigins("*")
                //允许任何方法（post、get等）
                .allowedMethods("*")
                //允许任何请求头
                .allowedHeaders("*")
                //带上cookie信息
                .allowCredentials(true)
                //maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor());
        //接口签名认证拦截器，该签名认证比较简单，实际项目中可以使用Json Web Token或其他更好的方式替代。
    }
}
