package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

public class AddFundsCommand implements Command {
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount())) {
                    account.setBalance(account.getBalance() + input.getAmount());
                }
            }
        }
    }
}
