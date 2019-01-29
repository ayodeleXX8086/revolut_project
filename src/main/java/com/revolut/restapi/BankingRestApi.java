package com.revolut.restapi;


import com.revolut.domain.Account;
import com.revolut.domain.TransferHistory;
import com.revolut.repository.AccountingManagementImpl;
import com.revolut.repository.TransactionHistoryManagement;
import com.revolut.representation.AccountJson;
import com.revolut.representation.TransferJson;
import com.revolut.restapi.exception.RestException;
import com.revolut.service.BankingApplicationService;
import com.revolut.service.BankingApplicationServiceImpl;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by ayomide on 1/25/2019.
 */
@Path("/revolut")
public class BankingRestApi {

    BankingApplicationService bankingApplicationService;
    static Logger log = Logger.getLogger(BankingRestApi.class);

    public BankingRestApi(AccountingManagementImpl accountingManagement, TransactionHistoryManagement transferHistory){
        this.bankingApplicationService=new BankingApplicationServiceImpl(accountingManagement,transferHistory);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("/{accountId}")
    public Response accountDetails(@PathParam("accountId")String accountId) throws RestException {
        Account account = bankingApplicationService.accountDetails(accountId);
        AccountJson accountJson = AccountJson.setAccountEntity(account);
        return Response.ok(accountJson,MediaType.APPLICATION_JSON_TYPE).build();
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveAccount(AccountJson accountJson) throws RestException {
        Account account=accountJson.generateAccountEntity();
        Account createdAccount = bankingApplicationService.saveAccount(account);
        AccountJson returnedJson = AccountJson.setAccountEntity(createdAccount);
        return Response.ok(returnedJson,MediaType.APPLICATION_JSON_TYPE).build();
    }



    @GET
    @Path("/{accountId}/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response returnTransfers(@PathParam("accountId")String accountId) throws RestException {
        List<TransferHistory>transferHistories = bankingApplicationService.returnTransfers(accountId);
        List<TransferJson>transferJsonList=transferHistories.stream().
                                            map(e->TransferJson.getTransferJson(e,accountId)).
                                            collect(Collectors.toList());
        return Response.ok(transferJsonList,MediaType.APPLICATION_JSON_TYPE).build();

    }

    @GET
    @Path("{accountId}/transfer/{transferId}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response findTransfer(@PathParam("accountId")String accountId,@PathParam("transferId")String transferId) throws RestException {
        TransferHistory transferHistory = bankingApplicationService.findTransfer(accountId,transferId);
        TransferJson transferJson = TransferJson.getTransferJson(transferHistory,accountId);
        return  Response.ok(transferJson,MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/{accountId}/transfer/credit")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response returnCreditedTransfers(@PathParam("accountId")String accountId) throws RestException {
        List<TransferHistory>transferHistories=bankingApplicationService.returnCreditedTransfers(accountId);
        List<TransferJson>transferJsonList=transferHistories.stream().map(e->TransferJson.getTransferJson(e,accountId))
                                            .collect(Collectors.toList());
        return  Response.ok(transferJsonList,MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/{accountId}/transfer/debit")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response returnDebitTransfers(@PathParam("accountId")String accountId) throws RestException {
        List<TransferHistory>transferHistories=bankingApplicationService.returnDebitTransfers(accountId);
        List<TransferJson>transferJsonList=transferHistories.stream().map(e->TransferJson.getTransferJson(e,accountId))
                                            .collect(Collectors.toList());
        return Response.ok(transferJsonList,MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/{accountId}/transfer/debit/{state}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response returnDebitTransfer(@PathParam("accountId")String accountId,@PathParam("state")String state) throws RestException {
            List<TransferHistory> transferHistories = bankingApplicationService.returnDebitTransfer(accountId, state);
            List<TransferJson> transferJsonList = transferHistories.stream().
                    map(e -> TransferJson.getTransferJson(e, accountId)).collect(Collectors.toList());
            return Response.ok(transferJsonList,MediaType.APPLICATION_JSON_TYPE).build();

    }

    @POST
    @Path("/{accountId}/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response transferFunds(@PathParam("accountId")String accountId, TransferJson transferJson) throws RestException {
            TransferHistory transferHistory=bankingApplicationService.transferFunds(accountId,transferJson);
            TransferJson newTransferJson = TransferJson.getTransferJson(transferHistory, accountId);
            return Response.ok(newTransferJson,MediaType.APPLICATION_JSON_TYPE).build();
    }

}
