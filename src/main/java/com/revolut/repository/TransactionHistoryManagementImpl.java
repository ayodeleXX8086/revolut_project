package com.revolut.repository;


import com.revolut.domain.TransferHistory;
import com.revolut.exception.NoTransactionRecordFound;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by ayomide on 1/25/2019.
 */
public class TransactionHistoryManagementImpl extends AbstractDAO<TransferHistory> implements TransactionHistoryManagement {

    private static TransactionHistoryManagementImpl transactionHistoryManagement;

    public TransactionHistoryManagementImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    public List<TransferHistory> getDebitTransactionHistory(String accountId, String status) {
        Query query=currentSession().createQuery("select th from AccountTransferHistory th where th.sourceAcctId.accountUniqueId=:accountId and th.status = :state");
            query.setParameter("accountId",accountId).setParameter("state",status);
           List<TransferHistory>transferHistories = query.list();
        return transferHistories;
    }

    @Override
    public List<TransferHistory> getDebitTransactionHistory(String accountId) {
       Query query=currentSession().createQuery("select th from AccountTransferHistory  th where th.sourceAcctId.accountUniqueId=:accountId");
                query.setParameter("accountId",accountId);
        List<TransferHistory>transferHistories  = query.list();
        return transferHistories;
    }

    @Override
    public List<TransferHistory> getHistoryForAccount(String accountId) {
        Query query=currentSession().createQuery("select th from AccountTransferHistory th where th.destAcctId.accountUniqueId=:accountId or th.sourceAcctId.accountUniqueId=:accountId");
        query.setParameter("accountId",accountId);
        List<TransferHistory>transferHistories  =  query.list();
        return transferHistories;

    }

    @Override
    public List<TransferHistory> getCreditedTransactionHistory(String accountId) {
        Query query=currentSession().createQuery("select th from AccountTransferHistory th where th.destAcctId.accountUniqueId=:accountId");
                query.setParameter("accountId",accountId);
        List<TransferHistory>transferHistories  =  query.list();
        return transferHistories;
    }

    @Override
    public TransferHistory findTransaction(String transactionId) throws NoTransactionRecordFound {

        Query query = currentSession().createQuery("select th from AccountTransferHistory th where th.transactionId = :transactionId");
                query.setParameter("transactionId",transactionId);
        TransferHistory transferHistory  = (TransferHistory) query.uniqueResult();
        if(transferHistory==null){
            throw new NoTransactionRecordFound(transactionId);
        }
        return transferHistory;
    }

    @Override
    public TransferHistory createTransferHistory(TransferHistory transferHistory) {
        currentSession().persist(transferHistory);
        return transferHistory;
    }
}
