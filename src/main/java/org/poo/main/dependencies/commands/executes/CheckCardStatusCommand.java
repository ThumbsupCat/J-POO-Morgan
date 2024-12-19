package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.ErrorTransaction;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.Card;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class CheckCardStatusCommand implements Command {
    private final ArrayNode output;
    private final ObjectMapper objectMapper;
    public CheckCardStatusCommand(final ArrayNode output) {
        objectMapper = new ObjectMapper();
        this.output = output;
    }

    /**
     * Executes the checkCardStatus command, verifying the status of
     * a card and freezing it
     * if the associated account balance has reached the minimum allowable funds.
     * <p>If the card is not found, an error message is added to the output.</p>
     *
     * @param input        the input data containing command details, including the card number
     *                     to be checked and the timestamp.
     * @param users        a list of users, each containing accounts and associated cards, to be
     *                     evaluated during the command execution.
     * @param exchangeRates a list of exchange rates, which is unused in
     *                      this specific implementation.
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        boolean found = false;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().contentEquals(input.getCardNumber())) {
                        if (account.getBalance() - account.getMinBalance()
                                <= account.getMinBalance()) {
                            user.getTransactions().add(new ErrorTransaction(input.getTimestamp(),
                                    "You have reached the minimum amount of funds,"
                                            + " the card will be frozen")
                            );
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
