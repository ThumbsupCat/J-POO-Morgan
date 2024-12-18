package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

public class DeleteAccountTransaction implements TransactionHelper {
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
    public DeleteAccountTransaction(final String description, final int timestamp) {
        this.command = "deleteAccountTransaction";
        this.timestamp = timestamp;
        this.description = description;
        this.mapper = new ObjectMapper();
    }
    public ObjectNode printTransactions() {
        ObjectNode node = mapper.createObjectNode();
        if (command.contentEquals("deleteAccount")) {
            node.put("command", command);
            ObjectNode output = mapper.createObjectNode();
            output.put("success", description);
            output.put("timestamp", timestamp);
            node.set("output", output);
            return node;
        }
        node.put("description", description);
        node.put("timestamp", timestamp);
        return node;
    }
    public boolean matchesType(final String type) {
        return "DeleteAccountTransaction".contentEquals(type);
    }
}
