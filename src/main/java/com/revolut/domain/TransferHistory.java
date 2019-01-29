package com.revolut.domain;

import com.revolut.constant.BankingCurrency;
import com.revolut.constant.Status;
import com.revolut.representation.TransferJson;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ayomide on 1/24/2019.
 */
@Entity(name = "AccountTransferHistory")
public class TransferHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transferId;

    @Column(name = "transactionId")
    private String transactionId;

    @ManyToOne(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY,targetEntity = Account.class)
    @JoinColumn(name = "sourceAcctId")
    private Account sourceAcctId;

    @ManyToOne(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY,targetEntity = Account.class)
    @JoinColumn(name = "destAcctId")
    private Account destAcctId;

    @Column(name = "amount")
    private BigDecimal amount;


    @Column(name="bankingCurrency")
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "time_stamp")
    private Date createdDate;

    public TransferHistory(){
        transactionId = UUID.randomUUID().toString();
    }



    public TransferHistory(Account sourceAcctId, Account destAcctId, BigDecimal amount, String currency, String status, Date createdDate) {
        this();
        this.sourceAcctId = sourceAcctId;
        this.destAcctId = destAcctId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdDate = createdDate;
    }


    public static TransferHistory executeTransaction(TransferJson transferJson,Account sourceAcct, Account destAcct){
        TransferHistory transferHistory = new TransferHistory();
        transferHistory.amount = transferJson.getAmountTransfer();
        transferHistory.transactionId = UUID.randomUUID().toString();
        transferHistory.createdDate = new Date();
        transferHistory.currency = transferJson.getCurrency();
        transferHistory.sourceAcctId = sourceAcct;
        transferHistory.destAcctId = destAcct;
        if(transferHistory.getAmount().compareTo(sourceAcct.getBalance())<=0){
            transferHistory.status = Status.Success.getState();
        }else{
            transferHistory.status  = Status.Failed.getState();
        }
        return transferHistory;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Account getSourceAcctId() {
        return sourceAcctId;
    }

    public void setSourceAcctId(Account sourceAcctId) {
        this.sourceAcctId = sourceAcctId;
    }

    public Account getDestAcctId() {
        return destAcctId;
    }

    public void setDestAcctId(Account destAcctId) {
        this.destAcctId = destAcctId;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
