package com.revolut.service;

import com.revolut.domain.Account;
import com.revolut.domain.TransferHistory;
import com.revolut.representation.AccountJson;
import com.revolut.representation.TransferJson;
import com.revolut.restapi.exception.RestException;

import java.util.List;

/**
 * Created by ayomide on 1/29/2019.
 */
public interface BankingApplicationService {
    Account accountDetails(String accountId) throws RestException;
    Account saveAccount(Account account) throws RestException;
    List<TransferHistory>returnTransfers(String accountId) throws RestException;
    TransferHistory findTransfer(String accountId, String transferId) throws RestException;
    List<TransferHistory> returnCreditedTransfers(String accountId) throws RestException;
    List<TransferHistory> returnDebitTransfers(String accountId) throws RestException;
    List<TransferHistory>returnDebitTransfer(String accountId,String state) throws RestException;
    TransferHistory transferFunds(String accountId, TransferJson transferJson) throws RestException;

}
