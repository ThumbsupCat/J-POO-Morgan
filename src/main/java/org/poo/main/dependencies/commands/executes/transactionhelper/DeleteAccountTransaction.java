package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

public class DeleteAccountTransaction {
    /*
      "command" : "deleteAccount",
  "output" : {
    "success" : "Account deleted",
    "timestamp" : 10
  }
     */
    private final String command;
    private final int timestamp;
    private final String description;
    private final ObjectMapper mapper;
    public DeleteAccountTransaction(final String command, final int timestamp, final String description) {
        this.command = command;
        this.timestamp = timestamp;
        this.description = description;
        this.mapper = new ObjectMapper();
    }
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", command);
        ObjectNode output = mapper.createObjectNode();
        output.put("success", description);
        output.put("timestamp", timestamp);
        node.set("output", output);
        return node;
    }
}
