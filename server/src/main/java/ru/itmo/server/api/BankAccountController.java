package ru.itmo.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmo.server.exception.BankTransactionException;
import ru.itmo.server.exception.InvalidIdException;
import ru.itmo.server.model.AddAmountForm;
import ru.itmo.server.model.BankAccount;
import ru.itmo.server.model.TransferMoneyForm;
import ru.itmo.server.service.BankAccountService;

import java.util.List;

@RequestMapping("api/bank")
@RestController
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    public List<BankAccount> getBankAccounts() {
        return bankAccountService.getBankAccounts();
    }

    @GetMapping(path = "{id}")
    public BankAccount getBankAccount(@PathVariable("id") Long id) throws InvalidIdException {
        return bankAccountService.getBankAccount(id);
    }

    @GetMapping(path = "{id}/balance")
    public Long getBalance(@PathVariable("id") Long id) throws InvalidIdException {
        return bankAccountService.getBalance(id);
    }

    @PostMapping(path = "{id}")
    public void addBankAccount(@RequestBody BankAccount bankAccount) throws InvalidIdException {
        bankAccountService.addBankAccount(bankAccount);
    }

    @GetMapping(path = "send")
    public void addAmount(@RequestBody AddAmountForm form) throws BankTransactionException {
        bankAccountService.addAmount(form.getId(), form.getAmount());
    }

    @PostMapping(path = "send")
    public void transfer(@RequestBody TransferMoneyForm form)
            throws BankTransactionException {
        bankAccountService.transfer(form.getFromAccountId(),
                form.getToAccountId(), form.getAmount());
    }
}
