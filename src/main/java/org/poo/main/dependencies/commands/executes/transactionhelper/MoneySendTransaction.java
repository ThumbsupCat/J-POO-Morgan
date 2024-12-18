package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MoneySendTransaction implements TransactionHelper {
    private final ObjectMapper mapper;
    private final int timestamp;
    private final String description;
    private final String senderIBAN;
    private final String receiverIBAN;
    private final double amount;
    private final String amountCurrency;
    private final String transferType;
    public MoneySendTransaction(final int timestamp, final String description,
                                final String senderIBAN, final String receiverIBAN,
                                final double amount, final String amountCurrency,
                                final String transferType) {
        mapper = new ObjectMapper();
        this.timestamp = timestamp;
        this.description = description;
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = Double.parseDouble(String.valueOf(amount));
        this.amountCurrency = amountCurrency;
        this.transferType = transferType;
    }

    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        node.put("senderIBAN", senderIBAN);
        node.put("receiverIBAN", receiverIBAN);
        node.put("amount", toString());
        node.put("transferType", transferType);
        return node;
    }
    public String toString() {
        return amount + " " + amountCurrency;
    }
    public boolean matchesType(final String type) {
        return "MoneySendTransaction".contentEquals(type);
    }
}
