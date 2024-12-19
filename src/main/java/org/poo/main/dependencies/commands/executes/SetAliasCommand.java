package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class SetAliasCommand implements Command {
    public SetAliasCommand() {
    }

    /**
     * Executes the command to set an alias for
     * a specific user account.
     * <p></p>The method identifies the user by their email and updates the alias
     * for a specified account identified by its IBAN.
     *
     * @param input        the input containing the command details,
     *                    including the email, account IBAN and the new alias to be set
     * @param users        the list of all users in the system
     * @param exchangeRates the list of exchange rates (not used in this implementation)
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        for (User user : users) {
            if (user.getEmail().contentEquals(input.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().contentEquals(input.getAccount())) {
                        /*
                        *   Setting the new Alias to the selected account
                        */
                        account.setAlias(input.getAlias());
                    }
                }
            }
        }
    }
}
