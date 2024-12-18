package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.MoneySendTransaction;
import org.poo.main.dependencies.commands.executes.transactionhelper.SplitPaymentTransaction;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static org.poo.main.dependencies.ExchangeRate.reverseConvert;

public class SplitPaymentCommand implements Command {
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        double splitPay = input.getAmount() / input.getAccounts().size();
        String error = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (input.getAccounts().contains(account.getIBAN())) {
                    double convertedAmount = 0;
                    double senderBalance = account.getBalance();
                    if (account.getCurrency().contentEquals(input.getCurrency())) {
                        convertedAmount = splitPay;
                    } else {
                        convertedAmount = reverseConvert(exchangeRates, splitPay, input.getCurrency(), account.getCurrency());
                        senderBalance = reverseConvert(exchangeRates, senderBalance,  input.getCurrency(), account.getCurrency());
                    }
                    if (senderBalance >= convertedAmount) {
                        account.setBalance(account.getBalance() - convertedAmount);
                    } else {
                        error = "Account " + account.getIBAN()
                                + " has insufficient funds for a split payment.";
                    }
                }
            }
        }
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (input.getAccounts().contains(account.getIBAN())) {
                    user.getTransactions().add(new SplitPaymentTransaction(
                            input.getTimestamp(), "Split payment of "
                            + BigDecimal.valueOf(input.getAmount()).setScale(
                                    2, RoundingMode.HALF_UP) + " " + input.getCurrency(),
                            input.getCurrency(), splitPay,
                            input.getAccounts(), error));
                    account.getTransactions().add(new SplitPaymentTransaction(
                            input.getTimestamp(), "Split payment of "
                            + BigDecimal.valueOf(input.getAmount()).setScale(
                            2, RoundingMode.HALF_UP) + " " + input.getCurrency(),
                            input.getCurrency(), splitPay,
                            input.getAccounts(), error));
                }
            }
        }
    }
}