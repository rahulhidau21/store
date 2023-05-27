# Retail Store Discounts

Spring Boot Java application, which exposes an API such that given a
bill, it finds the net payable amount

please check the attched postman collection for API's
 
# API's

1) authenticate API for generating user jwt token
    
    Type - POST
    
    URL -  localhost:8080/api/v1/authenticate
    
    Body
    {
        "username":"customer@g.com",
        "password":"123456"
    }
   
      Response
      `{
      "status": 200,
      "message": "Token Generated Successfully",
      "data": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXN0b21lcjJAZy5jb20iLCJ1c2VyTmFtZSI6IkN1c3RvbWVyIDIiLCJ1c2VySWQiOiI1IiwiYXV0aCI6IlJPTEVfQ1VTVE9NRVIiLCJleHAiOjE2ODUxODk3ODZ9.lg58-nbT3O-LlS2WOuypF2iofW3s7xTwo9_-DtJCu5Cindne5wp-Xc76chMzauP-F8ftX3w_rwdHwHN4Wi7oZQ"
      }`
    
    
    `Use below credential role wise
    
    employee@g.com	ROLE_EMPLOYEE  123456
    
    affiliate@g.com	ROLE_AFFILIATE 123456
    
    customer@g.com	ROLE_CUSTOMER  123456 (Note: this customer is older than 2 years)
    
    customer2@g.com	ROLE_CUSTOMER  123456`


2) Bill generate API
   
    Type - POST
   
    Headers - Bearer Token
    
    URL - localhost:8080/api/v1/bill/generate
    
    Body
            `[
               {
               "productName": "item 1 ",
               "price": 60,
               "qty": 1,
               "type": "groceries"
               },
               {
               "productName": "item 2 ",
               "price": 60,
               "qty": 1,
               "type": "non-groceries"
               }
               ]`

    Response 
    `{
   "status": 200,
   "message": "Bill Generated Successfully",
   "data": {
   "netPayableAmount": 115
   }
   }`