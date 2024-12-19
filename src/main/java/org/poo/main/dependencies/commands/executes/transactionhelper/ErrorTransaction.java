package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class ErrorTransaction implements TransactionHelper {
    private final int timestamp;
    private final String error;
    private final ObjectMapper mapper;
    public ErrorTransaction(final int timestamp, final String error) {
        this.timestamp = timestamp;
        this.error = error;
        mapper = new ObjectMapper();
    }

    /**
     * Constructs and returns an ObjectNode representing the transaction details
     * from an error transaction.
     * <p>Due to the different nature of each output, for homework purpose, keywords
     * have been added to filter and match the ObjectNode in the ref.</p>
     * <p>In practice, there would be only the node building</p>
     *
     * @return an ObjectNode containing the transaction details based on the
     *         error type and its associated information
     */
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        if (!error.contains("kind")) {
            node.put("timestamp", timestamp);
        }
        if (!error.contains("funds") && !error.contains("frozen") && !error.contains("savings")
                && !error.contains("not found") && !error.contains("changed")) {
            node.put("error", error);
        } else {
            node.put("description", error);
        }
        return node;
    }

    /**
     * Checks if the given type matches the specific transaction type "ErrorTransaction".
     *
     * @param type the string representing the type to be checked
     * @return true if the type matches "ErrorTransaction", false otherwise
     */
    public boolean matchesType(final String type) {
        return "ErrorTransaction".contentEquals(type);
    }
}
