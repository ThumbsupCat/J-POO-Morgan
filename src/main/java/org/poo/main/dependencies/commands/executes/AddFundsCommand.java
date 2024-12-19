package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class AddFundsCommand implements Command {
    /**
     * Executes the "add funds" command,
     * which increases the balance of the specified
     * account by the specified amount from the input.
     *
     * @param input         the command input containing details such as the target
     *                      account's IBAN and the amount to be added
     * @param users         the list of users, each containing accounts that may match
     *                      the target IBAN from the input
     * @param exchangeRates the list of exchange rates, currently not utilized in
     *                      this method
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount())) {
                    account.setBalance(account.getBalance() + input.getAmount());
                }
            }
        }
    }
}
