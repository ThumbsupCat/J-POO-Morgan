package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.ErrorTransaction;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.Card;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

public class CheckCardStatusCommand implements Command {
    private ArrayNode output;
    private ObjectMapper objectMapper;
    public CheckCardStatusCommand(final ArrayNode output) {
        objectMapper = new ObjectMapper();
        this.output = output;
    }
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        boolean found = false;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().contentEquals(input.getCardNumber())) {
                        if (account.getBalance() - account.getMinBalance() <= account.getMinBalance()) {
                            user.getTransactions().add(new ErrorTransaction(input.getTimestamp(),
                                    "You have reached the minimum amount of funds,"
                                            + " the card will be frozen"));
                            card.setStatus("frozen");
                        }
                        found = true;
                    }
                }
            }
        }
        if (!found) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "checkCardStatus");
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("description", "Card not found");
            outputNode.put("timestamp", input.getTimestamp());
            node.set("output", outputNode);
            node.put("timestamp", input.getTimestamp());
            output.add(node);
        }
    }
}
