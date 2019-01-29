package com.revolut;

/**
 * Created by ayomide on 1/27/2019.
 */
import com.codahale.metrics.health.HealthCheck;

public class RestStubCheck extends HealthCheck {
    private final String version;

    public RestStubCheck(String version) {
        this.version = version;
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy("OK with version: " + this.version);
    }
}