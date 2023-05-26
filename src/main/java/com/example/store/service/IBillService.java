package com.example.store.service;


import com.example.store.dto.BillRequest;
import com.example.store.dto.BillResponse;

import java.util.List;

public interface IBillService {

    BillResponse generate(List<BillRequest> request);

}
