package ru.itmo.server.dao;

import ru.itmo.server.exception.BankTransactionException;
import ru.itmo.server.model.BankAccount;

import java.util.List;

public interface BankAccountDao {

    List<BankAccount> getBankAccounts();

    BankAccount findBankAccount(Long id);

    void addBankAccount(BankAccount bankAccount);

    void addAmount(Long id, Long amount) throws BankTransactionException;

    void transfer(Long fromAccountId, Long toAccountId, Long amount) throws BankTransactionException;

}
