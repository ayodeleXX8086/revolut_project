package com.revolut.exception;

/**
 * Created by ayomide on 1/25/2019.
 */
public class NoTransactionRecordFound extends  Exception {

    public NoTransactionRecordFound(){
        super("No transaction record found ");
    }
    public NoTransactionRecordFound(String transactionId){
        super("No transaction record found "+transactionId);

    }
}
