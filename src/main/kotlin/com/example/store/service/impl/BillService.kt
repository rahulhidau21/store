package com.example.store.service.impl

import com.example.store.dto.BillRequest
import com.example.store.dto.BillResponse
import com.example.store.service.IBillService
import com.example.store.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BillService : IBillService {
    @Autowired
    private lateinit var userService: IUserService

    override fun generate(request: MutableList<BillRequest?>?): BillResponse? {
        val user = userService?.getCurrentUser()
        var discount = 0
        if (user != null) {
            if (user.getAuthorities()?.getName() == ROLE_EMPLOYEE) {
                discount = 30
            } else if (user.getAuthorities()?.getName() == ROLE_AFFILIATE) {
                discount = 10
            } else if (user.getAuthorities()?.getName() == ROLE_CUSTOMER) {
                if (LocalDateTime.now().minusYears(2).isAfter(user.getCreatedDate())) {
                    discount = 5
                }
            }
        }
        var itemsTotal = 0
        if (request != null) {
            for (item in request) {
                var itemtotal = (item?.getPrice())?.times(item?.getQty()!!)
                if (item != null) {
                    if (item.getType() != "groceries" && discount != 0) {
                        if (itemtotal != null) {
                            itemtotal = itemtotal - itemtotal * discount / 100
                        }
                    }
                }
                itemsTotal = itemsTotal + itemtotal!!
            }
        }

        // Discount of 5 on every 100
        itemsTotal = itemsTotal - itemsTotal / 100 * 5
        val billResponse = BillResponse()
        billResponse.setNetPayableAmount(itemsTotal)
        return billResponse
    }

    companion object {
        val ROLE_EMPLOYEE: String? = "ROLE_EMPLOYEE"
        val ROLE_AFFILIATE: String? = "ROLE_AFFILIATE"
        val ROLE_CUSTOMER: String? = "ROLE_CUSTOMER"
    }
}