package ru.itmo.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferMoneyForm {

    private final Long fromAccountId;
    private final Long toAccountId;
    private final Long amount;

    public TransferMoneyForm(@JsonProperty("fromAccountId") Long fromAccountId,
                             @JsonProperty("toAccountId") Long toAccountId,
                             @JsonProperty("amount") Long amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public Long getAmount() {
        return amount;
    }

}
