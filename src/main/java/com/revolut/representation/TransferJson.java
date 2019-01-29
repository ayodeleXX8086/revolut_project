package com.revolut.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revolut.constant.TransferType;
import com.revolut.domain.TransferHistory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ayomide on 1/27/2019.
 */
public class TransferJson {

    @JsonProperty
    private String sourceAccountId;

    @JsonProperty
    private String destAccountId;

    @JsonProperty
    private BigDecimal amountTransfer;

    @JsonProperty
    private String transactionId;

    @JsonProperty
    private String currency;
    @JsonProperty
    private String state;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date createdTime;

    @JsonProperty
    private String transferType;


    public TransferJson(){
    }

    public TransferJson(String sourceAccountId, String destAccountId, BigDecimal amountTransfer) {
        this.sourceAccountId = sourceAccountId;
        this.destAccountId = destAccountId;
        this.amountTransfer = amountTransfer;
    }

    @JsonIgnore
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(String sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public String getDestAccountId() {
        return destAccountId;
    }

    public void setDestAccountId(String destAccountId) {
        this.destAccountId = destAccountId;
    }

    public BigDecimal getAmountTransfer() {
        return amountTransfer;
    }

    public void setAmountTransfer(BigDecimal amountTransfer) {
        this.amountTransfer = amountTransfer;
    }

    @JsonIgnore
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonIgnore
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonIgnore
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @JsonIgnore
    public String getTransferType(){return transferType;}

    @Override
    public String toString() {
        return "TransferJson{" +
                "sourceAccountId='" + sourceAccountId + '\'' +
                ", destAccountId='" + destAccountId + '\'' +
                ", amountTransfer=" + amountTransfer +
                ", transactionId='" + transactionId + '\'' +
                ", currency='" + currency + '\'' +
                ", state='" + state + '\'' +
                ", createdTime=" + createdTime +
                ", transferType='" + transferType + '\'' +
                '}';
    }

    public static TransferJson getTransferJson(TransferHistory transferHistory, String accountId){
        TransferJson transferJson=new TransferJson();
        transferJson.amountTransfer = transferHistory.getAmount();
        transferJson.createdTime=transferHistory.getCreatedDate();
        transferJson.state = transferHistory.getStatus();
        transferJson.transactionId = transferHistory.getTransactionId();
        transferJson.destAccountId = transferHistory.getDestAcctId()!=null?transferHistory.getDestAcctId().getAccountUniqueId():"";
        transferJson.sourceAccountId =transferHistory.getSourceAcctId()!=null?transferHistory.getSourceAcctId().getAccountUniqueId():"";
        transferJson.currency = transferHistory.getCurrency();
        if(transferHistory.getSourceAcctId()!=null) {
            transferJson.transferType = transferHistory.getDestAcctId().getAccountUniqueId().equals(accountId) ? TransferType.Credited.getTransferType() : TransferType.Debited.getTransferType();
        }else {
            transferJson.transferType = TransferType.AccountCreation.getTransferType() ;
        }
        return transferJson;
    }
}
