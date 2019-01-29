package com.revolut.constant;

/**
 * Created by ayomide on 1/24/2019.
 */
public enum BankingCurrency {
    USD("USD"),POUND("POUNDS"),EURO("EURO"),YEN("YEN");
    private String currency;
    BankingCurrency(String currency){
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
