package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.ErrorTransaction;
import org.poo.main.dependencies.commands.executes.transactionhelper.TransactionHelper;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpendingReportCommand implements Command {
    private ArrayNode output;
    private ObjectMapper mapper;
    public SpendingReportCommand(final ArrayNode output) {
        mapper = new ObjectMapper();
        this.output = output;
    }
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        boolean accountFound = false;
        String error = "Account not found";
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount())) {
                    accountFound = true;
                    if (!account.getType().equals("savings")) {
                        ObjectNode reportNode = mapper.createObjectNode();
                        reportNode.put("command", "spendingsReport");

                        ObjectNode outputNode = mapper.createObjectNode();
                        outputNode.put("balance", account.getBalance());
                        outputNode.put("currency", account.getCurrency());
                        outputNode.put("IBAN", account.getIBAN());

                        ArrayNode transactionsArray = mapper.createArrayNode();
                        Map<String, Double> commerciantTotals = new HashMap<>();

                        for (TransactionHelper transaction : account.getTransactions()) {
                            if (transaction.matchesType("PayOnlineTransaction")) {
                                ObjectNode transactionNode = transaction.printTransactions();
                                int transactionTimestamp = transactionNode.get("timestamp").asInt();

                                if (transactionTimestamp >= input.getStartTimestamp()
                                        && transactionTimestamp <= input.getEndTimestamp()) {

                                    transactionsArray.add(transactionNode);

                                    String commerciantName = transactionNode.get("commerciant").asText();
                                    double amount = transactionNode.get("amount").asDouble();

                                    commerciantTotals.put(commerciantName,
                                            commerciantTotals.getOrDefault(commerciantName, 0.0) + amount);
                                }
                            }
                        }
                        ArrayNode commerciantsArray = mapper.createArrayNode();
                        commerciantTotals.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey()) // Sort by commerciant name
                                .forEach(entry -> {
                                    ObjectNode commerciantNode = mapper.createObjectNode();
                                    commerciantNode.put("commerciant", entry.getKey());
                                    commerciantNode.put("total", entry.getValue());
                                    commerciantsArray.add(commerciantNode);
                                });

                        outputNode.set("transactions", transactionsArray);
                        outputNode.set("commerciants", commerciantsArray);
                        reportNode.set("output", outputNode);
                        reportNode.put("timestamp", input.getTimestamp());
                        output.add(reportNode);
                    } else {
                        accountFound = false;
                        error = "This kind of report is not supported for a saving account";
                    }
                }
            }
        }
        if (!accountFound) {
            ObjectNode node = mapper.createObjectNode();
            node.put("command", "spendingsReport");
            node.set("output", new ErrorTransaction(
                    input.getTimestamp(), error).printTransactions());
            node.put("timestamp", input.getTimestamp());
            output.add(node);
        }
    }
}
