package ru.itmo.client.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import ru.itmo.client.model.AddAmountForm;
import ru.itmo.client.model.BankAccount;
import ru.itmo.client.model.TransferMoneyForm;
import ru.itmo.client.service.ClientService;


@Controller
public class MainController {

    private ClientService service = new ClientService();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showBankAccounts(Model model) {
        model.addAttribute("accountInfos", service.getBankAccounts());
        return "_accounts";
    }

    @RequestMapping(value = "/add_balance", method = RequestMethod.GET)
    public String addBalance(Model model) {
        AddAmountForm form = new AddAmountForm(0L, 0L);
        model.addAttribute("addAmountForm", form);
        return "_add_balance";
    }

    @RequestMapping(value = "/add_balance", method = RequestMethod.POST)
    public String processAddBalance(Model model, AddAmountForm addAmountForm) {
        String result = service.addAmount(addAmountForm);
        if (result == null) {
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", "Error: " + result);
            return "_add_balance";
        }
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
    public String transferMoney(Model model) {
        TransferMoneyForm form = new TransferMoneyForm(0L, 0L, 0L);
        model.addAttribute("transferMoneyForm", form);
        return "_transfer_money";
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String processTransferMoney(Model model, TransferMoneyForm transferMoneyForm) {
        String result = service.transferMoney(transferMoneyForm);
        if (result == null) {
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", "Error: " + result);
            return "_transfer_money";
        }
    }

    @RequestMapping(value = "/add_account", method = RequestMethod.GET)
    public String addAccount(Model model) {
        BankAccount account = new BankAccount(0L, "", 0L);
        model.addAttribute("bankAccount", account);
        return "_add_account";
    }

    @RequestMapping(value = "/add_account", method = RequestMethod.POST)
    public String processAddAccount(Model model, BankAccount bankAccount) {
        String result = service.addAccount(bankAccount);
        if (result == null) {
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", "Error: " + result);
            return "_add_account";
        }
    }

    @RequestMapping(value = "/get_account", method = RequestMethod.GET)
    public String getAccount(Model model) {
        BankAccount account = new BankAccount(0L, "", 0L);
        model.addAttribute("bankAccount", account);
        return "_get_account";
    }

    @RequestMapping(value = "/get_account", method = RequestMethod.POST)
    public String processGetAccount(Model model, BankAccount bankAccount) {
        BankAccount account = service.getBankAccount(bankAccount.getId());
        if (account != null) {
            model.addAttribute("accountInfo", account);
            return "_account";
        } else {
            model.addAttribute("errorMessage", "Error: no user with this ID");
            return "_get_account";
        }
    }

    @RequestMapping(value = "/get_balance", method = RequestMethod.GET)
    public String getBalance(Model model) {
        BankAccount account = new BankAccount(0L, "", 0L);
        model.addAttribute("bankAccount", account);
        return "_get_balance";
    }

    @RequestMapping(value = "/get_balance", method = RequestMethod.POST)
    public String processGetBalance(Model model, BankAccount bankAccount) {
        Long balance = service.getAccountBalance(bankAccount.getId());
        if (balance != null) {
            model.addAttribute("balance", balance);
            return "_balance";
        } else {
            model.addAttribute("errorMessage", "Error: no user with this ID");
            return "_get_balance";
        }
    }

}
