package com.gocardless.pro.services;

import java.util.List;
import java.util.Map;

import com.gocardless.pro.http.*;
import com.gocardless.pro.resources.Mandate;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;

/**
 * Service class for working with Mandate resources.
 *
 * Mandates represent the Direct Debit mandate with a
 * [customer](https://developer.gocardless.com/pro/#api-endpoints-customers).
 * 
 * GoCardless will
 * notify you via a [webhook](https://developer.gocardless.com/pro/#webhooks) whenever the status of
 * a mandate changes.
 */
public class MandateService {
    private HttpClient httpClient;

    /**
     * Constructor.  Users of this library should have no need to call this - an instance
     * of this class can be obtained by calling
      {@link com.gocardless.pro.GoCardlessClient#mandates() }.
     */
    public MandateService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Creates a new mandate object
     */
    public MandateCreateRequest create() {
        return new MandateCreateRequest(httpClient);
    }

    /**
     * Returns a [cursor-paginated](https://developer.gocardless.com/pro/#overview-cursor-pagination)
     * list of your mandates. Except where stated, these filters can only be used one at a time.
     */
    public MandateListRequest list() {
        return new MandateListRequest(httpClient);
    }

    /**
     * Retrieves the details of an existing mandate.
     * 
     * If you specify `Accept: application/pdf` on a
     * request to this endpoint it will return a PDF complying to the relevant scheme rules, which you
     * can present to your customer.
     * 
     * PDF mandates can be retrieved in Dutch, English, French,
     * German, Italian, Portuguese and Spanish by specifying the [ISO
     * 639-1](http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes#Partial_ISO_639_table) language code
     * as an `Accept-Language` header.
     */
    public MandateGetRequest get(String identity) {
        return new MandateGetRequest(httpClient, identity);
    }

    /**
     * Updates a mandate object. This accepts only the metadata parameter.
     */
    public MandateUpdateRequest update(String identity) {
        return new MandateUpdateRequest(httpClient, identity);
    }

    /**
     * Immediately cancels a mandate and all associated cancellable payments. Any metadata supplied to
     * this endpoint will be stored on the mandate cancellation event it causes.
     * 
     * This will fail with
     * a `cancellation_failed` error if the mandate is already cancelled.
     */
    public MandateCancelRequest cancel(String identity) {
        return new MandateCancelRequest(httpClient, identity);
    }

    /**
     * <a name="mandate_not_inactive"></a>Reinstates a cancelled or expired mandate to the banks. You
     * will receive a `resubmission_requested` webhook, but after that reinstating the mandate follows
     * the same process as its initial creation, so you will receive a `submitted` webhook, followed by a
     * `reinstated` or `failed` webhook up to two working days later. Any metadata supplied to this
     * endpoint will be stored on the `resubmission_requested` event it causes.
     * 
     * This will fail with
     * a `mandate_not_inactive` error if the mandate is already being submitted, or is active.
     */
    public MandateReinstateRequest reinstate(String identity) {
        return new MandateReinstateRequest(httpClient, identity);
    }

    /**
     * Request class for {@link MandateService#create }.
     *
     * Creates a new mandate object
     */
    public static final class MandateCreateRequest extends PostRequest<Mandate> {
        private Links links;
        private Map<String, String> metadata;
        private String reference;
        private String scheme;

        public MandateCreateRequest withLinks(Links links) {
            this.links = links;
            return this;
        }

        /**
         * Key-value store of custom data. Up to 3 keys are permitted, with key names up to 50 characters and
         * values up to 200 characters.
         */
        public MandateCreateRequest withMetadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        /**
         * Unique 6 to 18 character reference. Can be supplied or auto-generated.
         */
        public MandateCreateRequest withReference(String reference) {
            this.reference = reference;
            return this;
        }

        /**
         * Direct Debit scheme to which this mandate and associated payments are submitted. Can be supplied
         * or automatically detected from the customer's bank account. Currently only "bacs" and "sepa_core"
         * are supported.
         */
        public MandateCreateRequest withScheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        private MandateCreateRequest(HttpClient httpClient) {
            super(httpClient);
        }

        @Override
        protected String getPathTemplate() {
            return "/mandates";
        }

        @Override
        protected String getEnvelope() {
            return "mandates";
        }

        @Override
        protected Class<Mandate> getResponseClass() {
            return Mandate.class;
        }

        @Override
        protected boolean hasBody() {
            return true;
        }

        public static class Links {
            private String creditor;
            private String customerBankAccount;

