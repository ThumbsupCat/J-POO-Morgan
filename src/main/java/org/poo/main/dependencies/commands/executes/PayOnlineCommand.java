package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.Card;
import org.poo.main.dependencies.userinfo.User;
import org.poo.utils.Utils;

import java.util.ArrayList;

import static org.poo.main.dependencies.ExchangeRate.reverseConvert;

public class PayOnlineCommand implements Command {
    private ArrayNode output;
    private ObjectMapper mapper;
    public PayOnlineCommand(final ArrayNode output) {
        mapper = new ObjectMapper();
        this.output = output;
    }
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        //De adaugat in tranzactii mai tarziu
        boolean found = false;
        for (final User user : users) {
            if (user.getEmail().contentEquals(input.getEmail())) {
                for (Account account : user.getAccounts()) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().contentEquals(input.getCardNumber())) {
                            found = true;
                            double convertedAmount = 0;
                            if (!input.getCurrency().contentEquals(account.getCurrency())) {
                                convertedAmount = reverseConvert(exchangeRates, input.getAmount(), input.getCurrency(), account.getCurrency());
                            } else {
                                convertedAmount = input.getAmount();
                            }
                            System.out.println(convertedAmount);
                            if (account.getBalance() >= convertedAmount) {
                                account.setBalance(account.getBalance() - convertedAmount);
                                if (card.isOneTime()) {
                                    Card newCard = new Card(
                                            Utils.generateCardNumber(), "active", true);
                                    account.getCards().remove(card);
                                    account.getCards().add(newCard);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!found) {
            ObjectNode node = mapper.createObjectNode();
            node.put("command", input.getCommand());
            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", input.getTimestamp());
            outputNode.put("description", "Card not found");
            node.set("output", outputNode);
            node.put("timestamp", input.getTimestamp());
            output.add(node);
        }
    }

}
