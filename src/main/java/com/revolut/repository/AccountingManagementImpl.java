package com.revolut.repository;


import com.revolut.domain.Account;
import com.revolut.domain.TransferHistory;
import com.revolut.exception.AccountNotFound;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by ayomide on 1/24/2019.
 */
public class AccountingManagementImpl extends AbstractDAO<Account> implements AccountingManagement  {

    private static AccountingManagementImpl accountingManagement;


    public AccountingManagementImpl(SessionFactory sessionFactory){
        super(sessionFactory);
    }


    @Override
    public Account findAccount(String accountId) throws AccountNotFound {
        Query query = currentSession().createQuery("select account from BankingAccount account where account.accountUniqueId = :accountId ");
        query.setParameter("accountId",accountId);
        Account account = (Account) query.uniqueResult();
        if(account==null){
            throw new AccountNotFound(accountId);
        }
        return account;
    }

    @Override
    public Account updateAccount(Account account) {
        return (Account) currentSession().merge(account);
    }

    @Override
    public Account createAccount(Account account) {
        currentSession().persist(account);
        return account;
    }
}
