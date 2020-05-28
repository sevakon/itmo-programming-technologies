package ru.itmo.client.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import ru.itmo.client.model.AddAmountForm;
import ru.itmo.client.model.BankAccount;
import ru.itmo.client.model.TransferMoneyForm;

import java.util.List;

public class ClientService {

    private static String API_URL;
    private static String ADD_AMOUNT_URL;
    private static String TRANSFER_URL;


    public ClientService() {
        API_URL = "http://localhost:8080/api/bank";
        ADD_AMOUNT_URL = "http://localhost:8080/api/bank/add_amount";
        TRANSFER_URL = "http://localhost:8080/api/bank/transfer";
    }

    public List<BankAccount> getBankAccounts() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(API_URL, List.class);
    }

    public String addAmount(AddAmountForm addAmountForm) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<AddAmountForm> requestBody = new HttpEntity<>(addAmountForm, headers);
        try {
            restTemplate.postForEntity(ADD_AMOUNT_URL, requestBody, String.class);
        } catch (Exception e) {
            return e.getMessage();
        }

        return null;
    }

    public String transferMoney(TransferMoneyForm transferMoneyForm) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<TransferMoneyForm> requestBody = new HttpEntity<>(transferMoneyForm, headers);
        try {
            restTemplate.postForEntity(TRANSFER_URL, requestBody, String.class);
        } catch (Exception e) {
            return e.getMessage();
        }

        return null;
    }

    public String addAccount(BankAccount bankAccount) {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<BankAccount> requestBody = new HttpEntity<>(bankAccount, headers);
        try {
            restTemplate.postForEntity(API_URL +"/" + bankAccount.getId(), requestBody, String.class);
        } catch (Exception e) {
            return e.getMessage();
        }

        return null;
    }

    public BankAccount getBankAccount(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        BankAccount account = null;
        try {
            account = restTemplate.getForObject(API_URL + "/" + id, BankAccount.class);
        } catch (Exception e) {
        }
        return account;
    }

    public Long getAccountBalance(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        Long balance = null;
        try {
            balance = restTemplate.getForObject(API_URL + "/"  + id + "/balance", Long.class);
        } catch (Exception e) {
        }
        return balance;
    }
}


