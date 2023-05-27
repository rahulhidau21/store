package com.example.store.service.impl;

import com.example.store.data.User;
import com.example.store.dto.BillRequest;
import com.example.store.dto.BillResponse;
import com.example.store.service.IBillService;
import com.example.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillService implements IBillService {
    public static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    public static final String ROLE_AFFILIATE = "ROLE_AFFILIATE";
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    @Autowired
    private IUserService userService;

    @Override
    public BillResponse generate(List<BillRequest> request) {
        User user = userService.getCurrentUser();

        int discount = 0;
        if (user.getAuthorities().getName().equals(ROLE_EMPLOYEE)) {
            discount = 30;
        } else if (user.getAuthorities().getName().equals(ROLE_AFFILIATE)) {
            discount = 10;
        } else if (user.getAuthorities().getName().equals(ROLE_CUSTOMER)) {
            if(LocalDateTime.now().minusYears(2).isAfter(user.getCreatedDate())){
                discount = 5;
            }
        }

        int itemsTotal = 0;
        for (BillRequest item : request) {
            int itemtotal = item.getPrice() * item.getQty();
            if (!item.getType().equals("groceries") && discount != 0) {
                itemtotal = itemtotal - ((itemtotal * discount) / 100);
            }
            itemsTotal = itemsTotal + itemtotal;
        }

        // Discount of 5 on every 100
        itemsTotal = itemsTotal - (itemsTotal / 100) * 5;

        return BillResponse.builder().netPayableAmount(itemsTotal).build();
    }

}
