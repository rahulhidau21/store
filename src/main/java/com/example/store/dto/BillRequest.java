package com.example.store.dto;

import lombok.Data;

@Data
public class BillRequest {
    private String productName;
    private Integer price;
    private Integer qty;
    private String type;
}
