package com.revolut.domain;

import com.revolut.constant.BankingCurrency;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ayomide on 1/24/2019.
 */
@Entity(name = "BankingAccount")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="accountId",unique = true,nullable = false)
    private String accountUniqueId;

    @Column(name="bankingCurrency")
    private String bankingCurrency;

    @OneToMany(mappedBy = "sourceAcctId",targetEntity = TransferHistory.class)
    private Set<TransferHistory> debitTransfer = new HashSet<>();

    @OneToMany(mappedBy = "destAcctId",targetEntity = TransferHistory.class)
    private Set<TransferHistory> creditTransfer = new HashSet<>();

    @Column(name = "balance")
    private BigDecimal balance;

    /* We using version to make sure two concurrent request don't update the Account at the same time
        These approach is known as optimistic locking
     */
    @Version
    private Integer versionId;

    @Column(name = "time_stamp")
    private Date updateTime;

    @Column(name = "creation_date")
    private Date  creationDate;


    public Account(){
    }



    public Account(String accountUniqueId, String bankingCurrency, BigDecimal balance, Date updateTime) {
        this.accountUniqueId = accountUniqueId;
        this.bankingCurrency = bankingCurrency.toUpperCase();
        this.balance = balance;
        this.updateTime = updateTime;
        this.creationDate = new Date();
    }

    public Long getId() {
        return id;

    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public void setAccountUniqueId(String accountUniqueId) {
        this.accountUniqueId = accountUniqueId;
    }

    public Date getCreationDate(){
        return creationDate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountUniqueId='" + accountUniqueId + '\'' +
                ", bankingCurrency='" + bankingCurrency + '\'' +
                ", balance=" + balance +
                ", versionId=" + versionId +
                ", updateTime=" + updateTime +
                '}';
    }

    public String getAccountUniqueId() {
        return accountUniqueId;
    }


}
