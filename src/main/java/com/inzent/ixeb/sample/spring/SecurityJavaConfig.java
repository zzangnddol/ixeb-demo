package com.inzent.ixeb.sample.spring;

import com.inzent.ixeb.sample.security.MySavedRequestAwareAuthenticationSuccessHandler;
import com.inzent.ixeb.sample.security.RestAuthenticationEntryPoint;
import com.inzent.ixeb.sample.web.error.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("com.inzent.ixeb.sample.security")
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;

    private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    public SecurityJavaConfig() {
        super();
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication()
        //        .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
        //        .and()
        //        .withUser("user").password(encoder().encode("userPass")).roles("USER");
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select user_id, user_pw, 1 as enabled from userfile where user_id = ?")
                .authoritiesByUsernameQuery("select user_id, 'ROLE_ADMIN' as role from userfile where user_id = ?");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/js/**")
                .antMatchers("/css/**")
                .antMatchers("/images/**")
                .antMatchers("/ixeb/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                //.authorizeRequests()
                //.and()
                //.exceptionHandling()
                //.accessDeniedHandler(accessDeniedHandler)
                //.authenticationEntryPoint(restAuthenticationEntryPoint)
                //.and()
                .authorizeRequests()
                .antMatchers("/api/account/find").permitAll()

                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/csrfAttacker*").permitAll()
                .antMatchers("/api/customer/**").permitAll()
                .antMatchers("/api/foos/**").authenticated()
                .antMatchers("/api/async/**").permitAll()

                .antMatchers("/screen/COM_login.html").permitAll()
                .antMatchers("/screen/Com_userFind.html").permitAll()

                .antMatchers("/**").hasRole("ADMIN")

                .and()
                .formLogin()
                .loginPage("/screen/COM_login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/frame.html", true)
                //.successHandler(mySuccessHandler)
                //.failureHandler(myFailureHandler)

                .and()
                .httpBasic()

                .and()
                .logout();
    }

    @Bean
    public PasswordEncoder encoder() {
        //return new BCryptPasswordEncoder();
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return charSequence.toString().equals(s);
            }
        };
    }

}