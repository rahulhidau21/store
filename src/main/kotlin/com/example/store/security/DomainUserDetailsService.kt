package com.example.store.security

import com.example.store.data.User
import com.example.store.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component("userDetailsService")
class DomainUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(s: String?): UserDetails? {
        return userRepository.findByEmail(s)
            ?.map { user: User? -> createSpringSecurityUser(s, user) }
            ?.orElseThrow { BadCredentialsException("Invalid Username Or Password") }
    }

    private fun createSpringSecurityUser(lowercaseLogin: String?, user: User?): UserDetails {
        val grantedAuthorities: MutableList<GrantedAuthority?> = ArrayList()
        if (user != null) {
            grantedAuthorities.add(SimpleGrantedAuthority(user.getAuthorities()?.getName() ?: ""))
            return org.springframework.security.core.userdetails.User(
                lowercaseLogin,
                user.getPassword(),
                grantedAuthorities
            )
        } else {
            return org.springframework.security.core.userdetails.User("", "", grantedAuthorities)
        }
    }
}