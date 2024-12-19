package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.List;

public final class AddInterestCommand implements Command {
    private final ArrayNode output;
    private final ObjectMapper mapper;
    public AddInterestCommand(final ArrayNode output) {
        mapper = new ObjectMapper();
        this.output = output;
    }

    /**
     * Executes the "add interest" command. This method processes looks for the account specified
     * and applies interest to the targeted savings account.
     * <p>If the given account is not of type "savings",
     * an error is logged into the output.</p>
     *
     * @param input the command input containing account details and timestamp
     * @param users the list of users with their associated accounts
     * @param exchangeRates the list of exchange rates (not used in this method)
     */
    public void execute(final CommandInput input, final List<User> users,
                        final List<ExchangeRate> exchangeRates) {
        boolean isSavingsAccount = false;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount())
                        && account.getType().equals("savings")) {
                    account.setBalance(
                            account.getBalance() + account.getBalance() * account.getInterestRate()
                    );
                    isSavingsAccount = true;
                }
            }
        }
        if (!isSavingsAccount) {
            ObjectNode node = mapper.createObjectNode();
            node.put("command", "addInterest");
            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", input.getTimestamp());
            outputNode.put("description", "This is not a savings account");
            node.set("output", outputNode);
            node.put("timestamp", input.getTimestamp());
            output.add(node);
        }
    }
}
