package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.ErrorTransaction;
import org.poo.main.dependencies.commands.executes.transactionhelper.MoneySendTransaction;
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
        User userSender = null;
        User userReceiver = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount())) {
                    sender = account;
                    userSender = user;
                }
                if (account.getIBAN().contentEquals(input.getReceiver())) {
                    receiver = account;
                    userReceiver = user;
                }
            }
        }
        if (sender == null || receiver == null) {
            return;
        }
        double convertedAmount = 0;
        double senderBalance = sender.getBalance();
        if (sender.getCurrency().contentEquals(receiver.getCurrency())) {
            convertedAmount = input.getAmount();
        } else {
            convertedAmount = reverseConvert(exchangeRates, input.getAmount(), sender.getCurrency(), receiver.getCurrency());
            senderBalance = reverseConvert(exchangeRates, senderBalance, sender.getCurrency(), receiver.getCurrency());
        }
        if (senderBalance >= convertedAmount) {
            sender.setBalance(sender.getBalance() - input.getAmount());
            receiver.setBalance(receiver.getBalance() + convertedAmount);
            userSender.getTransactions().add(
                    new MoneySendTransaction(input.getTimestamp(), input.getDescription(),
                    sender.getIBAN(), receiver.getIBAN(),
                    input.getAmount(), sender.getCurrency(), "sent"));
            sender.getTransactions().add(new MoneySendTransaction(
                    input.getTimestamp(), input.getDescription(),
                    sender.getIBAN(), receiver.getIBAN(),
                    input.getAmount(), sender.getCurrency(), "sent"));
            userReceiver.getTransactions().add(
                    new MoneySendTransaction(input.getTimestamp(), input.getDescription(),
                    sender.getIBAN(), receiver.getIBAN(),
                    convertedAmount, receiver.getCurrency(), "received"));
            receiver.getTransactions().add(new MoneySendTransaction(
                    input.getTimestamp(), input.getDescription(),
                    sender.getIBAN(), receiver.getIBAN(),
                    convertedAmount, receiver.getCurrency(), "received"));

        } else {
            userSender.getTransactions().add(
                    new ErrorTransaction(input.getTimestamp(), "Insufficient funds"));
            sender.getTransactions().add(
                    new ErrorTransaction(input.getTimestamp(), "Insufficient funds"));
        }
    }
}
