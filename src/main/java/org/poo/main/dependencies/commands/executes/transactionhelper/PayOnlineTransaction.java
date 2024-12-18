package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PayOnlineTransaction implements TransactionHelper {
    private double amount;
    private String commerciant;
    private String description;
    private int timestamp;
    private ObjectMapper mapper;
    public PayOnlineTransaction(final double amount, final String commerciant, final String description, final int timestamp) {
        this.amount = amount;
        this.commerciant = commerciant;
        this.description = description;
        this.timestamp = timestamp;
        mapper = new ObjectMapper();
    }
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("amount", amount);
        node.put("commerciant", commerciant);
        node.put("description", description);
        node.put("timestamp", timestamp);
        return node;
    }

    public boolean matchesType(final String type) {
        return "PayOnlineTransaction".contentEquals(type);
    }
}
