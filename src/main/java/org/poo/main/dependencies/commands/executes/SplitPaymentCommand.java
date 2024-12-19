package org.poo.main.dependencies.commands.executes;

import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.SplitPaymentTransaction;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.poo.main.dependencies.ExchangeRate.reverseConvert;

public final class SplitPaymentCommand implements Command {
    /**
     * Executes the split payment operation by dividing
     * the specified payment amount equally across a
     * list of target accounts while considering currency conversions and account balances.
     * <p>If any account has insufficient funds,
     * the transaction will be marked with an error message.</p>
     *
     * @param input         The command input containing details about the split payment,
     *                      including the amount, target accounts, and currency.
     * @param users         The list of users whose accounts may be involved in the
     *                      split payment process.
     * @param exchangeRates The list of exchange rates used for currency conversion
     *                      during the split payment operation.
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        double splitPay = input.getAmount() / input.getAccounts().size();
        double convertedAmount;
        String error = null;
        for (String accountString : input.getAccounts()) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().contentEquals(accountString)) {
                        if (account.getCurrency().contentEquals(input.getCurrency())) {
                            /*
                            *   splitPay is in account's currency
                            */
                            convertedAmount = splitPay;
                        } else {
                            /*
                            *   Converting the splitPay to the account's currency
                            */
                            convertedAmount = reverseConvert(
                                    exchangeRates,
                                    splitPay,
                                    input.getCurrency(),
                                    account.getCurrency()
                            );
                        }
                        if (account.getBalance() < convertedAmount) {
                            /* Keeping the error message */
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
                            convertedAmount = reverseConvert(
                                    exchangeRates,
                                    splitPay,
                                    input.getCurrency(),
                                    account.getCurrency()
                            );
                        }
                        account.setBalance(senderBalance - convertedAmount);
                    }
                }
            }
        }
        /*
        *   Regardless of the error (null / non-null), we can handle the output node
        *   in the transaction helper
        *   Also, logging the transaction regardless of the result
        */
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (input.getAccounts().contains(account.getIBAN())) {
                    user.getTransactions().add(
                            new SplitPaymentTransaction(
                                input.getTimestamp(),
                                "Split payment of "
                                + BigDecimal.valueOf(
                                    input.getAmount()).setScale(
                                    2, RoundingMode.HALF_UP
                                ) + " " + input.getCurrency(),
                                input.getCurrency(),
                                splitPay,
                                input.getAccounts(),
                                error
                            )
                    );
                    account.getTransactions().add(
                            new SplitPaymentTransaction(
                                input.getTimestamp(),
                                "Split payment of "
                                        + BigDecimal.valueOf(
                                                input.getAmount()
                                ).setScale(
                            2,
                                     RoundingMode.HALF_UP
                                )
                                + " " + input.getCurrency(),
                                input.getCurrency(), splitPay,
                                input.getAccounts(), error
                            )
                    );
                }
            }
        }
    }
}
