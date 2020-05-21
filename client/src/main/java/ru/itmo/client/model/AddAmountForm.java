package ru.itmo.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddAmountForm {

    private Long id;
    private Long amount;

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
