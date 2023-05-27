package com.example.store.controller;

import com.example.store.data.User;
import com.example.store.dto.LoginVM;
import com.example.store.dto.ResponseMessage;
import com.example.store.security.JwtTokenUtil;
import com.example.store.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
public class AuthController {
    private final IUserService userService;

    private final JwtTokenUtil tokenUtil;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getUsername(),
                loginVM.getPassword()
        );

        User user = userService.getUser(loginVM.getUsername());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenUtil.createToken(authentication, true, user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Bearer " + jwt);
        return new ResponseEntity(new ResponseMessage(HttpStatus.OK.value(), "Token Generated Successfully", jwt),
                httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/ping")
    public String ping() {
        return "pong";
    }

}

