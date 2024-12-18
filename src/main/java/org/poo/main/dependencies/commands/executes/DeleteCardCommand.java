package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.NewCard;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.Card;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

public class DeleteCardCommand implements Command {
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        Card targetedCard = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().contentEquals(input.getCardNumber())) {
                        user.getTransactions().add(
                                new NewCard(account.getIBAN(), input.getCardNumber(),
                                        user.getEmail(), "The card has been destroyed",
                                        input.getTimestamp()));
                        targetedCard = card;
                    }
                }
                if (targetedCard != null) {
                    account.getCards().remove(targetedCard);
                }
            }
        }
    }
}
