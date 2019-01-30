# Banking transfer project
This applications performs several operations
 * Get account details (HTTP operation GET)
 * Save account (HTTP operation POST)
 * Return transfers transactions in a list (HTTP operation GET)
 * Return a transfer detail (HTTP operation GET)
 * Return  debit transfers detail HTTP operation GET
 * Return credit transfers detail HTTP operation GET
 * Return debit transfer detail that is either Success or Failed

# Resources and payloads

## Get Account

```
       {url}/9000/revolut/8c897351-4572-405e-ba2f-bae2856b34e3
```
## Save account

```
        {url}/9000/revolut
        {
        "bankingCurrency":"USD",
        "balance"	:"23000.5"
        }
```
# Get account transfer details
```
       {url}:9000/revolut/{account_id}/transfer
```
## Transfer transaction details
```
        Post operating
        {url}:9000/revolut/{source_account_id}/transfer
        {
        "sourceAccountId":"8c897351-4572-405e-ba2f-bae2856b34e3",
        "destAccountId"	:"a88e38bd-424f-4d88-98d8-42ae6e45766d",
        "amountTransfer":"100.3"
        }
```

## Build
   * Dropwizard
   * Maven
   * Java 1.8

## How to run the program

*java filename.jar server config.yml
*This argument needs to passed to the when running "server config.yml"