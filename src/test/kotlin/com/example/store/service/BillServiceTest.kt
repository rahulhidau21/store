package com.example.store.service

import com.example.store.data.Authority
import com.example.store.data.User
import com.example.store.dto.BillRequest
import com.example.store.service.impl.BillService
import com.example.store.service.impl.UserService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
class BillServiceTest {
    @InjectMocks
    var billService: BillService? = null

    @Mock
    var userService: UserService? = null

    @Mock
    private val securityContext: SecurityContext? = null

    @Mock
    private val authentication: Authentication? = null
    @Before
    fun setUp() {
        mockAuthenticationPrincipal()
    }

    @Test
    fun generateBillForEmployee() {
        val list = getGroceriesList()
        val user = User()
        user.setAuthorities(getAuthority("ROLE_EMPLOYEE"))
        Mockito.`when`(userService?.getCurrentUser()).thenReturn(user)
        val response = billService?.generate(list)
        Assert.assertNotNull(response)
        Assert.assertEquals(response?.getNetPayableAmount().toString(), "115")
        val list2 = getNonGroceriesList()
        val response2 = billService?.generate(list2)
        Assert.assertNotNull(response2)
        Assert.assertEquals(response2?.getNetPayableAmount().toString(), "84")
        val list3 = getGroceriesAndNonGroceriesList()
        val response3 = billService?.generate(list3)
        Assert.assertNotNull(response3)
        Assert.assertEquals(response3?.getNetPayableAmount().toString(), "97")
    }

    @Test
    fun generateBillForAffiliate() {
        val list = getGroceriesList()
        val user = User()
        user.setAuthorities(getAuthority("ROLE_AFFILIATE"))
        Mockito.`when`(userService?.getCurrentUser()).thenReturn(user)
        val response = billService?.generate(list)
        Assert.assertNotNull(response)
        Assert.assertEquals(response?.getNetPayableAmount().toString(), "115")
        val list2 = getNonGroceriesList()
        val response2 = billService?.generate(list2)
        Assert.assertNotNull(response2)
        Assert.assertEquals(response2?.getNetPayableAmount().toString(), "103")
        val list3 = getGroceriesAndNonGroceriesList()
        val response3 = billService?.generate(list3)
        Assert.assertNotNull(response3)
        Assert.assertEquals(response3?.getNetPayableAmount().toString(), "109")
    }

    @Test
    fun generateBillForCustomerOlderThanTwoYears() {
        val list = getGroceriesList()
        val user = User()
        user.setAuthorities(getAuthority("ROLE_CUSTOMER"))
        user.setCreatedDate(LocalDateTime.now().minusDays(1).minusYears(2))
        Mockito.`when`(userService?.getCurrentUser()).thenReturn(user)
        val response = billService?.generate(list)
        Assert.assertNotNull(response)
        Assert.assertEquals(response?.getNetPayableAmount().toString(), "115")
        val list2 = getNonGroceriesList()
        val response2 = billService?.generate(list2)
        Assert.assertNotNull(response2)
        Assert.assertEquals(response2?.getNetPayableAmount().toString(), "109")
        val list3 = getGroceriesAndNonGroceriesList()
        val response3 = billService?.generate(list3)
        Assert.assertNotNull(response3)
        Assert.assertEquals(response3?.getNetPayableAmount().toString(), "112")
    }

    @Test
    fun generateBillForCustomer() {
        val list = getGroceriesList()
        val user = User()
        user.setAuthorities(getAuthority("ROLE_CUSTOMER"))
        user.setCreatedDate(LocalDateTime.now())
        Mockito.`when`(userService?.getCurrentUser()).thenReturn(user)
        val response = billService?.generate(list)
        Assert.assertNotNull(response)
        Assert.assertEquals(response?.getNetPayableAmount().toString(), "115")
        val list2 = getNonGroceriesList()
        val response2 = billService?.generate(list2)
        Assert.assertNotNull(response2)
        Assert.assertEquals(response2?.getNetPayableAmount().toString(), "115")
        val list3 = getGroceriesAndNonGroceriesList()
        val response3 = billService?.generate(list3)
        Assert.assertNotNull(response3)
        Assert.assertEquals(response3?.getNetPayableAmount().toString(), "115")
    }
    private fun getAuthority(name: String): Authority {
        var authority = Authority()
        authority.setName(name)
        return authority
    }

    private fun getGroceriesList(): MutableList<BillRequest?>? {
        val billRequests: MutableList<BillRequest?> = ArrayList()
        billRequests.add(getBillRequest("test1", 60, 1, "groceries"))
        billRequests.add(getBillRequest("test2", 60, 1, "groceries"))
        return billRequests
    }

    private fun getNonGroceriesList(): MutableList<BillRequest?>? {
        val billRequests: MutableList<BillRequest?> = ArrayList()
        billRequests.add(getBillRequest("test1", 60, 1, "non-groceries"))
        billRequests.add(getBillRequest("test2", 60, 1, "non-groceries"))
        return billRequests
    }

    private fun getGroceriesAndNonGroceriesList(): MutableList<BillRequest?>? {
        val billRequests: MutableList<BillRequest?> = ArrayList()
        billRequests.add(getBillRequest("test1", 60, 1, "groceries"))
        billRequests.add(getBillRequest("test2", 60, 1, "non-groceries"))
        return billRequests
    }

    private fun mockAuthenticationPrincipal() {
        ReflectionTestUtils.setField(userService, "secret", "3242343")
        val securityContext = Mockito.mock(SecurityContext::class.java)
        // Mock the security context and authentication
        val authorities: MutableCollection<GrantedAuthority?> = ArrayList()
        authorities.add(SimpleGrantedAuthority("anonymous"))
        securityContext.authentication = UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities)
        Mockito.`when`(securityContext.authentication).thenReturn(
                UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities))
        SecurityContextHolder.setContext(securityContext)
    }

    private fun getBillRequest(name: String, price: Int, qty:Int, type: String): BillRequest {
        var bill = BillRequest();
        bill.setPrice(price)
        bill.setQty(qty)
        bill.setType(type)
        return bill
    }
}
