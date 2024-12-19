package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class SetMinBalanceCommand implements Command {
    /**
     * Executes the command to update the minimum balance of an account associated with
     * a specific IBAN.
     * <p></p>The operation iterates through the list of users and their accounts
     * to find the account matching the IBAN provided in the input and sets its minimum
     * balance to the specified amount.
     *
     * @param input the command input containing details such as the
     *             target IBAN and the new minimum balance
     * @param users the list of users whose accounts will be checked against the input IBAN
     * @param exchangeRates unused parameter in the current implementation
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount())) {
                    account.setMinBalance(input.getAmount());
                }
            }
        }
    }
}
