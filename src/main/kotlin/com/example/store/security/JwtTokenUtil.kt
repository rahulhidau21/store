package com.example.store.security

import com.example.store.data.User
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class JwtTokenUtil {
    @Value("\${jwt.secret}")
    private val secret: String? = null

    @Value("\${jwt.jwtExpirationInMs}")
    private val jwtExpirationInMs = 0

    fun createToken(authentication: Authentication?, rememberMe: Boolean, user: User?): String? {
        val authorities = authentication?.getAuthorities()?.stream()?.map { obj: GrantedAuthority? -> obj?.getAuthority()
            ?: "" }?.collect(Collectors.joining(","))
        val now = Date().time
        val validity: Date
        validity = if (rememberMe) {
            Date(now + jwtExpirationInMs)
        } else {
            Date(now + jwtExpirationInMs)
        }
        val userDetail: MutableMap<String?, Any?> = HashMap()
        if (user != null) {
            userDetail["userId"] = user.getId().toString()
            userDetail["userName"] = user.getName()
        }
        return Jwts
                .builder()
                .setSubject(authentication?.getName())
                .addClaims(userDetail)
                .claim("auth", authorities)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(validity)
                .compact()
    }

    fun validateToken(authToken: String?): Boolean {
        var claims: Jws<Claims?>? = null
        return try {
            // Jwt token has not been tampered with
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken)
            true
        } catch (ex: SignatureException) {
            false
        } catch (ex: MalformedJwtException) {
            false
        } catch (ex: UnsupportedJwtException) {
            false
        } catch (ex: IllegalArgumentException) {
            false
        } catch (ex: ExpiredJwtException) {
            false
        }
    }

    fun getUsernameFromToken(token: String?): String? {
        val claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
        return claims.subject
    }

    fun getRolesFromToken(authToken: String?): MutableList<SimpleGrantedAuthority?>? {
        var roles: MutableList<SimpleGrantedAuthority?>? = null
        val claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).body
        val isAdmin = claims.get("isAdmin", Boolean::class.java)
        val isUser = claims.get("isUser", Boolean::class.java)
        if (isAdmin != null && isAdmin == true) {
            roles = Arrays.asList(SimpleGrantedAuthority("ROLE_ADMIN"))
        }
        if (isUser != null && isUser == true) {
            roles = Arrays.asList(SimpleGrantedAuthority("ROLE_USER"))
        }
        return roles
    }
}