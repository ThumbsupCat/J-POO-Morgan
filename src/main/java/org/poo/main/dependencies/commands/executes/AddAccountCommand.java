package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.NewAccount;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;
import org.poo.utils.Utils;

import java.util.List;

public final class AddAccountCommand implements Command {
    public AddAccountCommand() {

    }

    /**
     * Executes the process of creating a new account for a user and
     * updates the associated user transactions.
     *
     * @param input         the command input containing details about the account
     *                      to be created such as type, currency,
     *                      interest rate if mentioned, and user email.
     * @param users         the list of users to check against for associating the new account
     *                      with the correct user.
     * @param exchangeRates the list of exchange rates
     *                      (unused in the current implementation but provided for context).
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        String newIBAN = Utils.generateIBAN();
        Account newAccount = getNewAccount(input, newIBAN); /* Creating account method */
        for (User user : users) {
            if (user.getEmail().contentEquals(input.getEmail())) {
                /* Logging the activity */
                user.getAccounts().add(newAccount);
                user.getTransactions().add(
                        new NewAccount(input.getTimestamp(), "New account created"));
                newAccount.getTransactions().add(
                        new NewAccount(input.getTimestamp(), "New account created"));
            }
        }
    }

    private static Account getNewAccount(final CommandInput input, final String newIBAN) {
        Account newAccount;
        if (input.getAccountType().contains("savings")) { /* Is it a savings account? */
            /* Creating a savings account... */
            newAccount = new Account(newIBAN,
                    input.getCurrency(),
                    input.getAccountType(),
                    input.getInterestRate()
            );
        } else {
            /* Creating a classic account */
            newAccount = new Account(
                    newIBAN, input.getCurrency(), input.getAccountType()
            );
        }
        return newAccount;
    }
}
