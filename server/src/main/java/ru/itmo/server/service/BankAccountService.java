package ru.itmo.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.itmo.server.dao.BankAccountDao;
import ru.itmo.server.exception.BankTransactionException;
import ru.itmo.server.exception.InvalidIdException;
import ru.itmo.server.model.BankAccount;

import java.util.List;

@Service
public class BankAccountService {

    private final BankAccountDao bankAccountDao;

    @Autowired
    public BankAccountService(@Qualifier("postgres") BankAccountDao bankAccountDao) {
        this.bankAccountDao = bankAccountDao;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccountDao.getBankAccounts();
    }

    public BankAccount getBankAccount(Long id) throws InvalidIdException {
        BankAccount queriedAccount = bankAccountDao.findBankAccount(id);
        if (queriedAccount == null) {
            throw new InvalidIdException("No user with ID: " + id);
        }

        return queriedAccount;
    }

    public void addBankAccount(BankAccount bankAccount) throws InvalidIdException {
        if (bankAccountDao.findBankAccount(bankAccount.getId()) != null) {
            throw new InvalidIdException(
                    "User with ID: " + bankAccount.getId() + " already in the banking system");
        }

        bankAccountDao.addBankAccount(bankAccount);
    }

    public Long getBalance(Long id) throws InvalidIdException {
        return getBankAccount(id).getBalance();
    }

    public void addAmount(Long id, Long amount) throws BankTransactionException {
        bankAccountDao.addAmount(id, amount);
    }

    public void transfer(Long fromAccountId, Long toAccountId, Long amount)
            throws BankTransactionException {
        bankAccountDao.transfer(fromAccountId, toAccountId, amount);
    }

}
