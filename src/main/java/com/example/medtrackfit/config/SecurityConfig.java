package com.example.medtrackfit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.medtrackfit.services.impl.SecurityCustomUserDetailsService;


@Configuration
public class SecurityConfig {


    @Autowired
    private SecurityCustomUserDetailsService userDetailsService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;
    //configure authentication provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //user detail service ka object
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Autowired
    private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.authorizeHttpRequests(authorize->{
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        //form default login page change related to form login 
        httpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/dashboard");
            // formLogin.failureForwardUrl("/login?error=True");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
            // formLogin.failureHandler(new AuthenticationFailureHandler() {

            //     @Override
            //     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            //             AuthenticationException exception) throws IOException, ServletException {
            //         // TODO Auto-generated method stub
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
            //     }
                
                
            // });
            // formLogin.successHandler(new AuthenticationSuccessHandler() {

            //     @Override
            //     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            //             Authentication authentication) throws IOException, ServletException {
            //         // TODO Auto-generated method stub
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
            //     }
                
            // });
            
        });
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });


        // oauth Configuration

        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        return httpSecurity.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
