package com.revolut.repository;

import com.revolut.domain.Account;
import com.revolut.domain.TransferHistory;
import com.revolut.exception.AccountNotFound;

import java.util.List;

/**
 * Created by ayomide on 1/24/2019.
 */
public interface AccountingManagement {
    Account findAccount(String accountId) throws AccountNotFound;
    Account updateAccount(Account account);
    Account createAccount(Account account);
}
