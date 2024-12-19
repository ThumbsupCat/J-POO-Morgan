package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class NewAccount implements TransactionHelper {
    private final ObjectMapper mapper;
    private final int timestamp;
    private final String description;
    public NewAccount(final int timestamp, final String description) {
        mapper = new ObjectMapper();
        this.timestamp = timestamp;
        this.description = description;
    }

    /**
     * Constructs and returns an ObjectNode containing the transaction details for
     * a new account. The ObjectNode includes the timestamp and description of the
     * transaction.
     *
     * @return an ObjectNode representing the transaction details
     */
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        return node;
    }

    /**
     * Checks if the given type matches the specific transaction type "NewAccount".
     *
     * @param type the string representing the type to be checked
     * @return true if the type matches "NewAccount", false otherwise
     */
    public boolean matchesType(final String type) {
        return "NewAccount".contentEquals(type);
    }
}
