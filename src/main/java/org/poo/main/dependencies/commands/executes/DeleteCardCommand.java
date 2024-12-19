package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.NewCard;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.Card;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class DeleteCardCommand implements Command {
    /**
     * Executes the delete card command by locating and removing the specified card from
     * the user's account, and logs the transaction indicating the destruction of the card.
     *
     * @param input        the input data containing the cardNumber and other required details
     * @param users        the list of users to search for the target card
     * @param exchangeRates the list of exchange rates,
     *                     not used in this method but provided for command interface compliance
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        Card targetedCard = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().contentEquals(input.getCardNumber())) {
                        user.getTransactions().add(
                                new NewCard(account.getIBAN(), input.getCardNumber(),
                                        user.getEmail(), "The card has been destroyed",
                                        input.getTimestamp())
                        );
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
