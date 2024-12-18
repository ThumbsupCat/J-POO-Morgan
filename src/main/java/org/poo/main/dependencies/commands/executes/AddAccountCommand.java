package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.NewAccount;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class AddAccountCommand implements Command {
    public AddAccountCommand() {

    }
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        String newIBAN = Utils.generateIBAN();
        Account newAccount;
        if (input.getAccountType().contains("savings")) {
            newAccount = new Account(newIBAN,
                    input.getCurrency(),
                    input.getAccountType(),
                    input.getInterestRate());
        } else {
            newAccount = new Account(newIBAN, input.getCurrency(), input.getAccountType());
        }
        for (User user : users) {
            if (user.getEmail().contentEquals(input.getEmail())) {
                user.getAccounts().add(newAccount);
                user.getTransactions().add(
                        new NewAccount(input.getTimestamp(), "New account created"));
                newAccount.getTransactions().add(
                        new NewAccount(input.getTimestamp(), "New account created"));
            }
        }
    }
}
