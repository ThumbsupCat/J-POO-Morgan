package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.commands.executes.transactionhelper.DeleteAccountTransaction;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class DeleteAccountCommand implements Command {
    private final ArrayNode output;
    private final ObjectMapper mapper;
    public DeleteAccountCommand(final ArrayNode output) {
        this.output = output;
        this.mapper = new ObjectMapper();
    }

    /**
     * Executes the delete account command. Deletes a user's account if it meets the given
     * criteria, such as having a zero balance. If the deletion fails, an error message is logged,
     * and details about the failure are added as a transaction entry.
     *
     * @param input the input command containing the necessary parameters to identify the account
     *              and its details (such as email, IBAN, and timestamp)
     * @param users a list of users from which the target user and account are searched
     * @param exchangeRates a list of exchange rate information; currently unused by this method
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        boolean success = false;
        Account targetedAccount = null;
        User targetedUser = null;
        for (final User user : users) {
            if (user.getEmail().contentEquals(input.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().contentEquals(input.getAccount())) {
                        /* Account to be deleted, found!
                        Checking the balance */
                        if (account.getBalance() == 0) {
                            /* The deletion can take place */
                            targetedAccount = account;
                            success = true;
                        } else {
                            /* Account can't be deleted due to having a positive balance */
                            /* Keeping the user to add the error into the transaction list */
                            targetedUser = user;
                        }
                    }
                }
            }
            user.getAccounts().remove(targetedAccount);
        }
        /* Output can be either an error, or a success notice.
        Managing the output based on the booleans
        */
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "deleteAccount");
        ObjectNode outputNode = mapper.createObjectNode();
        if (!success) { /* Error message, there are still funds remaining */
            outputNode.put(
                    "error", "Account couldn't be deleted - see org.poo.transactions for details"
            );
            if (targetedUser != null) {
                /* Defensive programming */
                /* Adding the transaction error to the user as well */
                targetedUser.getTransactions().add(
                        new DeleteAccountTransaction(
                                "Account couldn't be deleted"
                                        +
                                        " - there are funds remaining",
                                input.getTimestamp())
                );
            }
        } else {
            outputNode.put("success", "Account deleted");
        }
        outputNode.put("timestamp", input.getTimestamp());
        node.set("output", outputNode);
        node.put("timestamp", input.getTimestamp());
        output.add(node);
    }
}
