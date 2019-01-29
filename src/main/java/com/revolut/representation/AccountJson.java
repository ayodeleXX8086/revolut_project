package com.revolut.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revolut.domain.Account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ayomide on 1/27/2019.
 */

public class AccountJson {

    @JsonProperty
    private String accountId;

    @JsonProperty
    private String bankingCurrency;

    @JsonProperty
    private BigDecimal balance;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date updatedDate = new Date();

    public static AccountJson setAccountEntity(Account account){
        AccountJson accountJson = new AccountJson();
        accountJson.accountId=account.getAccountUniqueId();
        accountJson.balance=account.getBalance();
        accountJson.updatedDate=account.getUpdateTime();
        accountJson.bankingCurrency=account.getBankingCurrency();
        return accountJson;
    }

    public Account generateAccountEntity(){
        Account account=new Account(UUID.randomUUID().toString(),bankingCurrency,balance,updatedDate);
        return account;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBankingCurrency() {
        return bankingCurrency;
    }

    public void setBankingCurrency(String bankingCurrency) {
        this.bankingCurrency = bankingCurrency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }


    public void setUpdatedDate(Date updateTime) {
        this.updatedDate = updateTime;
    }
}
