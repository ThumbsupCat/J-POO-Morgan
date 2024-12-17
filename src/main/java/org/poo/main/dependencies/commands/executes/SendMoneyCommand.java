package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

import static org.poo.main.dependencies.ExchangeRate.reverseConvert;

public class SendMoneyCommand implements Command {
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        Account sender = null;
        Account receiver = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount())) {
                    sender = account;
                }
                if (account.getIBAN().contentEquals(input.getReceiver())) {
                    receiver = account;
                }
            }
        }
        if (sender == null || receiver == null) {
            return;
        }
        double convertedAmount = 0;
        if (sender.getCurrency().contentEquals(receiver.getCurrency())) {
            convertedAmount = input.getAmount();
        } else {
            convertedAmount = reverseConvert(exchangeRates, input.getAmount(), sender.getCurrency(), receiver.getCurrency());
        }
        if (sender.getBalance() >= convertedAmount) {
            sender.setBalance(sender.getBalance() - input.getAmount());
            receiver.setBalance(receiver.getBalance() + convertedAmount);
        }
    }
}
