package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.TransactionHelper;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class PrintTransactionsCommand implements Command {
    private final ArrayNode output;
    private final ObjectMapper mapper;
    public PrintTransactionsCommand(final ArrayNode output) {
        this.output = output;
        mapper = new ObjectMapper();
    }

    /**
     * Executes the "printTransactions" command, generating a response with details
     * of all transactions associated with the user specified in the input.
     * <p></p>The response includes the command name, user details,
     * a list of transactions, and the associated timestamp.
     *
     * @param input         the input command parameters, including the email address of the
     *                      user and the timestamp for the operation
     * @param users         the list of users to search for the user specified in the input
     * @param exchangeRates the list of exchange rates, currently not used in this implementation
     *                      but part of the method signature
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        for (final User user : users) {
            if (user.getEmail().contentEquals(input.getEmail())) {
                /* Building the output with the needed nodes */
                ObjectNode node = mapper.createObjectNode();
                node.put("command", "printTransactions");
                ArrayNode outputNode = mapper.createArrayNode();
                /* Going through every transaction and adding it to the output node */
                for (TransactionHelper transaction : user.getTransactions()) {
                    outputNode.add(transaction.printTransactions());
                }
                node.set("output", outputNode);
                node.put("timestamp", input.getTimestamp());
                output.add(node);
            }
        }
    }
}
