package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NewAccount implements TransactionHelper {
    private ObjectMapper mapper;
    private int timestamp;
    private String description;
    public NewAccount(final int timestamp, final String description) {
        mapper = new ObjectMapper();
        this.timestamp = timestamp;
        this.description = description;
    }
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        return node;
    }
}
