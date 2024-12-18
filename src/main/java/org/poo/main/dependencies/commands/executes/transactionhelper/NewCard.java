package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NewCard implements TransactionHelper{
    //new NewCard(
    //                                    account.getIBAN(), newCard.getCardNumber(), user.getEmail(), "New card created")
    private String iban;
    private String cardNumber;
    private String email;
    private String description;
    private int timestamp;
    private ObjectMapper mapper;
    public NewCard(final String iBAN, final String cardNumber,
                   final String email, final String description,
                   final int timestamp) {
        this.iban = iBAN;
        this.cardNumber = cardNumber;
        this.email = email;
        this.description = description;
        this.timestamp = timestamp;
        mapper = new ObjectMapper();
    }
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("account", iban);
        node.put("card", cardNumber);
        node.put("cardHolder", email);
        node.put("description", description);
        node.put("timestamp", timestamp);
        return node;
    }
    public boolean matchesType(final String type) {
        return "NewCard".contentEquals(type);
    }
}
