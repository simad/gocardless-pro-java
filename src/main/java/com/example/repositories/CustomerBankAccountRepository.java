


package com.gocardless.pro.repositories;

import com.gocardless.pro.GoCardlessHttpClient;
import com.gocardless.pro.resources.CustomerBankAccount;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;

public class CustomerBankAccountRepository {
    private GoCardlessHttpClient httpClient;

    public CustomerBankAccountRepository(GoCardlessHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    
        
        public CustomerBankAccount create() throws IOException {
        
            throw new IllegalStateException("Not implemented!");
        
        }
    
        
        public CustomerBankAccount list() throws IOException {
        
            throw new IllegalStateException("Not implemented!");
        
        }
    
        
        public CustomerBankAccount get(String identity) throws IOException {
        
            ImmutableMap.Builder<String, String> params = ImmutableMap.builder();

            
                params.put("identity", identity);
            

            return httpClient.get("/customer_bank_accounts/:identity", params.build(), "customer_bank_accounts", CustomerBankAccount.class);
        
        }
    
        
        public CustomerBankAccount update(String identity) throws IOException {
        
            throw new IllegalStateException("Not implemented!");
        
        }
    
        
        public CustomerBankAccount disable(String identity) throws IOException {
        
            throw new IllegalStateException("Not implemented!");
        
        }
    
}