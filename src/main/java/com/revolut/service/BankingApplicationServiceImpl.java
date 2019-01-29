package com.revolut.service;

import com.revolut.constant.BankingCurrency;
import com.revolut.constant.Status;
import com.revolut.domain.Account;
import com.revolut.domain.TransferHistory;
import com.revolut.exception.AccountNotFound;
import com.revolut.exception.NoTransactionRecordFound;
import com.revolut.repository.AccountingManagement;
import com.revolut.repository.TransactionHistoryManagement;
import com.revolut.representation.AccountJson;
import com.revolut.representation.TransferJson;
import com.revolut.restapi.BankingRestApi;
import com.revolut.restapi.exception.RestException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ayomide on 1/29/2019.
 */
public class BankingApplicationServiceImpl implements BankingApplicationService {

    AccountingManagement accountingManagement;
    TransactionHistoryManagement transferHistoryManagement;
    static Logger log = Logger.getLogger(BankingApplicationServiceImpl.class);
    static Map<List<String>,Double>currencyRate = new HashMap<>();
    static Set<String>bankingCurrencies= new HashSet<>();
    static {
        bankingCurrencies.add(BankingCurrency.EURO.getCurrency());
        bankingCurrencies.add(BankingCurrency.POUND.getCurrency());
        bankingCurrencies.add(BankingCurrency.USD.getCurrency());
        bankingCurrencies.add(BankingCurrency.YEN.getCurrency());
    }

