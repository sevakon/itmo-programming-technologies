package ru.itmo.server.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.itmo.server.model.BankAccount;

import java.sql.ResultSet;
import java.sql.SQLException;


public class BankAccountMapperService implements RowMapper<BankAccount> {

    @Override
    public BankAccount mapRow(ResultSet rs, int rowNum) throws SQLException {

        Long id = rs.getLong("id");
        String fullName = rs.getString("full_Name");
        Long balance = rs.getLong("balance");

        return new BankAccount(id, fullName, balance);
    }

}