            /**
             * ID of the associated [creditor](https://developer.gocardless.com/pro/#api-endpoints-creditors).
             */
            public Links withCreditor(String creditor) {
                this.creditor = creditor;
                return this;
            }

            /**
             * ID of the associated [customer bank
             * account](https://developer.gocardless.com/pro/#api-endpoints-customer-bank-account) which the
             * mandate is created and submits payments against.
             */
            public Links withCustomerBankAccount(String customerBankAccount) {
                this.customerBankAccount = customerBankAccount;
                return this;
            }
        }
    }

    /**
     * Request class for {@link MandateService#list }.
     *
     * Returns a [cursor-paginated](https://developer.gocardless.com/pro/#overview-cursor-pagination)
     * list of your mandates. Except where stated, these filters can only be used one at a time.
     */
    public static final class MandateListRequest extends ListRequest<Mandate> {
        private String after;
        private String before;
        private String creditor;
        private String customer;
        private String customerBankAccount;
        private Integer limit;
        private String reference;
        private List<Status> status;

        /**
         * Cursor pointing to the start of the desired set.
         */
        public MandateListRequest withAfter(String after) {
            this.after = after;
            return this;
        }

        /**
         * Cursor pointing to the end of the desired set.
         */
        public MandateListRequest withBefore(String before) {
            this.before = before;
            return this;
        }

        /**
         * ID of a [creditor](https://developer.gocardless.com/pro/#api-endpoints-creditors). If specified,
         * this endpoint will return all mandates for the given creditor. Cannot be used in conjunction with
         * `customer_bank_account`
         */
        public MandateListRequest withCreditor(String creditor) {
            this.creditor = creditor;
            return this;
        }

        /**
         * Unique identifier, beginning with "CU".
         */
        public MandateListRequest withCustomer(String customer) {
            this.customer = customer;
            return this;
        }

        /**
         * ID of a [customer bank
         * account](https://developer.gocardless.com/pro/#api-endpoints-customer-bank-accounts). If
         * specified, this endpoint will return all mandates for the given bank account. Cannot be used in
         * conjunction with `creditor`
         */
        public MandateListRequest withCustomerBankAccount(String customerBankAccount) {
            this.customerBankAccount = customerBankAccount;
            return this;
        }

        /**
         * Number of records to return.
         */
        public MandateListRequest withLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Unique 6 to 18 character reference. Can be supplied or auto-generated.
         */
        public MandateListRequest withReference(String reference) {
            this.reference = reference;
            return this;
        }

        /**
         * At most three valid status values
         */
        public MandateListRequest withStatus(List<Status> status) {
            this.status = status;
            return this;
        }

        private MandateListRequest(HttpClient httpClient) {
            super(httpClient);
        }

        @Override
        protected Map<String, Object> getQueryParams() {
            ImmutableMap.Builder<String, Object> params = ImmutableMap.builder();
            if (after != null) {
                params.put("after", after);
            }
            if (before != null) {
                params.put("before", before);
            }
            if (creditor != null) {
                params.put("creditor", creditor);
            }
            if (customer != null) {
                params.put("customer", customer);
            }
            if (customerBankAccount != null) {
                params.put("customer_bank_account", customerBankAccount);
            }
            if (limit != null) {
                params.put("limit", limit);
            }
            if (reference != null) {
                params.put("reference", reference);
            }
            if (status != null) {
                params.put("status", Joiner.on(",").join(status));
            }
            return params.build();
        }

        @Override
        protected String getPathTemplate() {
            return "/mandates";
        }

        @Override
        protected String getEnvelope() {
            return "mandates";
        }

        @Override
        protected TypeToken<List<Mandate>> getTypeToken() {
            return new TypeToken<List<Mandate>>() {};
        }

        public enum Status {
            PENDING_SUBMISSION, SUBMITTED, ACTIVE, FAILED, CANCELLED, EXPIRED;
            @Override
            public String toString() {
                return name().toLowerCase();
            }
        }
    }

    /**
     * Request class for {@link MandateService#get }.
     *
     * Retrieves the details of an existing mandate.
     * 
     * If you specify `Accept: application/pdf` on a
     * request to this endpoint it will return a PDF complying to the relevant scheme rules, which you
     * can present to your customer.
     * 
     * PDF mandates can be retrieved in Dutch, English, French,
     * German, Italian, Portuguese and Spanish by specifying the [ISO
     * 639-1](http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes#Partial_ISO_639_table) language code
     * as an `Accept-Language` header.
     */
    public static final class MandateGetRequest extends GetRequest<Mandate> {
        @PathParam
        private final String identity;

