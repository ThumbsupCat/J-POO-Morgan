package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.TransactionHelper;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

public class PrintTransactionsCommand implements Command {
    private final ArrayNode output;
    private final ObjectMapper mapper;
    public PrintTransactionsCommand(final ArrayNode output) {
        this.output = output;
        mapper = new ObjectMapper();
    }
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        for (final User user : users) {
            if (user.getEmail().contentEquals(input.getEmail())) {
                ObjectNode node = mapper.createObjectNode();
                node.put("command", "printTransactions");
                ArrayNode outputNode = mapper.createArrayNode();
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
