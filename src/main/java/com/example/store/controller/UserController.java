package com.example.store.controller;

import com.example.store.dto.ResponseMessage;
import com.example.store.dto.UserRequest;
import com.example.store.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users/")
public class UserController {

    private IUserService userService;

    @PostMapping("user")
    public ResponseEntity<ResponseMessage> save(@RequestBody UserRequest userRequest) {
        userService.save(userRequest);
        return new ResponseEntity(new ResponseMessage(HttpStatus.OK.value(), "User Saved Successfully" ), HttpStatus.OK);
    }
}