    static {
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.USD.getCurrency(),BankingCurrency.USD.getCurrency()))
                ,1.0);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.POUND.getCurrency(),BankingCurrency.POUND.getCurrency()))
                ,1.0);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.EURO.getCurrency(),BankingCurrency.EURO.getCurrency()))
                ,1.0);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.YEN.getCurrency(),BankingCurrency.YEN.getCurrency()))
                ,1.0);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.USD.getCurrency(),BankingCurrency.EURO.getCurrency()))
                        ,0.87);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.USD.getCurrency(),BankingCurrency.POUND.getCurrency()))
                   ,0.76);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.USD.getCurrency(),BankingCurrency.YEN.getCurrency()))
                ,109.34);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.EURO.getCurrency(),BankingCurrency.POUND.getCurrency()))
                ,0.87);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.EURO.getCurrency(),BankingCurrency.USD.getCurrency()))
                ,1.14);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.EURO.getCurrency(),BankingCurrency.YEN.getCurrency()))
                ,125.01);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.POUND.getCurrency(),BankingCurrency.USD.getCurrency()))
                ,1.32);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.POUND.getCurrency(),BankingCurrency.EURO.getCurrency()))
                ,1.15);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.POUND.getCurrency(),BankingCurrency.YEN.getCurrency()))
                ,143.80);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.YEN.getCurrency(),BankingCurrency.USD.getCurrency()))
                ,0.0091);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.YEN.getCurrency(),BankingCurrency.POUND.getCurrency()))
                ,0.0070);
        currencyRate.put(Collections.unmodifiableList(Arrays.asList(BankingCurrency.YEN.getCurrency(),BankingCurrency.EURO.getCurrency())),0.0080);
    }

    public BankingApplicationServiceImpl(AccountingManagement accountingManagement, TransactionHistoryManagement transferHistoryManagement) {
        this.accountingManagement = accountingManagement;
        this.transferHistoryManagement = transferHistoryManagement;
    }


    @Override
    public Account accountDetails(String accountId) throws RestException {
        Account account = null;
        try {
            account = accountingManagement.findAccount(accountId);
        } catch (AccountNotFound accountNotFound) {
            throw new RestException(404,accountNotFound.getMessage());
        }
        return account;
    }

    @Override
    public Account saveAccount(Account account) throws RestException {
        if(bankingCurrencies.contains(account.getBankingCurrency())) {
            accountingManagement.createAccount(account);
            TransferHistory transferHistory=new TransferHistory(null,account,account.getBalance(),account.getBankingCurrency(),Status.Success.getState(),new Date());
            transferHistoryManagement.createTransferHistory(transferHistory);
            log.info("Json Payload " + account);
            return account;
        }
        throw new RestException(422,"The currency does not exist");
    }

    @Override
    public List<TransferHistory> returnTransfers(String accountId) throws RestException {
        try {
            Account account = accountingManagement.findAccount(accountId);
            List<TransferHistory> transferHistories = transferHistoryManagement.getHistoryForAccount(account.getAccountUniqueId());
            return transferHistories;
        }catch (AccountNotFound accountNotFound) {
            throw new RestException(accountNotFound.getMessage());
        }
    }

    @Override
    public TransferHistory findTransfer(String accountId, String transferId) throws RestException {
        TransferHistory transferHistory = null;
        try {
            transferHistory = transferHistoryManagement.findTransaction(transferId);
        } catch (NoTransactionRecordFound noTransactionRecordFound) {
            throw new RestException(404,noTransactionRecordFound.getMessage());
        }
        if(transferHistory.getSourceAcctId()!=null){
        if(transferHistory.getSourceAcctId().getAccountUniqueId().equals(accountId)){
               return transferHistory;
        }
        }
        if (transferHistory.getDestAcctId()!=null) {
            if (transferHistory.getDestAcctId().getAccountUniqueId().equals(accountId)) {
                return transferHistory;
            }
        }
        throw new RestException(404,"This account is not valid");
    }

    @Override
    public List<TransferHistory> returnCreditedTransfers(String accountId) throws RestException {
        Account account= null;
        try {
            account = accountingManagement.findAccount(accountId);
        } catch (AccountNotFound accountNotFound) {
            throw new RestException(404,accountNotFound.getMessage());
        }
        List<TransferHistory>transferHistories=transferHistoryManagement.getCreditedTransactionHistory(account.getAccountUniqueId());
        return transferHistories;
    }

    @Override
    public List<TransferHistory> returnDebitTransfers(String accountId) throws RestException {
        Account account = null;
        try {
            account = accountingManagement.findAccount(accountId);
        } catch (AccountNotFound accountNotFound) {
            throw new RestException(accountNotFound.getMessage());
        }
        List<TransferHistory>transferHistories=transferHistoryManagement.getDebitTransactionHistory(account.getAccountUniqueId());
        return transferHistories;
    }

    @Override
    public List<TransferHistory> returnDebitTransfer(String accountId, String state) throws RestException {
        if(state.equalsIgnoreCase(Status.Failed.getState())){
            state=Status.Failed.getState();
        }else{
            state=Status.Success.getState();
        }
        try {
            Account account = accountingManagement.findAccount(accountId);
            List<TransferHistory> transferHistories = transferHistoryManagement.getDebitTransactionHistory(account.getAccountUniqueId(), state);
            return transferHistories;
        }catch (AccountNotFound accountNotFound) {
            throw new RestException(accountNotFound.getMessage());
        }

    }

    @Override
    public TransferHistory transferFunds(String accountId, TransferJson transferJson) throws RestException {
        try {
            verifyAccount(accountId,transferJson);
            Account sourceAcct = accountingManagement.findAccount(transferJson.getSourceAccountId());
            Account destAcct = accountingManagement.findAccount(transferJson.getDestAccountId());
            transferJson.setCurrency(sourceAcct.getBankingCurrency());
            log.info("Source Account " + sourceAcct + " Destination Account " + destAcct+" transfer json "+transferJson);
            BigDecimal transferFund = currencyConversion(sourceAcct.getBankingCurrency(),destAcct.getBankingCurrency(),transferJson.getAmountTransfer());
            TransferHistory transferHistory = TransferHistory.executeTransaction(transferJson, sourceAcct, destAcct);
            if (transferHistory.getStatus().equals(Status.Success.getState())) {
                sourceAcct.setBalance(sourceAcct.getBalance().subtract(transferJson.getAmountTransfer()));
                destAcct.setBalance(destAcct.getBalance().add(transferFund));
                accountingManagement.updateAccount(sourceAcct);
                accountingManagement.updateAccount(destAcct);
            }
            transferHistoryManagement.createTransferHistory(transferHistory);
            return transferHistory;
        }catch (AccountNotFound accountNotFound){
            throw new RestException(404,accountNotFound.getMessage());
        }
    }

    public BigDecimal currencyConversion(String fromCurrency,String toCurrency,BigDecimal bigDecimal){
        Double value=currencyRate.get(Arrays.asList(fromCurrency,toCurrency))*bigDecimal.doubleValue();
        return new BigDecimal(value).setScale(2,BigDecimal.ROUND_CEILING);
    }

    private void verifyAccount(String sourceAcctId,TransferJson transferJson) throws RestException {
        if(!sourceAcctId.equals(transferJson.getSourceAccountId())){
            throw new RestException(404,"The source account param is not same as the account sent ");
        }
        if(sourceAcctId.equals(transferJson.getDestAccountId())){
            throw new RestException(404,"Can not send request to the same account ");
        }
    }
}
