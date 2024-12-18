package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DeleteCardTransaction implements TransactionHelper {
    private final String iban;
    private final String cardNumber;
    private final String email;
    private final String description;
    private final int timestamp;
    private final ObjectMapper mapper;
    public DeleteCardTransaction(final String iBAN, final String cardNumber,
                                 final String email, final String description,
                                 final int timestamp) {
        this.iban = iBAN;
        this.cardNumber = cardNumber;
        this.email = email;
        this.description = description;
        this.timestamp = timestamp;
        this.mapper = new ObjectMapper();
    }

    @Override
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("account", iban);
        node.put("card", cardNumber);
        node.put("cardHolder", email);
        node.put("description", description);
        node.put("timestamp", timestamp);
        return node;
    }

    @Override
    public boolean matchesType(String type) {
        return "DeleteCardTransaction".contentEquals(type);
    }
}
