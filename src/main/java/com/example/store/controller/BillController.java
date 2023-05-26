package com.example.store.controller;

import com.example.store.dto.BillRequest;
import com.example.store.dto.ResponseMessage;
import com.example.store.service.IBillService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bill/")
@AllArgsConstructor
public class BillController {
    private final IBillService billService;

    @PostMapping("generate")
    public ResponseEntity<ResponseMessage> generate(@RequestBody List<BillRequest> request){
        return new ResponseEntity(new ResponseMessage(HttpStatus.OK.value(),
                "Bill Generated Successfully", billService.generate(request) ),
                HttpStatus.OK);
    }
}
