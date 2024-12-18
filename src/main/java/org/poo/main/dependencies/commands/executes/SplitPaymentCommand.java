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
        double convertedAmount;
        String error = null;
        for (String accountString : input.getAccounts()) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().contentEquals(accountString)) {
                        if (account.getCurrency().contentEquals(input.getCurrency())) {
                            convertedAmount = splitPay;
                        } else {
                            convertedAmount = reverseConvert(exchangeRates, splitPay, input.getCurrency(), account.getCurrency());
                        }
                        if (account.getBalance() < convertedAmount) {
                                error = "Account " + accountString
                                    + " has insufficient funds for a split payment.";
                        }
                    }
                }
            }
        }
        if (error == null) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (input.getAccounts().contains(account.getIBAN())) {
                        double senderBalance = account.getBalance();
                        if (account.getCurrency().contentEquals(input.getCurrency())) {
                            convertedAmount = splitPay;
                        } else {
                            convertedAmount = reverseConvert(exchangeRates, splitPay, input.getCurrency(), account.getCurrency());
                        }
                        account.setBalance(senderBalance - convertedAmount);
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