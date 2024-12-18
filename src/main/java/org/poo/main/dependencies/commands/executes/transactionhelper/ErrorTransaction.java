package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ErrorTransaction implements TransactionHelper {
    private final int timestamp;
    private final String error;
    private final ObjectMapper mapper;
    public ErrorTransaction(final int timestamp, final String error) {
        this.timestamp = timestamp;
        this.error = error;
        mapper = new ObjectMapper();
    }
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
    public boolean matchesType(final String type) {
        return "ErrorTransaction".contentEquals(type);
    }
}
