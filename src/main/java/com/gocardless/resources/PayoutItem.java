package com.gocardless.resources;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a payout item resource returned from the API.
 *
 * When we collect a payment on your behalf, we add the money you've collected to your
 * GoCardless balance, minus any fees paid. Periodically (usually every working day),
 * we take any positive balance in your GoCardless account, and pay it out to your
 * nominated bank account.
 * 
 * Other actions in your GoCardless account can also affect your balance. For example,
 * if a customer charges back a payment, we'll deduct the payment's amount from your
 * balance, but add any fees you paid for that payment back to your balance.
 * 
 * The Payout Items API allows you to view, on a per-payout basis, the credit and debit
 * items that make up that payout's amount.
 * 
 * <p class="beta-notice"><strong>Beta</strong>:	The Payout Items API is in beta, and is
 * subject to <a href="#overview-backwards-compatibility">backwards incompatible changes</a>
 * with 30 days' notice. Before making any breaking changes, we will contact all integrators
 * who have used the API.</p>
 * 
 */
public class PayoutItem {
    private PayoutItem() {
        // blank to prevent instantiation
    }

    private String amount;
    private Links links;
    private Type type;

    /**
     * The positive (credit) or negative (debit) value of the item, in fractional currency;
     * either pence (GBP), cents (EUR), or öre (SEK), to one decimal place.
     * <p class="notice">For accuracy, we store some of our fees to greater precision than
     * we can actually pay out (for example, a GoCardless fee we record might come to 0.5
     * pence, but it is not possible to send a payout via bank transfer including a half
     * penny).<br><br>To calculate the final amount of the payout, we sum all of the items
     * and then round to the nearest currency unit.</p>
     * 
     */
    public String getAmount() {
        return amount;
    }

    public Links getLinks() {
        return links;
    }

    /**
     * The type of the credit (positive) or debit (negative) item in the payout. One of:
     * <ul>
     * <li>`payment_paid_out` (credit)</li>
     * <li>`payment_failed` (debit): The payment failed to be processed.</li>
     * <li>`payment_charged_back` (debit): The payment has been charged back.</li>
     * <li>`payment_refunded` (debit)</li>
     * <li>`gocardless_fee` (credit/debit): The fees that GoCardless charged for a payment. In the case
     * of a payment failure or chargeback, these will appear as credits.</li>
     * <li>`app_fee` (credit/debit): The optional fees that a partner may have taken for a payment. In
     * the case of a payment failure or chargeback, these will appear as credits.</li>
     * <li>`revenue_share` (credit): Only shown in partner payouts.</li>
     * </ul>
     * 
     */
    public Type getType() {
        return type;
    }

    public enum Type {
        @SerializedName("payment_paid_out")
        PAYMENT_PAID_OUT, @SerializedName("payment_failed")
        PAYMENT_FAILED, @SerializedName("payment_charged_back")
        PAYMENT_CHARGED_BACK, @SerializedName("payment_refunded")
        PAYMENT_REFUNDED, @SerializedName("gocardless_fee")
        GOCARDLESS_FEE, @SerializedName("app_fee")
        APP_FEE, @SerializedName("revenue_share")
        REVENUE_SHARE,
    }

    public static class Links {
        private Links() {
            // blank to prevent instantiation
        }

        private String payment;

        /**
         * Unique identifier, beginning with "PM".
         */
        public String getPayment() {
            return payment;
        }
    }
}
