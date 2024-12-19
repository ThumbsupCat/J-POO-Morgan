package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public final class SplitPaymentTransaction implements TransactionHelper {
    private final int timestamp;
    private final String description;
    private final String currency;
    private final double amount;
    private final List<String> involvedAccounts;
    private String error;
    private final ObjectMapper mapper;

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

    /**
     * Constructs and returns an ObjectNode representing the transaction details
     * for a split payment transaction. The ObjectNode includes the timestamp,
     * description, currency, amount, involved accounts, and any error information
     * associated with the transaction.
     *
     * @return an ObjectNode containing transaction details for a split payment
     */
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

    /**
     * Checks if the given type matches the specific transaction type
     * "SplitPaymentTransaction".
     *
     * @param type the string representing the type to be checked
     * @return true if the type matches "SplitPaymentTransaction", false otherwise
     */
    public boolean matchesType(final String type) {
        return "SplitPaymentTransaction".contentEquals(type);
    }
}
