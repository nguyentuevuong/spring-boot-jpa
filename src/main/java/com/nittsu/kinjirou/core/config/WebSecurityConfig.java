package com.nittsu.kinjirou.core.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
@ComponentScan(basePackages = { "com.nittsu.kinjirou" })
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String API_ROOT_URL = "/api/**";
    private static final String REFRESH_TOKEN_URL = "/api/auth/token";
    private static final String AUTHENTICATION_URL = "/api/auth/login";
    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CorsFilter customCorsFilter;

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

    protected AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(AUTHENTICATION_URL, successHandler,
                failureHandler, objectMapper);

        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter jwtAuthProcessingFilter(List<String> pathsToSkip)
            throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, API_ROOT_URL);

        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(matcher,
                tokenExtractor, failureHandler);

        filter.setAuthenticationManager(authenticationManager);

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
        List<String> permitEndpointList = Arrays.asList(AUTHENTICATION_URL, REFRESH_TOKEN_URL);
        Class<UsernamePasswordAuthenticationFilter> simpleAuthFilter = UsernamePasswordAuthenticationFilter.class;

        http
                // chain
                // We don't need CSRF for JWT based authentication
                .csrf().disable()
                // set session policy
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // permit authen & refresh url
                .authorizeRequests().antMatchers(permitEndpointList.toArray(new String[0])).permitAll().and()
                // authen all request
                .authorizeRequests().antMatchers(API_ROOT_URL).authenticated().and()
                // accept cors
                .addFilterBefore(customCorsFilter, CorsFilter.class)
                // add ajax filter (without token)
                .addFilterBefore(ajaxLoginProcessingFilter(), simpleAuthFilter)
                // add jwt filter (with token)
                .addFilterBefore(jwtAuthProcessingFilter(permitEndpointList), simpleAuthFilter);
    }
}
