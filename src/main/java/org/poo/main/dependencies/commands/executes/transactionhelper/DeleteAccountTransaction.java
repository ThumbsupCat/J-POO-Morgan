package org.poo.main.dependencies.commands.executes.transactionhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class DeleteAccountTransaction implements TransactionHelper {
    private final String command;
    private final int timestamp;
    private final String description;
    private final ObjectMapper mapper;
    public DeleteAccountTransaction(final String command, final int timestamp,
                                    final String description) {
        //Used for future implementation
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

    /**
     * Constructs and returns an ObjectNode representing the transaction details
     * for the delete account transaction. Depending on the transaction type
     * (if it's an account transaction or a user transaction)
     *
     * @return an ObjectNode containing the details of the delete account transaction
     */
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

    /**
     * Checks if the given type matches the specific transaction type
     * "DeleteAccountTransaction".
     *
     * @param type the string representing the type to be checked
     * @return true if the type matches "DeleteAccountTransaction", false otherwise
     */
    public boolean matchesType(final String type) {
        return "DeleteAccountTransaction".contentEquals(type);
    }
}
