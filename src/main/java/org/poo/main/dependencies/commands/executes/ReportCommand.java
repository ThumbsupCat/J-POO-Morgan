package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.TransactionHelper;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

public class ReportCommand implements Command {
    private ArrayNode output;
    private ObjectMapper mapper;
    public ReportCommand(final ArrayNode output) {
        this.output = output;
        mapper = new ObjectMapper();
    }
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        for (final User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount())) {
                    ObjectNode node = mapper.createObjectNode();
                    node.put("command", "report");
                    ObjectNode accountNode = mapper.createObjectNode();
                    accountNode.put("balance", account.getBalance());
                    accountNode.put("currency", account.getCurrency());
                    accountNode.put("IBAN", account.getIBAN());
                    ArrayNode transactionsNode = mapper.createArrayNode();
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
                }
            }
        }
    }
}
