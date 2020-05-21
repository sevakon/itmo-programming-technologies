package ru.itmo.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddAmountForm {

    private final Long id;
    private final Long amount;

    public AddAmountForm(@JsonProperty("id") Long id,
                         @JsonProperty("amount") Long amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getAmount() {
        return amount;
    }

}
