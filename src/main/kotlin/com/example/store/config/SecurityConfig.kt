package com.example.store.config

import com.example.store.security.DomainUserDetailsService
import com.example.store.security.JwtTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
open class SecurityConfig() : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var jwtTokenFilter: JwtTokenFilter

    @Autowired
    private lateinit var domainUserDetailsService: DomainUserDetailsService

    @Throws(Exception::class)
    public override fun configure(auth: AuthenticationManagerBuilder?) {
        if (auth != null) {
            auth.userDetailsService(domainUserDetailsService).passwordEncoder(passwordEncoder())
        }
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity?) {
        // Enable CORS and disable CSRF
        if (http != null) {
            http.cors().and().csrf().disable() // Add JWT token filter
                .addFilterBefore(
                    jwtTokenFilter,
                    UsernamePasswordAuthenticationFilter::class.java
                ) // Set session management to stateless
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and() // Set permissions on endpoints
                .authorizeRequests() // Our public endpoints
                .antMatchers("/api/v1/authenticate").permitAll() // Our private endpoints
                .anyRequest().authenticated()
                .and()
                .httpBasic()
        }
    }

    // Used by Spring Security if CORS is enabled.
    @Bean
    open fun corsFilter(): CorsFilter? {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}