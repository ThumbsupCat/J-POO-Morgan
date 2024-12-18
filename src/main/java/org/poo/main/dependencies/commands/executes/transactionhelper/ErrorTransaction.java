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
        node.put("timestamp", timestamp);
        if (!error.contentEquals("Insufficient funds") && !error.contains("frozen")) {
            node.put("error", error);
        } else {
            node.put("description", error);
        }
        return node;
    }
}
