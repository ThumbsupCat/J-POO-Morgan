package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.ErrorTransaction;
import org.poo.main.dependencies.commands.executes.transactionhelper.NewCard;
import org.poo.main.dependencies.commands.executes.transactionhelper.PayOnlineTransaction;
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
                            if (account.getBalance() >= convertedAmount && card.getStatus().contentEquals("active")) {
                                account.setBalance(account.getBalance() - convertedAmount);
                                user.getTransactions().add(
                                        new PayOnlineTransaction(
                                                convertedAmount, input.getCommerciant(),
                                                "Card payment", input.getTimestamp()));
                                account.getTransactions().add(new PayOnlineTransaction(
                                        convertedAmount, input.getCommerciant(),
                                        "Card payment", input.getTimestamp()));
                                if (card.isOneTime()) {
                                    Card newCard = new Card(
                                            Utils.generateCardNumber(), "active", true);
                                    user.getTransactions().add(
                                            new NewCard(account.getIBAN(), card.getCardNumber(),
                                                    user.getEmail(), "The card has been destroyed",
                                                    input.getTimestamp()));
                                    user.getTransactions().add(
                                            new NewCard(account.getIBAN(), newCard.getCardNumber(),
                                                    user.getEmail(), "New card created",
                                                    input.getTimestamp()));
                                    account.getCards().remove(card);
                                    account.getCards().add(newCard);
                                }
                                boolean foundCommerciant = false;
                                for (Commerciant commerciant : account.getCommerciants()) {
                                    if (commerciant.getName().contentEquals(input.getCommerciant())) {
                                        foundCommerciant = true;
                                        commerciant.setAmount(commerciant.getAmount() + convertedAmount);
                                    }
                                }
                                if (!foundCommerciant) {
                                    account.getCommerciants().add(
                                            new Commerciant(input.getCommerciant(), convertedAmount));
                                }
                                card.cardStatusUpdate(account);
                            } else {
                                String error = "";
                                if (card.getStatus().contentEquals("frozen")) {
                                    error = "The card is frozen";
                                } else {
                                    error = "Insufficient funds";
                                }
                                user.getTransactions().add(new ErrorTransaction(
                                                input.getTimestamp(), error));
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
