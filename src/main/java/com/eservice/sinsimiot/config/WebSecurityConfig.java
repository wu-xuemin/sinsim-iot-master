package com.eservice.sinsimiot.config;

//import com.eservice.api.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Bean
//    UserDetailsService customUserService() {
////        return new UserService();
//        return new UserServiceImpl();
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserService());
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setUsernameParameter("account");
        filter.setPasswordParameter("password");
        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                /**
                 * 允许所有用户访问，不需要带JWT
                 */
                //todo: 这里加了，从树莓派直接访问接口，还是会被据，"Can not get token"
//                .antMatchers(HttpMethod.POST, "/iot/machine/updateInfo").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/iot/machine/updateInfo").permitAll()
        ;

    }

}
