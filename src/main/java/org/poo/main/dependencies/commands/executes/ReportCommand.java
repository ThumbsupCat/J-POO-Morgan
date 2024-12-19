package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.ErrorTransaction;
import org.poo.main.dependencies.commands.executes.transactionhelper.TransactionHelper;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class ReportCommand implements Command {
    private final ArrayNode output;
    private final ObjectMapper mapper;
    public ReportCommand(final ArrayNode output) {
        this.output = output;
        mapper = new ObjectMapper();
    }

    /**
     * Executes the "report" command by processing a list of
     * users and their accounts
     * based on the provided input criteria.
     * <p></p>It generates a report of account details
     * and transactions falling within the specified time range. If no matching account
     * is found, an error response is added to the output.
     *
     * @param input         the input command parameters, including account details
     *                      and time range for filtering transactions
     * @param users         the list of users to search for the specified account
     *                      information
     * @param exchangeRates the list of exchange rates, currently not used in this
     *                      implementation but part of the method signature
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        boolean found = false;
        for (final User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount())) {
                    /*
                    *  Building the output node for the command call
                    */
                    ObjectNode node = mapper.createObjectNode();
                    node.put("command", "report");
                    ObjectNode accountNode = mapper.createObjectNode();
                    accountNode.put("balance", account.getBalance());
                    accountNode.put("currency", account.getCurrency());
                    accountNode.put("IBAN", account.getIBAN());
                    ArrayNode transactionsNode = mapper.createArrayNode();
                    /*
                    *  Going through every transaction in which the account was involved
                    *  and adding it to the output
                    */
                    for (TransactionHelper transaction : account.getTransactions()) {
                        int timestamp = transaction.printTransactions().get("timestamp").asInt();
                        if (timestamp >= input.getStartTimestamp()
                                && timestamp <= input.getEndTimestamp()) {
                            transactionsNode.add(transaction.printTransactions());
                        }
                    }
                    accountNode.set("transactions", transactionsNode);
                    node.set("output", accountNode);
                    node.put("timestamp", input.getTimestamp());
                    output.add(node);
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            /* Showing an error in the output for the failed command */
            ObjectNode node = mapper.createObjectNode();
            node.put("command", "report");
            node.set("output", new ErrorTransaction(
                    input.getTimestamp(), "Account not found").printTransactions());
            node.put("timestamp", input.getTimestamp());
            output.add(node);
        }
    }
}
