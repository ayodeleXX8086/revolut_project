package com.revolut.constant;

/**
 * Created by ayomide on 1/27/2019.
 */
public enum Status {
    Success("Success"),Failed("Failed");
   private String state;
    Status(String state){
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
