package org.poo.main.dependencies.commands.executes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.main.dependencies.Commerciant;
import org.poo.main.dependencies.ExchangeRate;
import org.poo.main.dependencies.commands.Command;
import org.poo.main.dependencies.userinfo.Account;
import org.poo.main.dependencies.userinfo.User;

import java.util.ArrayList;

public class AddInterestCommand implements Command {
    private ArrayNode output;
    private ObjectMapper mapper;
    public AddInterestCommand(final ArrayNode output) {
        mapper = new ObjectMapper();
        this.output = output;
    }
    public void execute(final CommandInput input, final ArrayList<User> users,
                        final ArrayList<ExchangeRate> exchangeRates,
                        final ArrayList<Commerciant> commerciants) {
        boolean isSavingsAccount = false;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().contentEquals(input.getAccount()) && account.getType().equals("savings")) {
                    account.setBalance(account.getBalance() + account.getBalance() * account.getInterestRate());
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
