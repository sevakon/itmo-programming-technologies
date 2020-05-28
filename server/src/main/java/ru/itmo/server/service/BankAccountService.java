package ru.itmo.server.service;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
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

    /**
     * addAmount Service Layer method to add money to user
     * @param id user ID
     * @param amount amount to add, can be both positive and negative
     * @throws BankTransactionException
     * Since the isolation level in the DataBase transaction is set to SERIALIZABLE,
     * if one serializable transaction gets to know that the data is accessed by any other concurrent transaction
     * at any stage, and both transactions try to commit, then one of the transactions will be aborted with SQLSTATE
     * value of '40001', meaning "ERROR: could not serialize access due to read/write dependencies among transactions".
     * So one of the concurrent transaction was successfully committed, while the other one was not. In case of failure,
     * we just retry the transaction until it is succeeded; success in this case is guaranteed by Serialization!
     * The code below does exactly this: we retry DataBase transaction until no TransactionSystemException is received.
     *
     * NO DeadLocks possible as stated in https://www.postgresql.org/docs/9.5/transaction-iso.html:
     * "In PostgreSQL these locks do not cause any blocking and therefore can not play any part in causing a deadlock"
     */
    public void addAmount(Long id, Long amount) throws BankTransactionException {
        boolean executedSuccessfully = false;

        while (!executedSuccessfully) {
            try {
                bankAccountDao.addAmount(id, amount);
            } catch (TransactionSystemException e) {
                if (e.getCause() != null && e.getCause().getCause() instanceof PSQLException) {
                    PSQLException nestedException = (PSQLException) e.getCause().getCause();
                    System.out.println(nestedException.getMessage());
                }
            } finally {
                executedSuccessfully = true;
            }
        }
    }

    /**
     * transfer Service Layer method to transfer money from one user to another
     * @param fromAccountId sender user ID
     * @param toAccountId receiver user ID
     * @param amount amount to transfer
     * @throws BankTransactionException
     *
     * Similar to addAmount method, this transaction is Serializable Isolation.
     * Some of the Serializable transactions can fail at committing and throw SQLSTATE value of '40001' error.
     * If this happens, we try to make transaction once again, success in the repeat is guaranteed by Serialization
     */
    public void transfer(Long fromAccountId, Long toAccountId, Long amount)
            throws BankTransactionException {
        if (amount < 0) {
            throw new BankTransactionException("Transfer amount should be positive!");
        }

        boolean executedSuccessfully = false;

        while (!executedSuccessfully) {
            try {
                bankAccountDao.transfer(fromAccountId, toAccountId, amount);
            } catch (TransactionSystemException e) {
                if (e.getCause() != null && e.getCause().getCause() instanceof PSQLException) {
                    PSQLException nestedException = (PSQLException) e.getCause().getCause();
                    System.out.println(nestedException.getMessage());
                }
            } finally {
                executedSuccessfully = true;
            }
        }
    }

}
