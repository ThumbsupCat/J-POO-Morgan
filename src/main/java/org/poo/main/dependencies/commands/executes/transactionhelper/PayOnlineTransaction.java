package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class PayOnlineTransaction implements TransactionHelper {
    private final double amount;
    private final String commerciant;
    private final String description;
    private final int timestamp;
    private final ObjectMapper mapper;
    public PayOnlineTransaction(final double amount, final String commerciant,
                                final String description, final int timestamp) {
        this.amount = amount;
        this.commerciant = commerciant;
        this.description = description;
        this.timestamp = timestamp;
        mapper = new ObjectMapper();
    }

    /**
     * Constructs and returns an ObjectNode containing the transaction details
     * for an online payment. The returned ObjectNode includes the amount,
     * commerciant name, description, and timestamp of the transaction.
     *
     * @return an ObjectNode representing the transaction details
     */
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("amount", amount);
        node.put("commerciant", commerciant);
        node.put("description", description);
        node.put("timestamp", timestamp);
        return node;
    }

    /**
     * Checks if the given type matches the specific transaction type "PayOnlineTransaction".
     *
     * @param type the string representing the type to be checked
     * @return true if the type matches "PayOnlineTransaction", false otherwise
     */
    public boolean matchesType(final String type) {
        return "PayOnlineTransaction".contentEquals(type);
    }
}
