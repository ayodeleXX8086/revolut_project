package com.revolut;

import com.revolut.domain.Account;
import com.revolut.domain.TransferHistory;
import com.revolut.repository.AccountingManagementImpl;
import com.revolut.repository.TransactionHistoryManagementImpl;
import com.revolut.restapi.BankingRestApi;

import com.revolut.restapi.exception.RestExceptionMapper;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;



/**
 * Created by ayomide on 1/25/2019.
 */
public class Application  extends io.dropwizard.Application<RestStubConfig> {

    private static final String JERSEY_SERVLET_NAME = "jersey-container-servlet";

    public static void main(String[] args) throws Exception {
        new Application().run(args);
    }
    private final HibernateBundle<RestStubConfig> hibernateAccount = new HibernateBundle<RestStubConfig>(Account.class,TransferHistory.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(RestStubConfig configuration) {
            return configuration.getDatabaseAppDataSourceFactory();
        }
    };


    @Override
    public void initialize(Bootstrap<RestStubConfig> bootstrap) {
        bootstrap.addBundle(hibernateAccount);
    }

    @Override
    public void run(RestStubConfig restStubConfig,
                    Environment environment) throws Exception {
        //Register resource
        AccountingManagementImpl accountingManagement=new AccountingManagementImpl(hibernateAccount.getSessionFactory());
        TransactionHistoryManagementImpl transactionHistoryManagement=new TransactionHistoryManagementImpl(hibernateAccount.getSessionFactory());
        BankingRestApi bankingRestApi = new BankingRestApi(accountingManagement,transactionHistoryManagement);
        environment.jersey().register(bankingRestApi);
        environment.jersey().register(new RestExceptionMapper());
        environment.healthChecks().register("template",new RestStubCheck(restStubConfig.getVersion()));
    }
}
