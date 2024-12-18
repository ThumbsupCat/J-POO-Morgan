package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class SplitPaymentTransaction implements TransactionHelper {
    private int timestamp;
    private String description;
    private String currency;
    private double amount;
    private List<String> involvedAccounts;
    private String error;
    private ObjectMapper mapper;

    public SplitPaymentTransaction(final int timestamp, final String description,
                                   final String currency, final double amount,
                                   final List<String> involvedAccounts) {
        this.timestamp = timestamp;
        this.description = description;
        this.currency = currency;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
        mapper = new ObjectMapper();
    }
    public SplitPaymentTransaction(final int timestamp, final String description,
                                   final String currency, final double amount,
                                   final List<String> involvedAccounts, final String error) {
        this.timestamp = timestamp;
        this.description = description;
        this.currency = currency;
        this.amount = amount;
        this.involvedAccounts = involvedAccounts;
        this.error = error;
        mapper = new ObjectMapper();
    }
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        node.put("currency", currency);
        node.put("amount", amount);
        ArrayNode accounts = mapper.createArrayNode();
        for (String account : involvedAccounts) {
            accounts.add(account);
        }
        node.set("involvedAccounts", accounts);
        if (error != null) {
            node.put("error", error);
        }
        return node;
    }
    public boolean matchesType(String type) {
        return "SplitPaymentTransaction".contentEquals(type);
    }
}
