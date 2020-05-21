package ru.itmo.server.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.server.exception.BankTransactionException;
import ru.itmo.server.exception.InvalidBalanceException;
import ru.itmo.server.exception.InvalidIdException;
import ru.itmo.server.model.BankAccount;

import javax.sql.DataSource;
import java.util.List;

@Repository("postgres")
@Transactional
public class BankAccountDataAccessService extends JdbcDaoSupport implements BankAccountDao {

    private static final String BASE_SQL
            = "Select ba.id, ba.full_Name, ba.balance From bank_account ba ";

    private static final String SEARCH_SQL
            = BASE_SQL + " where ba.id = ? ";

    private static final String UPDATE_SQL
            = "Update bank_account set balance = ? where id = ?";

    private static final String INSERT_SQL
            = "INSERT INTO bank_account(id, full_name, balance) VALUES (?, ?, ?)";

    @Autowired
    public BankAccountDataAccessService(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    @Override
    public List<BankAccount> getBankAccounts() {
        BankAccountMapperService mapper = new BankAccountMapperService();
        return this.getJdbcTemplate().query(BASE_SQL, new Object[] {}, mapper);
    }

    @Override
    public BankAccount findBankAccount(Long id) {
        Object[] params = new Object[] { id };
        BankAccountMapperService mapper = new BankAccountMapperService();

        try {
            return this.getJdbcTemplate().queryForObject(SEARCH_SQL, params, mapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void addBankAccount(BankAccount bankAccount) {
        this.getJdbcTemplate().update(INSERT_SQL, bankAccount.getId(),
                bankAccount.getFullName(), bankAccount.getBalance());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void addAmount(Long id, Long amount) throws BankTransactionException {
        BankAccount queriedAccount = findBankAccount(id);

        if (queriedAccount == null) {
            throw new InvalidIdException("No user with " + id + " ID exists.");
        }

        if (queriedAccount.getBalance() + amount < 0) {
            throw new InvalidBalanceException();
        }

        Long newBalance = queriedAccount.getBalance() + amount;
        queriedAccount.setBalance(newBalance);

        this.getJdbcTemplate().update(UPDATE_SQL, queriedAccount.getBalance(), queriedAccount.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BankTransactionException.class)
    public void transfer(Long fromAccountId, Long toAccountId, Long amount) throws BankTransactionException {
        addAmount(toAccountId, amount);
        addAmount(fromAccountId, -amount);
    }
}
