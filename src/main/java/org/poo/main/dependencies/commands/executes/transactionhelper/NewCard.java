package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class NewCard implements TransactionHelper {
    private final String iban;
    private final String cardNumber;
    private final String email;
    private final String description;
    private final int timestamp;
    private final ObjectMapper mapper;
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

    /**
     * Constructs and returns an ObjectNode representing the transaction details
     * for a new card. The ObjectNode contains the IBAN, card number, cardholder
     * email, description, and timestamp associated with the transaction.
     *
     * @return an ObjectNode representing the transaction details
     */
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("account", iban);
        node.put("card", cardNumber);
        node.put("cardHolder", email);
        node.put("description", description);
        node.put("timestamp", timestamp);
        return node;
    }

    /**
     * Checks if the given type matches the specific transaction type "NewCard".
     *
     * @param type the string representing the type to be checked
     * @return true if the type matches "NewCard", false otherwise
     */
    public boolean matchesType(final String type) {
        return "NewCard".contentEquals(type);
    }
}
