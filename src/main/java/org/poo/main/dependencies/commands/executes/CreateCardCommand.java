package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.NewCard;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.Card;
import org.poo.main.dependencies.userinfo.User;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class CreateCardCommand implements Command {
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        for (User user : users) {
            if (user.getEmail().contentEquals(input.getEmail())) {
                boolean found = false;
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().contentEquals(input.getAccount())) {
                        found = true;
                        Card newCard;
                        if (input.getCommand().contains("OneTime")) {
                            newCard = new Card(Utils.generateCardNumber(), "active", true);
                        } else {
                            newCard = new Card(Utils.generateCardNumber(), "active");
                        }
                        user.getTransactions().add(new NewCard(
                                account.getIBAN(), newCard.getCardNumber(),
                                user.getEmail(), "New card created",
                                input.getTimestamp()));
                        account.getTransactions().add(new NewCard(account.getIBAN(), newCard.getCardNumber(),
                                user.getEmail(), "New card created",
                                input.getTimestamp()));
                        account.getCards().add(newCard);
                    }
                }
                if (!found) {
                    System.out.println("muie");
                }
            }
        }
    }
}
