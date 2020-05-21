package ru.itmo.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;


public class BankAccount {

    @NotNull
    private final Long id;

    private final String fullName;

    @NotNull
    private Long balance;

    public BankAccount(@JsonProperty("id") Long id,
                       @JsonProperty("fullName") String fullName,
                       @JsonProperty("balance") Long balance) {
        this.id = id;
        this.fullName = fullName;

        if (balance == null)
            balance = 0L;

        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
