package com.example.store.security

import com.example.store.data.User
import com.example.store.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
open class JwtTokenFilter() : OncePerRequestFilter() {

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    private lateinit var userRepository: UserRepository

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest?,
                                  response: HttpServletResponse?,
                                  chain: FilterChain?) {
        // Get authorization header and validate
        val header = request?.getHeader(HttpHeaders.AUTHORIZATION)
        if (header == null || !header.startsWith("Bearer ")) {
            if (chain != null) {
                chain.doFilter(request, response)
            }
            return
        }

        // Get jwt token and validate
        val token = header.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' }
        if (!jwtTokenUtil.validateToken(token)) {
            if (chain != null) {
                chain.doFilter(request, response)
            }
            return
        }

        // Get user identity and set it on the spring security context
        val userDetails = createSpringSecurityUser(
            userRepository
                .findByEmail(jwtTokenUtil.getUsernameFromToken(token))
                ?.orElse(null)
        )
        val authentication = UsernamePasswordAuthenticationToken(
                userDetails, token,
                if (userDetails == null) ArrayList() else userDetails.authorities
        )
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
        if (chain != null) {
            chain.doFilter(request, response)
        }
    }

    private fun createSpringSecurityUser(user: User?): UserDetails {
        val grantedAuthorities: MutableList<GrantedAuthority?> = ArrayList()
        if (user != null) {
            grantedAuthorities.add(SimpleGrantedAuthority(user.getAuthorities()?.getName() ?: ""))
            return org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), grantedAuthorities)
        }
        return org.springframework.security.core.userdetails.User("", "", grantedAuthorities)
    }
}