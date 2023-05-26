package com.example.store.service.impl;

import com.example.store.data.Authority;
import com.example.store.data.User;
import com.example.store.dto.UserRequest;
import com.example.store.exception.GenricException;
import com.example.store.repository.UserRepository;
import com.example.store.service.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public User getUser(String username) {
        return userRepository.findByEmail(username).
                orElseThrow(() -> new BadCredentialsException("Username not found"));
    }

    @Override
    public void save(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAuthorities(new Authority(request.getAuthorities().getName()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedDate(LocalDateTime.now());
        List<User> list = userRepository.findAllByEmail(request.getEmail());
        if (!list.isEmpty()) {
            throw new GenricException("Email already exits", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getCredentials().toString();
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        Long userId = Long.valueOf((String) claims.get("userId"));
        return userRepository.findById(userId).orElseThrow(() -> new GenricException("User not Found", HttpStatus.NOT_FOUND));
    }
}
