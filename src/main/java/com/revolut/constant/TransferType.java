package com.revolut.constant;

/**
 * Created by ayomide on 1/28/2019.
 */
public enum TransferType {
    Credited("Credited"),Debited("Debited"),AccountCreation("AccountCreation");
    private String transferType;

    TransferType(String transferType){
        this.transferType = transferType;
    }

    public String getTransferType() {
        return transferType;
    }
}
