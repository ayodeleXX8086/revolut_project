import com.revolut.constant.BankingCurrency;
import com.revolut.constant.Status;
import com.revolut.constant.TransferType;
import com.revolut.domain.Account;
import com.revolut.domain.TransferHistory;
import com.revolut.exception.AccountNotFound;
import com.revolut.exception.NoTransactionRecordFound;
import com.revolut.repository.AccountingManagement;
import com.revolut.repository.AccountingManagementImpl;
import com.revolut.repository.TransactionHistoryManagementImpl;
import com.revolut.representation.TransferJson;
import com.revolut.restapi.BankingRestApi;
import com.revolut.restapi.exception.RestException;
import com.revolut.service.BankingApplicationServiceImpl;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Created by ayomide on 1/28/2019.
 */
public class AccountingManagementTest {
    private static final AccountingManagementImpl accountManagement=mock(AccountingManagementImpl.class);
    private static final TransactionHistoryManagementImpl transactionHistory=mock(TransactionHistoryManagementImpl.class);
    Account account1=new Account("1",BankingCurrency.USD.getCurrency(),new BigDecimal(234.5),new Date());
    Account account2=new Account("2",BankingCurrency.EURO.getCurrency(),new BigDecimal(23400.5),new Date());
   private BankingApplicationServiceImpl bankingApplicationService = new BankingApplicationServiceImpl(accountManagement,transactionHistory);

   @Before
   public void beforeCurrency() throws AccountNotFound {
       when(accountManagement.findAccount(eq("2"))).thenReturn(account2);
       when(accountManagement.findAccount(eq("1"))).thenReturn(account1);
       when(accountManagement.updateAccount(account1)).thenReturn(account1);
       when(accountManagement.updateAccount(account2)).thenReturn(account2);
   }


   @Test
   public void testTransfer() throws RestException {
       TransferJson transferJson=new TransferJson("2","1",new BigDecimal(230));
       bankingApplicationService.transferFunds("2",transferJson);
       assert account2.getBalance().doubleValue()==new BigDecimal(23170.5).doubleValue();
       assert account1.getBalance().doubleValue()==new BigDecimal(496.7).doubleValue();
   }



   @Test
   public void testfindTransfer() throws NoTransactionRecordFound, RestException {
       TransferHistory transferHistory=new TransferHistory(account1,account2,new BigDecimal("2300"),BankingCurrency.USD.getCurrency(),Status.Success.getState(),new Date());
       when(transactionHistory.findTransaction(transferHistory.getTransactionId())).thenReturn(transferHistory);
      TransferHistory transferHistory1 = bankingApplicationService.findTransfer("1",transferHistory.getTransactionId());
      TransferJson transferJson1 = TransferJson.getTransferJson(transferHistory1,"1");
      TransferJson transferJson2 = TransferJson.getTransferJson(transferHistory1,"2");
      assert transferJson1.getTransferType().equals(TransferType.Debited.getTransferType());
      assert transferJson2.getTransferType().equals(TransferType.Credited.getTransferType());
   }


   @Test
    public void testCurrency(){
    BigDecimal bigDecimal=   bankingApplicationService.currencyConversion(BankingCurrency.EURO.getCurrency(),BankingCurrency.USD.getCurrency(),new BigDecimal(230));
    assert bigDecimal.doubleValue()==new BigDecimal(262.20).doubleValue();
   }


}
