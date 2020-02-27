package com.nittsu.kinjirou.core.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nittsu.kinjirou.core.entrypoint.RestAuthenticationEntryPoint;
import com.nittsu.kinjirou.core.filter.CustomCorsFilter;
import com.nittsu.kinjirou.identity.security.auth.ajax.AjaxAuthenticationProvider;
import com.nittsu.kinjirou.identity.security.auth.ajax.AjaxLoginProcessingFilter;
import com.nittsu.kinjirou.identity.security.auth.jwt.JwtAuthenticationProvider;
import com.nittsu.kinjirou.identity.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.nittsu.kinjirou.identity.security.auth.jwt.SkipPathRequestMatcher;
import com.nittsu.kinjirou.identity.security.auth.jwt.extrator.TokenExtractor;

/**
 * WebSecurityConfig
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String API_ROOT_URL = "/api/**";
    public static final String REFRESH_TOKEN_URL = "/api/auth/token";
    public static final String AUTHENTICATION_URL = "/api/auth/login";
    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private AjaxAuthenticationProvider ajaxAuthenticationProvider;

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    protected AjaxLoginProcessingFilter ajaxLoginProcessingFilter(String loginEntryPoint) throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint, successHandler,
                failureHandler, objectMapper);

        filter.setAuthenticationManager(this.authenticationManager);

        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter jwtTokenAuthProcessingFilter(List<String> pathsToSkip,
            String pattern) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler,
                tokenExtractor, matcher);

        filter.setAuthenticationManager(this.authenticationManager);

        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(jwtAuthenticationProvider);
        auth.authenticationProvider(ajaxAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(AUTHENTICATION_URL, REFRESH_TOKEN_URL);
        Class<UsernamePasswordAuthenticationFilter> simpleAuthFilter = UsernamePasswordAuthenticationFilter.class;

        http // chain
            .csrf() // disable csrf
            .disable() // We don't need CSRF for JWT based authentication
            // catch exception
            .exceptionHandling() // eol
            .authenticationEntryPoint(authenticationEntryPoint) // eol
            .and() // eol
            // set session policy
            .sessionManagement() // eol
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // eol
            .and() // eol
            // permit authen & refresh url
            .authorizeRequests() // eol
            .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()])) // eol
            .permitAll() // eol
            .and() // eol
            // protected all api
            .authorizeRequests() // eol
            .antMatchers(API_ROOT_URL) // eol
            .authenticated() // Protected API End-points
            .and() // eol
            // accept cors
            .addFilterBefore(new CustomCorsFilter(), simpleAuthFilter) // eol
            // add ajax filter (without token)
            .addFilterBefore(ajaxLoginProcessingFilter(AUTHENTICATION_URL), simpleAuthFilter) // eol
            // add jwt filter (with token)
            .addFilterBefore(jwtTokenAuthProcessingFilter(permitAllEndpointList, API_ROOT_URL), simpleAuthFilter);
    }
}