        private MandateGetRequest(HttpClient httpClient, String identity) {
            super(httpClient);
            this.identity = identity;
        }

        @Override
        protected Map<String, String> getPathParams() {
            ImmutableMap.Builder<String, String> params = ImmutableMap.builder();
            params.put("identity", identity);
            return params.build();
        }

        @Override
        protected String getPathTemplate() {
            return "/mandates/:identity";
        }

        @Override
        protected String getEnvelope() {
            return "mandates";
        }

        @Override
        protected Class<Mandate> getResponseClass() {
            return Mandate.class;
        }
    }

    /**
     * Request class for {@link MandateService#update }.
     *
     * Updates a mandate object. This accepts only the metadata parameter.
     */
    public static final class MandateUpdateRequest extends PutRequest<Mandate> {
        @PathParam
        private final String identity;
        private Map<String, String> metadata;

        /**
         * Key-value store of custom data. Up to 3 keys are permitted, with key names up to 50 characters and
         * values up to 200 characters.
         */
        public MandateUpdateRequest withMetadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        private MandateUpdateRequest(HttpClient httpClient, String identity) {
            super(httpClient);
            this.identity = identity;
        }

        @Override
        protected Map<String, String> getPathParams() {
            ImmutableMap.Builder<String, String> params = ImmutableMap.builder();
            params.put("identity", identity);
            return params.build();
        }

        @Override
        protected String getPathTemplate() {
            return "/mandates/:identity";
        }

        @Override
        protected String getEnvelope() {
            return "mandates";
        }

        @Override
        protected Class<Mandate> getResponseClass() {
            return Mandate.class;
        }

        @Override
        protected boolean hasBody() {
            return true;
        }
    }

    /**
     * Request class for {@link MandateService#cancel }.
     *
     * Immediately cancels a mandate and all associated cancellable payments. Any metadata supplied to
     * this endpoint will be stored on the mandate cancellation event it causes.
     * 
     * This will fail with
     * a `cancellation_failed` error if the mandate is already cancelled.
     */
    public static final class MandateCancelRequest extends PostRequest<Mandate> {
        @PathParam
        private final String identity;
        private Map<String, String> metadata;

        /**
         * Key-value store of custom data. Up to 3 keys are permitted, with key names up to 50 characters and
         * values up to 200 characters.
         */
        public MandateCancelRequest withMetadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        private MandateCancelRequest(HttpClient httpClient, String identity) {
            super(httpClient);
            this.identity = identity;
        }

        @Override
        protected Map<String, String> getPathParams() {
            ImmutableMap.Builder<String, String> params = ImmutableMap.builder();
            params.put("identity", identity);
            return params.build();
        }

        @Override
        protected String getPathTemplate() {
            return "/mandates/:identity/actions/cancel";
        }

        @Override
        protected String getEnvelope() {
            return "mandates";
        }

        @Override
        protected Class<Mandate> getResponseClass() {
            return Mandate.class;
        }

        @Override
        protected boolean hasBody() {
            return true;
        }
    }

    /**
     * Request class for {@link MandateService#reinstate }.
     *
     * <a name="mandate_not_inactive"></a>Reinstates a cancelled or expired mandate to the banks. You
     * will receive a `resubmission_requested` webhook, but after that reinstating the mandate follows
     * the same process as its initial creation, so you will receive a `submitted` webhook, followed by a
     * `reinstated` or `failed` webhook up to two working days later. Any metadata supplied to this
     * endpoint will be stored on the `resubmission_requested` event it causes.
     * 
     * This will fail with
     * a `mandate_not_inactive` error if the mandate is already being submitted, or is active.
     */
    public static final class MandateReinstateRequest extends PostRequest<Mandate> {
        @PathParam
        private final String identity;
        private Map<String, String> metadata;

        /**
         * Key-value store of custom data. Up to 3 keys are permitted, with key names up to 50 characters and
         * values up to 200 characters.
         */
        public MandateReinstateRequest withMetadata(Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        private MandateReinstateRequest(HttpClient httpClient, String identity) {
            super(httpClient);
            this.identity = identity;
        }

        @Override
        protected Map<String, String> getPathParams() {
            ImmutableMap.Builder<String, String> params = ImmutableMap.builder();
            params.put("identity", identity);
            return params.build();
        }

        @Override
        protected String getPathTemplate() {
            return "/mandates/:identity/actions/reinstate";
        }

        @Override
        protected String getEnvelope() {
            return "mandates";
        }

        @Override
        protected Class<Mandate> getResponseClass() {
            return Mandate.class;
        }

        @Override
        protected boolean hasBody() {
            return true;
        }
    }
}