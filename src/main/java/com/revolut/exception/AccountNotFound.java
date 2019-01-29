package com.revolut.exception;

/**
 * Created by ayomide on 1/25/2019.
 */
public class AccountNotFound extends Exception {

    public AccountNotFound(){
        super("Account was not found");
    }
    public AccountNotFound(String accountId){
        super("Account was not found "+accountId);
    }

}
