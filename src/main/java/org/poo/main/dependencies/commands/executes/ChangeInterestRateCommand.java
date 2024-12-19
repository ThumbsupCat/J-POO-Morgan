package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.ErrorTransaction;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class ChangeInterestRateCommand implements Command {
    private final ArrayNode output;
    private final ObjectMapper mapper;

    public ChangeInterestRateCommand(final ArrayNode output) {
        this.output = output;
        mapper = new ObjectMapper();
    }

    /**
     * Executes the command to change the interest rate of a specified account.
     * If the account is a savings account, the interest rate is updated, and
     * a transaction is added to the user's transactions. Otherwise, an error
     * message is logged indicating that the account is not a savings account.
     *
     * @param input         The command input containing details such as the account IBAN,
     *                      the new interest rate, and the timestamp of the action.
     * @param users         The list of users whose accounts are to be checked for the
     *                      matching IBAN and account type.
     * @param exchangeRates The list of exchange rates, unused in this specific command
     *                      function.
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        boolean isSavingsAccount = false;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount())
                        && account.getType().equals("savings")) {
                    account.setInterestRate(input.getInterestRate());
                    user.getTransactions().add(
                            new ErrorTransaction(input.getTimestamp(),
                                    "Interest rate of the account changed to "
                                            + input.getInterestRate()));
                    isSavingsAccount = true;
                }
            }
        }
        if (!isSavingsAccount) {
            ObjectNode node = mapper.createObjectNode();
            node.put("command", "changeInterestRate");
            ObjectNode outputNode = new ErrorTransaction(
                    input.getTimestamp(), "This is not a savings account").printTransactions();
            node.set("output", outputNode);
            node.put("timestamp", input.getTimestamp());
            output.add(node);
        }
    }
}
