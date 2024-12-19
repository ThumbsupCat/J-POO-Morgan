package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.ErrorTransaction;
import org.poo.main.dependencies.commands.executes.transactionhelper.MoneySendTransaction;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

import static org.poo.main.dependencies.ExchangeRate.reverseConvert;

public final class SendMoneyCommand implements Command {
    /**
     * Executes a money transfer operation between two accounts based on the provided input.
     * Handles validation, currency conversion, and records the transaction details.
     *
     * @param input The command input containing transaction details such as sender account IBAN,
     *              receiver account IBAN, amount, currency, description, and timestamp.
     * @param users A list of users whose accounts are searched for
     *              the sender and receiver accounts.
     * @param exchangeRates A list of exchange rates used for currency conversion between the sender
     *                      and receiver's account currencies, if applicable.
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
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
        double convertedAmount;
        double senderBalance = sender.getBalance();
        if (sender.getCurrency().contentEquals(receiver.getCurrency())) {
            convertedAmount = input.getAmount();
        } else {
            convertedAmount = reverseConvert(
                    exchangeRates, input.getAmount(), sender.getCurrency(), receiver.getCurrency());
            senderBalance = reverseConvert(
                    exchangeRates, senderBalance, sender.getCurrency(), receiver.getCurrency());
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
