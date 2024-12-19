package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.ErrorTransaction;
import org.poo.main.dependencies.commands.executes.transactionhelper.CardTransaction;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.Card;
import org.poo.main.dependencies.userinfo.User;
import org.poo.utils.Utils;

import java.util.List;

public final class CreateCardCommand implements Command {
    /**
     * Executes the process of handling a user command to create a new card
     * and register the corresponding transactions for the specified account.
     *
     * @param input the command input containing details such as
     *             command type, email, account, and timestamp
     * @param users the list of users among which the targeted user is searched based on
     *             the provided email
     * @param exchangeRates the list of exchange rates, although not directly used in this process
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
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
                        user.getTransactions().add(new CardTransaction(
                                account.getIBAN(), newCard.getCardNumber(),
                                user.getEmail(), "New card created",
                                input.getTimestamp()));
                        account.getTransactions().add(
                                new CardTransaction(account.getIBAN(), newCard.getCardNumber(),
                                user.getEmail(), "New card created",
                                input.getTimestamp())
                        );
                        account.getCards().add(newCard);
                    }
                }
                if (!found) {
                    /* Adding the error in the user transactions list
                    This should've been a test case, error message parsed to the user transactions
                    */
                    user.getTransactions().add(
                            new ErrorTransaction(input.getTimestamp(), "Account not found")
                    );
                }
            }
        }
    }
}
