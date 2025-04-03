package com.stripe.functional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.stripe.BaseStripeTest;
import com.stripe.exception.StripeException;
import com.stripe.model.Transfer;
import com.stripe.model.TransferCollection;
import com.stripe.net.ApiResource;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TransferTest extends BaseStripeTest {
  public static final String TRANSFER_ID = "tr_123";

  private Transfer getTransferFixture() throws StripeException {
    final Transfer transfer = Transfer.retrieve(TRANSFER_ID);
    resetNetworkSpy();
    return transfer;
  }

  @Test
  public void testCreate() throws StripeException {
    final Map<String, Object> params = new HashMap<>();
    params.put("amount", 100);
    params.put("currency", "usd");
    params.put("destination", "acct_123");

    final Transfer transfer = Transfer.create(params);

    assertNotNull(transfer);
    verifyRequest(ApiResource.RequestMethod.POST, "/v1/transfers", params);
  }

  @Test
  public void testRetrieve() throws StripeException {
    final Transfer transfer = Transfer.retrieve(TRANSFER_ID);

    assertNotNull(transfer);
    verifyRequest(ApiResource.RequestMethod.GET, String.format("/v1/transfers/%s", TRANSFER_ID));
  }

  @Test
  public void testUpdate() throws StripeException {
    Transfer transfer = getTransferFixture();

    final Map<String, Object> metadata = new HashMap<>();
    metadata.put("key", "value");
    final Map<String, Object> params = new HashMap<>();
    params.put("metadata", metadata);

    final Transfer updatedTransfer = transfer.update(params);

    assertNotNull(updatedTransfer);
    verifyRequest(
        ApiResource.RequestMethod.POST,
        String.format("/v1/transfers/%s", transfer.getId()),
        params);
  }

  @Test
  public void testList() throws StripeException {
    final Map<String, Object> params = new HashMap<>();
    params.put("limit", 1);

    final TransferCollection transfers = Transfer.list(params);

    assertNotNull(transfers);
    verifyRequest(ApiResource.RequestMethod.GET, "/v1/transfers", params);
  }
}
dependencies {
  implementation 'com.stripe:stripe-java:20.94.0'
}import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Transfer;
import com.stripe.param.TransferCreateParams;

public class StripeTransfer {
    public static void main(String[] args) {
        // Set your secret key. Remember to switch to your live secret key in production.
        // See your keys here: https://dashboard.stripe.com/apikeys
        Stripe.apiKey = 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;

        TransferCreateParams params =
          TransferCreateParams.builder()
            .setSourceType(TransferCreateParams.SourceType.BANK_ACCOUNT)
            .setAmount(1154866L) // Amount in cents, so $11548.66
            .setCurrency("usd")
            .setDestination("acct_1R49ZmRxxsLYJXXn")
            .setDescription("amount owed")
            .build();

        try {
            Transfer transfer = Transfer.create(params);
            System.out.println("Transfer created successfully: " + transfer);
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}
