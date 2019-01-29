package com.revolut.repository;

import com.revolut.domain.TransferHistory;
import com.revolut.exception.NoTransactionRecordFound;

import java.util.List;

/**
 * Created by ayomide on 1/25/2019.
 */
public interface TransactionHistoryManagement {
    List<TransferHistory> getDebitTransactionHistory(String accountId, String status);
    List<TransferHistory> getDebitTransactionHistory(String accountId);
    List<TransferHistory> getHistoryForAccount(String accountId);
    List<TransferHistory> getCreditedTransactionHistory(String accountId);
    TransferHistory findTransaction(String transactionId) throws NoTransactionRecordFound;
    TransferHistory createTransferHistory(TransferHistory transferHistory);
}
