package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.Card;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class PrintUsersCommand implements Command {
    private final ArrayNode output;
    private final ObjectMapper mapper;
    public PrintUsersCommand(final ArrayNode output) {
        this.output = output;
        mapper = new ObjectMapper();
    }
    @Override
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", input.getCommand());
        ArrayNode usersInfo = mapper.createArrayNode(); /* List of users' information */
        /* Going through every user's info in the database and showing the information requested */
        for (User user : users) {
            ObjectNode userNode = mapper.createObjectNode();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());
            ArrayNode accounts = mapper.createArrayNode();
            for (Account account : user.getAccounts()) {
                ObjectNode accountNode = mapper.createObjectNode();
                accountNode.put("IBAN", account.getIBAN());
                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getType());
                ArrayNode cards = mapper.createArrayNode();
                for (Card card : account.getCards()) {
                    ObjectNode cardNode = mapper.createObjectNode();
                    cardNode.put("cardNumber", card.getCardNumber());
                    cardNode.put("status", card.getStatus());
                    cards.add(cardNode);
                }
                accountNode.set("cards", cards);
                accounts.add(accountNode);
            }
            userNode.set("accounts", accounts);
            usersInfo.add(userNode);
        }
        node.set("output", usersInfo);
        node.put("timestamp", input.getTimestamp());
        output.add(node);
    }
}
