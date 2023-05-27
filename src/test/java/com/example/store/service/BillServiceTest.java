package com.example.store.service;

import com.example.store.data.Authority;
import com.example.store.data.User;
import com.example.store.dto.BillRequest;
import com.example.store.dto.BillResponse;
import com.example.store.service.impl.BillService;
import com.example.store.service.impl.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BillServiceTest {

    @InjectMocks
    BillService billService;

    @Mock
    UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Before
    public void setUp() {
        mockAuthenticationPrincipal();
    }

    @Test
    public void generateBillForEmployee(){
        List<BillRequest> list = getGroceriesList();
        User user = new User();
        user.setAuthorities(new Authority("ROLE_EMPLOYEE"));
        when(userService.getCurrentUser()).thenReturn(user);
        BillResponse response = billService.generate(list);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getNetPayableAmount().toString(), "115");

        List<BillRequest> list2 = getNonGroceriesList();
        BillResponse response2 = billService.generate(list2);
        Assert.assertNotNull(response2);
        Assert.assertEquals(response2.getNetPayableAmount().toString(), "84");

        List<BillRequest> list3 = getGroceriesAndNonGroceriesList();
        BillResponse response3 = billService.generate(list3);
        Assert.assertNotNull(response3);
        Assert.assertEquals(response3.getNetPayableAmount().toString(), "97");
    }

    @Test
    public void generateBillForAffiliate(){
        List<BillRequest> list = getGroceriesList();
        User user = new User();
        user.setAuthorities(new Authority("ROLE_AFFILIATE"));
        when(userService.getCurrentUser()).thenReturn(user);
        BillResponse response = billService.generate(list);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getNetPayableAmount().toString(), "115");

        List<BillRequest> list2 = getNonGroceriesList();
        BillResponse response2 = billService.generate(list2);
        Assert.assertNotNull(response2);
        Assert.assertEquals(response2.getNetPayableAmount().toString(), "103");

        List<BillRequest> list3 = getGroceriesAndNonGroceriesList();
        BillResponse response3 = billService.generate(list3);
        Assert.assertNotNull(response3);
        Assert.assertEquals(response3.getNetPayableAmount().toString(), "109");
    }

    @Test
    public void generateBillForCustomerOlderThanTwoYears(){
        List<BillRequest> list = getGroceriesList();
        User user = new User();
        user.setAuthorities(new Authority("ROLE_CUSTOMER"));
        user.setCreatedDate(LocalDateTime.now().minusDays(1).minusYears(2));
        when(userService.getCurrentUser()).thenReturn(user);
        BillResponse response = billService.generate(list);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getNetPayableAmount().toString(), "115");

        List<BillRequest> list2 = getNonGroceriesList();
        BillResponse response2 = billService.generate(list2);
        Assert.assertNotNull(response2);
        Assert.assertEquals(response2.getNetPayableAmount().toString(), "109");

        List<BillRequest> list3 = getGroceriesAndNonGroceriesList();
        BillResponse response3 = billService.generate(list3);
        Assert.assertNotNull(response3);
        Assert.assertEquals(response3.getNetPayableAmount().toString(), "112");
    }

    @Test
    public void generateBillForCustomer(){
        List<BillRequest> list = getGroceriesList();
        User user = new User();
        user.setAuthorities(new Authority("ROLE_CUSTOMER"));
        user.setCreatedDate(LocalDateTime.now());
        when(userService.getCurrentUser()).thenReturn(user);
        BillResponse response = billService.generate(list);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getNetPayableAmount().toString(), "115");

        List<BillRequest> list2 = getNonGroceriesList();
        BillResponse response2 = billService.generate(list2);
        Assert.assertNotNull(response2);
        Assert.assertEquals(response2.getNetPayableAmount().toString(), "115");

        List<BillRequest> list3 = getGroceriesAndNonGroceriesList();
        BillResponse response3 = billService.generate(list3);
        Assert.assertNotNull(response3);
        Assert.assertEquals(response3.getNetPayableAmount().toString(), "115");
    }


    private List<BillRequest> getGroceriesList() {
        List<BillRequest> billRequests = new ArrayList<>();
        billRequests.add(new BillRequest("test1",60,1,"groceries" ));
        billRequests.add(new BillRequest("test2",60,1,"groceries" ));
        return billRequests;
    }

    private List<BillRequest> getNonGroceriesList() {
        List<BillRequest> billRequests = new ArrayList<>();
        billRequests.add(new BillRequest("test1",60,1,"non-groceries" ));
        billRequests.add(new BillRequest("test2",60,1,"non-groceries" ));
        return billRequests;
    }

    private List<BillRequest> getGroceriesAndNonGroceriesList() {
        List<BillRequest> billRequests = new ArrayList<>();
        billRequests.add(new BillRequest("test1",60,1,"groceries" ));
        billRequests.add(new BillRequest("test2",60,1,"non-groceries" ));
        return billRequests;
    }

    private void mockAuthenticationPrincipal() {
        ReflectionTestUtils.setField(userService, "secret", "3242343");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        // Mock the security context and authentication
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("anonymous"));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities));
        Mockito.when(securityContext.getAuthentication()).thenReturn(
                new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities));
        SecurityContextHolder.setContext(securityContext);
    }

}
